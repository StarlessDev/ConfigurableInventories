package dev.starless.inventories.config;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.ConfigurableItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InventoryType implements PropertyType<ConfigurableInventory> {

    private final ItemType itemType = new ItemType();

    @Override
    public @Nullable ConfigurableInventory convert(@Nullable Object object,
                                                   @NotNull ConvertErrorRecorder errorRecorder) {
        if (object instanceof Map<?, ?> map) {
            String title = Objects.toString(map.get("title"), "");

            final Map<Character, ConfigurableItem> items = new HashMap<>();
            if (map.get("items") instanceof Map<?, ?> itemsMap) {
                for (Map.Entry<?, ?> entry : itemsMap.entrySet()) {
                    final ConfigurableItem item = itemType.convert(entry.getValue(), errorRecorder);
                    if (item != null) {
                        items.put((char) entry.getKey(), item);
                    }
                }
            }

            return ConfigurableInventory.builder()
                    .title(title)
                    .items(items)
                    .build();
        }
        return null;
    }

    @Override
    public @Nullable Object toExportValue(@Nullable ConfigurableInventory value) {
        if (value == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("title", value.getTitle());

        Map<Character, Object> itemsMap = new HashMap<>();
        for (Map.Entry<Character, ConfigurableItem> entry : value.getItems().entrySet()) {
            itemsMap.put(entry.getKey(), itemType.toExportValue(entry.getValue()));
        }
        map.put("items", itemsMap);

        return map;
    }
}
