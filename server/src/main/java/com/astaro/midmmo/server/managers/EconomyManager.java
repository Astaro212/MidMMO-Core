package com.astaro.midmmo.server.managers;

import astaro.midmmo.api.economy.EcoAPI;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import com.astaro.midmmo.server.economy.Economy;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EconomyManager implements EcoAPI {
    private static final Logger LOGGER = Logger.getLogger(EconomyManager.class.getName());

    public static double getBalance(ServerPlayer player, Economy.Currencies currency){
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if(data != null){
            return data.getCurrency(currency.name().toLowerCase());
        }
        return 0.0;
    }

    public static void setBalance(ServerPlayer player, Economy.Currencies currency, double amount){
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if(data != null) {
            data.setCurrency(currency.name().toLowerCase(), amount);
            LOGGER.log(Level.INFO, "Player's " + player + " balance is set: " + amount + currency);
        }
    }

    public static void addBalance(ServerPlayer player, Economy.Currencies currency, double amount) {
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if (data != null) {
            data.addCurrency(currency.name().toLowerCase(), amount);
            LOGGER.log(Level.INFO, "Player " + player + " received " + amount + currency);
        }
    }

    public static boolean subtractBalance(ServerPlayer player, Economy.Currencies currency, double amount) {
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if (data != null) {
            LOGGER.log(Level.INFO, "Player " + player + " lose " + amount + currency);
            return data.subtractCurrency(currency.name().toLowerCase(), amount);
        }
        LOGGER.log(Level.WARNING, "Player's transaction (" + player + " lose " + amount + currency + " ) failed;");
        return false;
    }

    public static boolean hasEnough(ServerPlayer player, Economy.Currencies currency, double amount) {
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if (data != null) {
            LOGGER.log(Level.INFO, "Player" + player + " have enough " + amount + currency);
            return data.hasEnough(currency.name().toLowerCase(), amount);
        }
        return false;
    }

    public static boolean transfer(ServerPlayer from, ServerPlayer to, Economy.Currencies currency, double amount) {
        if (subtractBalance(from, currency, amount) && isTradeable(currency)) {
            addBalance(to, currency, amount);
            LOGGER.log(Level.INFO, "Player" + from + " send successfully  " + amount + currency + " to " + to);
            return true;
        }
        return false;
    }

    public static boolean isTradeable(Economy.Currencies currency) {
        return currency.isTradeable();
    }

    public static Component getFormattedBalance(ServerPlayer player, Economy.Currencies currency){
        Component name = Economy.getCurrencyName(currency);
        double balance = getBalance(player, currency);
        return Component.translatable("economy.balance.format", name, balance);
    }

}
