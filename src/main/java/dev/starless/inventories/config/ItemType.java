package dev.starless.inventories.config;

import ch.jalu.configme.beanmapper.DefaultMapper;
import ch.jalu.configme.beanmapper.Mapper;
import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import dev.starless.inventories.ConfigurableItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
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

    private static final String MATERIAL_NODE = "material";
    private static final String DISPLAY_NAME_NODE = "displayName";
    private static final String AMOUNT_NODE = "amount";
    private static final String CUSTOM_MODEL_DATA_NODE = "customModelData";
    private static final String LORE_NODE = "lore";
    private static final String FLAGS_NODE = "flags";
    private static final String ENCHANTMENTS_NODE = "enchantments";

    @Override
    public @Nullable ConfigurableItem convert(@Nullable Object obj,
                                              @NotNull ConvertErrorRecorder recorder) {
        if (obj instanceof Map<?, ?> map) {
            final ConfigurableItem item = new ConfigurableItem();

            // Get item material
            this.readStringProperty(map.get(MATERIAL_NODE), id -> {
                Material material = null;
                if (id != null) {
                    material = Registry.MATERIAL.get(NamespacedKey.minecraft(id.toLowerCase()));
                }
                item.setMaterial(Objects.requireNonNullElse(material, Material.BARRIER));
            });
            // Other item properties
            this.readStringProperty(map.get(DISPLAY_NAME_NODE), name -> {
                item.setDisplayName(Objects.requireNonNullElse(name, ""));
            });
            this.readIntegerProperty(map.get(AMOUNT_NODE), amount -> {
                item.setAmount(Objects.requireNonNullElse(amount, 1));
            });
            this.readIntegerProperty(map.get(CUSTOM_MODEL_DATA_NODE), data -> {
                item.setCustomModelData(Objects.requireNonNullElse(data, -1));
            });

            // Set lore
            this.readStringListProperty(map.get(LORE_NODE), item::setLore);
            // Set item flags
            this.readStringListProperty(map.get(FLAGS_NODE), strings -> {
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

            // Set enchantments
            this.readMapProperty(map.get(ENCHANTMENTS_NODE), stringObjectMap -> {
                final Map<Enchantment, Integer> enchantments = new HashMap<>();
                stringObjectMap.forEach((key, value) -> {
                    if (value instanceof Integer level) {
                        final Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(key.toLowerCase()));
                        if (enchantment != null) {
                            enchantments.put(enchantment, level);
                        }
                    }
                });
                item.setEnchantments(enchantments);
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

    private void readMapProperty(final Object o, final Consumer<Map<String, Object>> action) {
        if (o instanceof Map<?, ?> map) {
            final Map<String, Object> newMap = new HashMap<>();
            map.forEach((key, value) -> {
                if (key instanceof String s) {
                    newMap.put(s, value);
                }
            });
            action.accept(newMap);
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
        map.put(MATERIAL_NODE, item.getMaterial());

        // Export item attributes if necessary
        final String displayName = item.getDisplayName();
        if (!displayName.isBlank()) {
            map.put(DISPLAY_NAME_NODE, displayName);
        }
        final int amount = item.getAmount();
        if (amount != 1) {
            map.put(AMOUNT_NODE, amount);
        }
        final int customModelData = item.getCustomModelData();
        if (customModelData != -1) {
            map.put(CUSTOM_MODEL_DATA_NODE, customModelData);
        }

        // Export lists and sets
        final Object exportedLore = mapper.toExportValue(item.getLore());
        if (exportedLore != null) {
            map.put(LORE_NODE, exportedLore);
        }
        final Object exportedFlags = mapper.toExportValue(item.getFlags());
        if (exportedFlags != null) {
            map.put(FLAGS_NODE, exportedFlags);
        }

        final Map<String, String> enchantments = new HashMap<>();
        if (item.getEnchantments() != null) {
            item.getEnchantments().forEach((enchantment, level) -> {
                enchantments.put(enchantment.getKey().getKey(), String.valueOf(level));
            });
        }

        if (!enchantments.isEmpty()) {
            map.put(ENCHANTMENTS_NODE, enchantments);
        }

        return map;
    }
}
