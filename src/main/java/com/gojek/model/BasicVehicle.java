package com.gojek.model;

public abstract class BasicVehicle implements Vehicle {
  private final String registrationNo;
  private final String color;
  private final int requiredParkingSlots;

  protected BasicVehicle(final String registrationNo, final String color, final int requiredParkingSlots) {
    this.registrationNo = registrationNo;
    this.color = color;
    this.requiredParkingSlots = requiredParkingSlots;
  }

  @Override
  public String getRegistrationNo() {
    return registrationNo;
  }

  @Override
  public String getColor() {
    return color;
  }

  @Override
  public int getRequiredParkingSlots() {
    return requiredParkingSlots;
  }
}
