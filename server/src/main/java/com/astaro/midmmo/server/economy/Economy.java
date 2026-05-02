package com.astaro.midmmo.server.economy;

import com.astaro.midmmo.api.data.CurrencyType;
import com.astaro.midmmo.api.interfaces.EcoAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class Economy implements EcoAPI {

    private final CurrencyType currency;
    private double amount;

    private Economy(CurrencyType currency) {
        this.currency = currency;
        this.amount = 0;
    }

    private Economy(CurrencyType currency, double initialAmount) {
        this.currency = currency;
        this.amount = initialAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }



    private final static Economy dollars = new Economy(CurrencyType.FIRST);
    private final static Economy coins = new Economy(CurrencyType.SECOND);
    private final static Economy diamonds = new Economy(CurrencyType.THIRD);


    public static Component getCurrencyName(CurrencyType currency) {
        return Component.translatable("economy.currency." + currency.name().toLowerCase());
    }




}



