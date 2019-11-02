package com.gojek.constants;

import com.gojek.utils.Settings;

import java.util.Arrays;

public enum Command {
  CREATE_PARKING_LOT(Settings.get().getProperty("command.create.parking_lot").orElse("create_parking_lot")),
  PARK_VEHICLE(Settings.get().getProperty("command.park.vehicle").orElse("park")),
  LEAVE_VEHICLE(Settings.get().getProperty("command.leave.vehicle").orElse("leave")),
  STATUS(Settings.get().getProperty("command.status.parking_lot").orElse("status")),
  LIST_VEHICLES_WITH_COLOR(Settings.get().getProperty("command.list_all.vehicles_with_color").orElse("registration_numbers_for_cars_with_colour")),
  SLOT_NUMBERS_FOR_VEHICLES_WITH_COLOR(Settings.get().getProperty("command.list_all_slot_numbers.vehicles_with_color").orElse("slot_numbers_for_cars_with_colour")),
  SLOT_NUMBER_FOR_REGISTRATION_NUMBER(Settings.get().getProperty("command.slot_number.vehicle_registration_number").orElse("slot_number_for_registration_number"));

  private final String value;

  Command(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Command getCommand(final String command) throws CommandNotFoundException {
    return Arrays.stream(values())
            .filter(cmd -> cmd.getValue().equals(command))
            .findFirst()
            .orElseThrow(() -> new CommandNotFoundException(
                    String.format(
                            Settings.get().getProperty("errormsg.unknown.command").orElse("ERROR: Unknown command %s"),
                            command)
            ));
  }
}
