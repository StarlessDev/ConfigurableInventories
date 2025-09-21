package dev.starless.inventories;

import dev.starless.inventories.adventure.ColorUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents an item you can use in guis.
 * Can be converted to an {@link ItemStack}.
 * <br>
 * You can edit this item by calling {@link ConfigurableItem#edit()} or
 * just using the setters.
 */

@Setter
@Getter
public class ConfigurableItem {

    /**
     * Creates a ConfigurableItem from an ItemStack.
     * Note that this will not copy all the data from the ItemStack,
     * only the material, amount, display name, lore, item flags and custom model data.
     *
     * @param item the {@link ItemStack} to convert
     * @return a new {@link ConfigurableItem} instance
     */
    public static ConfigurableItem fromItemStack(final ItemStack item) {
        final ConfigurableItem.Builder builder = ConfigurableItem.builder()
                .material(item.getType())
                .amount(item.getAmount());

        if (item.hasItemMeta()) {
            final ItemMeta meta = item.getItemMeta();
            final List<Component> components = Objects.requireNonNullElse(meta.lore(), Collections.emptyList());
            builder.lore(components.stream().map(ColorUtils::string).toList())
                    .modelData(meta.getCustomModelData());

            meta.getItemFlags().forEach(builder::addFlag);
        }

        return builder.build();
    }

    /**
     * Returns a ConfigurableItem builder class.
     *
     * @return a {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for {@link ConfigurableItem}.
     * Can be instantiated only with {@link ConfigurableItem#builder()}
     */
    public static class Builder {

        private final ConfigurableItem item;

        private Builder() {
            item = new ConfigurableItem();
        }

        /**
         * Change the item material.
         *
         * @param material the new {@link Material}
         * @return this builder
         */
        public Builder material(final Material material) {
            item.setMaterial(material);
            return this;
        }

        /**
         * Change the item display name.
         *
         * @param name the new display name
         * @return this builder
         */
        public Builder name(final String name) {
            item.setDisplayName(name);
            return this;
        }

        /**
         * Adds a new line to the current item lore.
         *
         * @param line {@link String} to add
         * @return this builder
         */
        public Builder addLore(final String line) {
            item.getLore().add(line);
            return this;
        }

        /**
         * Sets the lore of the item.
         * This will erase any lore set previously!
         *
         * @param lore new lines
         * @return this builder
         */
        public Builder lore(final String... lore) {
            return this.lore(Arrays.asList(lore));
        }

        /**
         * Sets the lore of the item.
         * This will erase any lore set previously!
         *
         * @param lore new lines
         * @return this builder
         */
        public Builder lore(final List<String> lore) {
            item.getLore().clear();
            item.getLore().addAll(lore);
            return this;
        }

        /**
         * Adds a new {@link ItemFlag} to the item.
         *
         * @param flag the {@link ItemFlag} to add
         * @return this builder
         */
        public Builder addFlag(final ItemFlag flag) {
            item.getFlags().add(flag);
            return this;
        }

        /**
         * Sets the item flags.
         * This will erase any flags set previously!
         *
         * @param flags new {@link ItemFlag} array
         * @return this builder
         */
        public Builder flags(final ItemFlag... flags) {
            return this.flags(Arrays.asList(flags));
        }

        /**
         * Sets the item flags.
         * This will erase any flags set previously!
         *
         * @param flags new {@link ItemFlag} list
         * @return this builder
         */
        public Builder flags(final List<ItemFlag> flags) {
            item.getFlags().clear();
            item.getFlags().addAll(flags);
            return this;
        }

        /**
         * Sets the item amount.
         *
         * @param amount the new amount
         * @return this builder
         */
        public Builder amount(final int amount) {
            item.setAmount(amount);
            return this;
        }

        /**
         * Sets the custom model data for the item.
         *
         * @param modelData the custom model data value
         * @return this builder
         */
        public Builder modelData(final int modelData) {
            item.setCustomModelData(modelData);
            return this;
        }

        /**
         * @return a {@link ConfigurableItem} instance
         */
        public ConfigurableItem build() {
            return item;
        }
    }

    private @NotNull Material material;
    private @NotNull String displayName;
    private List<String> lore = new ArrayList<>();
    private Set<ItemFlag> flags = new HashSet<>();

    private int amount = 1;
    private int customModelData = -1;

    /**
     * Returns a builder for modifying this object.
     *
     * @return a new {@link Builder} with the current values of this object
     */
    public Builder edit() {
        return new Builder()
                .material(this.getMaterial())
                .name(this.getDisplayName())
                .lore(this.getLore().toArray(new String[0]))
                .flags(this.getFlags().toArray(new ItemFlag[0]))
                .amount(this.getAmount())
                .modelData(this.getCustomModelData());
    }

    /**
     * Converts this object to an {@link ItemStack}
     * without applying placeholders.
     *
     * @return the resulting {@link ItemStack} object
     */
    public ItemStack asItemStack() {
        return this.asItemStack(Collections.emptyList());
    }

    /**
     * Converts this object to an {@link ItemStack} applying the provided placeholders.
     *
     * @param placeholders list of {@link ItemPlaceholder} to apply
     * @return the resulting {@link ItemStack} object
     */
    public ItemStack asItemStack(List<ItemPlaceholder> placeholders) {
        final ItemStack is = new ItemStack(material, amount);
        is.editMeta(meta -> {
            meta.displayName(this.processString(placeholders, displayName));
            meta.lore(lore.stream().map(str -> this.processString(placeholders, str)).toList());
            meta.addItemFlags(flags.toArray(ItemFlag[]::new));

            if (customModelData != -1) {
                meta.setCustomModelData(customModelData);
            }
        });
        return is;
    }

    /**
     * Creates a copy of this item.
     * The copy will have the same material, display name, lore, flags, amount and custom model data.
     *
     * @return a new {@link ConfigurableItem} instance with the same properties
     */
    public ConfigurableItem copy() {
        return ConfigurableItem.builder()
                .material(this.getMaterial())
                .name(this.getDisplayName())
                .lore(this.getLore().toArray(new String[0]))
                .flags(this.getFlags().toArray(new ItemFlag[0]))
                .amount(this.getAmount())
                .modelData(this.getCustomModelData())
                .build();
    }

    private Component processString(List<ItemPlaceholder> placeholders, String str) {
        for (ItemPlaceholder placeholder : placeholders) {
            str = placeholder.apply(str);
        }
        return ColorUtils.component(str).decoration(TextDecoration.ITALIC, false);
    }
}