package dev.starless.inventories.items.placeholders;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ItemPlaceholder {

    private final String target;
    private final Object replacement;

    private String prefix = "\\{";
    private String suffix = "}";

    public String apply(final String input) {
        return input.replaceAll(
                this.prefix + target + this.suffix,
                Objects.toString(replacement)
        );
    }
}
