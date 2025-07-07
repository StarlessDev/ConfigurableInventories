package dev.starless.inventories;

import dev.starless.inventories.items.generic.GenericItem;
import xyz.xenondevs.invui.item.Click;

/**
 * Represents an action that is performed when an item is clicked.
 * This interface is used to handle item clicks in inventories.
 */
public interface ItemClick {

    /**
     * Handles the click event on a specific item.
     *
     * @param item the item that was clicked
     * @param click the click event
     */
    void handle(final GenericItem item, final Click click);
}
