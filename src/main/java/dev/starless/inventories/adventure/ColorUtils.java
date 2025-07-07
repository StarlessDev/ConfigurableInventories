package dev.starless.inventories.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class to provide support for color formats.
 */

@UtilityClass
public class ColorUtils {

    private final MiniMessage minimessage = MiniMessage.builder()
            .preProcessor(input -> LegacyPreprocessor.getInstance().apply(input))
            .build();

    /**
     * Get a component from a string, respecting color codes.
     *
     * @param message a {@link String}
     * @return a new {@link Component}
     */
    public Component component(@Nullable final String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }

        return minimessage.deserialize(message);
    }

    /**
     * Get a legacy string from a component, respecting color codes.
     *
     * @param message a {@link String}
     * @return a new {@link String} with legacy color codes
     */
    public String section(@Nullable final String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        return LegacyComponentSerializer.legacySection().serialize(minimessage.deserialize(message));
    }
}
