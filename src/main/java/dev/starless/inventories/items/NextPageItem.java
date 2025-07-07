package dev.starless.inventories.items;

import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.items.generic.GenericPageItem;

/**
 * This item is used to navigate to the next page of a PagedGui.
 */
public class NextPageItem extends GenericPageItem {

    public NextPageItem(ConfigurableItem item) {
        super(item, Type.FORWARD);
    }
}
