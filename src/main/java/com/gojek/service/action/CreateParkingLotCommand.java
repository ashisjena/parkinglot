package com.gojek.service.action;

import com.gojek.ParkingException;
import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;

public class CreateParkingLotCommand extends AbstractCommandAction {

  public CreateParkingLotCommand(String inputCommand) {
    super(inputCommand, Command.CREATE_PARKING_LOT);
  }

  @Override
  public boolean isValidCommand() {
    return getInputCommands().length == 2 && getInputCommands()[1].matches("\\d+");
  }

  @Override
  public String execute(ParkingLot parkingLot) throws ParkingException {
    return parkingLot.createParkingLot(Integer.parseInt(getInputCommands()[1]));
  }
}
