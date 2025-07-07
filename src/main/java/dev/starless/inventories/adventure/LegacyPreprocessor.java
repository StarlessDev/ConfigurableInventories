package dev.starless.inventories.adventure;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyPreprocessor {

    @Getter
    private static final LegacyPreprocessor instance = new LegacyPreprocessor();

    private final Map<String, String> colorMap = new HashMap<>();

    public LegacyPreprocessor() {
        // Colors
        colorMap.put("(&|§)0", "<black>");
        colorMap.put("(&|§)1", "<dark_blue>");
        colorMap.put("(&|§)2", "<dark_green>");
        colorMap.put("(&|§)3", "<dark_aqua>");
        colorMap.put("(&|§)4", "<dark_red>");
        colorMap.put("(&|§)5", "<dark_purple>");
        colorMap.put("(&|§)6", "<gold>");
        colorMap.put("(&|§)7", "<gray>");
        colorMap.put("(&|§)8", "<dark_gray>");
        colorMap.put("(&|§)9", "<blue>");
        colorMap.put("(&|§)a", "<green>");
        colorMap.put("(&|§)b", "<aqua>");
        colorMap.put("(&|§)c", "<red>");
        colorMap.put("(&|§)d", "<light_purple>");
        colorMap.put("(&|§)e", "<yellow>");
        colorMap.put("(&|§)f", "<white>");

        // Formatting codes
        colorMap.put("(&|§)k", "<obfuscated>");
        colorMap.put("(&|§)l", "<bold>");
        colorMap.put("(&|§)m", "<strikethrough>");
        colorMap.put("(&|§)n", "<underlined>");
        colorMap.put("(&|§)o", "<italic>");
        colorMap.put("(&|§)r", "<reset>");
    }

    private final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private final Pattern BUKKIT_HEX_PATTERN = Pattern.compile("([&§])x" + "([&§])([A-Fa-f0-9])".repeat(6));

    public String apply(String input) {
        // Handle Bukkit hex colors first
        final Matcher bukkitHexMatcher = BUKKIT_HEX_PATTERN.matcher(input);
        while (bukkitHexMatcher.find()) {
            final String hexColor = bukkitHexMatcher.group(3) + bukkitHexMatcher.group(5) +
                              bukkitHexMatcher.group(7) + bukkitHexMatcher.group(9) +
                              bukkitHexMatcher.group(11) + bukkitHexMatcher.group(13);
            input = input.replace(bukkitHexMatcher.group(0), "<#" + hexColor + ">");
        }

        // Handle regular color codes
        for (Map.Entry<String, String> entry : colorMap.entrySet()) {
            input = input.replaceAll(entry.getKey(), entry.getValue());
        }

        // Handle &#RRGGBB format
        final StringBuilder sb = new StringBuilder(input);
        final Matcher spigotHexMatcher = HEX_PATTERN.matcher(sb);
        while (spigotHexMatcher.find()) {
            sb.insert(spigotHexMatcher.end(), ">");
            sb.setCharAt(spigotHexMatcher.start(), '<');
        }

        return sb.toString();
    }
}
