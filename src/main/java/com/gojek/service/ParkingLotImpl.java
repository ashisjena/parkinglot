package com.gojek.service;

import com.gojek.ParkingException;
import com.gojek.dao.*;
import com.gojek.message.ResponseMessage;
import com.gojek.model.Car;
import com.gojek.model.ParkingStructure;
import com.gojek.model.Vehicle;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ParkingLotImpl implements ParkingLot {
  private ParkingDAO<Vehicle> parkingDAO;
  private final ResponseMessage<Vehicle> responseMsg;
  private final ReentrantReadWriteLock.ReadLock readLock;
  private final ReentrantReadWriteLock.WriteLock writeLock;
  private final ParkingStructure parkingStructure;

  public ParkingLotImpl(ResponseMessage<Vehicle> responseMsg, ParkingStructure parkingStructure) {
    this.responseMsg = responseMsg;
    this.parkingStructure = parkingStructure;
    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    this.readLock = lock.readLock();
    this.writeLock = lock.writeLock();
  }

  @Override
  public String createParkingLot(int capacity) throws ParkingException {
    try {
      writeLock.lock();
      if (this.parkingDAO != null) {
        throw new ParkingException("ERROR: ParkingLot already created");
      }
      this.parkingDAO = ParkingDAOFactory.getInstance().getParkingDAO(capacity, this.parkingStructure, DAOType.IN_MEMORY);
    } finally {
      writeLock.unlock();
    }

    return this.responseMsg.createParkingLotMsg(capacity);
  }

  @Override
  public String parkVehicle(String regNo, String color) {
    try {
      writeLock.lock();
      final int allocatedSlot = this.parkingDAO.parkVehicle(new Car(regNo, color));
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
  public String leaveParking(int slotNo) {
    try {
      writeLock.lock();
      this.parkingDAO.leaveVehicle(slotNo);
      return this.responseMsg.leaveParkingMsg(slotNo);
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
      final List<Vehicle> vehicles = this.parkingDAO.listAllVehicles();
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
      final List<Vehicle> vehicles = this.parkingDAO.listVehiclesWithColor(color);
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
      final List<Integer> slots = this.parkingDAO.listSlotsWithVehicleColor(color);
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
      final int slot = this.parkingDAO.getSlotNumberForVehicle(regNo);
      return this.responseMsg.slotForVehicleMsg(slot);
    } catch (VehicleNotFoundException e) {
      return this.responseMsg.slotNotFoundForVehicleErrorMsg();
    } finally {
      readLock.unlock();
    }
  }
}
