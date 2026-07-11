package dev.starless.inventories.serialization;

import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.ConfigurablePotionComponent;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ItemSerializer implements TypeSerializer<ConfigurableItem> {

    private static final String NODE_MATERIAL = "material";
    private static final String NODE_DISPLAY_NAME = "name";
    private static final String NODE_AMOUNT = "amount";
    private static final String NODE_CUSTOM_MODEL_DATA_FLOATS = "customModelData-floats";
    private static final String NODE_CUSTOM_MODEL_DATA_BOOLEANS = "customModelData-booleans";
    private static final String NODE_CUSTOM_MODEL_DATA_STRINGS = "customModelData-strings";
    private static final String NODE_CUSTOM_MODEL_DATA_COLORS = "customModelData-colors";
    private static final String NODE_LORE = "lore";
    private static final String NODE_FLAGS = "flags";
    private static final String NODE_ENCHANTMENTS = "enchantments";
    private static final String NODE_POTION_META = "potion-meta";
    private static final String NODE_UNBREAKABLE = "unbreakable";
    private static final String NODE_ENCHANTMENT_GLINT = "enchantment-glint";

    @Override
    public ConfigurableItem deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        // Get item material
        final String materialId = node.node(NODE_MATERIAL).getString();
        if (materialId == null) {
            throw new SerializationException("Item material is required");
        }

        final Material material = Registry.MATERIAL.get(NamespacedKey.minecraft(materialId.toLowerCase()));
        final ConfigurableItem.Builder builder = ConfigurableItem.builder(material);

        // Other item properties
        final ConfigurationNode nameNode = node.node(NODE_DISPLAY_NAME);
        if (!node.virtual()) {
            builder.name(nameNode.get(Component.class));
        }
        final ConfigurationNode amountNode = node.node(NODE_AMOUNT);
        if (!amountNode.virtual()) {
            builder.amount(amountNode.getInt(1));
        }

        final ConfigurationNode unbreakableNode = node.node(NODE_UNBREAKABLE);
        if (!unbreakableNode.virtual()) {
            builder.unbreakable(unbreakableNode.getBoolean(false));
        }
        final ConfigurationNode glintNode = node.node(NODE_ENCHANTMENT_GLINT);
        if (!glintNode.virtual()) {
            builder.enchantmentGlintOverride(glintNode.getBoolean(false));
        }

        // Set lore
        final ConfigurationNode loreNode = node.node(NODE_LORE);
        if (!loreNode.virtual() && loreNode.isList()) {
            builder.addLore(node.node(NODE_LORE).getList(Component.class));
        }

        // Set item flags
        List<String> flagStrings = node.node(NODE_FLAGS).getList(String.class);
        if (flagStrings != null) {
            final Set<ItemFlag> flags = flagStrings.stream()
                    .map(str -> {
                        try {
                            return ItemFlag.valueOf(str.toUpperCase());
                        } catch (IllegalArgumentException ex) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            builder.addFlags(flags);
        }

        // Set enchantments
        ConfigurationNode enchantmentsNode = node.node(NODE_ENCHANTMENTS);
        if (enchantmentsNode.isMap()) {
            final Registry<@NotNull Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
            final Map<Enchantment, Integer> enchantments = new HashMap<>();
            for (Map.Entry<Object, ? extends ConfigurationNode> entry : enchantmentsNode.childrenMap().entrySet()) {
                if (entry.getKey() instanceof String key) {
                    final Enchantment enchantment = registry.get(NamespacedKey.minecraft(key.toLowerCase()));
                    if (enchantment != null) {
                        enchantments.put(enchantment, entry.getValue().getInt());
                    }
                }
            }
            builder.addEnchants(enchantments);
        }

        final ConfigurationNode potionMetaNode = node.node(NODE_POTION_META);
        if (!potionMetaNode.virtual()) {
            builder.potionComponent(potionMetaNode.get(ConfigurablePotionComponent.class));
        }

        // Set custom model data floats
        final ConfigurationNode customModelDataFloatsNode = node.node(NODE_CUSTOM_MODEL_DATA_FLOATS);
        if (!customModelDataFloatsNode.virtual() && customModelDataFloatsNode.isList()) {
            List<Float> floatList = customModelDataFloatsNode.getList(Float.class);
            if (floatList != null) {
                builder.setFloatCustomModelData(new FloatArrayList(floatList));
            }
        }

        // Set custom model data booleans
        final ConfigurationNode customModelDataBooleansNode = node.node(NODE_CUSTOM_MODEL_DATA_BOOLEANS);
        if (!customModelDataBooleansNode.virtual() && customModelDataBooleansNode.isList()) {
            List<Boolean> booleanList = customModelDataBooleansNode.getList(Boolean.class);
            if (booleanList != null) {
                builder.setBooleanCustomModelData(new BooleanArrayList(booleanList));
            }
        }

        // Set custom model data strings
        final ConfigurationNode customModelDataStringsNode = node.node(NODE_CUSTOM_MODEL_DATA_STRINGS);
        if (!customModelDataStringsNode.virtual() && customModelDataStringsNode.isList()) {
            final List<String> stringList = customModelDataStringsNode.getList(String.class);
            if (stringList != null) {
                builder.setStringCustomModelData(stringList);
            }
        }

        // Set custom model data colors
        final ConfigurationNode customModelDataColorsNode = node.node(NODE_CUSTOM_MODEL_DATA_COLORS);
        if (!customModelDataColorsNode.virtual() && customModelDataColorsNode.isList()) {
            builder.setColorCustomModelData(customModelDataColorsNode.getList(Color.class));
        }

        return builder.build();
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable ConfigurableItem item,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (item == null) {
            node.raw(null);
            return;
        }

        // Material is never null
        node.node(NODE_MATERIAL).set(item.getMaterial().getKey().getKey());
        node.node(NODE_DISPLAY_NAME).set(Component.class, item.getName());

        final int amount = item.getAmount();
        if (amount != 1) {
            node.node(NODE_AMOUNT).set(amount);
        }

        // Export custom model data lists
        if (item.getCustomModelDataFloats() != null && !item.getCustomModelDataFloats().isEmpty()) {
            node.node(NODE_CUSTOM_MODEL_DATA_FLOATS).set(item.getCustomModelDataFloats());
        }

        if (item.getCustomModelDataBooleans() != null && !item.getCustomModelDataBooleans().isEmpty()) {
            node.node(NODE_CUSTOM_MODEL_DATA_BOOLEANS).set(item.getCustomModelDataBooleans());
        }

        if (item.getCustomModelDataStrings() != null && !item.getCustomModelDataStrings().isEmpty()) {
            node.node(NODE_CUSTOM_MODEL_DATA_STRINGS).set(item.getCustomModelDataStrings());
        }

        if (item.getCustomModelDataColors() != null && !item.getCustomModelDataColors().isEmpty()) {
            List<Integer> colorValues = new ArrayList<>();
            for (Color color : item.getCustomModelDataColors()) {
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                int rgb = (red << 16) | (green << 8) | blue;
                colorValues.add(rgb);
            }
            node.node(NODE_CUSTOM_MODEL_DATA_COLORS).set(colorValues);
        }

        // Export lists and sets
        if (item.getLore() != null && !item.getLore().isEmpty()) {
            node.node(NODE_LORE).setList(Component.class, item.getLore());
        }

        if (item.getFlags() != null && !item.getFlags().isEmpty()) {
            node.node(NODE_FLAGS).set(item.getFlags().stream().map(Enum::name).toList());
        }
        node.node(NODE_UNBREAKABLE).set(item.isUnbreakable());
        node.node(NODE_ENCHANTMENT_GLINT).set(item.isGlint());

        if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
            Map<String, Integer> enchantments = new HashMap<>();
            item.getEnchantments().forEach((enchantment, level) -> enchantments.put(enchantment.getKey().getKey(), level));
            node.node(NODE_ENCHANTMENTS).set(enchantments);
        }

        if (item.getPotionComponent() != null) {
            node.node(NODE_POTION_META).set(TypeToken.get(ConfigurablePotionComponent.class), item.getPotionComponent());
        }
    }
}
