package com.example.vendingmachine.database;

import static com.example.vendingmachine.database.RowItems.TABLE_NAME;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.vendingmachine.models.ProductModel;

@Entity(tableName = TABLE_NAME)
public class RowItems {

    public static final String TABLE_NAME = "items_table";

    @PrimaryKey(autoGenerate = true)
    private int id;

    private ProductModel product;

    private int count;

    public RowItems(ProductModel product, int count) {
        this.product = product;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int decreaseCount() {
        return count--;
    }

}
