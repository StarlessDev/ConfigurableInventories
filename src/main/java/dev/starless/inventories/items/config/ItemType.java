package dev.starless.inventories.items.config;

import ch.jalu.configme.beanmapper.DefaultMapper;
import ch.jalu.configme.beanmapper.Mapper;
import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import dev.starless.inventories.ConfigurableItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A ConfigMe Property type for {@link ConfigurableItem} objects.
 * This class handles the conversion between a map representation of an item
 * and the actual {@link ConfigurableItem} object.
 */
public class ItemType implements PropertyType<ConfigurableItem> {

    @Override
    public @Nullable ConfigurableItem convert(@Nullable Object obj,
                                              @NotNull ConvertErrorRecorder recorder) {
        if (obj instanceof Map<?, ?> map) {
            final ConfigurableItem item = new ConfigurableItem();

            // Get item material
            this.readStringProperty(map.get("material"), id -> {
                Material material = null;
                if (id != null) {
                    material = Registry.MATERIAL.get(NamespacedKey.minecraft(id.toLowerCase()));
                }
                item.setMaterial(Objects.requireNonNullElse(material, Material.BARRIER));
            });
            // Other item properties
            this.readStringProperty(map.get("displayName"), name -> {
                item.setDisplayName(Objects.requireNonNullElse(name, ""));
            });
            this.readIntegerProperty(map.get("amount"), amount -> {
                item.setAmount(Objects.requireNonNullElse(amount, 1));
            });
            this.readIntegerProperty(map.get("customModelData"), data -> {
                item.setCustomModelData(Objects.requireNonNullElse(data, -1));
            });

            // Set lore
            this.readStringListProperty(map.get("lore"), item::setLore);
            // Set item flags
            this.readStringListProperty(map.get("flags"), strings -> {
                final Set<ItemFlag> flags = strings.stream()
                        .map(str -> {
                            try {
                                return ItemFlag.valueOf(str.toUpperCase());
                            } catch (IllegalArgumentException ex) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());
                item.setFlags(flags);
            });

            return item;
        }

        return null;
    }

    private void readStringListProperty(@NotNull Object o,
                                        @NotNull Consumer<List<String>> consumer) {
        if (o instanceof Collection<?> collection) {
            consumer.accept(collection.stream()
                    .map(el -> {
                        if (el instanceof String str) {
                            return str;
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList());
        }
    }

    private void readIntegerProperty(final Object o,
                                     final Consumer<Integer> action) {
        this.readProperty(o, Integer.class, action);
    }

    private void readStringProperty(final Object o,
                                    final Consumer<String> action) {
        this.readProperty(o, String.class, action);
    }

    private <T> void readProperty(final Object o,
                                  final Class<T> clazz,
                                  final Consumer<T> action) {
        if (o == null) return;

        if (o.getClass().equals(clazz)) {
            action.accept(clazz.cast(o));
        }
    }

    @Override
    public @Nullable Object toExportValue(@Nullable ConfigurableItem item) {
        if (item == null) return null;

        final Mapper mapper = DefaultMapper.getInstance();
        final Map<String, Object> map = new HashMap<>();
        // The material should never be null
        map.put("material", Objects.requireNonNullElse(item.getMaterial(), Material.BARRIER));

        // Export item attributes if necessary
        final String displayName = item.getDisplayName();
        if (!displayName.isBlank()) {
            map.put("displayName", displayName);
        }
        final int amount = item.getAmount();
        if (amount != 1) {
            map.put("amount", amount);
        }
        final int customModelData = item.getCustomModelData();
        if (customModelData != -1) {
            map.put("customModelData", customModelData);
        }

        // Export lists and sets
        final Object exportedLore = mapper.toExportValue(item.getLore());
        if (exportedLore != null) {
            map.put("lore", exportedLore);
        }
        final Object exportedFlags = mapper.toExportValue(item.getFlags());
        if (exportedFlags != null) {
            map.put("flags", exportedFlags);
        }

        return map;
    }
}
