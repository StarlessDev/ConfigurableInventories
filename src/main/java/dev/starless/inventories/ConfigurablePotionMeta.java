package dev.starless.inventories;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConfigurablePotionMeta {

    private Color color = null;
    private String customPotionName = null;
    private PotionType basePotionType = null;
    private List<PotionEffect> effects = new ArrayList<>();

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ConfigurablePotionMeta meta;

        private Builder() {
            this.meta = new ConfigurablePotionMeta();
        }

        public Builder color(Color color) {
            meta.setColor(color);
            return this;
        }

        public Builder customPotionName(String name) {
            meta.setCustomPotionName(name);
            return this;
        }

        public Builder basePotionType(PotionType type) {
            meta.setBasePotionType(type);
            return this;
        }

        public Builder effects(List<PotionEffect> effects) {
            meta.getEffects().clear();
            meta.getEffects().addAll(effects);
            return this;
        }

        public Builder addEffect(PotionEffect effect) {
            meta.getEffects().add(effect);
            return this;
        }

        public ConfigurablePotionMeta build() {
            return meta;
        }
    }

    public void apply(final PotionMeta meta) {
        if (color != null) {
            meta.setColor(color);
        }
        if (customPotionName != null) {
            meta.setCustomPotionName(customPotionName);
        }
        if (basePotionType != null) {
            meta.setBasePotionType(basePotionType);
        }
        for (final PotionEffect effect : effects) {
            meta.addCustomEffect(effect, true);
        }
    }
}
