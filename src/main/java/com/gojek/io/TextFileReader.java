package com.gojek.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TextFileReader implements InputReader {
  private final Path path;
  private Stream<String> stream;

  public TextFileReader(final Path path) throws IOException {
    this.path = path;
    if (!Files.exists(path.toAbsolutePath())) {
      throw new FileNotFoundException("ERROR: The file " + path.toAbsolutePath() + " doesn't exist");
    }
  }

  @Override
  public Stream<String> stream() throws IOException {
    this.stream = Files.lines(path.toAbsolutePath(), Charset.defaultCharset());
    return this.stream;
  }

  @Override
  public void close() {
    if (this.stream != null) {
      this.stream.close();
    }
  }
}
