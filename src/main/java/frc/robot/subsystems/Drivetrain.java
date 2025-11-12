// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.RobotContainer;

public class Drivetrain extends SubsystemBase {
  /** Creates a new Drivetrain. */
  // Sets up the motors corresponding to their position on the bot.
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
    // This method will be called once per scheduler run, aka every 20 ms
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
    double speed = output + turnOutput;
    front.set(TalonSRXControlMode.PercentOutput, Math.abs(speed) > 0.01 ? speed : 0);
    rear.set(TalonSRXControlMode.PercentOutput, Math.abs(speed) > 0.01 ? speed : 0);
  }
}
