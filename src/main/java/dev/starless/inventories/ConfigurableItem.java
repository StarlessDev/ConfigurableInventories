package dev.starless.inventories;

import dev.starless.inventories.i18n.I18n;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * Represents an item you can use in guis.
 * Can be converted to an {@link ItemStack}.
 * <br>
 * You can edit this item by calling {@link ConfigurableItem#edit()} or
 * just using the setters.
 */

public final class ConfigurableItem {

    /**
     * Returns a ConfigurableItem builder class.
     *
     * @return a {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns a ConfigurableItem builder class
     * initialized already with a material.
     *
     * @return a {@link Builder} instance
     */
    public static Builder builder(final Material material) {
        return new Builder(material);
    }

    /**
     * Builder class for {@link ConfigurableItem}.
     * Can be instantiated only with {@link ConfigurableItem#builder(Material)}
     */
    public static class Builder {

        private final ConfigurableItem item;

        private Builder() {
            item = new ConfigurableItem();
        }

        private Builder(Material material) {
            item = new ConfigurableItem(material);
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
        public Builder name(final Component name) {
            item.setName(name);
            return this;
        }

        /**
         * Adds a new line to the current item lore.
         *
         * @param line {@link String} to add
         * @return this builder
         */
        public Builder addLore(@NotNull final Component line) {
            if (item.getLore() == null) {
                item.setLore(new ArrayList<>());
            }
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
        public Builder addLore(@NotNull final Component... lore) {
            return this.addLore(Arrays.asList(lore));
        }

        /**
         * Sets the lore of the item.
         * This will erase any lore set previously!
         *
         * @param lore new lines
         * @return this builder
         */
        public Builder addLore(@NotNull final List<Component> lore) {
            if (item.getLore() == null) {
                item.setLore(new ArrayList<>());
            }
            item.getLore().addAll(lore);
            return this;
        }

        /***
         * Adds a new enchantment to the item.
         *
         * @param enchantment the {@link Enchantment} to add
         * @param level       the level of the enchantment (must be greater than 0)
         * @return this builder
         */
        public Builder addEnchantment(@NotNull final Enchantment enchantment, final int level) {
            if (item.getEnchantments() == null) {
                item.setEnchantments(new HashMap<>());
            }
            item.getEnchantments().put(enchantment, level);
            return this;
        }

        /**
         * Sets the enchantments of the item.
         * This will erase any enchantments set previously!
         * The level of an enchantment must be greater than 0.
         *
         * @param enchantments new enchantments map
         * @return this builder
         */
        public Builder addEnchants(@Nullable final Map<Enchantment, Integer> enchantments) {
            if (enchantments == null) return this;

            if (item.getEnchantments() == null) {
                item.setEnchantments(new HashMap<>());
            }
            item.getEnchantments().putAll(enchantments);
            return this;
        }

        /**
         * Adds a new {@link ItemFlag} to the item.
         *
         * @param flag the {@link ItemFlag} to add
         * @return this builder
         */
        public Builder addFlag(@NotNull final ItemFlag flag) {
            if (item.getFlags() == null) {
                item.setFlags(new HashSet<>());
            }
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
        public Builder addFlags(@NotNull final ItemFlag... flags) {
            return this.addFlags(Arrays.asList(flags));
        }

        /**
         * Sets the item flags.
         * This will erase any flags set previously!
         *
         * @param flags new {@link ItemFlag} list
         * @return this builder
         */
        public Builder addFlags(@NotNull final Collection<ItemFlag> flags) {
            if (item.getFlags() == null) {
                item.setFlags(new HashSet<>());
            }
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
            item.setAmount(Math.max(1, amount));
            return this;
        }

        /**
         * Adds an integer value to the custom model data list (converted to float).
         *
         * @param value the integer value to add
         * @return this builder
         */
        public Builder addIntCustomModelData(final int value) {
            if (item.getCustomModelDataFloats() == null) {
                item.setCustomModelDataFloats(new FloatArrayList());
            }
            item.getCustomModelDataFloats().add(value);
            return this;
        }

        /**
         * Adds a double value to the custom model data list (converted to float).
         *
         * @param value the double value to add
         * @return this builder
         */
        public Builder addDoubleCustomModelData(final double value) {
            if (item.getCustomModelDataFloats() == null) {
                item.setCustomModelDataFloats(new FloatArrayList());
            }
            item.getCustomModelDataFloats().add((float) value);
            return this;
        }

        /**
         * Adds a float value to the custom model data list.
         *
         * @param value the float value to add
         * @return this builder
         */
        public Builder addFloatCustomModelData(final float value) {
            if (item.getCustomModelDataFloats() == null) {
                item.setCustomModelDataFloats(new FloatArrayList());
            }
            item.getCustomModelDataFloats().add(value);
            return this;
        }

        /**
         * Sets the entire float custom model data list.
         *
         * @param list the list of float values to set (can be null)
         * @return this builder
         */
        public Builder setFloatCustomModelData(@Nullable final FloatList list) {
            item.setCustomModelDataFloats(list);
            return this;
        }

        /**
         * Adds a boolean value to the custom model data list.
         *
         * @param value the boolean value to add
         * @return this builder
         */
        public Builder addBooleanCustomModelData(final boolean value) {
            if (item.getCustomModelDataBooleans() == null) {
                item.setCustomModelDataBooleans(new BooleanArrayList());
            }
            item.getCustomModelDataBooleans().add(value);
            return this;
        }

        /**
         * Sets the entire boolean custom model data list.
         *
         * @param list the list of boolean values to set (can be null)
         * @return this builder
         */
        public Builder setBooleanCustomModelData(@Nullable final BooleanList list) {
            item.setCustomModelDataBooleans(list);
            return this;
        }

        /**
         * Adds a string value to the custom model data list.
         *
         * @param value the string value to add
         * @return this builder
         */
        public Builder addStringCustomModelData(@NotNull final String value) {
            if (item.getCustomModelDataStrings() == null) {
                item.setCustomModelDataStrings(new ArrayList<>());
            }
            item.getCustomModelDataStrings().add(value);
            return this;
        }

        /**
         * Sets the entire string custom model data list.
         *
         * @param list the list of string values to set (can be null)
         * @return this builder
         */
        public Builder setStringCustomModelData(@Nullable final List<String> list) {
            item.setCustomModelDataStrings(list);
            return this;
        }

        /**
         * Adds a color's ARGB value to the custom model data list.
         *
         * @param color the color to add (converted to ARGB integer)
         * @return this builder
         */
        public Builder addColorCustomModelData(final Color color) {
            if (item.getCustomModelDataColors() == null) {
                item.setCustomModelDataColors(new ArrayList<>());
            }
            item.getCustomModelDataColors().add(color);
            return this;
        }

        /**
         * Sets the entire color custom model data list.
         *
         * @param list the list of color values to set (can be null)
         * @return this builder
         */
        public Builder setColorCustomModelData(@Nullable final List<Color> list) {
            item.setCustomModelDataColors(list);
            return this;
        }

        /**
         * Sets the potion meta for the item.
         * Note that the item material must be a potion type.
         *
         * @param potion the {@link ConfigurablePotionComponent} to set
         * @return this builder
         */
        public Builder potionComponent(@Nullable final ConfigurablePotionComponent potion) {
            item.setPotionComponent(potion);
            return this;
        }

        /**
         * Sets the skull meta for the item.
         *
         * @param profile the {@link ConfigurableProfileComponent} to set
         * @return this builder
         */
        public Builder profileComponent(@Nullable final ConfigurableProfileComponent profile) {
            item.setProfileComponent(profile);
            return this;
        }

        /**
         * Sets whether the item is unbreakable or not.
         *
         * @param unbreakable true if the item should be unbreakable, false otherwise
         * @return this builder
         */
        public Builder unbreakable(final boolean unbreakable) {
            item.setUnbreakable(unbreakable);
            return this;
        }

        /**
         * Sets whether the item should show the enchantment glint or not,
         * regardless of whether it has enchantments.
         *
         * @param override true to show the glint, false otherwise
         * @return this builder
         */
        public Builder enchantmentGlintOverride(final boolean override) {
            item.setGlint(override);
            return this;
        }

        public Builder modifier(@Nullable final Consumer<ItemStack> modifier) {
            item.setModifier(modifier);
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
    private @Nullable Component name;
    private int amount;
    private Map<Enchantment, Integer> enchantments;
    private List<Component> lore;
    private Set<ItemFlag> flags;
    private boolean unbreakable;
    private boolean glint;

    private @Nullable FloatList customModelDataFloats;
    private @Nullable BooleanList customModelDataBooleans;
    private @Nullable List<String> customModelDataStrings;
    private @Nullable List<Color> customModelDataColors;

    private @Nullable ConfigurablePotionComponent potionComponent;
    private @Nullable ConfigurableProfileComponent profileComponent;
    private @Nullable Consumer<ItemStack> modifier;

    public ConfigurableItem() {
        this.unbreakable = false;
        this.glint = false;
        this.amount = 1;
    }

    public ConfigurableItem(@NonNull Material material) {
        this();
        this.material = material;
    }

    @SuppressWarnings("UnstableApiUsage")
    public ConfigurableItem(@NonNull ItemStack item) {
        this.material = item.getType();
        this.name = item.getData(DataComponentTypes.ITEM_NAME);
        this.amount = item.getAmount();

        final ItemEnchantments enchants = item.getData(DataComponentTypes.ENCHANTMENTS);
        if (enchants != null) {
            this.enchantments = enchants.enchantments();
        }

        final ItemLore itemLore = item.getData(DataComponentTypes.LORE);
        if (itemLore != null) {
            this.lore = itemLore.lines();
        }

        this.flags = new HashSet<>(item.getItemFlags());
        this.unbreakable = item.getData(DataComponentTypes.UNBREAKABLE) != null;
        this.glint = Boolean.TRUE.equals(item.getData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE));

        final CustomModelData cmd = item.getData(DataComponentTypes.CUSTOM_MODEL_DATA);
        if (cmd != null) {
            this.customModelDataFloats = new FloatArrayList(cmd.floats());
            this.customModelDataBooleans = new BooleanArrayList(cmd.flags());
            this.customModelDataStrings = new ArrayList<>(cmd.strings());
            this.customModelDataColors = new ArrayList<>(cmd.colors());
        }
    }


    /**
     * Returns a builder for modifying this object.
     *
     * @return a new {@link Builder} with the current values of this object
     */
    public Builder edit() {
        return new Builder(material)
                .name(name)
                .addLore(Objects.requireNonNullElse(lore, Collections.emptyList()))
                .addFlags(Objects.requireNonNullElse(flags, Collections.emptyList()))
                .addEnchants(Objects.requireNonNullElse(enchantments, Collections.emptyMap()))
                .amount(amount)
                .setFloatCustomModelData(customModelDataFloats)
                .setStringCustomModelData(customModelDataStrings)
                .setBooleanCustomModelData(customModelDataBooleans)
                .setColorCustomModelData(customModelDataColors)
                .potionComponent(potionComponent)
                .profileComponent(profileComponent)
                .unbreakable(unbreakable)
                .enchantmentGlintOverride(glint)
                .modifier(modifier);
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
        return this.asLocalizedItemStack(placeholders, Locale.ENGLISH);
    }

    /**
     * Converts this object to an {@link ItemStack} applying the provided placeholders
     * and localizing the item if necessary. A convenience method for {@link ConfigurableItem#asLocalizedItemStack(List, Locale)}.
     *
     * @param placeholders list of {@link ItemPlaceholder} to apply
     * @param player       {@link Player} player used for localization
     * @return the resulting {@link ItemStack} object
     */
    public ItemStack asLocalizedItemStack(List<ItemPlaceholder> placeholders, Player player) {
        return asLocalizedItemStack(placeholders, player.locale());
    }

    /**
     * Converts this object to an {@link ItemStack} applying the provided placeholders.
     *
     * @param placeholders list of {@link ItemPlaceholder} to apply
     * @param locale       {@link Locale} locale to use for localization
     * @return the resulting {@link ItemStack} object
     */
    @SuppressWarnings("UnstableApiUsage")
    public ItemStack asLocalizedItemStack(List<ItemPlaceholder> placeholders, Locale locale) {
        final ItemStack is = new ItemStack(material, amount);
        if (name != null) {
            is.setData(DataComponentTypes.ITEM_NAME, createComponent(name, placeholders, locale));
        }
        if (lore != null && !lore.isEmpty()) {
            final ItemLore.Builder loreBuilder = ItemLore.lore();
            for (Component component : lore) {
                loreBuilder.addLine(createComponent(component, placeholders, locale));
            }
            is.setData(DataComponentTypes.LORE, loreBuilder);
        }
        if (enchantments != null) {
            is.addEnchantments(enchantments);
        }
        if (potionComponent != null) {
            is.setData(DataComponentTypes.POTION_CONTENTS, potionComponent.toComponent());
        }
        if (profileComponent != null) {
            is.setData(DataComponentTypes.PROFILE, profileComponent.toComponent());
        }

        final CustomModelData data = is.getData(DataComponentTypes.CUSTOM_MODEL_DATA);
        final CustomModelData.Builder cmd = CustomModelData.customModelData();
        if (data != null) {
            cmd.addColors(data.colors())
                    .addFlags(data.flags())
                    .addStrings(data.strings())
                    .addFloats(data.floats());
        }
        if (customModelDataFloats != null) {
            cmd.addFloats(customModelDataFloats);
        }
        if (customModelDataBooleans != null) {
            cmd.addFlags(customModelDataBooleans);
        }
        if (customModelDataStrings != null) {
            cmd.addStrings(customModelDataStrings);
        }
        if (customModelDataColors != null) {
            cmd.addColors(customModelDataColors);
        }
        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, cmd.build());
        is.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint);

        is.editMeta(meta -> {
            meta.setUnbreakable(unbreakable);
            if (flags != null) {
                meta.addItemFlags(flags.toArray(ItemFlag[]::new));
            }
        });
        if (modifier != null) {
            modifier.accept(is);
        }
        return is;
    }

    /**
     * Creates a copy of this item.
     * The copy will have the same material, display name, lore, flags, amount and custom model data.
     *
     * @return a new {@link ConfigurableItem} instance with the same properties
     */
    public ConfigurableItem copy() {
        return edit().build();
    }

    private Component createComponent(Component str,
                                      List<ItemPlaceholder> placeholders,
                                      Locale locale) {
        Component component = locale != null ? I18n.getInstance().localized(locale, str) : str;
        for (final ItemPlaceholder placeholder : placeholders) {
            component = placeholder.apply(component);
        }
        return component.decoration(TextDecoration.ITALIC, false);
    }

    public @NotNull Material getMaterial() {
        return material;
    }

    public void setMaterial(@NotNull Material material) {
        this.material = material;
    }

    public @Nullable Component getName() {
        return name;
    }

    public void setName(@Nullable Component name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
    }

    public List<Component> getLore() {
        return lore;
    }

    public void setLore(List<Component> lore) {
        this.lore = lore;
    }

    public Set<ItemFlag> getFlags() {
        return flags;
    }

    public void setFlags(Set<ItemFlag> flags) {
        this.flags = flags;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public boolean isGlint() {
        return glint;
    }

    public void setGlint(boolean glint) {
        this.glint = glint;
    }

    public @Nullable FloatList getCustomModelDataFloats() {
        return customModelDataFloats;
    }

    public void setCustomModelDataFloats(@Nullable FloatList customModelDataFloats) {
        this.customModelDataFloats = customModelDataFloats;
    }

    public @Nullable BooleanList getCustomModelDataBooleans() {
        return customModelDataBooleans;
    }

    public void setCustomModelDataBooleans(@Nullable BooleanList customModelDataBooleans) {
        this.customModelDataBooleans = customModelDataBooleans;
    }

    public @Nullable List<String> getCustomModelDataStrings() {
        return customModelDataStrings;
    }

    public void setCustomModelDataStrings(@Nullable List<String> customModelDataStrings) {
        this.customModelDataStrings = customModelDataStrings;
    }

    public @Nullable List<Color> getCustomModelDataColors() {
        return customModelDataColors;
    }

    public void setCustomModelDataColors(@Nullable List<Color> customModelDataColors) {
        this.customModelDataColors = customModelDataColors;
    }

    public @Nullable ConfigurablePotionComponent getPotionComponent() {
        return potionComponent;
    }

    public void setPotionComponent(@Nullable ConfigurablePotionComponent potionComponent) {
        this.potionComponent = potionComponent;
    }

    public @Nullable ConfigurableProfileComponent getProfileComponent() {
        return profileComponent;
    }

    public void setProfileComponent(@Nullable ConfigurableProfileComponent profileComponent) {
        this.profileComponent = profileComponent;
    }

    public @Nullable Consumer<ItemStack> getModifier() {
        return modifier;
    }

    public void setModifier(@Nullable Consumer<ItemStack> modifier) {
        this.modifier = modifier;
    }
}