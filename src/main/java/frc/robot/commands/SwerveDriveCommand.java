package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.SwerveModule;
import frc.robot.subsystems.SwerveSubsystem;

public class SwerveDriveCommand extends CommandBase {

  private SwerveSubsystem swerveSubsystem;
  private double xSpd, ySpd, turnSpd;
  private Supplier<Double> xSpdFunction, ySpdFunction, turnSpdFunction;
  private Supplier<Boolean> fieldOriented;
  private SlewRateLimiter xLimiter, yLimiter, turnLimiter;
  
  public SwerveDriveCommand(SwerveSubsystem swerveSubsystem, Supplier<Double> xSpdFunction, 
  Supplier<Double> ySpdFunction, Supplier<Double> turnSpeedFunction, Supplier<Boolean> fieldOrientedFunction) {
    
    this.swerveSubsystem = swerveSubsystem;
    this.fieldOriented = fieldOrientedFunction;
    this.turnSpdFunction = turnSpeedFunction;
    this.xSpdFunction = xSpdFunction;
    this.ySpdFunction = ySpdFunction;
    this.xLimiter = new SlewRateLimiter(7); // FIXME: tune this values
    this.yLimiter = new SlewRateLimiter(7); // FIXME: tune this values
    this.turnLimiter = new SlewRateLimiter(7); // FIXME: tune this values
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    xSpd = Math.abs(xSpdFunction.get()) > DriveConstants.kJoystickDeadZone ? xSpdFunction.get() : 0.0;
    ySpd = Math.abs(ySpdFunction.get()) > DriveConstants.kJoystickDeadZone ? ySpdFunction.get() : 0.0;
    turnSpd = Math.abs(turnSpdFunction.get()) > DriveConstants.kJoystickDeadZone ? turnSpdFunction.get() : 0.0;

    xSpd = xLimiter.calculate(xSpd) * DriveConstants.kMaxDriveSpeed * DriveConstants.kTeleOpDriveSpeed;
    ySpd = yLimiter.calculate(ySpd) * DriveConstants.kMaxDriveSpeed * DriveConstants.kTeleOpDriveSpeed;
    turnSpd = turnLimiter.calculate(turnSpd) * DriveConstants.kMaxTurnSpeed * DriveConstants.kTeleOpTurnSpeed;

    ChassisSpeeds chassisSpeeds = fieldOriented.get() 
    ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpd, ySpd,turnSpd,swerveSubsystem.geRotation2d())
    : new ChassisSpeeds(xSpd,ySpd,turnSpd);
    
    SwerveModuleState[] moduleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);

    swerveSubsystem.setModuleStates(moduleStates);
  }

  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.stopModules();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
