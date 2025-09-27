package dev.starless.inventories.config.misc;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitColorType implements PropertyType<Color> {

    @Override
    public @Nullable Color convert(@Nullable Object object,
                                   @NotNull ConvertErrorRecorder errorRecorder) {
        if (object instanceof Integer i) {
            return Color.fromRGB(i);
        }
        return null;
    }

    @Override
    public @Nullable Object toExportValue(@Nullable Color value) {
        return value != null ? Integer.toHexString(value.asRGB()) : null;
    }
}
