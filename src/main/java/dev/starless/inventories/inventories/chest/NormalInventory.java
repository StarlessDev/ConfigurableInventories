package dev.starless.inventories.inventories.chest;

import ch.jalu.configme.properties.Property;
import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.inventories.GenericInventory;
import xyz.xenondevs.invui.gui.Gui;

/**
 * Abstract base class for normal (non-paged) chest inventories.
 */
public abstract class NormalInventory extends GenericInventory<Gui, Gui.Builder.Normal> {

    public NormalInventory(Property<ConfigurableInventory> inventory) {
        super(inventory);
    }

    @Override
    public Gui.Builder.Normal getBuilder() {
        return Gui.normal();
    }
}
