package com.gojek.dao;

import com.gojek.ParkingException;
import com.gojek.model.ParkingStructure;
import com.gojek.model.Vehicle;
import com.gojek.service.action.DispatchStrategy;
import com.gojek.utils.Settings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ParkingManagerDAO {
  private LinkedHashMap<Integer, ParkingDAO<Vehicle>> parkingLotMaps;
  private AtomicInteger latestId = new AtomicInteger(0);
  private DispatchStrategy strategy;
  
  public ParkingManagerDAO() {
    this.parkingLotMaps = new LinkedHashMap<>();
  }

  public void createParking(int capacity, Class<? extends ParkingStructure> parkingStructureClazz, DAOType inMemory) throws ParkingException {
    this.parkingLotMaps.put(latestId.incrementAndGet(), ParkingDAOFactory.getInstance().getParkingDAO(capacity, parkingStructureClazz, inMemory));
  }

  public void setDispatchStrategy(DispatchStrategy strategy) {
    this.strategy = strategy;
  }

  public int parkVehicle(Vehicle vehicle) throws DuplicateVehicleRegNoException, NoParkingAvailableException {
    if (isDuplicateVehicleRegNo(vehicle.getRegistrationNo())) {
      throw new DuplicateVehicleRegNoException(String.format(
              Settings.get().getProperty("errormsg.duplicate.vehicle_regno").orElse("ERROR: Duplicate Vehicle %d"), vehicle.getRegistrationNo()));
    }
    ParkingDAO<Vehicle> parkingDAO = chooseParkingDAO();
    return parkingDAO.parkVehicle(vehicle);
  }

  private boolean isDuplicateVehicleRegNo(String regNo) {
    return this.parkingLotMaps.values()
            .stream()
            .flatMap(parkingDAO -> parkingDAO
                    .listAllVehicles()
                    .stream())
            .anyMatch(vehicle -> vehicle
                    .getRegistrationNo()
                    .equalsIgnoreCase(regNo));
  }

  private ParkingDAO<Vehicle> chooseParkingDAO() {
    if (getStrategy() == DispatchStrategy.EVEN_DISTRIBUTION) {
      return getParkingDAOForEvenDistribution().get();
    } else {
      return getParkingDAOForFillFirst().get();
    }
  }

  private Optional<ParkingDAO<Vehicle>> getParkingDAOForFillFirst() {
    return this.parkingLotMaps.values().stream().reduce((prev, curr) -> {
      if (prev.getRemainingSlots() > 0) {
        return prev;
      } else {
        return curr;
      }
    });
  }

  private Optional<ParkingDAO<Vehicle>> getParkingDAOForEvenDistribution() {
    return this.parkingLotMaps.values().stream().reduce((prev, curr) -> {
      if (prev.getRemainingSlots() < curr.getRemainingSlots()) {
        return curr;
      } else {
        return prev;
      }
    });
  }

  public Vehicle leaveVehicle(int id, int slotNo) throws EmptyParkingSlotException, InvalidParkingSlotException {
    return this.parkingLotMaps.get(id).leaveVehicle(slotNo);
  }

  public List<Vehicle> listAllVehicles() {
    return this.parkingLotMaps
            .values()
            .stream()
            .flatMap(parkingLotDAO -> parkingLotDAO
                    .listAllVehicles()
                    .stream())
            .collect(Collectors.toList());
  }

  public List<Vehicle> listVehiclesWithColor(String color) {
    return this.parkingLotMaps
            .values()
            .stream()
            .flatMap(parkingLotDAO -> parkingLotDAO
                    .listVehiclesWithColor(color)
                    .stream())
            .collect(Collectors.toList());
  }

  public List<Integer> listSlotsWithVehicleColor(String color) {
    return this.parkingLotMaps
            .values()
            .stream()
            .flatMap(parkingLotDAO -> parkingLotDAO
                    .listSlotsWithVehicleColor(color)
                    .stream())
            .collect(Collectors.toList());
  }

  public int getSlotNumberForVehicle(String regNo) throws VehicleNotFoundException {
    ParkingDAO<Vehicle> parkingDAO = this.parkingLotMaps
            .values()
            .stream()
            .filter(parkingLotDAO -> parkingLotDAO
                    .listAllVehicles()
                    .stream()
                    .anyMatch(vehicle -> vehicle
                            .getRegistrationNo()
                            .equalsIgnoreCase(regNo)))
            .findFirst()
            .orElseThrow(() -> new VehicleNotFoundException(String.format(Settings.get().getProperty("errormsg.vehicle.notfound").orElse("ERROR: Vehicle %s not found"), regNo)));

    return parkingDAO.getSlotNumberForVehicle(regNo);
  }

  public DispatchStrategy getStrategy() {
    return strategy;
  }
}
