package com.gojek.dao;

import com.gojek.model.ParkingStructure;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

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

  @Test(expected = InvocationTargetException.class)
  public void createInstanceThroughReflection() throws IllegalAccessException, InvocationTargetException, InstantiationException {
    ParkingDAOFactory.getInstance();
    Constructor<ParkingDAOFactory> defaultConstructor = AccessController.doPrivileged((PrivilegedAction<Constructor<ParkingDAOFactory>>) () -> {
      Constructor<ParkingDAOFactory> constructor = null;
      try {
        constructor = ParkingDAOFactory.class.getDeclaredConstructor();
        constructor.setAccessible(true);
      } catch (NoSuchMethodException e) {
      }
      return constructor;
    });
    defaultConstructor.newInstance();
  }
}