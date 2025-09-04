package astaro.midmmo.core.commands;


import astaro.midmmo.core.economy.Economy;
import astaro.midmmo.core.economy.EconomyManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class EcoCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("economy")
                .then(Commands.literal("balance")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Component message = EconomyManager.getFormattedBalance(player, Economy.DOLLARS());
                            context.getSource().sendSuccess(() -> message, false);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("pay")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("amount", DoubleArgumentType.doubleArg()))
                                .executes(context -> {
                                    ServerPlayer sender = context.getSource().getPlayerOrException();
                                    ServerPlayer target = EntityArgument.getPlayer(context, "target");
                                    double amount = DoubleArgumentType.getDouble(context, "amount");

                                    if (EconomyManager.transfer(sender, target, Economy.DOLLARS(), amount)) {
                                        context.getSource().sendSuccess(() ->
                                                Component.translatable("economy.transfer.success",
                                                        amount,
                                                        Economy.getCurrencyName(Economy.DOLLARS()),
                                                        target.getName()
                                                ), false);
                                    } else {
                                        context.getSource().sendFailure(
                                                Component.translatable("economy.transfer.failed")
                                        );
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))

        );
    }
}
