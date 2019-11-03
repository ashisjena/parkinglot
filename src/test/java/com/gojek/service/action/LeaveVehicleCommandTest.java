package com.gojek.service.action;

import org.junit.Assert;
import org.junit.Test;

public class LeaveVehicleCommandTest {

  @Test
  public void isValidCommandTrue() {
    final AbstractCommandAction command = new LeaveVehicleCommand("leave 6");
    Assert.assertTrue("Valid Command", command.isValidCommand());
  }

  @Test
  public void inValidCommand_NoSlot() {
    final AbstractCommandAction command = new LeaveVehicleCommand("leave");
    Assert.assertFalse("Invalid Command no slot", command.isValidCommand());
  }

  @Test
  public void inValidCommand_MalformedSlot() {
    final AbstractCommandAction command = new LeaveVehicleCommand("leave blah");
    Assert.assertFalse("Invalid Command Malformed slot", command.isValidCommand());
  }
}