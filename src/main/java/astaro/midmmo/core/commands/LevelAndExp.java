package astaro.midmmo.core.commands;


import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.PlayerDataCache;
import astaro.midmmo.core.expsystem.PlayerExp;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.UUID;
import java.util.function.Supplier;

public class LevelAndExp {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("addexp")
                        .then(Commands.argument("player", StringArgumentType.word())
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(1F))
                                        .executes(context -> {
                                            String playerName = StringArgumentType.getString(context, "player");
                                            float exp = FloatArgumentType.getFloat(context, "amount");
                                            return setPlayerExp(context.getSource(), playerName, exp);
                                        })
                                )
                        )

        );
    }

    private static int setPlayerExp(CommandSourceStack source, String playerName, float exp) {
        var server = source.getServer();
        var player = server.getPlayerList().getPlayerByName(playerName);
        if (player == null) {
            source.sendFailure(Component.literal("Игрок " + playerName + " не найден."));
            return 0;
        }

        UUID uuid = player.getUUID();

        PlayerExp playerExp = getOrCreateData(uuid);
        playerExp.addExperience(exp);
        playerExp.checkAndUpdateLevel();

        source.sendSuccess((Supplier<Component>) Component.literal("Получено " + exp + " опыта."), true);
        return 1;

    }

    private static PlayerExp getOrCreateData(UUID uuid) {

        PlayerData data = PlayerDataCache.get(uuid);
        if (data != null) {
            return new PlayerExp(uuid, data.getPlayerLvl(), data.getPlayerExp());
        } else {
            return new PlayerExp(uuid, 1, 0f);
        }
    }
}
