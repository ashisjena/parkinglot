package com.gojek.constants;

import com.gojek.ParkingException;

public class CommandNotFoundException extends ParkingException {
  public CommandNotFoundException(final String msg) {
    super(msg);
  }
}
