package dev.starless.inventories;

import lombok.Getter;
import lombok.Setter;

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
        public Builder item(Character key, ConfigurableItem item) {
            inventory.getItems().put(key, item);
            return this;
        }

        /**
         * Sets the items of the inventory.
         *
         * @param items the items to set
         * @return this builder
         */
        public Builder items(Map<Character, ConfigurableItem> items) {
            inventory.setItems(items);
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

    private Map<Character, ConfigurableItem> items = new HashMap<>();
    private List<String> structure = new ArrayList<>();
    private String title;

    /**
     * Retrieves an item from the inventory by its key.
     *
     * @param character the key character associated with the item
     * @return the {@link ConfigurableItem} associated with the key or null if not found
     */
    public ConfigurableItem getItem(char character) {
        return items.get(character).copy();
    }
}
