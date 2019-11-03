package com.gojek.message;

import com.gojek.model.Vehicle;

import java.util.List;

public interface ResponseMessage<T extends Vehicle> {
  String PROPERTY_NOTFOUND = "Message not found in properties";

  String createParkingLotMsg(int capacity);

  String parkVehicleMsg(int slot);

  String parkingLotFullErrorMsg();

  String duplicateVehicleErrorMsg(String regNo);

  String leaveParkingMsg(int slot);

  String slotAlreadyEmptyErrorMsg(int slot);

  String statusMsg(List<T> vehicles);

  String parkingLotEmptyMsg();

  String listRegNosForVehicleColorMsg(List<String> regNos);

  String vehiclesNotFoundForColorErrorMsg(String color);

  String listSlotsForVehicleColorMsg(List<Integer> slots);

  String slotsNotFoundWithVehicleColorErrorMsg(String color);

  String slotForVehicleMsg(int slot);

  String slotNotFoundForVehicleErrorMsg();

  String invalidSlotErrorMsg(int slotNo);
}
