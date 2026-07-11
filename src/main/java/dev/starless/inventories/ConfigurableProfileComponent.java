package dev.starless.inventories;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuppressWarnings("UnstableApiUsage")
public class ConfigurableProfileComponent {

    private UUID uuid = null;
    private String username = null;
    private List<ProfileProperty> properties;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ConfigurableProfileComponent meta;

        private Builder() {
            this.meta = new ConfigurableProfileComponent();
        }

        public Builder uuid(UUID uuid) {
            meta.setUuid(uuid);
            return this;
        }

        public Builder username(String username) {
            meta.setUsername(username);
            return this;
        }

        public Builder addProperty(ProfileProperty property) {
            if (meta.getProperties() == null) {
                meta.setProperties(new ArrayList<>());
            }
            meta.getProperties().add(property);
            return this;
        }

        public Builder addProperties(Collection<ProfileProperty> properties) {
            if (meta.getProperties() == null) {
                meta.setProperties(new ArrayList<>());
            }
            meta.getProperties().addAll(properties);
            return this;
        }

        public ConfigurableProfileComponent build() {
            return meta;
        }
    }

    public ConfigurableProfileComponent() {
    }

    public ConfigurableProfileComponent(final ItemStack item) {
        final ResolvableProfile profile = item.getData(DataComponentTypes.PROFILE);
        if  (profile != null) {
            this.uuid = profile.uuid();
            this.username = profile.name();
            this.properties = new ArrayList<>(profile.properties());
        }
    }

    public ResolvableProfile toComponent() {
        final ResolvableProfile.Builder builder = ResolvableProfile.resolvableProfile();
        if (uuid != null) {
            builder.uuid(uuid);
        }
        if (username != null) {
            builder.name(username);
        }
        if (properties != null) {
            builder.addProperties(properties);
        }
        return builder.build();
    }
}
