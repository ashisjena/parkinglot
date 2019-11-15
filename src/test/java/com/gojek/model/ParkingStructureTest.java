package com.gojek.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParkingStructureTest {

  private ParkingStructure parkingStructure;

  @Before
  public void setUp() {
    parkingStructure = new ParkingStructure();
  }

  private void createParking(int start, int end) {
    for (int i = start; i <= end; i++) {
      parkingStructure.addParking(i);
    }
  }

  @Test
  public void assignParking() {
    createParking(5, 10);

    Assert.assertEquals("Assign nearest parking from parking structure", 5, parkingStructure.assignParking());
    Assert.assertEquals("Assign next nearest parking from parking structure", 6, parkingStructure.assignParking());
  }

  @Test
  public void peekNextParking() {
    createParking(5, 10);

    parkingStructure.assignParking();
    Assert.assertEquals("Peek next parking from parking structure", 6, parkingStructure.peekNextParking());
  }

  @Test
  public void remainingParking() {
    createParking(5, 10);
    Assert.assertEquals("Remaining Parking", 6, parkingStructure.getRemainingParkingSlots());
  }

  @Test
  public void multiOperation() {
    createParking(5, 10);
    parkingStructure.assignParking();
    Assert.assertEquals(6, parkingStructure.peekNextParking());
    Assert.assertEquals(5, parkingStructure.getRemainingParkingSlots());
  }
}