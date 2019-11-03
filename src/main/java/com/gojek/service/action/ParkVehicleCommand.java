package com.gojek.service.action;

import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;
import com.gojek.utils.Settings;

public class ParkVehicleCommand extends AbstractCommandAction {

  public ParkVehicleCommand(String inputCommand) {
    super(inputCommand, Command.PARK_VEHICLE);
  }

  @Override
  public boolean isValidCommand() {
    return getInputCommands().length == 3 &&
            getInputCommands()[1].matches(Settings.get().getProperty("regex.registration.number").get()) &&
            getInputCommands()[2].matches("[a-zA-Z]+");
  }

  @Override
  public String execute(ParkingLot parkingLot) {
    return parkingLot.parkVehicle(getInputCommands()[1], getInputCommands()[2]);
  }
}
