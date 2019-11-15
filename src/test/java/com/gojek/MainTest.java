package com.gojek;

import com.gojek.io.InMemoryStringReaderImpl;
import com.gojek.io.InMemoryStringWriterImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class MainTest {
  @BeforeClass
  public static void beforeClass() throws ClassNotFoundException {
    Class.forName("com.gojek.utils.SettingsMockUtils");
  }

  @Test
  public void endToEndTest() throws IOException {
    final String input = "create_parking_lot 6" + System.lineSeparator() +
            "park KA-01-HH-1234 White" + System.lineSeparator() +
            "park KA-01-HH-9999 White" + System.lineSeparator() +
            "park KA-01-BB-0001 Black" + System.lineSeparator() +
            "park KA-01-HH-7777 Red" + System.lineSeparator() +
            "park KA-01-HH-2701 Blue" + System.lineSeparator() +
            "park KA-01-HH-3141 Black" + System.lineSeparator() +
            "leave 4" + System.lineSeparator() +
            "status" + System.lineSeparator() +
            "park KA-01-P-333 White" + System.lineSeparator() +
            "park DL-12-AA-9999 White" + System.lineSeparator() +
            "registration_numbers_for_cars_with_colour White" + System.lineSeparator() +
            "slot_numbers_for_cars_with_colour White" + System.lineSeparator() +
            "slot_number_for_registration_number KA-01-HH-3141" + System.lineSeparator() +
            "slot_number_for_registration_number MH-04-AY-1111";

    final String expectedResult = "Created a parking lot with 6 slots" + System.lineSeparator() +
            "Allocated slot number: 1" + System.lineSeparator() +
            "Allocated slot number: 2" + System.lineSeparator() +
            "Allocated slot number: 3" + System.lineSeparator() +
            "Allocated slot number: 4" + System.lineSeparator() +
            "Allocated slot number: 5" + System.lineSeparator() +
            "Allocated slot number: 6" + System.lineSeparator() +
            "Slot number 4 is free on parking 1" + System.lineSeparator() +
            "Slot No.    Registration No    Colour" + System.lineSeparator() +
            "1           KA-01-HH-1234      White" + System.lineSeparator() +
            "2           KA-01-HH-9999      White" + System.lineSeparator() +
            "3           KA-01-BB-0001      Black" + System.lineSeparator() +
            "5           KA-01-HH-2701      Blue" + System.lineSeparator() +
            "6           KA-01-HH-3141      Black" + System.lineSeparator() +
            "Allocated slot number: 4" + System.lineSeparator() +
            "Sorry, parking lot is full" + System.lineSeparator() +
            "KA-01-HH-1234, KA-01-HH-9999, KA-01-P-333" + System.lineSeparator() +
            "1, 2, 4" + System.lineSeparator() +
            "6" + System.lineSeparator() +
            "Not found" + System.lineSeparator();

    final InMemoryStringWriterImpl memoryWriter = new InMemoryStringWriterImpl();
    Main.execute(new InMemoryStringReaderImpl(input), memoryWriter);
    Assert.assertEquals("Main: End to End test", expectedResult, memoryWriter.toString());
  }
}
