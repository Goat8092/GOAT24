// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;

public class JoystickDriveCommand extends CommandBase {

  private DriveSubsystem m_drive;
  private Joystick m_stick;

  /** Creates a new JoystickDriveCommand. */
  public JoystickDriveCommand(DriveSubsystem drive, Joystick controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.m_drive = drive;
    this.m_stick = controller;

    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}


  private double speed,turn;
  private final SlewRateLimiter speedLimiter = new SlewRateLimiter(1);

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    speed = m_stick.getRawAxis(1); // ? axis
    turn = m_stick.getRawAxis(5); // ? axis

    m_drive.arcadeDrive(speedLimiter.calculate(speed), turn);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
