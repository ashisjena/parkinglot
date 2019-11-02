package com.gojek.dao;

import com.gojek.ParkingException;

public class EmptyParkingSlotException extends ParkingException {
  public EmptyParkingSlotException(final String msg) {
    super(msg);
  }
}
