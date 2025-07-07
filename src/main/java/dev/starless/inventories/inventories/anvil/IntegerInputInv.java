package dev.starless.inventories.inventories.anvil;

import dev.starless.inventories.inventories.AnvilInventory;

import java.util.Optional;

/**
 * Abstract base class for anvil inventories that accept integer input from players.
 */
public abstract class IntegerInputInv extends AnvilInventory<Integer> {

    @Override
    public String getAnvilPlaceholder() {
        return "Numero";
    }

    @Override
    public Optional<Integer> map(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
