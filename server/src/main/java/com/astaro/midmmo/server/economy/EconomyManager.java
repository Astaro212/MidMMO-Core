package com.astaro.midmmo.server.economy;


import com.astaro.midmmo.api.data.CurrencyType;
import com.astaro.midmmo.api.interfaces.EcoAPI;
import com.astaro.midmmo.server.MidMMOServer;
import com.astaro.midmmo.server.player.PlayerProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EconomyManager implements EcoAPI {
    private static final Logger LOGGER = Logger.getLogger(EconomyManager.class.getName());

    public PlayerProfile getProfile(ServerPlayer player) {
        return MidMMOServer.playerCache.get(player.getUUID());
    }

    public void setBalance(ServerPlayer player, CurrencyType currency, double amount) {
        getProfile(player).setCurrency(currency, amount);
        LOGGER.log(Level.INFO, "Player's " + player + " balance is set: " + amount + currency);
    }


    public void addBalance(ServerPlayer player, CurrencyType currency, double amount) {

        getProfile(player).addCurrency(currency, amount);
        LOGGER.log(Level.INFO, "Player " + player + " received " + amount + currency);

    }

    public boolean subtractBalance(ServerPlayer player, CurrencyType currency, double amount) {
        if (getProfile(player).hasEnough(currency, amount)) {
            LOGGER.log(Level.INFO, "Player " + player + " lose " + amount + currency);
            getProfile(player).subtractCurrency(currency, amount);
        }
        LOGGER.log(Level.WARNING, "Player's transaction (" + player + " lose " + amount + currency + " ) failed;");
        return false;
    }

    public boolean transfer(ServerPlayer from, ServerPlayer to, CurrencyType currency, double amount) {
        if (subtractBalance(from, currency, amount) && isTradeable(currency)) {
            addBalance(to, currency, amount);
            LOGGER.log(Level.INFO, "Player" + from + " send successfully  " + amount + currency + " to " + to);
            return true;
        }
        return false;
    }

    public static boolean isTradeable(CurrencyType currency) {
        return currency.isTradeable();
    }

    public Component getFormattedBalance(ServerPlayer player, CurrencyType currency) {
        Component name = Economy.getCurrencyName(currency);
        double balance = getProfile(player).getCurrency(currency.name().toLowerCase());
        return Component.translatable("economy.balance.format", name, balance);
    }

}
