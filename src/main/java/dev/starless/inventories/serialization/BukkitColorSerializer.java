package dev.starless.inventories.serialization;

import org.bukkit.Color;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class BukkitColorSerializer implements TypeSerializer<Color> {

    @Override
    public Color deserialize(@NotNull Type type, @NotNull ConfigurationNode node) {
        return Color.fromRGB(node.getInt(0xffffff));
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable Color obj,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.set(Integer.toHexString(obj.asRGB()));
    }
}
