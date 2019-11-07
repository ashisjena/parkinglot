package com.gojek.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public class InMemoryStringReaderImpl implements InputReader {
  private BufferedReader br;

  public InMemoryStringReaderImpl(String input) {
    this.br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes(Charset.defaultCharset()))));
  }

  @Override
  public Stream<String> stream() {
    return this.br.lines();
  }

  @Override
  public void close() throws Exception {
    this.br.close();
  }
}
