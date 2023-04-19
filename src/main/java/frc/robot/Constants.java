package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants {
  public static final class SwerveConstants{
    public static final double kWheelDiameter = Units.inchesToMeters(1.5);
    public static final double kDriveMotorGearRatio = 1/6.75; //FIXME: this is for MK4 L2 gear ratio
    public static final double kTurnMotorGearRatio = 1/12.8;
    public static final double kWheelCircumference = Math.PI * kWheelDiameter * kDriveMotorGearRatio;
    public static final double kTurnEncoderRadian = 2 * Math.PI * kTurnMotorGearRatio;
    public static final double kPTurn = 0.5; // FIXME: tune this value
    
  }

  public static final class DriveConstants{
    public static final double kTeleOpDriveSpeed = 0.8; 
    public static final double kJoystickDeadZone = 0.08; 
    public static final double kTeleOpTurnSpeed = 0.8; 
    public static final double kMaxDriveSpeed = Units.inchesToMeters(14.5); //meter/sec FIXME: change this value
    public static final double kMaxTurnSpeed = Math.PI * 2; //rad/sec FIXME: change this value
    public static final double kTrackWidth = 60; //FIXME: measure this value
    public static final double kWheelBase = 60; //FIXME: measure this value
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
      new Translation2d(kWheelBase / 2, kTrackWidth / 2),
      new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
      new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
      new Translation2d(-kWheelBase / 2, -kTrackWidth / 2)
    );

  }

}
