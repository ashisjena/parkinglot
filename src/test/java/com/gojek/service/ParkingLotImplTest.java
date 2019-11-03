package com.gojek.service;

import com.gojek.ParkingException;
import com.gojek.message.DefaultResponseMessage;
import com.gojek.model.Vehicle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParkingLotImplTest {

  private ParkingLot parkingLot;

  @BeforeClass
  public static void beforeClass() throws ClassNotFoundException {
    Class.forName("com.gojek.utils.SettingsMockUtils");
  }

  @Before
  public void setUp() {
    this.parkingLot = new ParkingLotImpl(new DefaultResponseMessage<>());
  }

  @Test
  public void createParkingLot() throws ParkingException {
    Assert.assertEquals("Create parking lot", "Created a parking lot with 6 slots", this.parkingLot.createParkingLot(6));
  }

  @Test(expected = ParkingException.class)
  public void expectErrorWithMultipleCreation() throws ParkingException {
    this.parkingLot.createParkingLot(6);
    this.parkingLot.createParkingLot(7);
  }

  @Test
  public void parkVehicle() throws ParkingException {
    this.parkingLot.createParkingLot(4);
    Assert.assertEquals("Park Vehicle", "Allocated slot number: 1", this.parkingLot.parkVehicle("KA-01-HH-1234", "White"));
  }

  @Test
  public void parkDuplicateVehicle() throws ParkingException {
    this.parkingLot.createParkingLot(4);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    Assert.assertEquals("Park existing Vehicle", "Vehicle has duplicate registration no KA-01-HH-1234", this.parkingLot.parkVehicle("KA-01-HH-1234", "White"));
  }

  @Test
  public void parkVehicleWhenFull() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "White");
    Assert.assertEquals("Park When full", "Sorry, parking lot is full", this.parkingLot.parkVehicle("KA-01-HH-1111", "BLACK"));
  }

  @Test
  public void leaveParking() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "White");
    Assert.assertEquals("Leave parking", "Slot number 2 is free", this.parkingLot.leaveParking(2));
  }

  @Test
  public void leaveEmptyParking() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    Assert.assertEquals("Leave empty parking", "Parking slot 2 is already empty", this.parkingLot.leaveParking(2));
  }

  @Test
  public void leaveInvalidParking() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    Assert.assertEquals("Leave Invalid parking", "Invalid slot", this.parkingLot.leaveParking(5));
  }

  @Test
  public void leaveUnoccupiedParking() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    Assert.assertEquals("Leave Unoccupied parking", "Parking slot 2 is already empty", this.parkingLot.leaveParking(2));
  }

  @Test
  public void getStatus() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "White");
    Assert.assertEquals("parking status", "Slot No.\tRegistration No\tColour" + System.lineSeparator() +
            "1\tKA-01-HH-1234\tWhite" + System.lineSeparator() +
            "2\tKA-01-HH-1134\tWhite", this.parkingLot.getStatus());
  }

  @Test
  public void getStatusEmptyParkingLot() throws ParkingException {
    this.parkingLot.createParkingLot(2);
    Assert.assertEquals("empty parking status", "Parking lot is empty", this.parkingLot.getStatus());
  }

  @Test
  public void getVehicleRegNosForColor() throws ParkingException {
    this.parkingLot.createParkingLot(3);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "BLACK");
    this.parkingLot.parkVehicle("KA-01-HH-1111", "Black");
    Assert.assertEquals("Get vehicle reg nos for color", "KA-01-HH-1134, KA-01-HH-1111", this.parkingLot.getVehicleRegNosForColor("Black"));
  }

  @Test
  public void getVehicleRegNosForColorNotFound() throws ParkingException {
    this.parkingLot.createParkingLot(3);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "BLACK");
    Assert.assertEquals("Get vehicle reg nos for color not found", "No Vehicles found with color Red", this.parkingLot.getVehicleRegNosForColor("Red"));
  }

  @Test
  public void getSlotNosForVehicleColor() throws ParkingException {
    this.parkingLot.createParkingLot(3);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "BLACK");
    this.parkingLot.parkVehicle("KA-01-HH-1111", "Black");
    Assert.assertEquals("Get slot nos for color", "2, 3", this.parkingLot.getSlotNosForVehicleColor("Black"));
  }

  @Test
  public void getSlotNosForVehicleColorNotFound() throws ParkingException {
    this.parkingLot.createParkingLot(3);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "BLACK");
    this.parkingLot.parkVehicle("KA-01-HH-1111", "Black");
    Assert.assertEquals("Get slot nos for color not found", "No Slots found for color Red", this.parkingLot.getSlotNosForVehicleColor("Red"));
  }

  @Test
  public void getSlotNoForVehicle() throws ParkingException {
    this.parkingLot.createParkingLot(3);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "BLACK");
    this.parkingLot.parkVehicle("KA-01-HH-1111", "Black");
    Assert.assertEquals("Get slot for vehicle", "2", this.parkingLot.getSlotNoForVehicle("KA-01-HH-1134"));
  }

  @Test
  public void getSlotNoForVehicleNotFound() throws ParkingException {
    this.parkingLot.createParkingLot(3);
    this.parkingLot.parkVehicle("KA-01-HH-1234", "White");
    this.parkingLot.parkVehicle("KA-01-HH-1134", "BLACK");
    this.parkingLot.parkVehicle("KA-01-HH-1111", "Black");
    Assert.assertEquals("Get slot for vehicle not found", "Not found", this.parkingLot.getSlotNoForVehicle("OD-01-HH-1134"));
  }
}