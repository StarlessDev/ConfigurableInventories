package dev.starless.inventories.inventories.chest;

import ch.jalu.configme.properties.Property;
import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.inventories.GenericInventory;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.Item;

import java.util.List;

public abstract class PagedInventory extends GenericInventory<PagedGui<Item>, PagedGui.Builder<Item>> {

    public PagedInventory(Property<ConfigurableInventory> inventory) {
        super(inventory);
    }

    @Override
    public PagedGui.Builder<Item> getBuilder() {
        return PagedGui.items();
    }

    protected abstract List<Item> getContent();

    protected void updateItems() {
        final PagedGui<Item> gui = this.gui;
        gui.setContent(this.getContent());
        gui.bake();
    }
}
