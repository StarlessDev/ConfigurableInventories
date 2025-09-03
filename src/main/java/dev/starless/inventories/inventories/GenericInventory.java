package dev.starless.inventories.inventories;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.properties.Property;
import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.ItemClick;
import dev.starless.inventories.adventure.ColorUtils;
import dev.starless.inventories.errors.GenericError;
import dev.starless.inventories.items.generic.GenericItem;
import dev.starless.inventories.items.placeholders.ItemPlaceholder;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Click;
import xyz.xenondevs.invui.window.Window;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;

/**
 * Abstract base class for creating configurable inventories.
 * This class provides a framework for building inventories that can be loaded from
 * configuration files and displayed to players with customizable items and placeholders.
 *
 * @param <G> the type of GUI this inventory uses
 * @param <S> the type of GUI builder this inventory uses
 */
public abstract class GenericInventory<G extends Gui, S extends Gui.Builder<G, S>> {

    protected G gui;
    protected ConfigurableInventory inventory;
    @Setter
    private GenericError error;
    @Setter
    private Duration clickCooldown;
    private Instant lastClick;

    private final Property<ConfigurableInventory> property;
    private final Map<Character, List<ItemPlaceholder>> placeholders = new HashMap<>();

    public GenericInventory(final Property<ConfigurableInventory> property) {
        this.property = property;
        this.error = null;
        this.clickCooldown = Duration.ZERO;
        this.lastClick = Instant.EPOCH;
    }

    /**
     * Loads the inventory configuration from the provided {@link SettingsManager}.
     *
     * @param inventoryConfig the settings manager containing the inventory configuration
     */
    public void loadFromConfig(final SettingsManager inventoryConfig) {
        this.inventory = inventoryConfig.getProperty(property);
    }

    /**
     * Returns the GUI builder for this inventory type.
     * This method must be implemented by subclasses to provide the appropriate builder.
     *
     * @return the GUI builder instance
     */
    protected abstract S getBuilder();

    /**
     * Compiles the GUI by adding items and configuring the builder.
     * This method is called when showing the inventory to a player.
     *
     * @param player the player for whom the GUI is being compiled
     * @param builder the GUI builder to configure
     */
    protected abstract void compileGui(Player player, S builder);

    /**
     * Compiles the window settings for the inventory.
     * This method can be overridden to customize window properties.
     *
     * @param player the player for whom the window is being compiled
     * @param window the window builder to configure
     */
    protected void compileWindow(Player player, Window.Builder.Normal.Single window) {
    }

    /**
     * Compiles the title for the inventory.
     * This method can be overridden to provide dynamic titles.
     *
     * @return the compiled title string
     */
    protected String compileTitle() {
        return inventory.getTitle();
    }

    /**
     * Adds an item to the GUI builder with the specified key.
     *
     * @param builder the GUI builder to add the item to
     * @param key the character key representing the item in the structure
     */
    protected void addItem(Gui.Builder<G, S> builder, char key) {
        this.addItem(builder, key, null);
    }

    /**
     * Adds an item to the GUI builder with the specified key and click action.
     *
     * @param builder the GUI builder to add the item to
     * @param key the character key representing the item in the structure
     * @param action the action to execute when the item is clicked
     */
    protected void addItem(Gui.Builder<G, S> builder,
                           char key,
                           ItemClick action) {
        this.addItem(builder, key, action, null);
    }

    /**
     * Adds an item to the GUI builder with the specified key, click action, and item modifications.
     *
     * @param builder the GUI builder to add the item to
     * @param key the character key representing the item in the structure
     * @param action the action to execute when the item is clicked
     * @param itemEdit a consumer to modify the item builder before creating the item
     */
    protected void addItem(Gui.Builder<G, S> builder,
                           char key,
                           ItemClick action,
                           Consumer<ConfigurableItem.Builder> itemEdit) {
        final GenericItem item = new GenericItem(inventory, key, this.getPlaceholders(key), itemEdit) {
            @Override
            public void handleClick(@NotNull ClickType clickType,
                                    @NotNull Player player,
                                    @NotNull InventoryClickEvent event) {
                if (action != null) {
                    action.handle(this, new Click(event));
                }
            }
        };

        this.addItem(builder, item);
    }

    /**
     * Adds a generic item to the GUI builder.
     *
     * @param builder the GUI builder to add the item to
     * @param item the generic item to add
     */
    protected void addItem(Gui.Builder<G, S> builder, GenericItem item) {
        builder.addIngredient(item.getKey(), item);
    }

    /**
     * Adds placeholders for a specific character key.
     * Placeholders are provided as alternating target-value pairs.
     *
     * @param c the character key to associate placeholders with
     * @param objects alternating target-value pairs for placeholders
     */
    public void addPlaceholders(char c, Object... objects) {
        final List<ItemPlaceholder> list = new ArrayList<>();
        for (int i = 0; (objects.length - i) >= 2; i += 2) {
            final String target = String.valueOf(objects[i]);
            final String value = String.valueOf(objects[i + 1]);
            list.add(new ItemPlaceholder(target, value));
        }

        placeholders.compute(c, (k, v) -> {
            if (v == null) v = new ArrayList<>();

            for (final ItemPlaceholder placeholder : list) {
                v.removeIf(p -> p.target().equals(placeholder.target()));
                v.add(placeholder);
            }

            return v;
        });
    }

    /**
     * Gets the list of placeholders associated with a character key.
     *
     * @param c the character key to get placeholders for
     * @return the list of placeholders, or an empty list if none exist
     */
    public List<ItemPlaceholder> getPlaceholders(char c) {
        return placeholders.getOrDefault(c, Collections.emptyList());
    }

    /**
     * Checks if there was an error loading or configuring the inventory.
     *
     * @return {@link Boolean}
     */
    public boolean hasError() {
        return error != null;
    }

    /**
     * Registers a click event, updating the last click timestamp.
     * This is used for implementing click cooldowns.
     */
    public void registerClick() {
        this.lastClick = Instant.now();
    }

    /**
     * Checks if the player is currently on cooldown
     * and cannot click until it expires.
     *
     * @return true if the player is on cooldown, false otherwise
     */
    public boolean isClickOnCooldown() {
        if (clickCooldown.isZero() || clickCooldown.isNegative()) {
            return false;
        }

        return Duration.between(lastClick, Instant.now()).compareTo(clickCooldown) < 0;
    }

    /**
     * Shows the inventory to the specified player.
     * This method compiles the GUI, creates the window, and opens it for the player.
     *
     * @param player the player to show the inventory to
     */
    public void show(final Player player) {
        final S builder = this.getBuilder().setStructure(inventory.getStructure().toArray(new String[0]));
        this.compileGui(player, builder);
        this.gui = builder.build();

        final Window.Builder.Normal.Single window = Window.single()
                .setViewer(player)
                .setTitle(ColorUtils.section(this.compileTitle()))
                .setGui(this.gui);
        this.compileWindow(player, window);

        if (this.hasError()) {
            final String message = error.message();
            if (message != null) {
                player.sendMessage(ColorUtils.component(message));
            }

            if (error.forceClose()) {
                return;
            }
        }

        window.build().open();
    }

    /**
     * Closes the inventory for every player.
     */
    public void closeForAllViewers() {
        if (gui != null) {
            gui.closeForAllViewers();
        }
    }
}
