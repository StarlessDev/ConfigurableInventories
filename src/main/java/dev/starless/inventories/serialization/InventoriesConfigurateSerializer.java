package dev.starless.inventories.serialization;

import com.destroystokyo.paper.profile.ProfileProperty;
import dev.starless.inventories.ConfigurableInventory;
import dev.starless.inventories.ConfigurableItem;
import dev.starless.inventories.ConfigurablePotionComponent;
import dev.starless.inventories.ConfigurableProfileComponent;
import dev.starless.inventories.serialization.misc.BukkitColorSerializer;
import dev.starless.inventories.serialization.misc.DataComponentTypeSerializer;
import dev.starless.inventories.serialization.potion.PotionComponentSerializer;
import dev.starless.inventories.serialization.potion.PotionEffectSerializer;
import dev.starless.inventories.serialization.profile.ProfileComponentSerializer;
import dev.starless.inventories.serialization.profile.ProfilePropertySerializer;
import io.papermc.paper.datacomponent.DataComponentType;
import lombok.Getter;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

@Getter
@SuppressWarnings("UnstableApiUsage")
public final class InventoriesConfigurateSerializer {

    public static final InventoriesConfigurateSerializer INSTANCE = new InventoriesConfigurateSerializer();

    private final TypeSerializerCollection serializers;

    private InventoriesConfigurateSerializer() {
        this.serializers = createCollection();
    }

    private TypeSerializerCollection createCollection() {
        return TypeSerializerCollection.defaults().childBuilder()
                .registerAll(ConfigurateComponentSerializer.configurate().serializers())
                //.register(Component.class, new TranslatableSerializer())
                .register(ConfigurableInventory.class, new InventorySerializer())
                .register(ConfigurableItem.class, new ItemSerializer())
                .register(ConfigurablePotionComponent.class, new PotionComponentSerializer())
                .register(PotionEffect.class, new PotionEffectSerializer())
                .register(ConfigurableProfileComponent.class, new ProfileComponentSerializer())
                .register(ProfileProperty.class, new ProfilePropertySerializer())
                .register(Color.class, new BukkitColorSerializer())
                .register(DataComponentType.class, new DataComponentTypeSerializer())
                .build();
    }
}
