package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.SwerveConstants;

public class SwerveSubsystem extends SubsystemBase {
  private final SwerveModule FL = new SwerveModule(
    "FL",
    6,
    7,
    2,
    0 // FIXME: Calibrate this value
  );
  private final SwerveModule FR = new SwerveModule(
    "FR",
    8,
    9,
    3,
    0 // FIXME: Calibrate this value
  );
  private final SwerveModule RL = new SwerveModule(
    "RL",
    10,
    11,
    4,
    0 // FIXME: Calibrate this value
  );
  private final SwerveModule RR = new SwerveModule(
    "RR",
    12,
    13,
    5,
    0 // FIXME: Calibrate this value
  );

  private final AHRS navx = new AHRS(SPI.Port.kMXP);
  private final SwerveDriveOdometry odometry = new SwerveDriveOdometry(DriveConstants.kDriveKinematics, geRotation2d(), getModulePositions());
  private final Field2d m_field = new Field2d();

  public void zeroHeading(){
    navx.reset();
  }
  public double getHeading(){
    return Math.IEEEremainder(navx.getAngle(), 360);
  }
  public Rotation2d geRotation2d(){
    return Rotation2d.fromDegrees(getHeading());
  }

  public void stopModules(){
    FL.stop();
    FR.stop();
    RL.stop();
    RR.stop();
  }
  public void setModuleStates(SwerveModuleState[] desiredStates){
    SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, DriveConstants.kMaxDriveSpeed);
    FL.setDesiredState(desiredStates[0]);
    FR.setDesiredState(desiredStates[1]);
    RL.setDesiredState(desiredStates[2]);
    RR.setDesiredState(desiredStates[3]);
  }

  public SwerveModulePosition[] getModulePositions(){
    return new SwerveModulePosition[]{
      FL.getModulePosition(),
      FR.getModulePosition(),
      RL.getModulePosition(),
      RR.getModulePosition()
    };
  }

  public Pose2d getPose(){
    return odometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose){
    odometry.resetPosition(geRotation2d(), getModulePositions(), pose);
  }
  
  public SwerveSubsystem() {
    new Thread(() -> {
      try{
        Thread.sleep(1000);
        zeroHeading();
      }
      catch(Exception e){}
    }).start();
  }

  @Override
  public void periodic() {
    odometry.update(geRotation2d(), getModulePositions());
    m_field.setRobotPose(getPose());
    SmartDashboard.putNumber("Robot Heading", getHeading());
    SmartDashboard.putData("Field", m_field);
  }

  
}
