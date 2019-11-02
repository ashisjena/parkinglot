package com.gojek.utils;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

public class SettingsTest {
  private static Settings settings;

  @BeforeClass
  public static void init() {
    settings = SettingsMockUtils.getSettingsMock();
  }

  @Test
  public void getIntProperty() {
    Assert.assertEquals("Fetch Int property from settings", Optional.of(1), settings.getIntProperty("data.car.required_slots"));
  }

  @Test
  public void getProperty() {
    Assert.assertEquals("Fetch String property from settings", Optional.of("park"), settings.getProperty("command.park.vehicle"));
  }
}