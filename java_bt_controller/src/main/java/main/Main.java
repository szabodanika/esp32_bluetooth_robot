package main;

import java.awt.Window;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

  private static final String SERIAL_PORT = "/dev/cu.usbmodem111401";

  private static final RobotWindow window = new RobotWindow();

  private static final RobotState robotState = new RobotState();

  private static BufferedReader br;
  private static BufferedWriter bw;

  // private static final String SERIAL_PORT = "/dev/cu.ESP32";
  public static void main(String[] args) {

    try {
      br = new BufferedReader(new FileReader(SERIAL_PORT));
      bw = new BufferedWriter(new FileWriter(SERIAL_PORT));

      setWindowListeners();

      String message = "Hello, ESP32!";
      bw.write(message);

      String line;
      while ((line = br.readLine()) != null) {
        handleIncomingData(line);
      }

      br.close();
      bw.close();
    } catch (IOException ex) {
      System.out.println("Error: " + ex.getMessage());
    }
  }

  private static void setWindowListeners() {
    window.whenLeftButtonPressed(() -> {
      RobotControlData controlData = robotState.getRobotControlData();
      controlData.setDirection(-1);
      controlData.setVelocity(0);
      robotState.setRobotControlData(controlData);
      dispatchControlData();
    });

    window.whenRightButtonPressed(() -> {
      RobotControlData controlData = robotState.getRobotControlData();
      controlData.setDirection(1);
      controlData.setVelocity(0);
      robotState.setRobotControlData(controlData);
      dispatchControlData();
    });

    window.whenUpButtonPressed(() -> {
      RobotControlData controlData = robotState.getRobotControlData();
      controlData.setDirection(0);
      controlData.setVelocity(1);
      robotState.setRobotControlData(controlData);
      dispatchControlData();
    });

    window.whenDownButtonPressed(() -> {
      RobotControlData controlData = robotState.getRobotControlData();
      controlData.setDirection(0);
      controlData.setVelocity(-1);
      robotState.setRobotControlData(controlData);
      dispatchControlData();
    });

    window.whenSpaceButtonPressed(() -> {
      RobotControlData controlData = robotState.getRobotControlData();
      controlData.setLed(!controlData.isLed());
      robotState.setRobotControlData(controlData);
      dispatchControlData();
    });
  }

  private static void dispatchControlData() {
    try {
      bw.write(new ObjectMapper().writeValueAsString(robotState.getRobotControlData()));
      bw.newLine();
      bw.flush();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void handleIncomingData(String line) {
    try {
      RobotReportData data = new ObjectMapper().readValue(line, RobotReportData.class);

      System.out.println(data);

      window.updateWith(data);

    } catch (JsonProcessingException e) {
      System.out.println("Non-JSON Data: " + line);
    }

  }
}