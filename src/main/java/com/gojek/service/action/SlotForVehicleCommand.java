package com.gojek.service.action;

import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;
import com.gojek.utils.Settings;

public class SlotForVehicleCommand extends AbstractCommandAction {

  public SlotForVehicleCommand(String inputCommand) {
    super(inputCommand, Command.LEAVE_VEHICLE);
  }

  @Override
  public boolean isValidCommand() {
    return getInputCommands().length == 2 &&
            getInputCommands()[1].matches(Settings.get().getProperty("regex.registration.number").get());
  }

  @Override
  public String execute(ParkingLot parkingLot) {
    return parkingLot.getSlotNoForVehicle(getInputCommands()[1]);
  }
}
