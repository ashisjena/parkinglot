package com.gojek.io;

import java.io.BufferedReader;
import java.util.stream.Stream;

public class CommandLineReader implements InputReader {
  private Stream<String> stream;

  @Override
  public Stream<String> stream() {
    this.stream = new BufferedReader(System.console().reader()).lines();
    return this.stream;
  }

  @Override
  public void close() {
    if (this.stream != null) {
      this.stream.close();
    }
  }
}
