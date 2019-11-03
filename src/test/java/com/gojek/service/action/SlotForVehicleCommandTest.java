package com.gojek.service.action;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SlotForVehicleCommandTest {
  @BeforeClass
  public static void beforeClass() throws ClassNotFoundException {
    Class.forName("com.gojek.utils.SettingsMockUtils");
  }

  @Test
  public void isValidCommandTrue() {
    final AbstractCommandAction command = new SlotForVehicleCommand("slot_number_for_registration_number KA-01-P-333");
    Assert.assertTrue("Valid Command", command.isValidCommand());
  }

  @Test
  public void inValidCommand_NoRegNo() {
    final AbstractCommandAction command = new SlotForVehicleCommand("slot_number_for_registration_number");
    Assert.assertFalse("Invalid Command no reg no", command.isValidCommand());
  }

  @Test
  public void inValidCommand_MalformedRegNo() {
    final AbstractCommandAction command = new SlotForVehicleCommand("slot_number_for_registration_number KA-01-333 White");
    Assert.assertFalse("Invalid Command Malformed reg no", command.isValidCommand());
  }
}