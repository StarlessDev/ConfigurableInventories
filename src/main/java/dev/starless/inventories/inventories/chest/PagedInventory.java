package dev.starless.inventories.inventories.chest;

import ch.jalu.configme.properties.Property;
import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.inventories.GenericInventory;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.Item;

import java.util.List;

/**
 * Abstract base class for paged chest inventories.
 */
public abstract class PagedInventory extends GenericInventory<PagedGui<Item>, PagedGui.Builder<Item>> {

    public PagedInventory(Property<ConfigurableInventory> inventory) {
        super(inventory);
    }

    @Override
    public PagedGui.Builder<Item> getBuilder() {
        return PagedGui.items();
    }

    /**
     * Retrieves the content for the paged GUI.
     * @return a List of {@link Item items}
     */
    protected abstract List<Item> getContent();

    /**
     * Updates the items in the paged GUI using
     * the items provided by {@link #getContent()}.
     * and rebakes it to reflect changes.
     */
    protected void updateItems() {
        final PagedGui<Item> gui = this.gui;
        gui.setContent(this.getContent());
        gui.bake();
    }
}
