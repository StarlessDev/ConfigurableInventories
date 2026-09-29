package dev.starless.inventories.serialization;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentSerializer implements TypeSerializer<Component> {

    private static final MiniMessage MM = MiniMessage.miniMessage();

    // &x&r&r&g&g&b&b (Bungee-style), &#rrggbb, then plain &c codes
    private static final Pattern BUNGEE_HEX = Pattern.compile("(?i)[&§]x((?:[&§][0-9a-f]){6})");
    private static final Pattern HEX = Pattern.compile("(?i)[&§]#([0-9a-f]{6})");
    private static final Pattern CODE = Pattern.compile("(?i)[&§]([0-9a-fk-or])");

    private static final Map<Character, String> TAGS = Map.ofEntries(
            Map.entry('0', "<reset><black>"), Map.entry('1', "<reset><dark_blue>"),
            Map.entry('2', "<reset><dark_green>"), Map.entry('3', "<reset><dark_aqua>"),
            Map.entry('4', "<reset><dark_red>"), Map.entry('5', "<reset><dark_purple>"),
            Map.entry('6', "<reset><gold>"), Map.entry('7', "<reset><gray>"),
            Map.entry('8', "<reset><dark_gray>"), Map.entry('9', "<reset><blue>"),
            Map.entry('a', "<reset><green>"), Map.entry('b', "<reset><aqua>"),
            Map.entry('c', "<reset><red>"), Map.entry('d', "<reset><light_purple>"),
            Map.entry('e', "<reset><yellow>"), Map.entry('f', "<reset><white>"),
            Map.entry('k', "<obfuscated>"), Map.entry('l', "<bold>"),
            Map.entry('m', "<strikethrough>"), Map.entry('n', "<underlined>"),
            Map.entry('o', "<italic>"), Map.entry('r', "<reset>")
    );

    @Override
    public Component deserialize(@NonNull Type type,
                                 @NonNull ConfigurationNode node) {
        return parse(node.getString());
    }

    @Override
    public void serialize(@NonNull Type type,
                          @Nullable Component obj,
                          @NonNull ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }
        node.set(MM.serialize(obj));
    }

    private Component parse(final String input) {
        if (input == null) return Component.empty();

        String s = BUNGEE_HEX.matcher(input).replaceAll(m -> {
            String hex = m.group(1).replaceAll("(?i)[&§]", "");
            return Matcher.quoteReplacement("<reset><#" + hex + ">");
        });
        s = HEX.matcher(s).replaceAll(m -> Matcher.quoteReplacement("<reset><#" + m.group(1) + ">"));
        s = CODE.matcher(s).replaceAll(m -> {
            return Matcher.quoteReplacement(TAGS.get(Character.toLowerCase(m.group(1).charAt(0))));
        });
        return MM.deserialize(s);
    }
}
