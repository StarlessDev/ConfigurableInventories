package dev.starless.inventories.inventories;

import dev.starless.inventories.adventure.ColorUtils;
import dev.starless.inventories.items.AnvilUpdatingItem;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.window.AnvilWindow;

import java.util.Optional;

/**
 * Abstract base class for creating anvil-based input GUI inventories.
 *
 * @param <T> the type of value expected from the player input
 */
public abstract class AnvilInventory<T> {

    /**
     * Called when the player provides valid input through the anvil interface.
     *
     * @param player the player who provided the input
     * @param value the parsed value from the player's input
     */
    public abstract void onInput(final Player player, final T value);

    /**
     * Maps the raw string input from the anvil to the expected type.
     *
     * @param input the raw string input from the player
     * @return an Optional containing the parsed value if valid, empty otherwise
     */
    public abstract Optional<T> map(final String input);

    /**
     * Called when the anvil window is closed.
     * Default implementation does nothing, can be overridden for custom behavior.
     */
    public void onClose() {
    }

    /**
     * Gets the title displayed on the anvil window.
     * You can override this method to customize the title.
     *
     * @return the title text for the anvil window
     */
    public String getAnvilTitle() {
        return "Scrivi";
    }

    /**
     * Gets the placeholder text displayed in the anvil input field.
     * You can override this method to customize the placeholder text.
     *
     * @return the placeholder text for the anvil input
     */
    public String getAnvilPlaceholder() {
        return "Scrivi qua";
    }

    /**
     * Opens the anvil input interface for the specified player.
     *
     * @param player the player to show the anvil interface to
     */
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
