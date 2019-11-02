package com.gojek.dao;

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
  private static ParkingDAOInMemoryImpl INSTANCE;

  private AtomicInteger capacity;
  private AtomicInteger remainingSlots;
  private ParkingStructure parkingStructure;
  private Map<Integer, Optional<T>> slotToVehicleMap;

  public static <T extends Vehicle> ParkingDAOInMemoryImpl<T> getInstance(final int capacity, final ParkingStructure parkingStructure) {
    if (INSTANCE == null) {
      synchronized (ParkingDAOInMemoryImpl.class) {
        if (INSTANCE == null) {
          INSTANCE = new ParkingDAOInMemoryImpl<T>(capacity, parkingStructure);
        }
      }
    }
    return INSTANCE;
  }

  protected static <T extends Vehicle> ParkingDAOInMemoryImpl<T> getInstanceForTEST(final int capacity, final ParkingStructure parkingStructure) {
    final ParkingDAOInMemoryImpl<T> daoInMemory = new ParkingDAOInMemoryImpl<>();
    daoInMemory.init(capacity, parkingStructure);
    return daoInMemory;
  }

  private ParkingDAOInMemoryImpl() {
  }

  private ParkingDAOInMemoryImpl(final int capacity, final ParkingStructure parkingStructure) {
    if (INSTANCE != null) {
      throw new RuntimeException("ERROR: ParkingDAOInMemoryImpl is already initialized");
    }
    init(capacity, parkingStructure);
  }

  private void init(final int capacity, final ParkingStructure parkingStructure) {
    this.capacity = new AtomicInteger(capacity);
    this.remainingSlots = new AtomicInteger(capacity);
    this.parkingStructure = parkingStructure;

    this.slotToVehicleMap = new ConcurrentHashMap<>();
    for (int slotNum = 1; slotNum <= this.capacity.get(); slotNum++) {
      this.slotToVehicleMap.put(slotNum, Optional.empty());
      this.parkingStructure.addParking(slotNum);
    }
  }

  @Override
  public int getRemainingSlots() {
    return this.remainingSlots.get();
  }

  @Override
  public int getCapacity() {
    return this.capacity.get();
  }

  @Override
  public int parkVehicle(final T vehicle) throws NoParkingAvailableException, ParkingSlotIsNotEmptyException, DuplicateVehicleRegNoException {
    if (this.remainingSlots.get() == 0) {
      throw new NoParkingAvailableException(Settings.get().getProperty("errormsg.parking.full").orElse("ERROR: ParkingLot is full"));
    } else if (isDuplicateVehicleRegistrationNo(vehicle)) {
      throw new DuplicateVehicleRegNoException(String.format(
              Settings.get().getProperty("errormsg.duplicate.vehicle_regno").orElse("ERROR: Duplicate Vehicle %d"), vehicle.getRegistrationNo()));
    }

    int availableSlot = this.parkingStructure.assignParking();
    if (this.slotToVehicleMap.get(availableSlot).isPresent()) {
      throw new ParkingSlotIsNotEmptyException(
              String.format(Settings.get().getProperty("errormsg.parkingslot.notempty").orElse("ERROR: Slot %d not empty"), availableSlot));
    }
    this.slotToVehicleMap.put(availableSlot, Optional.of(vehicle));
    this.remainingSlots.decrementAndGet();
    return availableSlot;
  }

  private boolean isDuplicateVehicleRegistrationNo(T vehicle) {
    return this.slotToVehicleMap.values().stream().anyMatch(opt -> opt.isPresent() && opt.get().equals(vehicle));
  }

  @Override
  public T leaveVehicle(final int parkingSlot) throws EmptyParkingSlotException {
    this.slotToVehicleMap.get(parkingSlot).orElseThrow(() -> new EmptyParkingSlotException(
            String.format(Settings.get().getProperty("errormsg.parkingslot.isempty").orElse("ERROR: Slot %d already empty"), parkingSlot)));
    this.remainingSlots.incrementAndGet();
    this.parkingStructure.addParking(parkingSlot);
    return this.slotToVehicleMap.remove(parkingSlot).get();
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
