package com.gojek.dao;

import com.gojek.ParkingException;
import com.gojek.model.Car;
import com.gojek.model.ParkingStructure;
import com.gojek.model.Vehicle;
import com.gojek.utils.Settings;
import com.gojek.utils.SettingsMockUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParkingDAOInMemoryImplTest {

  private ParkingDAO<Vehicle> parkingDAO;
  private static Settings settings;

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @BeforeClass
  public static void init() {
    settings = SettingsMockUtils.getSettingsMock();
  }

  @Before
  public void setUp() {
    this.parkingDAO = ParkingDAOInMemoryImpl.getInstanceForTEST(5, new ParkingStructure());
  }

  @Test
  public void getCapacity() {
    Assert.assertEquals("Get Total Capacity", 5, this.parkingDAO.getCapacity());
  }

  @Test
  public void parkVehicle() throws ParkingException {
    this.parkingDAO.parkVehicle(new Car("KA05JC5555", "BLACK"));
    Assert.assertNotNull("Vehicle parked successfully", this.parkingDAO.getSlotNumberForVehicle("KA05JC5555"));
  }

  @Test
  public void parkVehicleWhenFull() throws ParkingException {
    this.exceptionRule.expect(NoParkingAvailableException.class);
    this.exceptionRule.expectMessage(settings.getProperty("errormsg.parking.full").get());
    this.parkingDAO.parkVehicle(new Car("KA05JC5555", "BLACK"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5556", "BLACK"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5557", "WHITE"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5558", "BLACK"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5559", "BROWN"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5559", "BROWN"));
  }

  @Test
  public void parkVehicleWithDuplicateRegNo() throws ParkingException {
    this.exceptionRule.expect(DuplicateVehicleRegNoException.class);
    this.exceptionRule.expectMessage(String.format(settings.getProperty("errormsg.duplicate.vehicle_regno").get(), "KA05JC5555"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5555", "BLACK"));
    this.parkingDAO.parkVehicle(new Car("KA05JC5555", "BLACK"));
  }

  @Test
  public void leaveVehicle() throws ParkingException {
    this.parkingDAO.parkVehicle(new Car("KA05JC3456", "BLACK"));
    Assert.assertEquals("Leave Vehicle", "KA05JC3456", this.parkingDAO.leaveVehicle(1).getRegistrationNo());
  }

  @Test
  public void leaveInvalidVehicle() throws ParkingException {
    this.exceptionRule.expect(EmptyParkingSlotException.class);
    this.parkingDAO.parkVehicle(new Car("KA05JC3456", "BLACK"));
    this.parkingDAO.leaveVehicle(3).getRegistrationNo();
  }

  @Test
  public void listAllVehicles() throws ParkingException {
    final List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(
            new Car("KA05JC5555", "BLACK"),
            new Car("KA05JC6666", "BLACK"),
            new Car("KA05JC7777", "WHITE"),
            new Car("KA05JC8888", "GREY")));
    parkVehicles(vehicles);
    this.parkingDAO.leaveVehicle(3);
    vehicles.remove(2);
    Assert.assertArrayEquals("List all vehicles", vehicles.toArray(), this.parkingDAO.listAllVehicles().toArray());
  }

  private void parkVehicles(List<Vehicle> vehicles) throws ParkingException {
    for (Vehicle vehicle : vehicles) {
      this.parkingDAO.parkVehicle(vehicle);
    }
  }

  @Test
  public void listVehiclesWithColor() throws ParkingException {
    final List<Vehicle> vehiclesWithBlackColor = Arrays.asList(
            new Car("KA05JC5555", "BLACK"),
            new Car("KA05JC6666", "BLACK"));

    final List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(
            new Car("KA05JC7777", "WHITE"),
            new Car("KA05JC8888", "GREY")));
    vehicles.addAll(vehiclesWithBlackColor);

    parkVehicles(vehicles);
    for (Vehicle vehicle : this.parkingDAO.listVehiclesWithColor("Black")) {
      Assert.assertTrue("List Vehicles with Color", vehiclesWithBlackColor.contains(vehicle));
    }
  }

  @Test
  public void listSlotsWithVehicleColor() throws ParkingException {
    final List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(
            new Car("KA05JC5555", "BLACK"),
            new Car("KA05JC7777", "WHITE"),
            new Car("KA05JC6666", "BLACK"),
            new Car("KA05JC8888", "GREY")));
    parkVehicles(vehicles);

    List<Integer> slotsWithBlackVehicle = this.parkingDAO.listSlotsWithVehicleColor("Black");
    Collections.sort(slotsWithBlackVehicle);

    Assert.assertArrayEquals("List slots for vehicle Color", new Integer[]{1, 3}, slotsWithBlackVehicle.toArray(new Integer[2]));
  }

  @Test
  public void getSlotNumberForVehicle() throws ParkingException {
    final List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(
            new Car("KA05JC5555", "BLACK"),
            new Car("KA05JC7777", "WHITE"),
            new Car("KA05JC6666", "BLACK"),
            new Car("KA05JC8888", "GREY")));
    parkVehicles(vehicles);

    Assert.assertEquals("Get slot for a parked vehicle", 3, this.parkingDAO.getSlotNumberForVehicle("KA05JC6666"));
  }

  @Test
  public void getSlotNumberForInvalidVehicle() throws ParkingException {
    final List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(
            new Car("KA05JC5555", "BLACK"),
            new Car("KA05JC7777", "WHITE"),
            new Car("KA05JC6666", "BLACK"),
            new Car("KA05JC8888", "GREY")));
    parkVehicles(vehicles);

    this.exceptionRule.expect(VehicleNotFoundException.class);
    this.exceptionRule.expectMessage(String.format(settings.getProperty("errormsg.vehicle.notfound").get(), "KA05JC0000"));
    this.parkingDAO.getSlotNumberForVehicle("KA05JC0000");
  }

  @Test
  public void getRemainingSlots() throws ParkingException {
    final List<Vehicle> vehicles = new ArrayList<>(Arrays.asList(
            new Car("KA05JC5555", "BLACK"),
            new Car("KA05JC8888", "GREY")));
    parkVehicles(vehicles);

    Assert.assertEquals("Get remaining Slots", 3, this.parkingDAO.getRemainingSlots());
  }
}