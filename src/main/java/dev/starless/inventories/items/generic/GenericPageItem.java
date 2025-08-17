package dev.starless.inventories.items.generic;

import dev.starless.inventories.ConfigurableItem;
import org.bukkit.Material;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.PageItem;

public class GenericPageItem extends PageItem {

    protected final ConfigurableItem item;
    private final Type type;

    public GenericPageItem(final ConfigurableItem item, final Type type) {
        super(type.equals(Type.FORWARD));

        this.item = item;
        this.type = type;
    }

    @Override
    public ItemProvider getItemProvider(final PagedGui<?> gui) {
        return this.isVisible(gui) ? item.asItemProvider() : new ItemBuilder(Material.AIR);
    }

    public boolean isVisible(final PagedGui<?> gui) {
        if (type.equals(Type.FORWARD)) {
            return gui.hasNextPage();
        } else {
            return gui.hasPreviousPage();
        }
    }

    public enum Type {
        FORWARD,
        BACKWARD,
    }
}
