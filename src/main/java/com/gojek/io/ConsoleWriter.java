package com.gojek.io;

import java.io.PrintWriter;

public class ConsoleWriter implements OutputWriter {
  public final PrintWriter writer;

  public ConsoleWriter() {
    this.writer = new PrintWriter(System.out);
  }

  @Override
  public void write(final String line) {
    this.writer.write(line);
    this.writer.write(System.lineSeparator());
    this.flush();
  }

  @Override
  public void flush() {
    this.writer.flush();
  }

  @Override
  public void close() {
    this.writer.close();
  }
}
