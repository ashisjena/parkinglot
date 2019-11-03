package com.gojek.utils;


import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class SettingsMockUtils {
  private static Settings settingsMock;

  static {
    settingsMock = mock(Settings.class);
    setMockForSettingsSingleton();
    recordSettings();
  }

  public static Settings getSettingsMock() {
    return settingsMock;
  }

  private static void setMockForSettingsSingleton() {
    AccessController.doPrivileged((PrivilegedAction<Field>) () -> {
      Field instance = null;
      try {
        instance = Settings.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(instance, settingsMock);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        // log error
      }
      return instance;
    });
  }

  private static void recordSettings() {
    lenient().when(settingsMock.getProperty(anyString())).thenReturn(Optional.empty());
    lenient().when(settingsMock.getIntProperty(anyString())).thenReturn(Optional.empty());
    lenient().when(settingsMock.getIntProperty("data.car.required_slots")).thenReturn(Optional.of(1));
    lenient().when(settingsMock.getIntProperty("data.bus.required_slots")).thenReturn(Optional.of(2));
    lenient().when(settingsMock.getProperty("command.create.parking_lot")).thenReturn(Optional.of("create_parking_lot"));
    lenient().when(settingsMock.getProperty("command.park.vehicle")).thenReturn(Optional.of("park"));
    lenient().when(settingsMock.getProperty("command.leave.vehicle")).thenReturn(Optional.of("leave"));
    lenient().when(settingsMock.getProperty("command.status.parking_lot")).thenReturn(Optional.of("status"));
    lenient().when(settingsMock.getProperty("command.list_all.vehicles_with_color")).thenReturn(Optional.of("registration_numbers_for_cars_with_colour"));
    lenient().when(settingsMock.getProperty("command.list_all_slot_numbers.vehicles_with_color")).thenReturn(Optional.of("slot_numbers_for_cars_with_colour"));
    lenient().when(settingsMock.getProperty("command.slot_number.vehicle_registration_number")).thenReturn(Optional.of("slot_number_for_registration_number"));
    lenient().when(settingsMock.getProperty("errormsg.parking.full")).thenReturn(Optional.of("Sorry, parking lot is full"));
    lenient().when(settingsMock.getProperty("errormsg.vehicle.notfound")).thenReturn(Optional.of("Vehicle with registration number %s not found"));
    lenient().when(settingsMock.getProperty("errormsg.parkingslot.notempty")).thenReturn(Optional.of("Parking slot %d is not empty"));
    lenient().when(settingsMock.getProperty("errormsg.parkingslot.isempty")).thenReturn(Optional.of("Parking slot %d is already empty"));
    lenient().when(settingsMock.getProperty("errormsg.unknown.command")).thenReturn(Optional.of("Unknown command %s received"));
    lenient().when(settingsMock.getProperty("errormsg.duplicate.vehicle_regno")).thenReturn(Optional.of("Vehicle has duplicate registration no %s"));
    lenient().when(settingsMock.getProperty("errormsg.listregno.color.notfound")).thenReturn(Optional.of("No Vehicles found with color %s"));
    lenient().when(settingsMock.getProperty("errormsg.listslotno.color.notfound")).thenReturn(Optional.of("No Slots found for color %s"));
    lenient().when(settingsMock.getProperty("errormsg.slotno.regno.notfound")).thenReturn(Optional.of("Not found"));
    lenient().when(settingsMock.getProperty("errormsg.slotno.invalid")).thenReturn(Optional.of("Invalid slot"));
    lenient().when(settingsMock.getProperty("msg.create.parkinglot")).thenReturn(Optional.of("Created a parking lot with %d slots"));
    lenient().when(settingsMock.getProperty("msg.park.vehicle")).thenReturn(Optional.of("Allocated slot number: %d"));
    lenient().when(settingsMock.getProperty("msg.leave.parkinglot")).thenReturn(Optional.of("Slot number %d is free"));
    lenient().when(settingsMock.getProperty("msg.status.heading")).thenReturn(Optional.of("Slot No.\tRegistration No\tColour"));
    lenient().when(settingsMock.getProperty("msg.status.body")).thenReturn(Optional.of("%d\t%s\t%s"));
    lenient().when(settingsMock.getProperty("msg.status.empty")).thenReturn(Optional.of("Parking lot is empty"));
    lenient().when(settingsMock.getProperty("regex.registration.number")).thenReturn(Optional.of("[A-Za-z]{2}-\\d{2}-[A-Za-z]{1,2}-\\d{3,4}"));
  }
}
