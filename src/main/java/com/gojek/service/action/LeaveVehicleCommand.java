package com.gojek.service.action;

import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;

public class LeaveVehicleCommand extends AbstractCommandAction {

  public LeaveVehicleCommand(String inputCommand) {
    super(inputCommand, Command.LEAVE_VEHICLE);
  }

  @Override
  public boolean isValidCommand() {
    return getInputCommands().length == 2 && getInputCommands()[1].matches("\\d+");
  }

  @Override
  public String execute(ParkingLot parkingLot) {
    return parkingLot.leaveParking(1, Integer.parseInt(getInputCommands()[1]));
  }
}
