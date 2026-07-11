package dev.starless.inventories.i18n;

import com.google.gson.stream.JsonReader;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.minimessage.translation.MiniMessageTranslator;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.TriState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

// Lightly adapted from InvUI: https://github.com/NichtStudioCode/InvUI/blob/main/invui/src/main/java/xyz/xenondevs/invui/i18n/Languages.java
// (Under MIT License)
public class I18n {

    private static final I18n INSTANCE = new I18n();
    private final Map<Locale, Map<? extends String, ? extends String>> translations = new HashMap<>();
    private final Translator translator = new MiniMessageTranslator() {
        @Override
        public @NonNull Key name() {
            return Key.key("configurableinvs:translator");
        }

        @Override
        public boolean canTranslate(@NonNull String key, @NonNull Locale locale) {
            return getFormatString(locale, key) != null;
        }

        @Override
        protected String getMiniMessageString(@NonNull String key, @NonNull Locale locale) {
            return Objects.requireNonNull(getFormatString(locale, key));
        }
    };

    @Setter
    private Function<Player, Locale> localeFunction = Player::locale;

    /**
     * Gets the locale for the given player.
     *
     * @param player {@link Player} for which to get the locale.
     * @return The locale for the given player.
     */
    public Locale getLocale(final Player player) {
        return localeFunction.apply(player);
    }

    /**
     * Checks if the given locale has translations.
     *
     * @param locale {@link Locale} for which to check support.
     * @return {@code true} if the locale is supported, {@code false} otherwise.
     */
    public boolean isLocaleSupported(final Locale locale) {
        return translations.containsKey(locale);
    }

    /**
     * Adds a language under the given lang code.
     * <p>
     * This method will replace any existing language with the same lang code.
     *
     * @param locale       The lang code of the language.
     * @param translations The translations of the language.
     */
    public void addLanguage(Locale locale, Map<? extends String, ? extends String> translations) {
        this.translations.put(locale, translations);
    }

    /**
     * Adds a language under the given lang code after reading it from the given reader.
     * <p>
     * Note: The language is read as a json object with the translation keys as keys and the format strings as
     * their string values. Any other json structure will result in an {@link IllegalStateException}.
     * An example for such a structure are Minecraft's lang files.
     *
     * @param locale The locale.
     * @param reader The reader for a language json file.
     * @throws IOException           If an error occurs while reading.
     * @throws IllegalStateException If the json is not valid.
     */
    public void loadLanguage(Locale locale, Reader reader) throws IOException {
        var translations = new HashMap<String, String>();
        try (var jsonReader = new JsonReader(reader)) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                var key = jsonReader.nextName();
                var value = jsonReader.nextString();
                translations.put(key, value);
            }

            addLanguage(locale, translations);
        }
    }

    /**
     * Adds a language under the given lang code after reading it from the given file.
     *
     * @param locale  The locale.
     * @param file    The file to read the language from.
     * @param charset The charset to use.
     * @throws IOException If an error occurs while reading.
     */
    public void loadLanguage(Locale locale, File file, Charset charset) throws IOException {
        try (var reader = new FileReader(file, charset)) {
            loadLanguage(locale, reader);
        }
    }

    /**
     * Translates the given component into the language of the given player, if server-side translations are enabled.
     *
     * @param player    The player to translate the component for.
     * @param component The component to translate.
     * @param resolvers Additional mini message tag resolvers
     * @return The translated component or the original component if server-side translations are disabled.
     */
    public Component localized(Player player, Component component, TagResolver... resolvers) {
        return localized(getLocale(player), component, resolvers);
    }

    /**
     * Translates the given component into the given language, if server-side translations are enabled.
     *
     * @param locale    The language to translate the component to.
     * @param component The component to translate.
     * @param resolvers Additional mini message tag resolvers
     * @return The translated component or the original component if server-side translations are disabled.
     */
    public Component localized(Locale locale, Component component, TagResolver... resolvers) {
        return getRenderer(resolvers).render(component, locale);
    }

    public @Nullable String getFormatString(Locale locale, String key) {
        final Map<? extends String, ? extends String> map = translations.get(locale);
        return map != null ? map.get(key) : null;
    }

    private ComponentRenderer<Locale> getRenderer(TagResolver[] resolvers) {
        if (resolvers.length == 0) {
            return TranslatableComponentRenderer.usingTranslationSource(translator);
        }

        // specialized TranslatableComponentRenderer that injects resolvers to each TranslatableComponent's arguments
        return new TranslatableComponentRenderer<>() {

            @Override
            protected @Nullable MessageFormat translate(@NonNull String key, @NonNull Locale context) {
                return translator.translate(key, context);
            }

            @Override
            protected @NonNull Component renderTranslatable(@NonNull TranslatableComponent component,
                                                            @NonNull Locale context) {
                TriState anyTranslations = translator.hasAnyTranslations();
                if (anyTranslations == TriState.FALSE) {
                    return component;
                }

                Component translated;
                if (translator.canTranslate(component.key(), context)) {
                    var args = new ArrayList<>(component.arguments());
                    args.add(TranslationArgument.component(Argument.tagResolver(resolvers)));
                    translated = translator.translate(component.arguments(args), context);
                } else {
                    translated = null;
                }

                return translated != null
                        ? this.render(translated, context)
                        : super.renderTranslatable(component, context);
            }

        };
    }

    public static I18n getInstance() {
        return INSTANCE;
    }
}
