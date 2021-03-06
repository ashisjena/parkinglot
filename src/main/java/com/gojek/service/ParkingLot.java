package com.gojek.service;

import com.gojek.ParkingException;
import com.gojek.service.action.DispatchStrategy;

public interface ParkingLot {
  String createParkingLot(int capacity) throws ParkingException;

  String parkVehicle(String regNo, String color);

  String leaveParking(int id, int slotNo);

  String getStatus();

  String getVehicleRegNosForColor(String color);

  String getSlotNosForVehicleColor(String color);

  String getSlotNoForVehicle(String regNo);

  String setDispatchRule(DispatchStrategy strategy);
}
