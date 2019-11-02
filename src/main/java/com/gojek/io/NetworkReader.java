package com.gojek.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.stream.Stream;

public class NetworkReader implements InputReader {
  private final Socket socket;
  private Stream<String> stream;

  public NetworkReader(final Socket socket) {
    this.socket = socket;
  }

  @Override
  public Stream<String> stream() throws IOException {
    this.stream = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), Charset.defaultCharset())).lines();
    return this.stream;
  }

  @Override
  public void close() {
    if (this.stream != null) {
      this.stream.close();
    }
    if (this.socket != null) {
      try {
        this.socket.close();
      } catch (IOException e) {
        // log error
      }
    }
  }
}
