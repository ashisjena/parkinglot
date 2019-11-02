package com.gojek.dao;

import com.gojek.ParkingException;

public class VehicleNotFoundException extends ParkingException {
  public VehicleNotFoundException(final String msg) {
    super(msg);
  }
}
