package com.gojek.service.action;

import com.gojek.ParkingException;
import com.gojek.message.DefaultResponseMessage;
import com.gojek.model.ParkingStructure;
import com.gojek.service.ParkingLot;
import com.gojek.service.ParkingLotImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DispatchRuleCommandTest {
  @BeforeClass
  public static void beforeClass() throws ClassNotFoundException {
    Class.forName("com.gojek.utils.SettingsMockUtils");
  }

  @Test
  public void isValidCommandEvenDistribution() {
    DispatchRuleCommand dispatchRule = new DispatchRuleCommand("dispatch_rule even_distribution");
    Assert.assertTrue(dispatchRule.isValidCommand());
  }

  @Test
  public void isValidCommandFillFirst() {
    DispatchRuleCommand dispatchRule = new DispatchRuleCommand("dispatch_rule fill_first");
    Assert.assertTrue(dispatchRule.isValidCommand());
  }

  @Test
  public void execute() throws ParkingException {
    DispatchRuleCommand dispatchRule = new DispatchRuleCommand("dispatch_rule even_distribution");
    ParkingLot parkingLot = new ParkingLotImpl(new DefaultResponseMessage<>(), ParkingStructure.class);
    Assert.assertEquals("Dispatch rule set to even_distribution", dispatchRule.execute(parkingLot));
  }
}
