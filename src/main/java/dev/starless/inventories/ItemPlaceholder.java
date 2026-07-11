package dev.starless.inventories;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

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

    private String prefix = "%";
    private String suffix = "%";

    /**
     * Applies the placeholder replacement to the given input component.
     *
     * @param component the input component to process
     * @return the processed component with the placeholder replaced
     */
    public Component apply(final Component component) {
        TextReplacementConfig.Builder builder = TextReplacementConfig.builder()
                .matchLiteral(prefix + target + suffix);
        if (replacement instanceof Component c) {
            builder.replacement(c);
        } else {
            builder.replacement(Objects.toString(replacement));
        }
        return component.replaceText(builder.build());
    }
}
