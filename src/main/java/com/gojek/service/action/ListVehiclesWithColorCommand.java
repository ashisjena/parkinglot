package com.gojek.service.action;

import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;

public class ListVehiclesWithColorCommand extends AbstractCommandAction {

  public ListVehiclesWithColorCommand(String inputCommand) {
    super(inputCommand, Command.LEAVE_VEHICLE);
  }

  @Override
  public boolean isValidCommand() {
    return getInputCommands().length == 2 && getInputCommands()[1].matches("[a-zA-Z]+");
  }

  @Override
  public String execute(ParkingLot parkingLot) {
    return parkingLot.getVehicleRegNosForColor(getInputCommands()[1]);
  }
}
