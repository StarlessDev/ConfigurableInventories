package dev.starless.inventories;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("UnstableApiUsage")
public class ConfigurablePotionComponent {

    private Color color = null;
    private String name = null;
    private PotionType type = null;
    private List<PotionEffect> effects = new ArrayList<>();

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final ConfigurablePotionComponent meta;

        private Builder() {
            this.meta = new ConfigurablePotionComponent();
        }

        public Builder color(Color color) {
            meta.setColor(color);
            return this;
        }

        public Builder name(String name) {
            meta.setName(name);
            return this;
        }

        public Builder type(PotionType type) {
            meta.setType(type);
            return this;
        }

        public Builder addEffects(List<PotionEffect> effects) {
            meta.getEffects().addAll(effects);
            return this;
        }

        public Builder addEffect(PotionEffect effect) {
            meta.getEffects().add(effect);
            return this;
        }

        public ConfigurablePotionComponent build() {
            return meta;
        }
    }

    public ConfigurablePotionComponent() {
    }

    public ConfigurablePotionComponent(ItemStack item) {
        final PotionContents contents = item.getData(DataComponentTypes.POTION_CONTENTS);
        if (contents != null) {
            this.color = contents.customColor();
            this.name = contents.customName();
            this.type = contents.potion();
            this.effects = new ArrayList<>(contents.customEffects());
        }
    }

    public PotionContents toComponent() {
        final PotionContents.Builder builder = PotionContents.potionContents();
        if (color != null) {
            builder.customColor(color);
        }
        if (name != null) {
            builder.customName(name);
        }
        if (type != null) {
            builder.potion(type);
        }
        for (final PotionEffect effect : effects) {
            builder.addCustomEffect(effect);
        }
        return builder.build();
    }
}
