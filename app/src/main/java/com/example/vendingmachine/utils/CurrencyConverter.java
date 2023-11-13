package com.example.vendingmachine.utils;

import java.text.DecimalFormat;

public class CurrencyConverter {

    public static String convertDoubleToString(double price) {
        DecimalFormat dF = new DecimalFormat("####,###,###.00");

        if (dF.format(price).length() == 3) {
            return String.format("$0%s", dF.format(price));
        } else {
            return String.format("$%s", dF.format(price));
        }
    }

}
