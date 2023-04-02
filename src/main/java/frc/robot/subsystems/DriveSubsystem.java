// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {

  private final WPI_VictorSPX leftDrive = new WPI_VictorSPX(2);
  private final WPI_VictorSPX leftDriveFollower = new WPI_VictorSPX(3);

  private final WPI_VictorSPX rightDrive = new WPI_VictorSPX(4);
  private final WPI_VictorSPX rightDriveFollower = new WPI_VictorSPX(5);
  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    leftDrive.configFactoryDefault();
    leftDriveFollower.configFactoryDefault();
    rightDrive.configFactoryDefault();
    rightDriveFollower.configFactoryDefault();

    leftDriveFollower.follow(leftDrive);
    rightDriveFollower.follow(rightDrive);
    
    rightDrive.setInverted(InvertType.InvertMotorOutput);
    rightDriveFollower.setInverted(InvertType.InvertMotorOutput);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  private double leftSpeed,rightSpeed;
  public void arcadeDrive(double speed, double turn){
    leftSpeed = speed + turn;
    rightSpeed = speed - turn;
    
    leftDrive.set(ControlMode.PercentOutput, leftSpeed);
    rightDrive.set(ControlMode.PercentOutput, rightSpeed);
  }

  public void stop(){
    leftDrive.set(ControlMode.PercentOutput, 0);
    rightDrive.set(ControlMode.PercentOutput, 0);
  }

}
