package com.gojek.service.action;

import com.gojek.ParkingException;

import java.util.Arrays;

public enum DispatchStrategy {
  EVEN_DISTRIBUTION("even_distribution"),
  FILL_FIRST("fill_first");

  private final String value;

  DispatchStrategy(String value) {
    this.value = value;
  }

  public static DispatchStrategy getStrategy(String strategy) throws ParkingException {
    return Arrays.stream(values())
            .filter(dispatchStrategy -> dispatchStrategy.getValue().equalsIgnoreCase(strategy))
            .findFirst()
            .orElseThrow(ParkingException::new);
  }

  public String getValue() {
    return this.value;
  }
}
