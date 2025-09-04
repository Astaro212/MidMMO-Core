package astaro.midmmo.core.economy;

import astaro.midmmo.api.economy.EcoAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class Economy implements EcoAPI {

    private final Currencies currency;
    private double amount;

    private Economy(Currencies currency) {
        this.currency = currency;
        this.amount = 0;
    }

    private Economy(Currencies currency, double initialAmount) {
        this.currency = currency;
        this.amount = initialAmount;
    }

    public enum Currencies {
        DOLLARS("dollars", ChatFormatting.GREEN, true),
        COINS("coins", ChatFormatting.GOLD, true),
        DIAMONDS("diamonds", ChatFormatting.AQUA, false);

        private final String currencyName;
        private final ChatFormatting chatFormatting;
        private final boolean isTradeable;

        Currencies(String currency, ChatFormatting chatFormatting, boolean isTradeable) {
            this.currencyName = currency;
            this.chatFormatting = chatFormatting;
            this.isTradeable = isTradeable;
        }

        public boolean isTradeable() {
            return this.isTradeable;
        }

    }

    private final static Economy dollars = new Economy(Currencies.DOLLARS);
    private final static Economy coins = new Economy(Currencies.COINS);
    private final static Economy diamonds = new Economy(Currencies.DIAMONDS);


    public static Component getCurrencyName(Currencies currency) {
        return Component.translatable("economy.currency." + getCurrencyId(currency)).withStyle(currency.chatFormatting);
    }

    @Override
    public boolean isTradeable() {
        return currency.isTradeable;
    }



    public static Currencies DOLLARS() {
        return Currencies.DOLLARS;
    }

    public static Currencies COINS() {
        return Currencies.COINS;
    }

    public static Currencies DIAMONDS() {
        return Currencies.DIAMONDS;
    }

    public static Economy dollars() {
        return dollars;
    }

    public static Economy coins() {
        return coins;
    }

    public static Economy diamonds() {
        return diamonds;
    }

    public static String getCurrencyId(Currencies currency) {
        return currency.currencyName;
    }

    public Currencies getCurrencyType() {
        return currency;
    }

}



