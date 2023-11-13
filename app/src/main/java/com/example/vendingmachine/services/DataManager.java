package com.example.vendingmachine.services;

import android.content.Context;
import android.content.SharedPreferences;

public class DataManager {

    private static final String PREFS_NAME = "VendingMachine";

    private static DataManager instance = null;

    private Context context;

    private String balance;


    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public DataManager() {
        balance = "";
    }

    public void setContext(Context context) {
        this.context = context;
        read();
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
        write();
    }

    public void clearSharedPreferences() {
        SharedPreferences prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("BL");
        editor.commit();
        read();
    }

    private void read() {
        SharedPreferences prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        balance = prefs.getString("BL", "");
    }

    private void write() {
        SharedPreferences prefs = this.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("BL", balance);
        editor.commit();
    }

}
