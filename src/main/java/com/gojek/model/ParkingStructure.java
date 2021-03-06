package com.gojek.model;

import java.util.TreeSet;

public class ParkingStructure {
  private final TreeSet<Integer> availableParkingSlots;

  public ParkingStructure() {
    this.availableParkingSlots = new TreeSet<>();
  }

  public void addParking(final int parkingNumber) {
    this.availableParkingSlots.add(parkingNumber);
  }

  public int peekNextParking() {
    return this.availableParkingSlots.first();
  }

  public int assignParking() {
    final int nearestParking = this.availableParkingSlots.first();
    this.availableParkingSlots.remove(nearestParking);
    return nearestParking;
  }

  public int getRemainingParkingSlots() {
    return this.availableParkingSlots.size();
  }
}
