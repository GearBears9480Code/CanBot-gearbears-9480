// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.RobotContainer;

public class Drivetrain extends SubsystemBase {
  /** Creates a new Drivetrain. */
  // Sets up the motors corresponding to their position on the bot.
  private PWMTalonSRX rightRear = new PWMTalonSRX(10);
  private PWMTalonSRX leftRear = new PWMTalonSRX(11);
  private PWMTalonSRX rightFront = new PWMTalonSRX(12);
  private PWMTalonSRX leftFront = new PWMTalonSRX(13);

  private RobotContainer robotContainer;

  public Drivetrain(RobotContainer robot) {
    robotContainer = robot;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run, aka every 20 ms
    CommandXboxController m_driverController = robotContainer.getControl();

    double driveVelocity = m_driverController.getLeftY() * -1;
    double turnVelocity = m_driverController.getRightX();

    sendOutputToMotors(leftFront, leftRear, driveVelocity, turnVelocity);
    sendOutputToMotors(rightFront, rightRear, driveVelocity, turnVelocity * -1);
  }

  private void sendOutputToMotors(PWMTalonSRX front, PWMTalonSRX rear, double output, double turnOutput) {
    front.set(output + turnOutput);
    rear.set(output + turnOutput);
  }

}
