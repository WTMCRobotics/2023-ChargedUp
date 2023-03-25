// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayDeque;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.AutonomousActions.AutonArmCalibrate;
import frc.robot.AutonomousActions.AutonBalance;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonBalance.MovementDirection;
import frc.robot.InputtedGuitarControls.GribberState;
import frc.robot.motor.MotorController;
import frc.robot.motor.MotorControllerFactory;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public double circumference = 8 * Math.PI;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String autoSelected;
  private final SendableChooser<String> chooser = new SendableChooser<>();

  private MotorController frontLeft;
  private MotorController frontRight;
  private MotorController backLeft;
  private MotorController backRight;
  public XboxController xboxController;

  private MotorController armController;
  private MotorController gribberController;

  // private Vision robotVision;

  public MecanumDrive mecanumDriveTrain;

  private XboxController guitarXboxController;
  private InputtedDriverControls inputtedControls;
  private InputtedGuitarControls guitarControls;

  private AHRS robotGyroscope;

  private LidarProxy lidarSensor;

  private VideoSink videoArmServer;
  private VideoSink videoFrontServer;

  RobotMotors motors;
  private SendableChooser<String> testOptions = new SendableChooser<>();

  /*
   * m This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

    chooser.setDefaultOption("Default Auto", kDefaultAuto);
    chooser.addOption("Place object, leave community, and balance", "PlaceLeaveCommunityBalance");
    chooser.addOption("Place object and balance", "PlaceBalance");
    chooser.addOption("Place object", "justPlace");
    chooser.addOption("Place object and leave community", "PlaceLeaveCommunity");
    chooser.addOption("Leave community", "LeaveCommunity");
    chooser.addOption("Declare manually in code", "manualInCode");
    SmartDashboard.putData("Auton Routes", chooser);

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

    armController =
        MotorControllerFactory.create(this, Constants.ARM_MOTOR_ID, MotorController.Type.SparkMax);

    gribberController = MotorControllerFactory.create(this, Constants.GRIBBER_MOTOR_ID,
        MotorController.Type.SparkMax);

    guitarXboxController = new XboxController(1);

    frontLeft.setBrakeMode(true);
    frontRight.setBrakeMode(true);
    backLeft.setBrakeMode(true);
    backRight.setBrakeMode(true);
    gribberController.setBrakeMode(true);

    frontRight.setInverted(true);
    backRight.setInverted(true);

    System.out.println("Working");
    // backLeft.setInverted(true);
    // backRight.setInverted(true);
    motors = new RobotMotors(frontLeft, frontRight, backLeft, backRight, gribberController,
        armController);
    // Constants.bottomArmLimitSwitch = new
    // DigitalInput(Constants.bottomArmLimitSwitchID);

    mecanumDriveTrain = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

    inputtedControls = new InputtedDriverControls(xboxController);

    guitarControls =
        new InputtedGuitarControls(guitarXboxController, armController, gribberController);

    // Deadzone
    mecanumDriveTrain.setDeadband(0.08);

    robotGyroscope = new AHRS(SPI.Port.kMXP);

    armController.setEncoderPosition(0.0);

    frontLeft.setEncoderPosition(0.0);

    System.out.println("SYOUST WORKING");

    // lidarSensor = new LidarProxy(SerialPort.Port.kMXP);
    // System.out.println("The disatnce is " + lidarSensor.get());

    // hopefully after testing we can remove this
    mecanumDriveTrain.setSafetyEnabled(false);

    UsbCamera frontFacingCamera = CameraServer.startAutomaticCapture("Front Camera", 1);
    UsbCamera armCamera = CameraServer.startAutomaticCapture("Arm Camera", 0);
    armCamera.setFPS(16);
    frontFacingCamera.setFPS(16);
    armCamera.setResolution(196, 108);
    frontFacingCamera.setResolution(196, 108);

    videoArmServer = CameraServer.getServer();
    videoArmServer.setSource(armCamera);
    videoFrontServer = CameraServer.getServer();
    videoFrontServer.setSource(frontFacingCamera);

    testOptions.setDefaultOption("Move arm to starting position", "armCalibrate");
    // testOptions.addOption("Calibrate Arm", "armCalibrate");
    testOptions.addOption("Test Mechanics", "testMechanics");
    testOptions.addOption("Move arm to transport position", "armTransport");
    SmartDashboard.putData("Test mode action", testOptions);

    robotGyroscope.calibrate();
    // Constants.bottomArmLimitSwitch = new
    // DigitalInput(Constants.bottomArmLimitSwitchID);

    // Constants.bottomArmLimitSwitch.

    // driveTab.add("Gyro Reading",
    // robotGyroscope).withWidget(BuiltInWidgets.kGyro);
    // driveTab.add("The Drive",
    // mecanumDriveTrain).withWidget(BuiltInWidgets.kMecanumDrive);
    // driveTab.add("Arm Encoder", ((SparkMotorController)
    // armController).getEncoderObject())
    // .withWidget(BuiltInWidgets.kEncoder);

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
  public void robotPeriodic() {
    // Reset encoder value and stop motor, to prevent arm from over extending
    if (motors.getArmMotor().getReverseLimit()) {
      if (armController.getEncoderPosition() > 0) {
        System.out.println("Limit switch reset!");
      }
      armController.setEncoderPosition(0.0);
    }
    SmartDashboard.putNumber("Arm Encoder Pos", armController.getEncoderPosition() * 360);
    SmartDashboard.putNumber("Eheel Encoder Pos", frontLeft.getEncoderPosition());
    SmartDashboard.putNumber("Robot pitch", robotGyroscope.getRoll());

  }

  AutonMovement auton;

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
    ArrayDeque<AutonomousAction> selectedRoute;
    switch (autoSelected) {
      case "PlaceLeaveCommunityBalance":
        selectedRoute = AutonRoutes.placeLeaveCommunityThenBalance(robotGyroscope);
        break;
      case "PlaceBalance":
        selectedRoute = AutonRoutes.placeThenBalance(robotGyroscope);
        break;
      case "PlaceLeaveCommunity":
        selectedRoute = AutonRoutes.placeObjectAndLeaveCommunity();
        break;
      case "LeaveCommunity":
        selectedRoute = AutonRoutes.leaveCommunityWhilstFacingWall();
        break;
      case "justPlace":
        selectedRoute = AutonRoutes.placeObject();
        break;
      case "manualInCode":

        ArrayDeque<AutonomousAction> manualActions = new ArrayDeque<>();

        manualActions.add(new AutonBalance(MovementDirection.BACKWARDS, robotGyroscope));
        System.out.println("Manual action!");
        selectedRoute = manualActions;
        break;

      default:
        selectedRoute = AutonRoutes.leaveCommunityWhilstFacingWall();
    }
    System.out.println("Auto selected: " + autoSelected);

    auton = new AutonMovement(motors, selectedRoute);

    // new AutonMovement(motors, selectedRoute);

  }

  /** This function is called periodically during autonomous. Oliver doesnt know what it does */
  @Override
  public void autonomousPeriodic() {
    auton.autonomousEveryFrame();
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
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Turn Purple if Cube
    guitarControls.doEveryFrame();
    if (guitarControls.lightColor == InputtedGuitarControls.LightColor.CUBE) {
      Constants.LED_GREEN.set(true);
      Constants.LED_RED.set(true);
      // Turn Yellow-ish Green if Cone
    } else if (guitarControls.lightColor == InputtedGuitarControls.LightColor.CONE) {
      Constants.LED_BLUE.set(true);
      Constants.LED_RED.set(true);
    } else {
      Constants.LED_BLUE.set(false);
      Constants.LED_RED.set(false);
      Constants.LED_GREEN.set(false);
    }

    /*
     * mecanumDriveTrain.driveCartesian(inputtedControls.getLeftJoystickY() * -1,
     * inputtedControls.getLeftJoystickX() * -1, inputtedControls.getTurnAmount(),
     * robotGyroscope.getRotation2d());
     */
    mecanumDriveTrain.driveCartesian(inputtedControls.getLeftJoystickY(),
        inputtedControls.getLeftJoystickX(), inputtedControls.getTurnAmount());

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */

  @Override
  public void disabledPeriodic() {}

  AutonMovement resetMovement = null;

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    // if (testOptions.getSelected().equals("armCalibrate")
    // || testOptions.getSelected().equals("armTransport")) {
    // System.out.println("calibrating and ressseting arm");
    // ArrayDeque<AutonomousAction> resetQueue = new ArrayDeque<>();
    // resetQueue.add(new AutonArmCalibrate(true));
    // if (testOptions.getSelected().equals("armCalibrate")) {
    // resetQueue.add(new AutonMoveGribber(GribberState.OPENING));
    // }
    // resetMovement = new AutonMovement(motors, resetQueue);
    // } else if (testOptions.getSelected().equals("testMechanics")) {
    // System.out.println("Testing mechanics, check console for test info.");
    // MechanicsTest mechanicsTest = new MechanicsTest(motors);
    // mechanicsTest.testMechanics();
    // }
    // P - Proportional - How fast it approaches the target
    // I - Integral - Over time, how extra will it push based on how long it's been since we've been
    // close to the target
    // D - Derivative - How quickly we will slow down after we hit the target
    frontLeft.setPID(Constants.PRACTICE_ROBOT_GAINS);
    backLeft.setPID(Constants.PRACTICE_ROBOT_GAINS);
    frontRight.setPID(Constants.PRACTICE_ROBOT_GAINS);
    backRight.setPID(Constants.PRACTICE_ROBOT_GAINS);
    frontLeft.setDistance(12);
    backLeft.setDistance(12);
    frontRight.setDistance(12);
    backRight.setDistance(12);
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if (resetMovement != null) {
      resetMovement.autonomousEveryFrame();
    }
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    System.out.println("I know wherer you live oliver");
  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
