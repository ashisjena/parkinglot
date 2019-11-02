package com.gojek.model;

import com.gojek.utils.Settings;

public class Car extends BasicVehicle {

  public Car(String registrationNo, String color) {
    super(registrationNo, color, Settings.get().getIntProperty("data.car.required_slots").orElse(1));
  }
}
