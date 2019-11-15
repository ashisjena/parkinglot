package com.gojek.service.action;

import com.gojek.service.action.constants.Command;
import org.junit.Assert;
import org.junit.Test;

public class CreateParkingLotCommandTest {

  @Test
  public void isValidCommandTrue() {
    final AbstractCommandAction command = new CreateParkingLotCommand("create_parking_lot 6");
    Assert.assertTrue("Valid Command", command.isValidCommand());
  }

  @Test
  public void inValidCommand_NoCapacity() {
    final AbstractCommandAction command = new CreateParkingLotCommand("create_parking_lot");
    Assert.assertFalse("Invalid Command no capacity", command.isValidCommand());
  }

  @Test
  public void inValidCommand_MalformedCapacity() {
    final AbstractCommandAction command = new CreateParkingLotCommand("create_parking_lot blah");
    Assert.assertFalse("Invalid Command Malformed capacity", command.isValidCommand());
  }

  @Test
  public void getCommand() {
    final AbstractCommandAction command = new CreateParkingLotCommand("create_parking_lot 9");
    Assert.assertEquals("Get Command", Command.CREATE_PARKING_LOT, command.getCommand());
  }
}