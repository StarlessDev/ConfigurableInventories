package dev.starless.inventories.items.generic;

import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.items.placeholders.ItemPlaceholder;
import lombok.Getter;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class GenericItem extends AbstractItem {

    @Getter
    private final char key;

    private final ConfigurableItem item;
    private final Consumer<ConfigurableItem.Builder> editor;
    private final List<ItemPlaceholder> placeholders;

    public GenericItem(final ConfigurableInventory inv, final char key) {
        this(inv, key, Collections.emptyList(), null);
    }

    public GenericItem(final ConfigurableInventory inv,
                       final char key,
                       final List<ItemPlaceholder> placeholders) {
        this(inv, key, placeholders, null);
    }

    public GenericItem(final ConfigurableInventory inv,
                       final char key,
                       final Consumer<ConfigurableItem.Builder> edit) {
        this(inv, key, Collections.emptyList(), edit);
    }

    public GenericItem(final ConfigurableInventory inv,
                       final char key,
                       final List<ItemPlaceholder> placeholders,
                       final Consumer<ConfigurableItem.Builder> edit) {
        this.item = inv.getItem(key);
        if (item == null) throw new IllegalArgumentException("Item cannot be null");

        this.editor = edit;
        this.placeholders = placeholders;
        this.key = key;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (editor != null) {
            final ConfigurableItem.Builder builder = item.edit();
            editor.accept(builder);
            return builder.build().asItemProvider(placeholders);
        } else {
            return item.asItemProvider(placeholders);
        }
    }
}
