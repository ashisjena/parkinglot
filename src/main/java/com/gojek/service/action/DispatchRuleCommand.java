package com.gojek.service.action;

import com.gojek.ParkingException;
import com.gojek.service.ParkingLot;
import com.gojek.service.action.constants.Command;

public class DispatchRuleCommand extends AbstractCommandAction {

  public DispatchRuleCommand(String command) {
    super(command, Command.DISPATCH_RULE);
  }

  @Override
  public boolean isValidCommand() {
    return getInputCommands().length == 2 &&
            isValidStrategy(getInputCommands()[1]);
  }

  private boolean isValidStrategy(String strategy) {
    return DispatchStrategy.EVEN_DISTRIBUTION.getValue().equalsIgnoreCase(strategy) ||
            DispatchStrategy.FILL_FIRST.getValue().equalsIgnoreCase(strategy);
  }

  @Override
  public String execute(ParkingLot parkingLot) throws ParkingException {
    return parkingLot.setDispatchRule(DispatchStrategy.getStrategy(getInputCommands()[1]));
  }
}
