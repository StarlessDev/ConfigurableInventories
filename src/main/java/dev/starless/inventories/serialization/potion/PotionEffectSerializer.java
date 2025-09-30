package dev.starless.inventories.serialization.potion;

import org.bukkit.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PotionEffectSerializer implements TypeSerializer<PotionEffect> {

    @Override
    public PotionEffect deserialize(@NotNull Type type,
                                    @NotNull ConfigurationNode node) throws SerializationException {
        if (node.virtual() || !node.isMap()) {
            return null;
        }
        try {
            final Map<String, Object> map = new HashMap<>();
            for (final Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
                map.put(String.valueOf(entry.getKey()), Objects.requireNonNull(entry.getValue().get(Object.class)));
            }

            return new PotionEffect(map);
        } catch (final Exception e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable PotionEffect obj,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.set(obj.serialize());
    }
}
