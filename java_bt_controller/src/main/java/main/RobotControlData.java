package main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotControlData {

  public long timestamp;
  public boolean led;
  public int direction;
  public int velocity;

}
