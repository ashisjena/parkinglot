package com.gojek.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class CommandLineReader implements InputReader {
  private Stream<String> stream;
  private BufferedReader reader;

  public CommandLineReader() {
    this.reader = new BufferedReader(new InputStreamReader(System.in));
  }

  @Override
  public Stream<String> stream() {
    this.stream = this.reader.lines();
    return this.stream;
  }

  public String read() throws IOException {
    return this.reader.readLine();
  }

  @Override
  public void close() {
    try {
      if (this.reader != null) {
        this.reader.close();
      }
      if (this.stream != null) {
        this.stream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
