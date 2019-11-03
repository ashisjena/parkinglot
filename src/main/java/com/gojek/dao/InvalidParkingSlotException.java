package com.gojek.dao;

import com.gojek.ParkingException;

public class InvalidParkingSlotException extends ParkingException {
  public InvalidParkingSlotException(final String msg) {
    super(msg);
  }
}
