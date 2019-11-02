package com.gojek.dao;

import com.gojek.ParkingException;

public class ParkingSlotIsNotEmptyException extends ParkingException {
  public ParkingSlotIsNotEmptyException(final String msg) {
    super(msg);
  }
}
