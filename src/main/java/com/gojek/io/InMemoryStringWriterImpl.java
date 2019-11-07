package com.gojek.io;

public class InMemoryStringWriterImpl implements OutputWriter {
  private StringBuilder sBuilder;

  public InMemoryStringWriterImpl() {
    this.sBuilder = new StringBuilder();
  }

  @Override
  public void write(String line) {
    this.sBuilder.append(line);
    this.sBuilder.append(System.lineSeparator());
  }

  @Override
  public String toString() {
    return this.sBuilder.toString();
  }

  @Override
  public void close() {
  }

  @Override
  public void flush() {
  }
}
