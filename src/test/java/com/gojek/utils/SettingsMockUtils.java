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
  }
}
