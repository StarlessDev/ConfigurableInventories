package dev.starless.inventories.serialization.skull;

import dev.starless.inventories.ConfigurableSkullMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class SkullMetaSerializer implements TypeSerializer<ConfigurableSkullMeta> {

    private static final String OWNER_NODE = "owner";
    private static final String OWNER_UUID = "uuid";
    private static final String OWNER_NAME = "name";
    private static final String FETCH_FROM_MOJANG = "fetch_from_mojang";
    private static final String TEXTURE_VALUE = "texture";

    @Override
    public ConfigurableSkullMeta deserialize(@NotNull Type type,
                                             @NotNull ConfigurationNode node) throws SerializationException {
        final ConfigurableSkullMeta.Builder builder = ConfigurableSkullMeta.builder();
        final ConfigurationNode ownerNode = node.node(OWNER_NODE);
        if (!ownerNode.virtual()) {
            builder.ownerUUID(ownerNode.node(OWNER_UUID).get(UUID.class));
            builder.ownerName(ownerNode.node(OWNER_NAME).getString());
        }
        builder.fetchFromMojang(node.node(FETCH_FROM_MOJANG).getBoolean(false));
        builder.textureValue(node.node(TEXTURE_VALUE).getString());

        return builder.build();
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable ConfigurableSkullMeta obj,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        final ConfigurationNode ownerNode = node.node(OWNER_NODE);
        ownerNode.node(OWNER_UUID).set(obj.getOwnerUUID());
        ownerNode.node(OWNER_NAME).set(obj.getOwnerName());

        node.node(FETCH_FROM_MOJANG).set(obj.isFetchFromMojang());
        node.node(TEXTURE_VALUE).set(obj.getTextureValue());
    }
}
