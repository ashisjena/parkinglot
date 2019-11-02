package com.gojek.io;

import java.io.IOException;

public interface OutputWriter extends AutoCloseable {
  void write(final String line) throws IOException;

  void flush() throws IOException;
}
