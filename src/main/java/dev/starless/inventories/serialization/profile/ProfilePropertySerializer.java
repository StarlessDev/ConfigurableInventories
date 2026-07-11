package dev.starless.inventories.serialization.profile;

import com.destroystokyo.paper.profile.ProfileProperty;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class ProfilePropertySerializer implements TypeSerializer<ProfileProperty> {

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String SIGNATURE = "signature";

    @Override
    public ProfileProperty deserialize(@NonNull Type type,
                                       @NonNull ConfigurationNode node) throws SerializationException {
        final String name =  node.node(NAME).getString();
        final String value = node.node(VALUE).getString();

        final String signature;
        final ConfigurationNode signatureNode = node.node(SIGNATURE);
        if (!signatureNode.virtual()) {
            signature = signatureNode.getString();
        } else {
            signature = null;
        }
        return new ProfileProperty(name, value, signature);
    }

    @Override
    public void serialize(@NonNull Type type,
                          @Nullable ProfileProperty obj,
                          @NonNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        node.node(NAME).set(obj.getName());
        node.node(VALUE).set(obj.getValue());
        if (obj.isSigned()) {
            node.node(SIGNATURE).set(obj.getSignature());
        }
    }
}
