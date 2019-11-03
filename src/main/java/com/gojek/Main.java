package com.gojek;

import com.gojek.io.*;
import com.gojek.message.DefaultResponseMessage;
import com.gojek.service.ParkingLot;
import com.gojek.service.ParkingLotImpl;
import com.gojek.service.action.ActionFactory;
import com.gojek.utils.GojekTakeWhile;
import com.gojek.utils.Settings;

import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  public static final String EXIT = "exit";

  static {
    Settings.get().register();
  }

  public static void main(String[] args) {
    final InputReader reader;
    final OutputWriter writer;

    try {
      if (args.length == 0 || args[0].equalsIgnoreCase("CONSOLE")) {
        reader = new CommandLineReader();
        writer = new ConsoleWriter();
      } else if (args[0].equalsIgnoreCase("FILE") || args.length == 1) {
        reader = new TextFileReader(getInputPath(args[args.length - 1]));
        //  writer = new TextFileWriter(Paths.get(args[2]));
        writer = new ConsoleWriter();
      } else if (args[0].equalsIgnoreCase("NETWORK") || args.length == 2) {
        final Socket clientSocket = new Socket(args[args.length - 2], Integer.parseInt(args[args.length - 1]));
        clientSocket.setSoTimeout(60 * 1000);
        reader = new NetworkReader(clientSocket);
        writer = new ConsoleWriter();
      } else {
        throw new Exception("Invalid Input mode");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage() +
              System.lineSeparator() +
              getInstructionMsg());
    }

    try {
      ParkingLot parkingLot = new ParkingLotImpl(new DefaultResponseMessage<>());
      if (reader instanceof CommandLineReader) {
        while (true) {
          final String command = ((CommandLineReader) reader).read();
          if (command.equalsIgnoreCase(EXIT)) {
            break;
          }
          try {
            processAndWrite(writer, parkingLot, command);
          } catch (ParkingException e) {
            e.printStackTrace();
          }
        }
      } else {
        GojekTakeWhile.takeWhile(reader.stream(), command -> !command.equalsIgnoreCase(EXIT)).forEach(command -> {
          try {
            processAndWrite(writer, parkingLot, command);
          } catch (ParkingException | IOException e) {
            e.printStackTrace();
          }
        });
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        reader.close();
        writer.flush();
        writer.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static String getInstructionMsg() {
    return "Please execute again with arguments as below" +
            System.lineSeparator() +
            "CONSOLE/console" +
            System.lineSeparator() +
            "FILE/file <input file name>" +
            System.lineSeparator() +
            "NETWORK <hostname> <port>";
  }

  private static void processAndWrite(OutputWriter writer, ParkingLot parkingLot, String command) throws IOException, ParkingException {
    writer.write(ActionFactory.getInstance().getAction(command).execute(parkingLot));
  }

  private static Path getInputPath(String fileName) throws URISyntaxException {
    return Paths.get(fileName);
    //    return Paths.get(Main.class.getClassLoader().getResource("./" + fileName).toURI());
  }
}
