package com.example.vendingmachine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vendingmachine.R;
import com.example.vendingmachine.database.RowItems;
import com.example.vendingmachine.utils.CurrencyConverter;

import java.util.ArrayList;
import java.util.List;

public class RowItemsAdapter extends RecyclerView.Adapter<RowItemsAdapter.RowItemsHolder> {

    private Context context;
    private final LayoutInflater inflater;

    private OnItemClickedListener listener;

    private OnItemClickedListener infoButtonListener;

    private List<RowItems> items = new ArrayList<>();

    public void setInventory(List<RowItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public RowItemsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setInfoButtonOnClickListener(OnItemClickedListener infoButtonListener) {
        this.infoButtonListener = infoButtonListener;
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RowItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.row_item, parent, false);
        return new RowItemsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RowItemsAdapter.RowItemsHolder holder, int position) {
        RowItems rowItems = items.get(position);
        holder.nameTextView.setText(rowItems.getProduct().getName());
        holder.priceTextView.setText(CurrencyConverter.convertDoubleToString(rowItems.getProduct().getPrice()));
        holder.itemCountTextView.setText(String.format("x%s", rowItems.getCount()));

        switch (rowItems.getProduct().getType()) {
            case 1:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_drink, null));
                return;
            case 2:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_snackbar, null));
                return;
            case 3:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_chips, null));
                return;
            case 4:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_candy, null));
                return;
            case 5:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_sandwich, null));
                return;
            case 6:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_soft_drink, null));
                return;
            case 7:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_other, null));
                return;
            default:
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.product_other, null));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public RowItems getRowItemsAt(int position) {
        return items.get(position);
    }

    class RowItemsHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView nameTextView;
        private TextView priceTextView;
        private TextView itemCountTextView;
        private ImageView infoButton;

        public RowItemsHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            itemCountTextView = itemView.findViewById(R.id.itemCountTextView);
            infoButton = itemView.findViewById(R.id.infoButton);

            infoButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    infoButtonListener.onItemClicked(getRowItemsAt(position));
                }
            });

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClicked(getRowItemsAt(position));
                }
            });
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(RowItems rowItems);
    }

}
