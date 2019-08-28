package com.ruczajsoftware.workoutrival.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Bundle {

    public static final String LANGUAGE_OF_POLISH = "pl";
    private static final Locale[] LOCALES = {Locale.ENGLISH, new Locale(LANGUAGE_OF_POLISH)};
    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final String EMAIL_TEMPLATES = "emailService/emailTemplates_%s.properties";
    private static final Map<String, Properties> EMAIL_TEMPLATES_PROPERTIES = new HashMap<>();
    private static final String NO_KEY_EXCEPTION = "There is no key %s in resources for language %s.";
    private static final String IO_EXCEPTION_LOADING = "I/O exception during loading bundle file: %s.";

    static {
        loadBundle(EMAIL_TEMPLATES_PROPERTIES);
    }

    public static String getEmailTemplate(Locale locale, String key, Object... params) {
        return getEmailTemplateInternal(EMAIL_TEMPLATES_PROPERTIES, locale, key, params);
    }

    private static String getEmailTemplateInternal(Map<String, Properties> map, Locale localeIn, String key, Object... params) {
        Locale locale = Objects.isNull(localeIn) ? Locale.ENGLISH : localeIn;
        return map.containsKey(locale.toString())
                ? getTemplateMessage(map, locale, key, locale, params)
                : getTemplateMessage(map, Locale.ENGLISH, key, locale, params);
    }

    private static String getTemplateMessage(Map<String, Properties> map, Locale localeIn, String key, Locale locale, Object... params) {
        Properties properties = Optional.ofNullable(map.get(locale.toString()))
                .orElse(map.get(Locale.ENGLISH.toString()));
        return properties
                .containsKey(key)
                ? MessageFormat.format(properties.getProperty(key), params)
                : throwException(localeIn, key);
    }

    private static void loadBundleForEmailTemplates(Locale locale, Map<String, Properties> originalProperties) {
        loadBundleFileFor(locale, EMAIL_TEMPLATES, originalProperties);
    }

    private static void loadBundle(Map<String, Properties> properties) {
        Arrays.stream(LOCALES).forEach(locale -> loadBundleForEmailTemplates(locale, properties));
    }

    private static void loadBundleFileFor(Locale locale, String pathTemplate, Map<String, Properties> propertiesByLanguage) {
        String bundleFilePath = String.format(pathTemplate, locale.toString());
        Properties properties = new Properties();
        InputStream inputStream = CLASS_LOADER.getResourceAsStream(bundleFilePath);
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(inputStreamReader);
            propertiesByLanguage.put(locale.toString(), properties);
        } catch (IOException e) {
            logger.log(Level.INFO, String.format(IO_EXCEPTION_LOADING, bundleFilePath), e);
        }
    }

    private static String throwException(Locale locale, String key) {
        throw new IllegalArgumentException(String.format(NO_KEY_EXCEPTION, key, locale));
    }
}
