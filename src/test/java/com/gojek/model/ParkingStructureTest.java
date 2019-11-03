package com.gojek.model;

import org.junit.Assert;
import org.junit.Test;

public class ParkingStructureTest {

  @Test
  public void assignParking() {
    final ParkingStructure parkingStructure = new ParkingStructure();
    for (int i = 5; i < 10; i++) {
      parkingStructure.addParking(i);
    }

    Assert.assertEquals("Assign nearest parking from parking structure", 5, parkingStructure.assignParking());
    Assert.assertEquals("Assign next nearest parking from parking structure", 6, parkingStructure.assignParking());
  }

  @Test
  public void peekNextParking() {
    final ParkingStructure parkingStructure = new ParkingStructure();
    for (int i = 5; i < 10; i++) {
      parkingStructure.addParking(i);
    }

    parkingStructure.assignParking();
    Assert.assertEquals("Peek next parking from parking structure", 6, parkingStructure.peekNextParking());
  }
}