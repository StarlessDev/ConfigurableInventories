package dev.starless.inventories.serialization.profile;

import com.destroystokyo.paper.profile.ProfileProperty;
import dev.starless.inventories.ConfigurableProfileComponent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;

public class ProfileComponentSerializer implements TypeSerializer<ConfigurableProfileComponent> {

    private static final String UUID = "uuid";
    private static final String NAME = "name";
    private static final String PROPERTIES = "properties";

    @Override
    public ConfigurableProfileComponent deserialize(@NotNull Type type,
                                                    @NotNull ConfigurationNode node) throws SerializationException {
        final ConfigurableProfileComponent.Builder builder = ConfigurableProfileComponent.builder();
        builder.uuid(node.node(UUID).get(UUID.class));
        builder.username(node.node(NAME).getString());

        final ConfigurationNode propertiesNode = node.node(PROPERTIES);
        if (propertiesNode.isList()) {
            builder.addProperties(propertiesNode.getList(ProfileProperty.class));
        }
        return builder.build();
    }

    @Override
    public void serialize(@NotNull Type type,
                          @Nullable ConfigurableProfileComponent obj,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node(UUID).set(obj.getUuid());
        node.node(NAME).set(obj.getUsername());
        node.node(PROPERTIES).set(obj.getProperties());
    }
}
