package com.gojek.model;

import com.gojek.utils.Settings;

public class Bus extends BasicVehicle {

  public Bus(String registrationNo, String color) {
    super(registrationNo, color, Settings.get().getIntProperty("data.bus.required_slots").orElse(2));
  }
}
