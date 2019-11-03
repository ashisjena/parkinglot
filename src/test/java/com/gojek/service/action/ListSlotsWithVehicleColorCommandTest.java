package com.gojek.service.action;

import org.junit.Assert;
import org.junit.Test;

public class ListSlotsWithVehicleColorCommandTest {

  @Test
  public void isValidCommandTrue() {
    final AbstractCommandAction command = new ListSlotsWithVehicleColorCommand("slot_numbers_for_cars_with_colour White");
    Assert.assertTrue("Valid Command", command.isValidCommand());
  }

  @Test
  public void inValidCommand_NoColor() {
    final AbstractCommandAction command = new ListSlotsWithVehicleColorCommand("slot_numbers_for_cars_with_colour");
    Assert.assertFalse("Invalid Command no color", command.isValidCommand());
  }

  @Test
  public void inValidCommand_MalformedColor() {
    final AbstractCommandAction command = new ListSlotsWithVehicleColorCommand("slot_numbers_for_cars_with_colour 21323");
    Assert.assertFalse("Invalid Command Malformed color", command.isValidCommand());
  }
}