package com.astaro.midmmo;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Midmmo.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    private static final ModConfigSpec.ConfigValue<String> dbType = BUILDER.comment("Type of Database (MySQl/SQLite/MariaDB/PostgreSQL)").define("Type:", "MariaDB");
    private static final ModConfigSpec.ConfigValue<String> hostname = BUILDER.comment("Database Host").define("hostname:", "");
    private static final ModConfigSpec.ConfigValue<String> port = BUILDER.comment("Database Port.").define("port:", "3306");
    private static final ModConfigSpec.ConfigValue<String> dbName = BUILDER.comment("Name of the Database.").define("database:", "");
    private static final ModConfigSpec.ConfigValue<String> dbUser = BUILDER.comment("Database username.").define("username:", "");
    private static final ModConfigSpec.ConfigValue<String> dbPassword = BUILDER.comment("Database password.").define("password:", "");

    static final ModConfigSpec SPEC = BUILDER.build();


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

    }

    public static String getDbType() {
        return dbType.get();
    }

    public static String getHostname() {
        return hostname.get();
    }

    public static String getPort() {
        return port.get();
    }

    public static String getDbName() {
        return dbName.get();
    }

    public static String getUsername() {
        return dbUser.get();
    }

    public static String getPassword() {
        return dbPassword.get();
    }

}
