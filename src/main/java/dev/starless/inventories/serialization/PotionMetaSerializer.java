package dev.starless.inventories.serialization;

import dev.starless.inventories.ConfigurablePotionMeta;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PotionMetaSerializer implements TypeSerializer<ConfigurablePotionMeta> {

    private static final String COLOR = "color";
    private static final String CUSTOM_POTION_NAME = "custom-potion-name";
    private static final String BASE_POTION_TYPE = "base-potion-type";
    private static final String EFFECTS = "effects";

    @Override
    public ConfigurablePotionMeta deserialize(@NotNull Type type, ConfigurationNode node) throws SerializationException {
        if (node.virtual()) {
            return null;
        }

        final String customPotionName = node.node(CUSTOM_POTION_NAME).getString();
        final Color color = node.node(COLOR).get(Color.class);

        final Registry<@NotNull PotionType> potionRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.POTION);
        final String basePotionTypeString = node.node(BASE_POTION_TYPE).getString(PotionType.MUNDANE.name());
        final PotionType basePotionType = potionRegistry.get(NamespacedKey.minecraft(basePotionTypeString.toLowerCase()));
        final List<PotionEffect> effects = node.node(EFFECTS).getList(PotionEffect.class, Collections.emptyList());

        return ConfigurablePotionMeta.builder()
                .color(color)
                .customPotionName(customPotionName)
                .basePotionType(basePotionType)
                .effects(effects)
                .build();
    }

    @Override
    public void serialize(@NotNull Type type,
                          ConfigurablePotionMeta obj,
                          @NotNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        if (obj.getColor() != null) {
            node.node(COLOR).set("#" + Integer.toHexString(obj.getColor().asRGB()));
        }

        if (obj.getCustomPotionName() != null) {
            node.node(CUSTOM_POTION_NAME).set(obj.getCustomPotionName());
        }

        if (obj.getBasePotionType() != null) {
            node.node(BASE_POTION_TYPE).set(obj.getBasePotionType().name());
        }

        if (!obj.getEffects().isEmpty()) {
            node.node(EFFECTS).set(obj.getEffects());
        }
    }
}
