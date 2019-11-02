package com.gojek.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class GojekTakeWhileTest {

  @Test
  public void takeWhile() {
    final Stream<String> stream = Arrays.stream(new String[]{"Ram", "Hari", "Sita", "Shyam", "Laxman", "Ravan"});
    final List<String> actualResult = GojekTakeWhile.takeWhile(stream, element -> !element.equals("Shyam")).collect(Collectors.toList());

    Assert.assertEquals("Custom TakeWhile with tryAdvance", Arrays.asList("Ram", "Hari", "Sita"), actualResult);
  }
}