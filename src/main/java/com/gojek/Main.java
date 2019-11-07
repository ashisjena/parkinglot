package com.gojek;

import com.gojek.io.*;
import com.gojek.message.DefaultResponseMessage;
import com.gojek.model.ParkingStructure;
import com.gojek.service.ParkingLot;
import com.gojek.service.ParkingLotImpl;
import com.gojek.service.action.AbstractCommandAction;
import com.gojek.service.action.ActionFactory;
import com.gojek.utils.GojekTakeWhile;
import com.gojek.utils.Settings;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;

public class Main {

  private static final String EXIT = "exit";

  static {
    Settings.get().register();
  }

  public static void main(String[] args) throws Exception {
    final IOClass ioClass = new IOClass(args).invoke();
    try (final InputReader reader = ioClass.getReader();
         final OutputWriter writer = ioClass.getWriter()) {
      execute(reader, writer);
    }
  }

  public static void execute(InputReader reader, OutputWriter writer) throws IOException {
    final ParkingLot parkingLot = new ParkingLotImpl(new DefaultResponseMessage<>(), new ParkingStructure());
    GojekTakeWhile.takeWhile(reader.stream(), command -> !command.equalsIgnoreCase(EXIT)).forEach(command -> {
      try {
        final AbstractCommandAction action = ActionFactory.getInstance().getAction(command);
        if (action.isValidCommand()) {
          final String result = action.execute(parkingLot);
          writer.write(result);
        }
      } catch (ParkingException | IOException e) {
        try {
          writer.write(e.getMessage());
        } catch (IOException ex) {
        }
      }
    });
    writer.flush();
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

  private static class IOClass {
    private String[] args;
    private InputReader reader;
    private OutputWriter writer;

    private IOClass(String... args) {
      this.args = args;
    }

    private InputReader getReader() {
      return reader;
    }

    private OutputWriter getWriter() {
      return writer;
    }

    private IOClass invoke() throws IOException {
      if (args.length == 0 || args[0].equalsIgnoreCase("CONSOLE")) {
        reader = new CommandLineReader();
        writer = new ConsoleWriter();
      } else if (args[0].equalsIgnoreCase("FILE") || args.length == 1) {
        reader = new TextFileReader(Paths.get(args[args.length - 1]));
        //  writer = new TextFileWriter(Paths.get(args[2]));
        writer = new ConsoleWriter();
      } else if (args[0].equalsIgnoreCase("NETWORK") || args.length == 2) {
        final Socket clientSocket = new Socket(args[args.length - 2], Integer.parseInt(args[args.length - 1]));
        clientSocket.setSoTimeout(60 * 1000);
        reader = new NetworkReader(clientSocket);
        writer = new ConsoleWriter();
      } else {
        throw new RuntimeException("Invalid Input mode" +
                System.lineSeparator() +
                getInstructionMsg());
      }
      return this;
    }
  }
}
