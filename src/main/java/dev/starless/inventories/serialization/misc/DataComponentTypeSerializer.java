package dev.starless.inventories.serialization.misc;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class DataComponentTypeSerializer implements TypeSerializer<DataComponentType> {

    @Override
    public DataComponentType deserialize(@NonNull Type type,
                                         ConfigurationNode node) throws SerializationException {
        final String rawKey = node.getString();
        if (rawKey == null) {
            throw new SerializationException("Cannot deserialize null data component key");
        }

        final Key key;
        if (rawKey.startsWith(Key.MINECRAFT_NAMESPACE + Key.DEFAULT_SEPARATOR)) {
            key = Key.key(rawKey);
        } else {
            key = NamespacedKey.minecraft(rawKey);
        }

        final DataComponentType dct = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.DATA_COMPONENT_TYPE)
                .get(key);
        if (dct == null) {
            throw new SerializationException("Cannot deserialize data component type with unknown key: " + key);
        }
        return dct;
    }

    @Override
    public void serialize(@NonNull Type type,
                          @Nullable DataComponentType obj,
                          ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        final Key key = obj.key();
        node.set(key.asMinimalString());
    }
}
