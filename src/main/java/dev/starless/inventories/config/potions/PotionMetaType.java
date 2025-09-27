package dev.starless.inventories.config.potions;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import ch.jalu.configme.properties.types.PropertyType;
import dev.starless.inventories.ConfigurablePotionMeta;
import dev.starless.inventories.config.misc.BukkitColorType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PotionMetaType implements PropertyType<ConfigurablePotionMeta> {

    private static final String COLOR = "color";
    private static final String CUSTOM_POTION_NAME = "custom-potion-name";
    private static final String BASE_POTION_TYPE = "base-potion-type";
    private static final String EFFECTS = "effects";

    private final PotionEffectType effectType = new PotionEffectType();
    private final BukkitColorType colorType = new BukkitColorType();

    @Override
    public @Nullable ConfigurablePotionMeta convert(@Nullable Object object,
                                                    @NotNull ConvertErrorRecorder errorRecorder) {
        if (object instanceof Map<?, ?> map) {
            final String customPotionName = map.get(CUSTOM_POTION_NAME).toString();
            final Color potionColor = colorType.convert(map.get(COLOR), errorRecorder);

            final Registry<@NotNull PotionType> potionRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.POTION);
            final PotionType basePotionType = potionRegistry.get(NamespacedKey.minecraft(
                    Objects.toString(map.get(BASE_POTION_TYPE), PotionType.MUNDANE.getKey().getKey())
            ));

            final List<PotionEffect> effects;
            if (map.get(EFFECTS) instanceof List<?> effectsList) {
                effects = effectsList.stream()
                        .map(effect -> effectType.convert(effect, errorRecorder))
                        .filter(Objects::nonNull)
                        .toList();

            } else {
                effects = Collections.emptyList();
            }

            return ConfigurablePotionMeta.builder()
                    .color(potionColor)
                    .customPotionName(customPotionName)
                    .basePotionType(basePotionType)
                    .effects(effects)
                    .build();
        }
        return null;
    }

    @Override
    public @Nullable Object toExportValue(@Nullable ConfigurablePotionMeta value) {
        if (value == null) return null;

        final Map<String, Object> map = new HashMap<>();
        if (value.getColor() != null) {
            final Object colorObj = colorType.toExportValue(value.getColor());
            if (colorObj != null) {
                map.put(COLOR, colorObj);
            }
        }

        if (value.getCustomPotionName() != null) {
            map.put(CUSTOM_POTION_NAME, value.getCustomPotionName());
        }

        if (value.getBasePotionType() != null) {
            map.put(BASE_POTION_TYPE, value.getBasePotionType().getKey().getKey());
        }

        final List<PotionEffect> effect = value.getEffects();
        if (effect != null && !effect.isEmpty()) {
            map.put(EFFECTS, effect.stream()
                    .map(effectType::toExportValue)
                    .filter(Objects::nonNull)
                    .toList());
        }

        return map;
    }
}
