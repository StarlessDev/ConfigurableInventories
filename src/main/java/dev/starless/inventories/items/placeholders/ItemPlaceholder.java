package dev.starless.inventories.items.placeholders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * Represents a placeholder that can be used to replace
 * specific text patterns in strings.
 */
@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ItemPlaceholder {

    private final String target;
    private final Object replacement;

    private String prefix = "\\{";
    private String suffix = "}";

    /**
     * Applies the placeholder replacement to the given input string.
     *
     * @param input the input string to process
     * @return the processed string with the placeholder replaced
     */
    public String apply(final String input) {
        return input.replaceAll(
                this.prefix + target + this.suffix,
                Objects.toString(replacement)
        );
    }
}
