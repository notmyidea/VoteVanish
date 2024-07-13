package me.notmyidea.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileBuilder {

    private static final String SETTINGS_DIR = "settings";
    private static final String CONFIG_FILE = "config.yml";
    private Properties properties;

    private Map<String, String> strings;
    private Map<String, Integer> integers;
    private Map<String, Boolean> booleans;
    private Map<String, List<String>> stringLists;

    public FileBuilder() {
        try {
            Path settingsPath = Paths.get(SETTINGS_DIR);
            if(!Files.exists(settingsPath)) {
                Files.createDirectories(settingsPath);
            }

            Path configPath = settingsPath.resolve(CONFIG_FILE);
            boolean isNewFile = !Files.exists(configPath);
            if (isNewFile) {
                Files.createFile(configPath);
                try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                    writer.write("#Hey!! Thanks for buying my resource!\n");
                    writer.write("#If you have any issues, you can always contact me\n");
                    writer.write("#on Discord: (notmyidea) Email: (notmyidea@bytebond.net)\n");
                    writer.write("#-------------------------------------------------------------\n");
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Add default values
                properties = new Properties();
                write("load-bot", true);
                write("token", "1234-5678-9012-3456");
                write("delete-bots", false);
                write("undeletable-user", Arrays.asList("590503267809624067", "123456789012345678"));
                write("undeletable-roles", Arrays.asList("admin", "mod", "staff"));
                write("ignore-user", Arrays.asList("123456789012345678", "876543210987654321"));
                write("ignore-roles", Arrays.asList("muted", "punished", "jailed"));
                write("ignore-channels", Arrays.asList("1040958184824774700", "1340958184824774700"));
                write("require-users-to-delete", "10");
                write("logs-channel", "1040958504715943959");
                write("public-logs", true);
                write("public-logs-channel", "1240958504715943959");
                write("delete-emoji", ":wave:");
                write("keep-emoji", ":handshake:");
            } else {
                properties = new Properties();
                try (InputStream input = new FileInputStream(configPath.toFile())) {
                    properties.load(input);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        strings = new HashMap<>();
        integers = new HashMap<>();
        booleans = new HashMap<>();
        stringLists = new HashMap<>();

        int totalProperties = properties.stringPropertyNames().size();
        int loadedProperties = 0;

        System.out.println("#------------- Loading -------------#");
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            if(value.contains(",")) {
                stringLists.put(key, Arrays.asList(value.split(",")));
            } else if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                booleans.put(key, Boolean.parseBoolean(value));
            } else {
                try {
                    integers.put(key, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    strings.put(key, value);
                }
            }

            loadedProperties++;

        }
        System.out.println("Loaded: " + loadedProperties + "/" + totalProperties + " properties");
    }


    public String getString(String key) {
        return strings.get(key);
    }

    public Integer getInteger(String key) {
        return integers.get(key);
    }

    public Boolean getBoolean(String key) {
        return booleans.get(key);
    }

    public List<String> getStringList(String key) {
        return stringLists.get(key);
    }


    public String readString(String key) {
        return properties.getProperty(key);
    }

    public Integer readInteger(String key) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : null;
    }

    public Boolean readBoolean(String key) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : null;
    }

    public List<String> readStringList(String key) {
        String value = properties.getProperty(key);
        return value != null ? Arrays.asList(value.split(",")) : null;
    }

    public void write(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    public void write(String key, Integer value) {
        properties.setProperty(key, String.valueOf(value));
        saveProperties();
    }

    public void write(String key, Boolean value) {
        properties.setProperty(key, String.valueOf(value));
        saveProperties();
    }

    public void write(String key, List<String> value) {
        properties.setProperty(key, String.join(",", value));
        saveProperties();
    }

    private void saveProperties() {
        try (OutputStream output = new FileOutputStream(Paths.get(SETTINGS_DIR, CONFIG_FILE).toFile())) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}