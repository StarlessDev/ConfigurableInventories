package dev.starless.inventories;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

@Getter
@Setter
public class ConfigurableSkullMeta {

    private UUID ownerUUID = null;
    private String ownerName = null;
    private boolean fetchFromMojang = false;
    private String textureValue = null;

    private ConfigurableSkullMeta() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ConfigurableSkullMeta meta;

        private Builder() {
            this.meta = new ConfigurableSkullMeta();
        }

        public Builder ownerUUID(UUID ownerUUID) {
            meta.setOwnerUUID(ownerUUID);
            return this;
        }

        public Builder ownerName(String ownerName) {
            meta.setOwnerName(ownerName);
            return this;
        }

        public Builder textureValue(String textureValue) {
            meta.setTextureValue(textureValue);
            return this;
        }

        public Builder fetchFromMojang(boolean fetchFromMojang) {
            meta.setFetchFromMojang(fetchFromMojang);
            return this;
        }

        public ConfigurableSkullMeta build() {
            return meta;
        }
    }

    public void apply(final SkullMeta meta) {
        final PlayerProfile profile;
        final boolean hasUUID = ownerUUID != null;
        final boolean hasUsername = ownerName != null;
        if (hasUUID || hasUsername) {
            if (fetchFromMojang) {
                profile = Bukkit.createProfile(null, ownerName);
            } else if (hasUUID) {
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(ownerUUID));
                return;
            } else {
                meta.setOwningPlayer(Bukkit.getOfflinePlayerIfCached(ownerName));
                return;
            }
        } else {
            final UUID rnd = UUID.randomUUID();
            profile = Bukkit.createProfile(rnd, rnd.toString());
            profile.setProperty(new ProfileProperty("textures", textureValue));
        }

        if (fetchFromMojang) {
            profile.complete(true);
        }
        meta.setPlayerProfile(profile);
    }
}
