package com.gojek.service.action;

import com.gojek.service.action.constants.Command;

public class ActionFactory {
  private static ActionFactory INSTANCE;

  public static ActionFactory getInstance() {
    if (INSTANCE == null) {
      synchronized (ActionFactory.class) {
        if (INSTANCE == null) {
          INSTANCE = new ActionFactory();
        }
      }
    }
    return INSTANCE;
  }

  private ActionFactory() {
    if (INSTANCE != null) {
      throw new RuntimeException("ERROR: ActionFactory is already initialized");
    }
  }

  public final AbstractCommandAction getAction(String input) throws CommandNotFoundException {

    final Command command = input == null ? Command.DEFAULT_COMMAND : Command.getCommand(input.split("\\s+")[0]);

    switch (command) {
      case CREATE_PARKING_LOT:
        return new CreateParkingLotCommand(input);
      case PARK_VEHICLE:
        return new ParkVehicleCommand(input);
      case STATUS:
        return new StatusCommand(input);
      case LEAVE_VEHICLE:
        return new LeaveVehicleCommand(input);
      case LIST_VEHICLES_WITH_COLOR:
        return new ListVehiclesWithColorCommand(input);
      case SLOT_NUMBERS_FOR_VEHICLES_WITH_COLOR:
        return new ListSlotsWithVehicleColorCommand(input);
      case SLOT_NUMBER_FOR_REGISTRATION_NUMBER:
        return new SlotForVehicleCommand(input);
      default:
        throw new CommandNotFoundException(String.format("ERROR: Invalid command type %s", input));
    }
  }
}
