package com.gojek.dao;

import com.gojek.model.Vehicle;

import java.util.List;

public interface ParkingDAO<T extends Vehicle> {
  int getRemainingSlots();

  int getCapacity();

  int parkVehicle(T vehicle) throws NoParkingAvailableException, DuplicateVehicleRegNoException;

  T leaveVehicle(int parkingNumber) throws EmptyParkingSlotException, InvalidParkingSlotException;

  List<T> listAllVehicles();

  List<T> listVehiclesWithColor(String color);

  List<Integer> listSlotsWithVehicleColor(String color);

  int getSlotNumberForVehicle(String registrationNumber) throws VehicleNotFoundException;
}
