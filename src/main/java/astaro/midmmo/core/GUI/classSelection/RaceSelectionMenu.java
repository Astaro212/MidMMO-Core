package astaro.midmmo.core.GUI.classSelection;

import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.registries.MenuRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class RaceSelectionMenu extends AbstractContainerMenu {



    private static final MenuType<RaceSelectionMenu> MENU_TYPE = MenuRegistry.RACE_MENU.get();
    private String selectedRace;
    private String selectedClass;

    public RaceSelectionMenu(int windowId, Inventory inv) {
        super(MENU_TYPE, windowId);
    }

    public RaceSelectionMenu(int i, Inventory inventory, RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(i, inventory);

        this.selectedRace = registryFriendlyByteBuf.readUtf(255);
        this.selectedClass = registryFriendlyByteBuf.readUtf(255);
    }

    public static MenuType<RaceSelectionMenu> get() {
        return MENU_TYPE;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(selectedRace, 255);
        buf.writeUtf(selectedClass, 255);
    }

    public void setParams() {
        PlayerData playerData = new PlayerData();
        playerData.setPlayerRace(selectedRace);
        playerData.setPlayerClass(selectedClass);
    }
}
