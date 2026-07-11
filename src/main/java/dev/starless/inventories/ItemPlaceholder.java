package dev.starless.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.Objects;

/**
 * Represents a placeholder that can be used to replace
 * specific text patterns in strings.
 */
public class ItemPlaceholder {

    private final String target;
    private final Object replacement;

    private String prefix = "%";
    private String suffix = "%";

    public ItemPlaceholder(String target, Object replacement) {
        this.target = target;
        this.replacement = replacement;
    }

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

    public String getTarget() {
        return target;
    }

    public Object getReplacement() {
        return replacement;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
