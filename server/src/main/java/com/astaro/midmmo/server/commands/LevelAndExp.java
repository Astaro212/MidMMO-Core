package com.astaro.midmmo.server.commands;

import com.astaro.midmmo.server.MidMMOServer;
import com.astaro.midmmo.server.experience.PlayerExp;
import com.astaro.midmmo.server.player.PlayerProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.UUID;

//Register new command for adding experience
public class LevelAndExp {

    //register command
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

    //Send command
    private static int setPlayerExp(CommandSourceStack source, String playerName, float exp) {
        var server = source.getServer();
        var player = server.getPlayerList().getPlayerByName(playerName);
        if (player == null) {
            source.sendFailure(Component.translatable("midmmo.player_notfound" ,playerName));
            return 0;
        }

        UUID uuid = player.getUUID();

        PlayerExp playerExp = getOrCreateData(uuid, playerName);
        playerExp.addExperience(exp);
        playerExp.checkAndUpdateLevel();

        source.sendSuccess(() -> Component.translatable("midmmo.exp_gained", exp ), true);
        return 1;

    }

    //Get data for command
    private static PlayerExp getOrCreateData(UUID uuid, String playerName) {

        PlayerProfile data = MidMMOServer.playerCache.get(uuid);
        if (data != null) {
            return new PlayerExp(uuid, playerName, data.getPlayerLvl(), data.getPlayerExp());
        } else {
            return new PlayerExp(uuid, playerName,1, 0f);
        }
    }
}
