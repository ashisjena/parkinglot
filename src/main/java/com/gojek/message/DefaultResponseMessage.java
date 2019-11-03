package com.gojek.message;

import com.gojek.model.Vehicle;
import com.gojek.utils.Settings;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultResponseMessage<T extends Vehicle> implements ResponseMessage<T> {
  @Override
  public String createParkingLotMsg(int capacity) {
    return String.format(Settings.get().getProperty("msg.create.parkinglot").orElse(PROPERTY_NOTFOUND), capacity);
  }

  @Override
  public String parkVehicleMsg(int slot) {
    return String.format(Settings.get().getProperty("msg.park.vehicle").orElse(PROPERTY_NOTFOUND), slot);
  }

  @Override
  public String parkingLotFullErrorMsg() {
    return Settings.get().getProperty("errormsg.parking.full").orElse(PROPERTY_NOTFOUND);
  }

  @Override
  public String duplicateVehicleErrorMsg(String regNo) {
    return String.format(Settings.get().getProperty("errormsg.duplicate.vehicle_regno").orElse(PROPERTY_NOTFOUND), regNo);
  }

  @Override
  public String leaveParkingMsg(int slot) {
    return String.format(Settings.get().getProperty("msg.leave.parkinglot").orElse(PROPERTY_NOTFOUND), slot);
  }

  @Override
  public String slotAlreadyEmptyErrorMsg(int slot) {
    return String.format(Settings.get().getProperty("errormsg.parkingslot.isempty").orElse(PROPERTY_NOTFOUND), slot);
  }

  @Override
  public String statusMsg(List<T> vehicles) {
    final StringBuilder sBuilder = new StringBuilder();
    sBuilder.append(Settings.get().getProperty("msg.status.heading").orElse(PROPERTY_NOTFOUND));
    sBuilder.append(System.lineSeparator());
    sBuilder.append(vehicles.stream()
            .map(vehicle -> String.format(Settings.get().getProperty("msg.status.body").orElse(PROPERTY_NOTFOUND),
                    vehicle.getAssignedParkingSlot(), vehicle.getRegistrationNo(), vehicle.getColor()))
            .collect(Collectors.joining(System.lineSeparator())));
    return sBuilder.toString();
  }

  @Override
  public String parkingLotEmptyMsg() {
    return Settings.get().getProperty("msg.status.empty").orElse(PROPERTY_NOTFOUND);
  }

  @Override
  public String listRegNosForVehicleColorMsg(List<String> regNos) {
    return regNos.stream().collect(Collectors.joining(", "));
  }

  @Override
  public String vehiclesNotFoundForColorErrorMsg(String color) {
    return String.format(Settings.get().getProperty("errormsg.listregno.color.notfound").orElse(PROPERTY_NOTFOUND), color);
  }

  @Override
  public String listSlotsForVehicleColorMsg(List<Integer> slots) {
    return slots.stream().map(String::valueOf).collect(Collectors.joining(", "));
  }

  @Override
  public String slotsNotFoundWithVehicleColorErrorMsg(String color) {
    return String.format(Settings.get().getProperty("errormsg.listslotno.color.notfound").orElse(PROPERTY_NOTFOUND), color);
  }

  @Override
  public String slotForVehicleMsg(int slot) {
    return String.valueOf(slot);
  }

  @Override
  public String slotNotFoundForVehicleErrorMsg() {
    return Settings.get().getProperty("errormsg.slotno.regno.notfound").orElse(PROPERTY_NOTFOUND);
  }

  @Override
  public String invalidSlotErrorMsg(int slotNo) {
    return Settings.get().getProperty("errormsg.slotno.invalid").orElse(PROPERTY_NOTFOUND);
  }
}
