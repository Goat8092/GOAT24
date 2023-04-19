package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.SwerveConstants;

import com.revrobotics.CANSparkMax;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.SensorTimeBase;

public class SwerveModule {
    private String modulePosition;
    private CANSparkMax driveMotor;
    private CANSparkMax turnMotor;

    private RelativeEncoder driveEncoder;
    private RelativeEncoder turnEncoder;

    private CANCoder absoluteEncoder;

    private PIDController turnPID;

    private SwerveModuleState moduleState;

    public SwerveModule(String position, int driveID, int turnID, int encoderID, double encoderOffset){
        this.modulePosition = position;
        this.driveMotor = new CANSparkMax(driveID, MotorType.kBrushless);
        this.turnMotor = new CANSparkMax(turnID, MotorType.kBrushless);

        this.driveEncoder = driveMotor.getEncoder();
        this.turnEncoder = turnMotor.getEncoder();
        
        this.absoluteEncoder = new CANCoder(encoderID);
        
        absoluteEncoder.configMagnetOffset(encoderOffset);
        absoluteEncoder.configFeedbackCoefficient(2 * Math.PI * 4096.0, "rad", SensorTimeBase.PerSecond); // Radian / seconds

        driveEncoder.setPositionConversionFactor(SwerveConstants.kWheelCircumference);
        driveEncoder.setVelocityConversionFactor(SwerveConstants.kWheelCircumference / 60); // Meter / seconds
        turnEncoder.setPositionConversionFactor(SwerveConstants.kTurnEncoderRadian);
        turnEncoder.setVelocityConversionFactor(SwerveConstants.kTurnEncoderRadian / 60); // Radian / seconds

        this.turnPID = new PIDController(SwerveConstants.kPTurn, 0 , 0);
        turnPID.enableContinuousInput(-Math.PI, Math.PI);
        resetEncoders();
    }

    public double getDrivePosition(){
        return driveEncoder.getPosition();
    }
    public double getTurnPosition(){
        return turnEncoder.getPosition();
    }
    public double getDriveVelocity(){
        return driveEncoder.getVelocity();
    }
    public double getTurnVelocity(){
        return turnEncoder.getVelocity(); 
    }
    public double getAbsoluteEncoderRad(){
        return absoluteEncoder.getAbsolutePosition();
    }

    public Rotation2d geRotation2d(){
        return new Rotation2d(getTurnPosition());
    }
    
    public SwerveModuleState getState(){
        return new SwerveModuleState(getDriveVelocity(), geRotation2d());
    }


    public void resetEncoders(){
        driveEncoder.setPosition(0);
        turnEncoder.setPosition(getAbsoluteEncoderRad());
    }
    public void setDesiredState(SwerveModuleState state){
        moduleState = state;
        if(Math.abs(moduleState.speedMetersPerSecond) < 0.001){
            stop();
            return;
        }
        moduleState = SwerveModuleState.optimize(moduleState, getState().angle);
        driveMotor.set(moduleState.speedMetersPerSecond / DriveConstants.kMaxDriveSpeed);
        turnMotor.set(turnPID.calculate(getTurnPosition(), moduleState.angle.getRadians()));
        SmartDashboard.putString(modulePosition + "Swerve State", moduleState.toString());
    }

    public SwerveModulePosition getModulePosition(){
        return new SwerveModulePosition(getDrivePosition(),geRotation2d());
    }
    
    public void stop(){
        
        driveMotor.set(0);
        turnMotor.set(0);
    }

}
