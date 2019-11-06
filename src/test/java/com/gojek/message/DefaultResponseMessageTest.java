package com.gojek.message;

import com.gojek.model.Car;
import com.gojek.model.Vehicle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class DefaultResponseMessageTest {
  private ResponseMessage<Vehicle> message;

  @BeforeClass
  public static void beforeClass() throws ClassNotFoundException {
    Class.forName("com.gojek.utils.SettingsMockUtils");
  }

  @Before
  public void setUp() {
    this.message = new DefaultResponseMessage<>();
  }

  @Test
  public void createParkingLotMsg() {
    Assert.assertEquals("Create Parking log", "Created a parking lot with 6 slots", this.message.createParkingLotMsg(6));
  }

  @Test
  public void parkVehicleMsg() {
    Assert.assertEquals("park vehicle", "Allocated slot number: 7", this.message.parkVehicleMsg(7));
  }

  @Test
  public void parkingLotFullErrorMsg() {
    Assert.assertEquals("park vehicle", "Sorry, parking lot is full", this.message.parkingLotFullErrorMsg());
  }

  @Test
  public void duplicateVehicleErrorMsg() {
    Assert.assertEquals("duplicate vehicle", "Vehicle has duplicate registration no KA-01-HH-1234", this.message.duplicateVehicleErrorMsg("KA-01-HH-1234"));
  }

  @Test
  public void leaveParkingMsg() {
    Assert.assertEquals("leave vehicle", "Slot number 8 is free", this.message.leaveParkingMsg(8));
  }

  @Test
  public void slotAlreadyEmptyErrorMsg() {
    Assert.assertEquals("slot already empty", "Parking slot 8 is already empty", this.message.slotAlreadyEmptyErrorMsg(8));
  }

  @Test
  public void statusMsg() {
    final Car car1 = new Car("KA-01-HH-1234", "White");
    car1.setAssignedParkingSlot(1);
    final Car car2 = new Car("KA-01-HH-1111", "White");
    car2.setAssignedParkingSlot(2);

    final String expected = "Slot No.    Registration No    Colour" + System.lineSeparator() +
            car1.getAssignedParkingSlot() + "           " + car1.getRegistrationNo() + "      " + car1.getColor() + System.lineSeparator() +
            car2.getAssignedParkingSlot() + "           " + car2.getRegistrationNo() + "      " + car2.getColor();

    Assert.assertEquals("status msg", expected, this.message.statusMsg(
            Arrays.asList(new Car[]{car1, car2})));
  }

  @Test
  public void parkingLotEmptyMsg() {
    Assert.assertEquals("parking lot empty", "Parking lot is empty", this.message.parkingLotEmptyMsg());
  }

  @Test
  public void listRegNosForVehicleColorMsg() {
    Assert.assertEquals("list vehicle of color", "KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333", this.message.listRegNosForVehicleColorMsg(Arrays.asList(new String[]{"KA-01-HH-1234", "KA-01-HH-9999", "KA-01-P-333"})));
  }

  @Test
  public void vehiclesNotFoundForColorErrorMsg() {
    Assert.assertEquals("No vehicles of color found", "No Vehicles found with color RED", this.message.vehiclesNotFoundForColorErrorMsg("RED"));
  }

  @Test
  public void listSlotsForVehicleColorMsg() {
    Assert.assertEquals("Slots for vehicle with color", "1, 2, 3", this.message.listSlotsForVehicleColorMsg(Arrays.asList(new Integer[]{1, 2, 3})));
  }

  @Test
  public void slotsNotFoundWithVehicleColorErrorMsg() {
    Assert.assertEquals("Slots not found for vehicle with color", "No Slots found for color RED", this.message.slotsNotFoundWithVehicleColorErrorMsg("RED"));
  }

  @Test
  public void slotForVehicleMsg() {
    Assert.assertEquals("Slot for vehicle", "5", this.message.slotForVehicleMsg(5));
  }

  @Test
  public void slotNotFoundForVehicleErrorMsg() {
    Assert.assertEquals("No Slot for vehicle", "Not found", this.message.slotNotFoundForVehicleErrorMsg());
  }
}