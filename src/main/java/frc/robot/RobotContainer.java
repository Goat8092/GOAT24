// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.SwerveDriveCommand;
import frc.robot.subsystems.SwerveSubsystem;
import frc.com.goat8092.drivers.WS2812;

public class RobotContainer {
  private final Joystick m_stick = new Joystick(0);
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  private final WS2812 leftLed = new WS2812(1,60);

  public RobotContainer() {
    defaultCommands();
    configureBindings();
  }

  private void defaultCommands(){
    swerveSubsystem.setDefaultCommand(new SwerveDriveCommand(
      swerveSubsystem,
      () -> m_stick.getX(),
      () -> m_stick.getY(),
      () -> m_stick.getRawAxis(4),
      () -> true ));
  }

  private void configureBindings() {
  }

  public Command getAutonomousCommand() {
    return new SequentialCommandGroup();
  }
}
