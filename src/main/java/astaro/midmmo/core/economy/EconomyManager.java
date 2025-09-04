package astaro.midmmo.core.economy;

import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.Locale;
import java.util.logging.Logger;

public class EconomyManager {
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
        }
    }

    public static void addBalance(ServerPlayer player, Economy.Currencies currency, double amount) {
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if (data != null) {
            data.addCurrency(currency.name().toLowerCase(), amount);
        }
    }

    public static boolean subtractBalance(ServerPlayer player, Economy.Currencies currency, double amount) {
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if (data != null) {
            return data.subtractCurrency(currency.name().toLowerCase(), amount);
        }
        return false;
    }

    public static boolean hasEnough(ServerPlayer player, Economy.Currencies currency, double amount) {
        PlayerData data = PlayerDataCache.get(player.getUUID());
        if (data != null) {
            return data.hasEnough(currency.name().toLowerCase(), amount);
        }
        return false;
    }

    public static boolean transfer(ServerPlayer from, ServerPlayer to, Economy.Currencies currency, double amount) {
        if (subtractBalance(from, currency, amount) && isTradeable(currency)) {
            addBalance(to, currency, amount);
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
