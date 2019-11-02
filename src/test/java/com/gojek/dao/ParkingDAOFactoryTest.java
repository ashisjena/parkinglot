package com.gojek.dao;

import com.gojek.model.ParkingStructure;
import org.junit.Assert;
import org.junit.Test;

public class ParkingDAOFactoryTest {

  @Test
  public void getParkingDAO() {
    Assert.assertNotNull("InMemory Parking DAO", ParkingDAOFactory.getInstance().getParkingDAO(10, new ParkingStructure(), DAOType.IN_MEMORY));
  }

  @Test(expected = RuntimeException.class)
  public void getParkingDAOInvalidDAOType() {
    Assert.assertNotNull("InMemory Parking DAO throw exception for invalid type",
            ParkingDAOFactory.getInstance().getParkingDAO(10, new ParkingStructure(), DAOType.DB_MYSQL));
  }
}