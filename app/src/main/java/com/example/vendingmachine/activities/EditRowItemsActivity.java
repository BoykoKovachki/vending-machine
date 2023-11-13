package com.example.vendingmachine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vendingmachine.R;

import java.util.ArrayList;

public class EditRowItemsActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.vendingmachine.EXTRA_ID";
    public static final String EXTRA_COUNT = "com.example.vendingmachine.EXTRA_COUNT";
    public static final String EXTRA_PRODUCT_NAME = "com.example.vendingmachine.EXTRA_PRODUCT_NAME";
    public static final String EXTRA_PRODUCT_PRICE = "com.example.vendingmachine.EXTRA_PRODUCT_PRICE";
    public static final String EXTRA_PRODUCT_TYPE = "com.example.vendingmachine.EXTRA_PRODUCT_TYPE";

    EditText nameEditText;
    EditText priceEditText;
    EditText itemCountEditText;
    Spinner typeSpinner;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_row_items);

        setTitle(getString(R.string.edit_row_item_activity_title));

        nameEditText = findViewById(R.id.nameEditText);
        priceEditText = findViewById(R.id.priceEditText);
        itemCountEditText = findViewById(R.id.itemCountEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        imageView = findViewById(R.id.imageView);
        Button saveButton = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        itemCountEditText.setText(String.valueOf(intent.getIntExtra(EXTRA_COUNT, 0)));
        nameEditText.setText(intent.getStringExtra(EXTRA_PRODUCT_NAME));
        priceEditText.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRODUCT_PRICE, 0)));

        setSpinnerData();
        typeSpinner.setSelection(intent.getIntExtra(EXTRA_PRODUCT_TYPE, 0) - 1);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setImageViewFromProductType(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setImageViewFromProductType(intent.getIntExtra(EXTRA_PRODUCT_TYPE, 0));

        saveButton.setOnClickListener(view -> {
            if (validateFormData()) {
                saveProduct();
            }
        });
    }

    private void setSpinnerData() {
        ArrayList<String> types = new ArrayList<>();
        types.add(getString(R.string.product_type_drink));
        types.add(getString(R.string.product_type_snack_bar));
        types.add(getString(R.string.product_type_chips));
        types.add(getString(R.string.product_type_candy));
        types.add(getString(R.string.product_type_sandwich));
        types.add(getString(R.string.product_type_soft_drink));
        types.add(getString(R.string.product_type_other));

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, R.id.spinnerTextView, types);
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        typeSpinner.setAdapter(typeAdapter);
    }

    private void setImageViewFromProductType(int type) {
        switch (type) {
            case 1:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_drink, null));
                return;
            case 2:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_snackbar, null));
                return;
            case 3:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_chips, null));
                return;
            case 4:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_candy, null));
                return;
            case 5:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_sandwich, null));
                return;
            case 6:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_soft_drink, null));
                return;
            case 7:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_other, null));
                return;
            default:
                imageView.setImageDrawable(this.getResources().getDrawable(R.mipmap.product_other, null));
        }
    }

    private boolean validateFormData() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            showErrorAlertDialog(getString(R.string.edit_row_item_activity_validation_product_name));
            return false;
        }

        if (priceEditText.getText().toString().trim().isEmpty()) {
            showErrorAlertDialog(getString(R.string.edit_row_item_activity_validation_product_price));
            return false;
        } else {
            if (!priceEditText.getText().toString().trim().contains(".")) {
                showErrorAlertDialog(getString(R.string.edit_row_item_activity_validation_product_price_invalid));
                return false;
            }
        }

        if (itemCountEditText.getText().toString().trim().isEmpty()) {
            showErrorAlertDialog(getString(R.string.edit_row_item_activity_validation_product_count));
            return false;
        } else {
            int count = Integer.parseInt(itemCountEditText.getText().toString().trim());
            if (count > MainActivity.ROW_ITEMS_LIMIT) {
                showErrorAlertDialog(getString(R.string.edit_row_item_activity_validation_product_count_over_the_limit));
                return false;
            }
        }

        return true;
    }

    private void saveProduct() {
        int count = Integer.parseInt(itemCountEditText.getText().toString().trim());
        String name = nameEditText.getText().toString().trim();
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        int type = typeSpinner.getSelectedItemPosition() + 1;

        Intent data = new Intent();
        data.putExtra(EXTRA_COUNT, count);
        data.putExtra(EXTRA_PRODUCT_NAME, name);
        data.putExtra(EXTRA_PRODUCT_PRICE, price);
        data.putExtra(EXTRA_PRODUCT_TYPE, type);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    private void showErrorAlertDialog(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.dialog_title_warning);
        dialog.setMessage(message);
        dialog.setNeutralButton(R.string.dialog_button_close, null);
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
    }

}
