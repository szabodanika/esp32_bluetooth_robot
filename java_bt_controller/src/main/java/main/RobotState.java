package main;

import lombok.Data;

@Data
public class RobotState {
  private RobotControlData robotControlData = new RobotControlData();
  private RobotReportData robotReportData = new RobotReportData();
}
