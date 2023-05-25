package main;

import java.awt.*;
import java.awt.event.KeyEvent;

public class RobotWindow {

  private static final int WIDTH = 800, HEIGHT = 600;

  private Label timestampLabel;
  private Label accelerationLabel;
  private Label temperatureLabel;

  private Runnable onRightButtonPressed;
  private Runnable onLeftButtonPressed;
  private Runnable onUpButtonPressed;
  private Runnable onDownButtonPressed;
  private Runnable onSpaceButtonPressed;

  public RobotWindow() {

    // initialise window
    Frame frame = new Frame();
    frame.setTitle("Robot");
    frame.setSize(800, 600);

    // center window on display
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(screenSize.width / 2 - WIDTH / 2, screenSize.height / 2 - HEIGHT / 2);

    // add view elements
    frame.setLayout(new GridLayout(6, 2));

    timestampLabel = new Label("Label 1");
    accelerationLabel = new Label("Label 2");
    temperatureLabel = new Label("Label 3");

    frame.add(timestampLabel);
    frame.add(accelerationLabel);
    frame.add(temperatureLabel);

    // Add button listeners
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new MyDispatcher());

    // reveal window
    frame.setVisible(true);
  }

  public void updateWith(RobotReportData data) {
    timestampLabel.setText("Timestamp: " + data.getTimestamp());
    accelerationLabel.setText(
        "Acceleration:\t X: " + data.getX() +
            "\tY: " + data.getY() +
            "\tZ: " + data.getZ());
    temperatureLabel.setText("Temperature: " + data.getTemperature());
  }

  private class MyDispatcher implements KeyEventDispatcher {
    public boolean dispatchKeyEvent(KeyEvent e) {
      if (e.getID() == KeyEvent.KEY_PRESSED) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          System.out.println("UP");
          onUpButtonPressed.run();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          System.out.println("DOWN");
          onDownButtonPressed.run();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
          System.out.println("LEFT");
          onLeftButtonPressed.run();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
          System.out.println("RIGHT");
          onRightButtonPressed.run();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          System.out.println("SPACE");
          onSpaceButtonPressed.run();
        }
      }
      return false;
    }
  }

  public void whenLeftButtonPressed(Runnable runnable) {
    this.onLeftButtonPressed = runnable;
  }

  public void whenRightButtonPressed(Runnable runnable) {
    this.onRightButtonPressed = runnable;
  }

  public void whenUpButtonPressed(Runnable runnable) {
    this.onUpButtonPressed = runnable;
  }

  public void whenDownButtonPressed(Runnable runnable) {
    this.onDownButtonPressed = runnable;
  }

  public void whenSpaceButtonPressed(Runnable runnable) {
    this.onSpaceButtonPressed = runnable;
  }

}
