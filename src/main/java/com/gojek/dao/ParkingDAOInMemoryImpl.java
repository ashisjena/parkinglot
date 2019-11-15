package com.gojek.dao;

import com.gojek.ParkingException;
import com.gojek.model.ParkingStructure;
import com.gojek.model.Vehicle;
import com.gojek.utils.Settings;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ParkingDAOInMemoryImpl<T extends Vehicle> implements ParkingDAO<T> {
  private final int id;

  private AtomicInteger capacity;
  private ParkingStructure parkingStructure;
  private Map<Integer, Optional<T>> slotToVehicleMap;
  ParkingDAOInMemoryImpl(int id, final int capacity, final Class<? extends ParkingStructure> parkingStructureClazz) throws ParkingException {
    this.id = id;
    init(capacity, parkingStructureClazz);
  }

  private void init(final int capacity, final Class<? extends ParkingStructure> parkingStructureClazz) throws ParkingException {
    this.capacity = new AtomicInteger(capacity);
    try {
      this.parkingStructure = parkingStructureClazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ParkingException(e);
    }

    this.slotToVehicleMap = new ConcurrentHashMap<>();
    for (int slotNum = 1; slotNum <= this.capacity.get(); slotNum++) {
      this.slotToVehicleMap.put(slotNum, Optional.empty());
      this.parkingStructure.addParking(slotNum);
    }
  }

  public int getId() {
    return id;
  }

  @Override
  public int getRemainingSlots() {
    return this.parkingStructure.getRemainingParkingSlots();
  }

  @Override
  public int getCapacity() {
    return this.capacity.get();
  }

  @Override
  public int parkVehicle(final T vehicle) throws NoParkingAvailableException, DuplicateVehicleRegNoException {
    if (getRemainingSlots() == 0) {
      throw new NoParkingAvailableException(Settings.get().getProperty("errormsg.parking.full").orElse("ERROR: ParkingLot is full"));
    }/* else if (isDuplicateVehicleRegistrationNo(vehicle)) {
      throw new DuplicateVehicleRegNoException(String.format(
              Settings.get().getProperty("errormsg.duplicate.vehicle_regno").orElse("ERROR: Duplicate Vehicle %d"), vehicle.getRegistrationNo()));
    }*/

    int availableSlot = this.parkingStructure.peekNextParking();
    this.slotToVehicleMap.put(availableSlot, Optional.of(vehicle));
    vehicle.setAssignedParkingSlot(this.parkingStructure.assignParking());
    return availableSlot;
  }

  /*private boolean isDuplicateVehicleRegistrationNo(T vehicle) {
    return this.slotToVehicleMap.values().stream().anyMatch(opt -> opt.isPresent() && opt.get().equals(vehicle));
  }*/

  @Override
  public T leaveVehicle(final int parkingSlot) throws EmptyParkingSlotException, InvalidParkingSlotException {
    if (this.getCapacity() < parkingSlot) {
      throw new InvalidParkingSlotException(Settings.get().getProperty("errormsg.parkingslot.isempty").orElse("ERROR: Invalid Parking Slot"));
    }

    this.slotToVehicleMap.get(parkingSlot).orElseThrow(() -> new EmptyParkingSlotException(
            String.format(Settings.get().getProperty("errormsg.parkingslot.isempty").orElse("ERROR: Slot %d already empty"), parkingSlot)));

    this.parkingStructure.addParking(parkingSlot);
    final T result = this.slotToVehicleMap.get(parkingSlot).get();
    this.slotToVehicleMap.put(parkingSlot, Optional.empty());
    return result;
  }

  @Override
  public List<T> listAllVehicles() {
    return this.slotToVehicleMap
            .values()
            .stream()
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
  }

  @Override
  public List<T> listVehiclesWithColor(final String color) {
    return this.slotToVehicleMap
            .values()
            .stream()
            .filter(optVehicle -> optVehicle.isPresent() && optVehicle.get().getColor().equalsIgnoreCase(color))
            .map(Optional::get)
            .collect(Collectors.toList());
  }

  @Override
  public List<Integer> listSlotsWithVehicleColor(final String color) {
    return this.slotToVehicleMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getColor().equalsIgnoreCase(color))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
  }

  @Override
  public int getSlotNumberForVehicle(final String registrationNumber) throws VehicleNotFoundException {
    return this.slotToVehicleMap
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().isPresent() && entry.getValue().get().getRegistrationNo().equals(registrationNumber))
            .findFirst()
            .orElseThrow(() -> new VehicleNotFoundException(
                    String.format(Settings.get().getProperty("errormsg.vehicle.notfound").orElse("ERROR: Vehicle %s not found"), registrationNumber)))
            .getKey();
  }
}
