package com.gojek.io;

import java.io.PrintWriter;

public class ConsoleWriter implements OutputWriter {
  public final PrintWriter writer;

  public ConsoleWriter() {
    this.writer = System.console().writer();
  }

  @Override
  public void write(final String line) {
    this.writer.write(line);
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
