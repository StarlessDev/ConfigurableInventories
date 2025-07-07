package dev.starless.inventories.items;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Objects;
import java.util.function.Supplier;

public class AnvilUpdatingItem extends AbstractItem {

    @Getter
    private String name;
    @Setter
    private Supplier<String> anvilOutputSupplier;

    public AnvilUpdatingItem(String initialName) {
        this.name = Objects.requireNonNullElse(initialName, "");
        this.anvilOutputSupplier = null;
    }

    public AnvilAction getActionHandler() {
        return AnvilAction.doNothing();
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAPER).setDisplayName(name);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType,
                            @NotNull Player player,
                            @NotNull InventoryClickEvent event) {
        if (anvilOutputSupplier != null) {
            this.getActionHandler().execute(player, event, anvilOutputSupplier.get());
        }
    }

    public void updateName(String name) {
        this.name = name;
        this.notifyWindows();
    }

    public interface AnvilAction {

        static AnvilAction doNothing() {
            return (player, event, text) -> {
                // No action performed
            };
        }

        void execute(Player player, InventoryClickEvent event, String text);
    }
}
