package dev.starless.inventories.items.placeholders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        return Pattern.compile("(?i)" + this.prefix + target + this.suffix)
                .matcher(input)
                .replaceAll(Objects.toString(replacement));
    }
}
