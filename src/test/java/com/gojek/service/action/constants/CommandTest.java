package com.gojek.service.action.constants;

import org.junit.Assert;
import org.junit.Test;

public class CommandTest {

  @Test
  public void getCreateParkingCommand() {
    Assert.assertEquals("Create Parking Lot", Command.CREATE_PARKING_LOT, Command.getCommand("create_parking_lot"));
  }

  @Test
  public void getParkVehicleCommand() {
    Assert.assertEquals("Park Vehicle", Command.PARK_VEHICLE, Command.getCommand("park"));
  }

  @Test
  public void getLeaveCommand() {
    Assert.assertEquals("Leave Vehicle", Command.LEAVE_VEHICLE, Command.getCommand("leave"));
  }

  @Test
  public void getStatusCommand() {
    Assert.assertEquals("Status", Command.STATUS, Command.getCommand("status"));
  }

  @Test
  public void getListVehiclesWithColorCommand() {
    Assert.assertEquals("List Vehicle with color", Command.LIST_VEHICLES_WITH_COLOR, Command.getCommand("registration_numbers_for_cars_with_colour"));
  }

  @Test
  public void getListSlotsWithVehicleColorCommand() {
    Assert.assertEquals("List Slots with vehicle color", Command.SLOT_NUMBERS_FOR_VEHICLES_WITH_COLOR, Command.getCommand("slot_numbers_for_cars_with_colour"));
  }

  @Test
  public void getSlotsForVehicleCommand() {
    Assert.assertEquals("Get Slot for Vehicle", Command.SLOT_NUMBER_FOR_REGISTRATION_NUMBER, Command.getCommand("slot_number_for_registration_number"));
  }

  @Test
  public void getDefaultCommandForNullValue() {
    Assert.assertEquals("Null value", Command.DEFAULT_COMMAND, Command.getCommand(null));
  }

  @Test
  public void getDefaultCommandForInvalidValue() {
    Assert.assertEquals("Invalid value", Command.DEFAULT_COMMAND, Command.getCommand("blah blah"));
  }
}