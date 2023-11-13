package com.example.vendingmachine.database;

import androidx.room.TypeConverter;

import com.example.vendingmachine.models.ProductModel;
import com.google.gson.Gson;

public class Converters {

    @TypeConverter
    public String fromProductModelToString(ProductModel product) {
        return new Gson().toJson(product);
    }

    @TypeConverter
    public ProductModel fromStringToProductModel(String product) {
        return new Gson().fromJson(product, ProductModel.class);
    }

}
