package dev.starless.inventories.errors;

/**
 * Represents a generic error that can occur when populating an inventory.
 */
public interface GenericError {

    /**
     * Returns the error message.
     *
     * @return a {@link String}
     */
    default String message() {
        return null;
    }

    /**
     * If this method returns true, the inventory
     * will not be opened for the player.
     *
     * @return a {@link Boolean}
     */
    boolean forceClose();
}
