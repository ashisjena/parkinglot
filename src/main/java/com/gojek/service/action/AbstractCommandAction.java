package com.gojek.service.action;

import com.gojek.ParkingException;
import com.gojek.service.action.constants.Command;
import com.gojek.service.ParkingLot;

public abstract class AbstractCommandAction {
  private final String[] inputCommands;
  private final Command command;

  public AbstractCommandAction(String inputCommand, Command command) {
    this.inputCommands = inputCommand.split("\\s+");
    this.command = command;
  }

  public String[] getInputCommands() {
    return inputCommands;
  }

  public Command getCommand() {
    return this.command;
  }

  public abstract boolean isValidCommand();

  public abstract String execute(ParkingLot parkingLot) throws ParkingException;
}
