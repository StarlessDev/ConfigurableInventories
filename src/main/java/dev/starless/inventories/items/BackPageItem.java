package dev.starless.inventories.items;

import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.items.generic.GenericPageItem;

/**
 * This item is used to navigate to the previous page of a PagedGui.
 */
public class BackPageItem extends GenericPageItem {

    public BackPageItem(ConfigurableItem item) {
        super(item, Type.BACKWARD);
    }
}
