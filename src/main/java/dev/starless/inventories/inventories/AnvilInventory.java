package dev.starless.inventories.inventories;

import dev.starless.inventories.adventure.ColorUtils;
import dev.starless.inventories.items.AnvilUpdatingItem;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.window.AnvilWindow;

import java.util.Optional;

public abstract class AnvilInventory<T> {

    public abstract void onInput(final Player player, final T value);

    public abstract Optional<T> map(final String input);

    public void onClose() {
    }

    public String getAnvilTitle() {
        return "Scrivi";
    }

    public String getAnvilPlaceholder() {
        return "Scrivi qua";
    }

    public void askForInput(Player player) {
        final AnvilUpdatingItem outputItem = new OutputItem(this.getAnvilPlaceholder());
        final AnvilWindow window = AnvilWindow.single()
                .setTitle(ColorUtils.section(this.getAnvilTitle()))
                .setGui(Gui.of(new Structure("# . $")
                        .addIngredient('#', new AnvilUpdatingItem(this.getAnvilPlaceholder()))
                        .addIngredient('$', outputItem)))
                .addRenameHandler(outputItem::updateName)
                .addCloseHandler(this::onClose)
                .build(player);

        outputItem.setAnvilOutputSupplier(window::getRenameText);
        window.open();
    }

    class OutputItem extends AnvilUpdatingItem {

        public OutputItem(String initialName) {
            super(initialName);
        }

        @Override
        public AnvilAction getActionHandler() {
            return (player, click, text) -> {
                final Optional<T> obj = map(text);
                if (obj.isPresent()) {
                    onInput(player, obj.get());
                } else {
                    this.updateName("Errore.");
                }
            };
        }
    }
}
