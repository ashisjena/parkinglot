package com.gojek.service.action;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatusCommandTest {

  @Test
  public void isValidCommand() {
    Assert.assertTrue(new StatusCommand("status").isValidCommand());
  }
}