package astaro.midmmo.core.registries;


import astaro.midmmo.core.GUI.classSelection.RaceSelectionMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class MenuRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, "midmmo");

    public static final IContainerFactory<RaceSelectionMenu> RACE_FACTORY = RaceSelectionMenu::new;

    public static final Supplier<MenuType<RaceSelectionMenu>> RACE_MENU = registerMenuType(RACE_FACTORY, "race_menu");

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return (Supplier<MenuType<T>>) MENU_TYPES.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus modBus) {
        MENU_TYPES.register(modBus);
    }


}
