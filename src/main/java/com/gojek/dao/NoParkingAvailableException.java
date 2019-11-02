package com.gojek.dao;

import com.gojek.ParkingException;

public class NoParkingAvailableException extends ParkingException {
  public NoParkingAvailableException(final String msg) {
    super(msg);
  }
}
