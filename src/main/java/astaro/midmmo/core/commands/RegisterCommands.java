package astaro.midmmo.core.commands;

import astaro.midmmo.Midmmo;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static astaro.midmmo.Midmmo.MODID;


//Register command in event bus. It's an event listener for command
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class RegisterCommands {

    @SubscribeEvent
    public static void onRegister(RegisterCommandsEvent event){
        Midmmo.registerCommands(event.getDispatcher());
    }
}
