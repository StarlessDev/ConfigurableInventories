package dev.starless.inventories.serialization;

import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.ConfigurablePotionMeta;
import io.leangen.geantyref.TypeToken;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
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

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ItemSerializer implements TypeSerializer<ConfigurableItem> {

    private static final String NODE_MATERIAL = "material";
    private static final String NODE_DISPLAY_NAME = "displayName";
    private static final String NODE_AMOUNT = "amount";
    private static final String NODE_CUSTOM_MODEL_DATA = "customModelData";
    private static final String NODE_LORE = "lore";
    private static final String NODE_FLAGS = "flags";
    private static final String NODE_ENCHANTMENTS = "enchantments";
    private static final String NODE_POTION_META = "potion-meta";

    @Override
    public ConfigurableItem deserialize(@NotNull Type type, @NotNull ConfigurationNode node) throws SerializationException {
        final ConfigurableItem.Builder builder = ConfigurableItem.builder();

        // Get item material
        final String materialId = node.node(NODE_MATERIAL).getString();
        if (materialId == null) {
            throw new SerializationException("Item material is required");
        }

        final Material material = Registry.MATERIAL.get(NamespacedKey.minecraft(materialId.toLowerCase()));
        builder.material(Objects.requireNonNullElse(material, Material.BARRIER));

        // Other item properties
        final ConfigurationNode nameNode = node.node(NODE_DISPLAY_NAME);
        if (!node.virtual()) {
            builder.name(nameNode.getString());
        }
        final ConfigurationNode amountNode = node.node(NODE_AMOUNT);
        if (!amountNode.virtual()) {
            builder.amount(amountNode.getInt(1));
        }
        final ConfigurationNode cmdNode = node.node(NODE_CUSTOM_MODEL_DATA);
        if (!cmdNode.virtual()) {
            builder.modelData(cmdNode.getInt(-1));
        }

        // Set lore
        final ConfigurationNode loreNode = node.node(NODE_LORE);
        if (!loreNode.virtual() && loreNode.isList()) {
            builder.lore(node.node(NODE_LORE).getList(String.class));
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

            builder.flags(flags);
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
            builder.enchantments(enchantments);
        }

        final ConfigurationNode potionMetaNode = node.node(NODE_POTION_META);
        if (!potionMetaNode.virtual()) {
            builder.potionMeta(potionMetaNode.get(ConfigurablePotionMeta.class));
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
        node.node(NODE_DISPLAY_NAME).set(item.getDisplayName());

        final int amount = item.getAmount();
        if (amount != 1) {
            node.node(NODE_AMOUNT).set(amount);
        }

        final int customModelData = item.getCustomModelData();
        if (customModelData != -1) {
            node.node(NODE_CUSTOM_MODEL_DATA).set(customModelData);
        }

        // Export lists and sets
        if (item.getLore() != null && !item.getLore().isEmpty()) {
            node.node(NODE_LORE).set(item.getLore());
        }

        if (item.getFlags() != null && !item.getFlags().isEmpty()) {
            node.node(NODE_FLAGS).set(item.getFlags().stream().map(Enum::name).toList());
        }

        if (item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
            Map<String, Integer> enchantments = new HashMap<>();
            item.getEnchantments().forEach((enchantment, level) -> {
                enchantments.put(enchantment.getKey().getKey(), level);
            });
            node.node(NODE_ENCHANTMENTS).set(enchantments);
        }

        if (item.getPotionMeta() != null) {
            node.node(NODE_POTION_META).set(TypeToken.get(ConfigurablePotionMeta.class), item.getPotionMeta());
        }
    }
}
