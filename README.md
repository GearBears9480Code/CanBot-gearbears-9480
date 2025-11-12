# CanBot-gearbears-9480
A Github Repository for the Gear Bear's **Can Bot Project**.

## Documentation

### Drivetrain Subsystem

The Drivetrain Subsystem controls, shockingly, how the Can Bot drives. It uses an Arcade Drive system, where one joystick controls forward and backward, and the other controls turning left or right.

#### TalonSRX Motors

The robot uses TalonSRX Motor Speed Controllers, which are established the Drivetrain class.
```
public class Drivetrain extends SubsystemBase {
  TalonSRX rightRear = new TalonSRX(10);
  TalonSRX leftRear = new TalonSRX(11);
  TalonSRX rightFront = new TalonSRX(12);
  TalonSRX leftFront = new TalonSRX(13);
```
We establish these motors with the type "TalonSRX," and then assign them a name (being "rightRear," "leftRear," etc.) in accordance to their physical position on the robot. The numbers inside the parenthesis are ID's, and they allow the code to differentiate between the motors.

The ID's for the motors can be viewed and changed in the Pheonix Tuner X Application. You can observe how the ID's of each TalonSRX in the application match up with the ID's listed in the code.

**Note that this application can only support CTRE hardware pieces.** 
![The Pheonix Tuner X Interface](<Screenshot 2025-11-10 130718.png>)

***
#### Establishing the Controller

For this robot, we use a CommandXboxController, which we establish with this line:
```
private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
```
We name the controller as ```m_driverController```, then use the ```kDriverControllerPort``` constant as it's ID, which in this case, is 0.

**Connecting the controller to RobotContainer is still in progress, so the controller is established in the subststem for now.**

***
### The Drivetrain

Because each joystick is used for different purposes (driving for the left joystick, and turning for the right joystick), there must be a value for each, which in this case, is ```driveVelocity``` and ```turnVelocity```.
```
double rawDriveVelocity = (m_driverController.getLeftY() * -1) / 2;
double rawTurnVelocity = (m_driverController.getRightX()) / 2;
```
This is the reason why we established the controller in the previous section; so that we can get the axis values of each joystick.

For instance, ```getLeftY()``` is the function used to get the Y-Axis value of the left joystick, and ```getRightX()``` is used to get the X-Axis value of the right joystick.

Notice how the left Y-Axis value is being multiplied by -1. That is because, in regards to the Y-Axis values, the range is flipped, meaning that forward corresponds to negative values, and backwards corresponds to positive values. Because that's counterintuitive, we multiply the Y-Axis value by -1, so that pushing the joystick forward will make the robot move forward.

Also note that this weird property is only applicable to the Y-Values, which is why we do not multiply the X-Axis value by -1.

Dividing both values by 2 helps to make the robot more controllable. Both the speed and axis values have the same range, being -1 to +1. This means that pushing the joystick all the way forward will cause the robot to move as fast as possible, which is almost never needed, and may cause some accidents.

***

Next, we **truncate** the values with the ```Math.floor()```to the thousandths, and use those values for the speed of the wheels.
```
double driveVelocity = Math.floor(rawDriveVelocity * 1000) / 1000;
    double turnVelocity = Math.floor(rawTurnVelocity * 1000) / 1000;
```

Truncation is defined as simply cutting off a number to a certain decimal place. It is **not** the same as rounding. For instance, if you were to round 0.07 to the tenths, it would come out as 0.1. But with truncation, it would be 0.0.




This is the entirety of the Drivetrain.java Subsystem:

```
package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.RobotContainer;

public class Drivetrain extends SubsystemBase {
  TalonSRX rightRear = new TalonSRX(10);
  TalonSRX leftRear = new TalonSRX(11);
  TalonSRX rightFront = new TalonSRX(12);
  TalonSRX leftFront = new TalonSRX(13);

  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  private RobotContainer robotContainer;

  public Drivetrain(RobotContainer robot) {
    robotContainer = robot;
  }

  @Override
  public void periodic() {
    robotContainer.getControl();

    double rawDriveVelocity = (m_driverController.getLeftY() * -1) / 2;
    double rawTurnVelocity = (m_driverController.getRightX()) / 2;

    double driveVelocity = Math.floor(rawDriveVelocity * 1000) / 1000;
    double turnVelocity = Math.floor(rawTurnVelocity * 1000) / 1000;

    double deadBand = 0.1;
    
    sendOutputToMotors(rightFront, rightRear, -1 * driveVelocity, turnVelocity > deadBand || turnVelocity < -deadBand ? turnVelocity : 0);
    sendOutputToMotors(leftFront, leftRear, driveVelocity, turnVelocity > deadBand || turnVelocity < -deadBand ? turnVelocity : 0);
  }

  private void sendOutputToMotors(TalonSRX front, TalonSRX rear, double output, double turnOutput) {
    front.set(TalonSRXControlMode.PercentOutput, output + turnOutput);
    rear.set(TalonSRXControlMode.PercentOutput, output + turnOutput);
  }
}
```



## Contributors

Gerald Kang

Mackai Curtis