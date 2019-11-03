package com.gojek.service.action;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParkVehicleCommandTest {
  @BeforeClass
  public static void beforeClass() throws ClassNotFoundException {
    Class.forName("com.gojek.utils.SettingsMockUtils");
  }

  @Test
  public void isValidCommandTrue() {
    final AbstractCommandAction command = new ParkVehicleCommand("park KA-01-P-333 White");
    Assert.assertTrue("Valid Command", command.isValidCommand());
  }

  @Test
  public void inValidCommand_NoColor() {
    final AbstractCommandAction command = new ParkVehicleCommand("park KA-01-P-333");
    Assert.assertFalse("Invalid Command no color", command.isValidCommand());
  }

  @Test
  public void inValidCommand_NoRegNo() {
    final AbstractCommandAction command = new ParkVehicleCommand("park");
    Assert.assertFalse("Invalid Command no reg no", command.isValidCommand());
  }

  @Test
  public void inValidCommand_MalformedRegNo() {
    final AbstractCommandAction command = new ParkVehicleCommand("park KA-01-333 White");
    Assert.assertFalse("Invalid Command Malformed reg no", command.isValidCommand());
  }
}