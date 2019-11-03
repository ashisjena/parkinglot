package com.gojek.service.action;

import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;

public class StatusCommand extends AbstractCommandAction {

  public StatusCommand(String inputCommand) {
    super(inputCommand, Command.STATUS);
  }

  @Override
  public boolean isValidCommand() {
    return true;
  }

  @Override
  public String execute(ParkingLot parkingLot) {
    return parkingLot.getStatus();
  }
}
