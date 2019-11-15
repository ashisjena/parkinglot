package com.gojek.service.action;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ActionFactoryTest {

  private static ActionFactory factory;

  @BeforeClass
  public static void beforeClass() {
    factory = ActionFactory.getInstance();
  }

  @Test
  public void getActionCreateParkingLot() throws CommandNotFoundException {
    Assert.assertTrue("Create parking lot", factory.getAction("create_parking_lot 12") instanceof CreateParkingLotCommand);
  }

  @Test
  public void getActionParkVehicle() throws CommandNotFoundException {
    Assert.assertTrue("Park Vehicle", factory.getAction("park KA-01-HH-1234 White") instanceof ParkVehicleCommand);
  }

  @Test
  public void getActionLeave() throws CommandNotFoundException {
    Assert.assertTrue("Leave", factory.getAction("leave 4") instanceof LeaveVehicleCommand);
  }

  @Test
  public void getActionStatus() throws CommandNotFoundException {
    Assert.assertTrue("Status", factory.getAction("status") instanceof StatusCommand);
  }

  @Test
  public void getActionListVehiclesWithColor() throws CommandNotFoundException {
    Assert.assertTrue("List Vehicles with a color", factory.getAction("registration_numbers_for_cars_with_colour White") instanceof ListVehiclesWithColorCommand);
  }

  @Test
  public void getActionListSlotsWithVehicleColor() throws CommandNotFoundException {
    Assert.assertTrue("List slots with vehicle color", factory.getAction("slot_numbers_for_cars_with_colour White") instanceof ListSlotsWithVehicleColorCommand);
  }

  @Test
  public void getActionGetSlotForVehicle() throws CommandNotFoundException {
    Assert.assertTrue("Get slot for vehicle", factory.getAction("slot_number_for_registration_number MH-04-AY-1111") instanceof SlotForVehicleCommand);
  }

  @Test(expected = CommandNotFoundException.class)
  public void throwExceptionForEmptyInput() throws CommandNotFoundException {
    factory.getAction(null);
  }

  @Test(expected = CommandNotFoundException.class)
  public void throwExceptionForUnknownCommand() throws CommandNotFoundException {
    factory.getAction("blah blah");
  }

  @Test(expected = InvocationTargetException.class)
  public void createInstanceThroughReflection() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    ActionFactory.getInstance();
    Constructor<ActionFactory> defaultConstructor = AccessController.doPrivileged((PrivilegedAction<Constructor<ActionFactory>>) () -> {
      Constructor<ActionFactory> constructor = null;
      try {
        constructor = ActionFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);
      } catch (NoSuchMethodException e) {
      }
      return constructor;
    });
    defaultConstructor.newInstance();
  }

}