package com.gojek.model;

public interface Vehicle {
  String getRegistrationNo();

  String getColor();

  int getRequiredParkingSlots();

  Integer getAssignedParkingSlot();

  void setAssignedParkingSlot(Integer assignedParkingSlot);
}
