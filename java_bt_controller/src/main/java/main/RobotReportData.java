package main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotReportData {

  private long timestamp = -1;
  private float x = -1f, y = -1f, z = -1f;
  private float temperature = -1f;

}
