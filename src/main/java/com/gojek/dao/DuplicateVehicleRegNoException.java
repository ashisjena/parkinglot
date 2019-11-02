package com.gojek.dao;

import com.gojek.ParkingException;

public class DuplicateVehicleRegNoException extends ParkingException {
  public DuplicateVehicleRegNoException(final String msg) {
    super(msg);
  }
}
