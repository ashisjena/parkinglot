package com.gojek;

import com.gojek.io.*;
import com.gojek.message.DefaultResponseMessage;
import com.gojek.service.ParkingLot;
import com.gojek.service.ParkingLotImpl;
import com.gojek.service.action.ActionFactory;
import com.gojek.utils.Settings;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  static {
    Settings.get().register();
  }

  public static void main(String[] args) {
    final InputReader reader;
    final OutputWriter writer;

    try {
      String inputMode = args[0];
      if (inputMode.equalsIgnoreCase("CONSOLE")) {
        reader = new CommandLineReader();
        writer = new ConsoleWriter();
      } else if (inputMode.equalsIgnoreCase("FILE")) {
        reader = new TextFileReader(getInputPath(args[1]));
        writer = new TextFileWriter(Paths.get(args[2]));
      } else if (inputMode.equalsIgnoreCase("NETWORK")) {
        final Socket clientSocket = new Socket(args[1], Integer.parseInt(args[2]));
        clientSocket.setSoTimeout(60*1000);
        reader = new NetworkReader(clientSocket);
        writer = new TextFileWriter(Paths.get(args[1]));
      } else {
        throw new Exception("Invalid Input mode");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage() +
              System.lineSeparator() +
              "Please execute again with arguments as below" +
              System.lineSeparator() +
              "CONSOLE/console" +
              System.lineSeparator() +
              "FILE/file <input file name> <output file name>" +
              System.lineSeparator() +
              "NETWORK <hostname> <port> <output file name>");
    }

    try {
      ParkingLot parkingLot = new ParkingLotImpl(new DefaultResponseMessage<>());
      reader.stream().forEach(command -> {
        try {
          writer.write(ActionFactory.getInstance().getAction(command).execute(parkingLot));
        } catch (ParkingException | IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        writer.flush();
        writer.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static Path getInputPath(String fileName) throws URISyntaxException {
    return Paths.get(Main.class.getClassLoader().getResource("./" + fileName).toURI());
  }
}
