package com.gojek.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;

public class TextFileWriter implements OutputWriter {
  private final Path path;
  private final BufferedWriter writer;

  public TextFileWriter(final Path path) throws IOException {
    this.path = path;
    FileSystem fs = FileSystems.getDefault();
    if (!fs.isOpen()) {
      throw new IOException("ERROR: Write restricted for this System");
    }
    // Delete the old file.
    // TODO: FIXME - Just for testing purpose.
    Files.deleteIfExists(this.path);
    this.writer = Files.newBufferedWriter(this.path, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
  }

  @Override
  public void write(final String line) throws IOException {
    this.writer.write(line);
    this.writer.write(System.lineSeparator());
  }

  @Override
  public void flush() throws IOException {
    this.writer.flush();
  }

  @Override
  public void close() {
    try {
      this.writer.close();
    } catch (IOException e) {
      // Log Error
    }
  }
}
