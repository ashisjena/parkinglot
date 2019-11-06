package com.gojek.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class Settings {
  public static final String REGEX_PROPERTY_FILE_NAME = ".*[\\d]{2}.properties$";
  private static Settings INSTANCE;
  private final Map<String, String> inner;

  private Settings() {
    if (INSTANCE != null) {
      throw new RuntimeException("ERROR: Settings is already initialized");
    }
    this.inner = new ConcurrentHashMap<>();
  }

  public void register() {
    final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    try {
      if (jarFile.isFile()) {
        loadPropertiesFromJarFile(jarFile);
      } else {
        loadPropertiesWhileMavenExec();
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private void loadPropertiesWhileMavenExec() throws URISyntaxException, IOException {
    try (final Stream<Path> paths = Files.list(Paths.get(getClass().getClassLoader().getResource("./").toURI()))) {
      loadProperties(paths, false);
    }
  }

  private void loadPropertiesFromJarFile(File jarFile) throws IOException {
    try (final JarFile jar = new JarFile(jarFile);
         final Stream<Path> paths = jar.stream().map(jarEntry -> Paths.get(jarEntry.getName()))) {
      loadProperties(paths, true);
    }
  }

  private void loadProperties(Stream<Path> paths, boolean isJarFile) {
    paths.filter(path -> path.toString().matches(REGEX_PROPERTY_FILE_NAME)).forEach(p -> {
      final Properties prop = new Properties();
      try {
        prop.load(getInputStream(p, isJarFile));
        prop.forEach((key, value) -> this.inner.put(key.toString(), value.toString()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private InputStream getInputStream(Path p, boolean isJarFile) throws IOException {
    final InputStream inputStream;
    if (isJarFile) {
      inputStream = getClass().getClassLoader().getResourceAsStream(p.toString());
    } else {
      inputStream = Files.newInputStream(p, StandardOpenOption.READ);
    }
    return inputStream;
  }

  public Optional<Integer> getIntProperty(final String propertyName) {
    final Optional<String> result = this.getProperty(propertyName);
    return result.map(Integer::parseInt);
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
