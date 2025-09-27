package dev.starless.inventories.serialization;

import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.ConfigurableItem;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;

public class InventorySerializer implements TypeSerializer<ConfigurableInventory> {

    private static final String NODE_TITLE = "title";
    private static final String NODE_ITEMS = "items";

    @Override
    public ConfigurableInventory deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        final ConfigurableInventory.Builder builder = ConfigurableInventory.builder();

        // Set title
        final ConfigurationNode titleNode = node.node(NODE_TITLE);
        if (!titleNode.virtual()) {
            builder.title(titleNode.getString());
        }

        // Set items
        final ConfigurationNode itemsNode = node.node(NODE_ITEMS);
        if (itemsNode.isMap()) {
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : itemsNode.childrenMap().entrySet()) {
                if (entry.getKey() instanceof String key) {
                    final ConfigurableItem item = entry.getValue().get(ConfigurableItem.class);
                    if (item != null) {
                        builder.item(key, item);
                    }
                }
            }
        }

        return builder.build();
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable ConfigurableInventory inventory,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (inventory == null) {
            node.raw(null);
            return;
        }

        // Set title
        if (inventory.getTitle() != null) {
            node.node(NODE_TITLE).set(inventory.getTitle());
        }

        // Set items
        if (inventory.getItems() != null && !inventory.getItems().isEmpty()) {
            final ConfigurationNode itemsNode = node.node(NODE_ITEMS);
            for (Map.Entry<String, ConfigurableItem> entry : inventory.getItems().entrySet()) {
                itemsNode.node(entry.getKey()).set(TypeToken.get(ConfigurableItem.class), entry.getValue());
            }
        }
    }
}
