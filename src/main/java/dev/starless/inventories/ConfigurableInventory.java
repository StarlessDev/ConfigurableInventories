package dev.starless.inventories;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Represents a configurable inventory that can be used
 * to create custom inventories with a title, structure,
 * and items.
 * <p>
 * You can use the builder to create an instance of this class.
 */

@Setter
@Getter
public class ConfigurableInventory {

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
        public Builder title(final String title) {
            inventory.setTitle(title);
            return this;
        }

        /**
         * Sets the structure of the inventory using multiple lines.
         * This will clear any previously set structure.
         *
         * @param lines the structure lines representing the inventory layout
         * @return this builder
         */
        public Builder structure(final String... lines) {
            inventory.getStructure().clear();
            inventory.getStructure().addAll(Arrays.asList(lines));
            return this;
        }

        /**
         * Adds an item to the inventory with the specified key.
         *
         * @param key the key to associate with the item (typically a single character)
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
    private String title;

    /**
     * Retrieves an item from the inventory by its key.
     *
     * @param character the key character associated with the item
     * @return the {@link ConfigurableItem} associated with the key or null if not found
     */
    public ConfigurableItem getItem(char character) {
        return items.get(String.valueOf(character));
    }
}
