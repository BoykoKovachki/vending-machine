package com.example.vendingmachine.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vendingmachine.R;
import com.example.vendingmachine.adapters.RowItemsAdapter;
import com.example.vendingmachine.database.RowItems;
import com.example.vendingmachine.database.RowItemsViewModel;
import com.example.vendingmachine.models.ProductModel;
import com.example.vendingmachine.services.DataManager;
import com.example.vendingmachine.utils.CurrencyConverter;

public class MainActivity extends AppCompatActivity {

    public static final int ROW_ITEMS_LIMIT = 15;

    private RowItemsViewModel viewModel;

    private RecyclerView recyclerView;

    private ImageView insertCoinImageView;
    private ImageView changeImageView;

    private TextView statusTextView;


    private TextView infoTextView;
    private ImageView resetButton;

    private RowItemsAdapter adapter;

    private Handler handler;

    private double currentBalance;

    private final ActivityResultLauncher<Intent> addRowItemsActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                int type = result.getData().getIntExtra(AddRowItemsActivity.EXTRA_NEW_PRODUCT_TYPE, 0);
                String name = result.getData().getStringExtra(AddRowItemsActivity.EXTRA_NEW_PRODUCT_NAME);
                double price = result.getData().getDoubleExtra(AddRowItemsActivity.EXTRA_NEW_PRODUCT_PRICE, 0);
                int count = result.getData().getIntExtra(AddRowItemsActivity.EXTRA_NEW_COUNT, 0);

                ProductModel product = new ProductModel(name, price, type);
                RowItems rowItems = new RowItems(product, count);
                viewModel.insert(rowItems);
            }
        }
    });

    private final ActivityResultLauncher<Intent> editRowItemsActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {

                int id = result.getData().getIntExtra(EditRowItemsActivity.EXTRA_ID, -1);
                if (id == -1) {
                    Toast.makeText(MainActivity.this, getString(R.string.row_items_product_error_update), Toast.LENGTH_SHORT).show();
                    return;
                }

                int count = result.getData().getIntExtra(EditRowItemsActivity.EXTRA_COUNT, 0);
                String name = result.getData().getStringExtra(EditRowItemsActivity.EXTRA_PRODUCT_NAME);
                double price = result.getData().getDoubleExtra(EditRowItemsActivity.EXTRA_PRODUCT_PRICE, 0);
                int type = result.getData().getIntExtra(EditRowItemsActivity.EXTRA_PRODUCT_TYPE, 0);

                ProductModel product = new ProductModel(name, price, type);
                RowItems rowItems = new RowItems(product, count);
                rowItems.setId(id);
                viewModel.update(rowItems);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.main_activity_title));

        DataManager.getInstance().setContext(this);

        insertCoinImageView = findViewById(R.id.insertCoinImageView);
        changeImageView = findViewById(R.id.changeImageView);
        statusTextView = findViewById(R.id.statusTextView);
        infoTextView = findViewById(R.id.infoTextView);
        resetButton = findViewById(R.id.resetButton);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RowItemsAdapter(this.getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        viewModel = ViewModelProviders.of(this).get(RowItemsViewModel.class);
        viewModel.getAllItems().observe(this, items -> adapter.setInventory(items));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(adapter.getRowItemsAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickedListener(rowItems -> {
            if (currentBalance != 0) {
                if (currentBalance >= rowItems.getProduct().getPrice()) {
                    if (rowItems.decreaseCount() == 1) {
                        viewModel.delete(rowItems);
                    } else {
                        viewModel.update(rowItems);
                    }
                    currentBalance = currentBalance - rowItems.getProduct().getPrice();
                    infoTextView.setText(CurrencyConverter.convertDoubleToString(currentBalance));
                    statusTextView.setText(String.format(getString(R.string.main_activity_status_successful), rowItems.getProduct().getName(), CurrencyConverter.convertDoubleToString(rowItems.getProduct().getPrice())));
                    startTimer();

                } else {
                    showErrorAlertDialog(getString(R.string.main_activity_status_not_enough_cash));
                }
            } else {
                showErrorAlertDialog(getString(R.string.main_activity_status_no_cash));
            }
        });

        adapter.setInfoButtonOnClickListener(rowItems -> openEditRowItemsActivity(rowItems));

        insertCoinImageView.setOnClickListener(view -> showDialogForInsertingMoney());

        changeImageView.setOnClickListener(view -> takeChangeButtonClicked());

        resetButton.setOnClickListener(view -> resetButtonClicked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataManager.getInstance().getBalance() != null && !DataManager.getInstance().getBalance().isEmpty()) {
            currentBalance = Double.parseDouble(DataManager.getInstance().getBalance());
            infoTextView.setText(CurrencyConverter.convertDoubleToString(currentBalance));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentBalance != 0) {
            String currentBalance = String.valueOf(this.currentBalance);
            DataManager.getInstance().setBalance(currentBalance);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_row_items) {
            openAddNewItemRowActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddNewItemRowActivity() {
        Intent intent = new Intent(MainActivity.this, AddRowItemsActivity.class);
        addRowItemsActivityLauncher.launch(intent);
    }

    private void openEditRowItemsActivity(RowItems rowItems) {
        Intent intent = new Intent(MainActivity.this, EditRowItemsActivity.class);
        intent.putExtra(EditRowItemsActivity.EXTRA_ID, rowItems.getId());
        intent.putExtra(EditRowItemsActivity.EXTRA_COUNT, rowItems.getCount());
        intent.putExtra(EditRowItemsActivity.EXTRA_PRODUCT_NAME, rowItems.getProduct().getName());
        intent.putExtra(EditRowItemsActivity.EXTRA_PRODUCT_PRICE, rowItems.getProduct().getPrice());
        intent.putExtra(EditRowItemsActivity.EXTRA_PRODUCT_TYPE, rowItems.getProduct().getType());
        editRowItemsActivityLauncher.launch(intent);
    }

    private void showDialogForInsertingMoney() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog_view, null);
        alert.setView(customLayout);

        EditText amountEditText = customLayout.findViewById(R.id.amountEditText);

        alert.setTitle(this.getResources().getString(R.string.main_activity_insert_coins_title));
        alert.setMessage(this.getResources().getString(R.string.main_activity_insert_coins_info));
        alert.setCancelable(false);
        alert.setPositiveButton(this.getResources().getString(R.string.main_activity_insert_coins_continue_button), (dialog, whichButton) -> {
            String enteredText = amountEditText.getText().toString().trim();
            if (!enteredText.contains(".")) {
                showErrorAlertDialog(getString(R.string.main_activity_insert_coins_info_error));
                return;
            }
            currentBalance = currentBalance + Double.parseDouble(enteredText);
            infoTextView.setText(CurrencyConverter.convertDoubleToString(currentBalance));
            statusTextView.setText(String.format(getString(R.string.main_activity_status_successful_insert), CurrencyConverter.convertDoubleToString(currentBalance)));
            startTimer();
        });

        alert.setNegativeButton(this.getResources().getString(R.string.main_activity_insert_coins_cancel_button), null);
        alert.show();
    }

    private void resetStatusTextView() {
        statusTextView.setText("");
    }

    private void takeChangeButtonClicked() {
        if (currentBalance != 0) {
            infoTextView.setText("");
            statusTextView.setText(String.format(getString(R.string.main_activity_status_successful_change), CurrencyConverter.convertDoubleToString(currentBalance)));
            currentBalance = 0;
            DataManager.getInstance().clearSharedPreferences();
            startTimer();
        }
    }

    private void resetButtonClicked() {
        infoTextView.setText("");
        if (currentBalance != 0) {
            statusTextView.setText(String.format(getString(R.string.main_activity_status_reset_change), CurrencyConverter.convertDoubleToString(currentBalance)));
            currentBalance = 0;
            DataManager.getInstance().clearSharedPreferences();
        } else {
            statusTextView.setText(getString(R.string.main_activity_status_reset));
        }
        startTimer();
    }

    private void startTimer() {
        handler = new Handler();
        handler.postDelayed(this::resetStatusTextView, 3000);
    }

    private void showErrorAlertDialog(String message) {
        androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_title_information);
        dialog.setMessage(message);
        dialog.setNeutralButton(R.string.dialog_button_close, null);
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

}
