package com.gojek.io;

import java.io.IOException;
import java.util.stream.Stream;

public interface InputReader extends AutoCloseable {
  Stream<String> stream() throws IOException;
}
