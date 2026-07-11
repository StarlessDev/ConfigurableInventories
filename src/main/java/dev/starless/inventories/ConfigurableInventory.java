package dev.starless.inventories;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a configurable inventory that can be used
 * to create custom inventories with a title, structure,
 * and items.
 * <p>
 * You can use the builder to create an instance of this class.
 */

public final class ConfigurableInventory {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final ConfigurableInventory inventory;

        private Builder() {
            inventory = new ConfigurableInventory();
        }

        /**
         * Sets the title of the inventory.
         *
         * @param title the title to set
         * @return this builder
         */
        public Builder title(final Component title) {
            inventory.setTitle(title);
            return this;
        }

        public Builder structure(final String... structure) {
            return this.structure((List.of(structure)));
        }

        public Builder structure(final List<String> structure) {
            inventory.setStructure(structure.stream()
                    .map(str -> str.replaceAll(" ", ""))
                    .toList());
            return this;
        }

        /**
         * Adds an item to the inventory with the specified key.
         *
         * @param key  the key to associate with the item (typically a single character)
         * @param item the {@link ConfigurableItem} to add
         * @return this builder
         */
        public Builder item(String key, ConfigurableItem item) {
            inventory.getItems().put(key, item);
            return this;
        }

        /**
         * Builds and returns the configured inventory.
         *
         * @return a {@link ConfigurableInventory} instance
         */
        public ConfigurableInventory build() {
            return inventory;
        }
    }

    private Map<String, ConfigurableItem> items = new HashMap<>();
    private List<String> structure = new ArrayList<>();
    private Component title;

    /**
     * Retrieves an item from the inventory by its key.
     *
     * @param character the key character associated with the item
     * @return the {@link ConfigurableItem} associated with the key or null if not found
     */
    public ConfigurableItem getItem(char character) {
        return items.get(String.valueOf(character)).copy();
    }

    /**
     * Returns the structure in array for instead of list.
     * Useful for libraries not supporting lists.
     *
     * @return an array of strings
     */
    public String[] getStructureArray() {
        return structure.toArray(String[]::new);
    }

    public Map<String, ConfigurableItem> getItems() {
        return items;
    }

    public void setItems(Map<String, ConfigurableItem> items) {
        this.items = items;
    }

    public List<String> getStructure() {
        return structure;
    }

    public void setStructure(List<String> structure) {
        this.structure = structure;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }
}
