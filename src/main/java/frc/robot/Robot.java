// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.motor.MotorController;
import frc.robot.motor.MotorControllerFactory;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public double circumference = 1;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String autoSelected;
  private final SendableChooser<String> chooser = new SendableChooser<>();

  private MotorController frontLeft;
  private MotorController frontRight;
  private MotorController backLeft;
  private MotorController backRight;
  public XboxController xboxController;
  // private Vision robotVision;

  public MecanumDrive mecanumDriveTrain;
  private InputtedDriverControls inputtedControls;

  private Gyro robotGyroscope;

  /**
   * m This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    chooser.setDefaultOption("Default Auto", kDefaultAuto);
    chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", chooser);



    // Assuming the motors are talons, if not, switch to Spark
    frontLeft = MotorControllerFactory.create(this, Constants.FRONT_LEFT_MOTOR_ID,
        MotorController.Type.Talon);
    frontRight = MotorControllerFactory.create(this, Constants.FRONT_RIGHT_MOTOR_ID,
        MotorController.Type.Talon);
    backLeft = MotorControllerFactory.create(this, Constants.BACK_LEFT_MOTOR_ID,
        MotorController.Type.Talon);
    backRight = MotorControllerFactory.create(this, Constants.BACK_RIGHT_MOTOR_ID,
        MotorController.Type.Talon);
    xboxController = new XboxController(0);

    frontLeft.setBrakeMode(true);
    frontRight.setBrakeMode(true);
    backLeft.setBrakeMode(true);
    backRight.setBrakeMode(true);

    frontRight.setInverted(true);
    backRight.setInverted(true);

    System.out.println("Working");
    // backLeft.setInverted(true);
    // backRight.setInverted(true);

    mecanumDriveTrain = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

    inputtedControls = new InputtedDriverControls(xboxController);

    // Deadzone
    mecanumDriveTrain.setDeadband(0.04);

    robotGyroscope = new AHRS(SPI.Port.kMXP);


    // robotVision = new Vision();
    // robotVision.start();
    // robotVision.stop(); //This should stop the vision system

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow and SmartDashboard
   * integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */



  @Override
  public void autonomousInit() {
    autoSelected = chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + autoSelected);
    RobotMotors motors = new RobotMotors(frontLeft, frontRight, backLeft, backRight);
    AutonMovement auton = new AutonMovement(motors);
    auton.AutoForward(1, .25);


  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {



    switch (autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  public double applySlowMode(double scaleFactor, boolean slowModeEnabled) {
    if (slowModeEnabled) {
      scaleFactor *= 4;
    }
    return scaleFactor;
  }

  /** This function is called once when teleop is enabled. */


  @Override
  public void teleopInit() {

    robotGyroscope.calibrate();

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {

    // inputtedControls.updateValues();

    // I am testing the robotGyroscope thing, delete later probably
    mecanumDriveTrain.driveCartesian(inputtedControls.getLeftJoystickY(),
        inputtedControls.getLeftJoystickX(), inputtedControls.getTurnAmount(),
        robotGyroscope.getRotation2d().times(-1));


  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */

  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    System.out.println("I know wherer you live oliver");
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
