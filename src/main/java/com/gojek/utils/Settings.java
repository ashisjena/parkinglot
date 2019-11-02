package com.gojek.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Settings {
  private static Settings INSTANCE;
  private final Map<String, String> inner;

  private Settings() {
    if (INSTANCE != null) {
      throw new RuntimeException("ERROR: Settings is already initialized");
    }
    this.inner = new ConcurrentHashMap<>();
  }

  public void register() {
    try (final Stream<Path> paths = Files.list(Paths.get(getClass().getClassLoader().getResource("./").toURI()))) {
      paths.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".properties")).forEach(p -> {
        final Properties prop = new Properties();
        try {
          prop.load(Files.newInputStream(p, StandardOpenOption.READ));
          prop.entrySet().stream().forEach(entry -> this.inner.put((String) entry.getKey(), (String) entry.getValue()));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (URISyntaxException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Integer getIntProperty(final String propertyName, final int defaultValue) {
    final Optional<Integer> result = this.getIntProperty(propertyName);
    return result.isPresent() ? result.get() : defaultValue;
  }

  public Optional<Integer> getIntProperty(final String propertyName) {
    final Optional<String> result = this.getProperty(propertyName);
    return result.isPresent() ? Optional.of(Integer.parseInt(result.get())) : Optional.empty();
  }

  public String getProperty(final String propertyName, final String defaultValue) {
    final Optional<String> result = this.getProperty(propertyName);
    return result.isPresent() ? result.get() : defaultValue;
  }

  public Optional<String> getProperty(final String propertyName) {
    final String result = inner.get(propertyName);
    return result != null ? Optional.of(result.trim()) : Optional.empty();
  }

  public final static Settings get() {
    if (INSTANCE == null) {
      synchronized (Settings.class) {
        if (INSTANCE == null) {
          INSTANCE = new Settings();
        }
      }
    }
    return INSTANCE;
  }
}
