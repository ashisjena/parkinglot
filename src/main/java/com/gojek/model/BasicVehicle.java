package com.gojek.model;

import java.util.Objects;

public abstract class BasicVehicle implements Vehicle {
  private final String registrationNo;
  private final String color;
  private final int requiredParkingSlots;
  private Integer assignedParkingSlot;

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

  @Override
  public Integer getAssignedParkingSlot() {
    return assignedParkingSlot;
  }

  @Override
  public void setAssignedParkingSlot(Integer assignedParkingSlot) {
    this.assignedParkingSlot = assignedParkingSlot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BasicVehicle)) return false;
    BasicVehicle that = (BasicVehicle) o;
    return Objects.equals(getRegistrationNo(), that.getRegistrationNo());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getRegistrationNo());
  }
}
