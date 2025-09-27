package dev.starless.inventories.config.potions;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PotionEffectType implements PropertyType<PotionEffect> {

    @Override
    public @Nullable PotionEffect convert(@Nullable Object object,
                                          @NotNull ConvertErrorRecorder errorRecorder) {
        if (object instanceof Map<?, ?> map) {
            final Map<String, Object> effects = new HashMap<>();
            map.forEach((key, value) -> {
                effects.put(key.toString(), value);
            });
            return new PotionEffect(effects);
        }
        return null;
    }

    @Override
    public @Nullable Object toExportValue(@Nullable PotionEffect value) {
        return value != null ? value.serialize() : null;
    }
}
