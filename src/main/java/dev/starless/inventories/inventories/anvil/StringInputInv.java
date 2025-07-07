package dev.starless.inventories.inventories.anvil;

import dev.starless.inventories.inventories.AnvilInventory;

import java.util.Optional;

public abstract class StringInputInv extends AnvilInventory<String> {

    @Override
    public String getAnvilPlaceholder() {
        return "Testo";
    }

    @Override
    public Optional<String> map(String input) {
        return Optional.of(input);
    }
}
