package com.gojek;

public class ParkingException extends Throwable {
  public ParkingException() {
  }

  public ParkingException(final String msg) {
    super(msg);
  }

  public ParkingException(Throwable th) {
    super(th);
  }

  public ParkingException(final String msg, final Throwable th) {
    super(msg, th);
  }
}
