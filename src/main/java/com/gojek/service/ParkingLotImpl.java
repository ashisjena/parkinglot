package com.gojek.service;

import com.gojek.ParkingException;
import com.gojek.dao.*;
import com.gojek.message.ResponseMessage;
import com.gojek.model.Car;
import com.gojek.model.ParkingStructure;
import com.gojek.model.Vehicle;
import com.gojek.service.action.DispatchStrategy;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ParkingLotImpl implements ParkingLot {
  private final ResponseMessage<Vehicle> responseMsg;
  private final ReentrantReadWriteLock.ReadLock readLock;
  private final ReentrantReadWriteLock.WriteLock writeLock;
  private final Class<? extends ParkingStructure> parkingStructureClazz;
  private final ParkingManagerDAO parkingManagerDAO;

  public ParkingLotImpl(ResponseMessage<Vehicle> responseMsg, Class<? extends ParkingStructure> parkingStructureClass) {
    this.responseMsg = responseMsg;
    this.parkingStructureClazz = parkingStructureClass;
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    this.readLock = lock.readLock();
    this.writeLock = lock.writeLock();
    this.parkingManagerDAO = new ParkingManagerDAO();
  }

  @Override
  public String createParkingLot(int capacity) throws ParkingException {
    try {
      writeLock.lock();
      this.parkingManagerDAO.createParking(capacity, this.parkingStructureClazz, DAOType.IN_MEMORY);
    } finally {
      writeLock.unlock();
    }

    return this.responseMsg.createParkingLotMsg(capacity);
  }

  @Override
  public String parkVehicle(String regNo, String color) {
    try {
      writeLock.lock();
      final int allocatedSlot = this.parkingManagerDAO.parkVehicle(new Car(regNo, color));
      return this.responseMsg.parkVehicleMsg(allocatedSlot);
    } catch (DuplicateVehicleRegNoException e) {
      return this.responseMsg.duplicateVehicleErrorMsg(regNo);
    } catch (NoParkingAvailableException e) {
      return this.responseMsg.parkingLotFullErrorMsg();
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public String leaveParking(int id, int slotNo) {
    try {
      writeLock.lock();
      this.parkingManagerDAO.leaveVehicle(id, slotNo);
      return this.responseMsg.leaveParkingMsg(id, slotNo);
    } catch (EmptyParkingSlotException e) {
      return this.responseMsg.slotAlreadyEmptyErrorMsg(slotNo);
    } catch (InvalidParkingSlotException e) {
      return this.responseMsg.invalidSlotErrorMsg(slotNo);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public String getStatus() {
    try {
      readLock.lock();
      final List<Vehicle> vehicles = this.parkingManagerDAO.listAllVehicles();
      if (vehicles.size() == 0) {
        return this.responseMsg.parkingLotEmptyMsg();
      } else {
        return this.responseMsg.statusMsg(vehicles);
      }
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public String getVehicleRegNosForColor(String color) {
    try {
      readLock.lock();
      final List<Vehicle> vehicles = this.parkingManagerDAO.listVehiclesWithColor(color);
      if (vehicles.size() == 0) {
        return this.responseMsg.vehiclesNotFoundForColorErrorMsg(color);
      } else {
        return this.responseMsg.listRegNosForVehicleColorMsg(
                vehicles.stream().map(Vehicle::getRegistrationNo).collect(Collectors.toList()));
      }
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public String getSlotNosForVehicleColor(String color) {
    try {
      readLock.lock();
      final List<Integer> slots = this.parkingManagerDAO.listSlotsWithVehicleColor(color);
      if (slots.size() == 0) {
        return this.responseMsg.slotsNotFoundWithVehicleColorErrorMsg(color);
      } else {
        return this.responseMsg.listSlotsForVehicleColorMsg(slots);
      }
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public String getSlotNoForVehicle(String regNo) {
    try {
      readLock.lock();
      final int slot = this.parkingManagerDAO.getSlotNumberForVehicle(regNo);
      return this.responseMsg.slotForVehicleMsg(slot);
    } catch (VehicleNotFoundException e) {
      return this.responseMsg.slotNotFoundForVehicleErrorMsg();
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public String setDispatchRule(DispatchStrategy strategy) {
    try{
      writeLock.lock();
      this.parkingManagerDAO.setDispatchStrategy(strategy);
    } finally {
      writeLock.unlock();
    }
    return this.responseMsg.setDispatchRuleMsg(strategy);
  }
}
