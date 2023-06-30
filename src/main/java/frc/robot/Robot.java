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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.InputtedGuitarControls.GribberState;
import frc.robot.AutonomousActions.AutonArmCalibrate;
import frc.robot.AutonomousActions.AutonBalance;
import frc.robot.AutonomousActions.AutonMoveGribber;
import frc.robot.AutonomousActions.AutonMoveInches;
import frc.robot.AutonomousActions.AutonBalance.MovementDirection;
import frc.robot.AutonomousActions.AutonMoveInches.MoveInchesDirection;
import frc.robot.motor.MotorController;
import frc.robot.motor.MotorControllerFactory;

/**
 * The Virtual Machine(VM) is configured to automatically run this class, and to call the functions
 * corresponding to each mode, as described in the TimedRobot documentation. If you change the name
 * of this class or the package after creating this project, you must also update the build.gradle
 * file in the The VM is configured to automatically run this class, and to call the functions
 * corresponding to each mode, as described in the TimedRobot documentation. If you change the name
 * of this class or the package after creating this project, you must also update the build.gradle
 * file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String autoSelected;
  private final SendableChooser<String> autonRouteChooser = new SendableChooser<>();

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

  private VideoSink videoArmServer;
  private VideoSink videoFrontServer;

  RobotMotors motors;
  private SendableChooser<String> testOptions = new SendableChooser<>();
  private final SendableChooser<String> autonDirection = new SendableChooser<>();
  MoveInchesDirection autonMoveInchesDirection = MoveInchesDirection.FORWARD;


  double inches = 24;// TODO remove this later
  /*
   * m This function is run when the robot is first started up and should be used for any m This
   * function is run when the robot is first started up and should be used for any initialization
   * code.
   */

  @Override
  public void robotInit() {

    autonRouteChooser.setDefaultOption("Default Auto", kDefaultAuto);
    autonRouteChooser.addOption("Place object, leave community, and balance",
        "PlaceLeaveCommunityBalance");
    autonRouteChooser.addOption("Place object and balance", "PlaceBalance");
    autonRouteChooser.addOption("Place object", "justPlace");
    autonRouteChooser.addOption("Place object, strafe LEFT, and leave community",
        "PlaceStrafeLeftLeaveCommunity");
    autonRouteChooser.addOption("Place object, strafe RIGHT, and leave community",
        "PlaceStrafeRightLeaveCommunity");

    autonRouteChooser.addOption("Balance while facing driver wall", "balanceWhileFacingWall");
    autonRouteChooser.addOption("Balance while facing field", "balanceWhileFacingField");


    autonRouteChooser.addOption("Leave community whilst facing driver wall",
        "LeaveCommunityFaceWall");
    autonRouteChooser.addOption("Leave community whilst facing enemy side",
        "LeaveCommunityFaceEnemy");
    autonRouteChooser.addOption("Declare manually in code", "manualInCode");
    SmartDashboard.putData("Auton Routes", autonRouteChooser);
    SmartDashboard.putNumber("Proportion", Constants.BUMPERLESS_ROBOT_GAINS.P);
    SmartDashboard.putNumber("Integral", Constants.BUMPERLESS_ROBOT_GAINS.I);
    SmartDashboard.putNumber("Derivative", Constants.BUMPERLESS_ROBOT_GAINS.D);
    SmartDashboard.putNumber("Peak Output", Constants.BUMPERLESS_ROBOT_GAINS.PEAK_OUTPUT);
    SmartDashboard.putNumber("Max Auton Acceleration", Constants.ACCELERATION);
    SmartDashboard.putNumber("Balance Proportion", Constants.BALANCING_GAINS.P);
    SmartDashboard.putNumber("Balance Integral", Constants.BALANCING_GAINS.I);
    SmartDashboard.putNumber("Balance Derivative", Constants.BALANCING_GAINS.D);
    autonDirection.setDefaultOption("Forward", "FORWARD");
    autonDirection.addOption("Forward", "FORWARD");
    autonDirection.addOption("Backward", "BACKWARD");
    autonDirection.addOption("Left", "LEFT");
    autonDirection.addOption("Right", "RIGHT");
    SmartDashboard.putData("Direction", autonDirection);
    SmartDashboard.putNumber("Max balancing RPM", Constants.BALANCING_MAX_RPM);
    SmartDashboard.putNumber("PID Activation Delay", Constants.ACTIVATE_PID_DELAY);

    // Assuming the motors are talons, if not, switch to Spark
    frontLeft = MotorControllerFactory.create(this, Constants.FRONT_LEFT_MOTOR_ID,
        MotorController.Type.Talon);
    frontRight = MotorControllerFactory.create(this, Constants.FRONT_RIGHT_MOTOR_ID,
        MotorController.Type.Talon);
    backLeft = MotorControllerFactory.create(this, Constants.BACK_LEFT_MOTOR_ID,
        MotorController.Type.Talon);
    backRight = MotorControllerFactory.create(this, Constants.BACK_RIGHT_MOTOR_ID,
        MotorController.Type.Talon);

    armController =
        MotorControllerFactory.create(this, Constants.ARM_MOTOR_ID, MotorController.Type.SparkMax);

    gribberController = MotorControllerFactory.create(this, Constants.GRIBBER_MOTOR_ID,
        MotorController.Type.SparkMax);

    // swap controllers if the main controller is a guitar
    xboxController = new XboxController(0);
    if (xboxController.getName().contains("Guitar")) {
      guitarXboxController = xboxController;
      xboxController = new XboxController(1);
    } else {
      guitarXboxController = new XboxController(1);
    }

    frontLeft.setBrakeMode(true);
    frontRight.setBrakeMode(true);
    backLeft.setBrakeMode(true);
    backRight.setBrakeMode(true);
    gribberController.setBrakeMode(true);

    frontRight.setInverted(true);
    backRight.setInverted(true);

    gribberController.setInverted(true);

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
    guitarControls =
        new InputtedGuitarControls(guitarXboxController, armController, gribberController);

    // Deadzone
    mecanumDriveTrain.setDeadband(0.08);

    gribberController.setEncoderPosition(0);

    robotGyroscope = new AHRS(SPI.Port.kMXP);

    armController.setEncoderPosition(0.0);

    frontLeft.setEncoderPosition(0.0);

    System.out.println("SYSOUST WORKING");

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
    testOptions.addOption("Move robot via PID manually", "movePIDManually");
    SmartDashboard.putData("Test mode action", testOptions);

    frontLeft.setEncoderInverted(true);
    frontRight.setEncoderInverted(true);
    backLeft.setEncoderInverted(true);
    backRight.setEncoderInverted(true);



    initializeMotionMagicMaster(frontRight, Constants.BUMPERLESS_ROBOT_GAINS);
    initializeMotionMagicMaster(frontLeft, Constants.BUMPERLESS_ROBOT_GAINS);
    initializeMotionMagicMaster(backLeft, Constants.BUMPERLESS_ROBOT_GAINS);
    initializeMotionMagicMaster(backRight, Constants.BUMPERLESS_ROBOT_GAINS);

    robotGyroscope.calibrate();
    // Constants.bottomArmLimitSwitch = new
    // DigitalInput(Constants.bottomArmLimitSwitchID);

    // Constants.bottomArmLimitSwitch.

    // driveTab.add("Gyro Reading",
    // robotGyroscope).withWidget(BuiltInWidgets.kGyro);
    // Shuffleboard.getTab("Drive").add("The Drive", mecanumDriveTrain)
    // .withWidget(BuiltInWidgets.kMecanumDrive);
    // driveTab.add("Arm Encoder", ((SparkMotorController)
    // armController).getEncoderObject())
    // .withWidget(BuiltInWidgets.kEncoder);

    // robotVision = new Vision();
    // robotVision.start();
    // robotVision.stop(); //This should stop the vision system

  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow and SmartDashboard
   * This runs after the mode specific periodic functions, but before LiveWindow and SmartDashboard
   * integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Reset encoder value and stop motor, to prevent arm from over extending
    if (motors.getArmMotor().getReverseLimit()) {
      if (armController.getEncoderPosition() > 0) {
        System.out.println("Arm Limit switch reset!");
      }
      armController.setEncoderPosition(0.0);
    }
    if (gribberController.getForwardLimit()) {
      // if (gribberController.getEncoderPosition() < 0) {
      System.out.println("Gribber Limit switch reset!");
      // }S
      gribberController.setEncoderPosition(0.0);
      // SmartDashboard.putNumber("Gribber encoder", gribberController.getEncoderPosition());
      SmartDashboard.putNumber("Roll", robotGyroscope.getRoll());
    }
    // SmartDashboard.putNumber("Arm Encoder Pos", armController.getEncoderPosition() * 360);
    // SmartDashboard.putNumber("Wheel Encoder Pos", frontLeft.getEncoderPosition());
  }

  AutonMovement auton;

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro This
   * autonomous (along with the chooser code above) shows how to select between different autonomous
   * modes using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you
   * prefer the LabVIEW Dashboard, remove all of the chooser code and uncomment the getString line
   * to get the auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the You
   * can add additional auto modes by adding additional comparisons to the switch structure below
   * with additional strings. If using the SendableChooser make sure to add them to the chooser code
   * above as well.
   */

  @Override
  public void autonomousInit() {
    AutonRoutes autonRoutes = new AutonRoutes(robotGyroscope, this.motors);
    autoSelected = autonRouteChooser.getSelected();
    ArrayDeque<AutonomousAction> selectedRoute;
    switch (autoSelected) {
      case "PlaceLeaveCommunityBalance":
        selectedRoute = autonRoutes.placeLeaveCommunityThenBalance(robotGyroscope);
        break;
      case "PlaceBalance":
        selectedRoute = autonRoutes.placeThenBalance();
        break;
      case "PlaceStrafeLeftLeaveCommunity":
        selectedRoute = autonRoutes.placeObjectStrafeLeftLeaveCommunity();
        break;
      case "PlaceStrafeRightLeaveCommunity":
        selectedRoute = autonRoutes.placeObjectStrafeRightLeaveCommunity();
        break;
      case "LeaveCommunityFaceWall":
        selectedRoute = autonRoutes.leaveCommunityWhilstFacingWall();
        break;
      case "LeaveCommunityFaceEnemy":
        selectedRoute = autonRoutes.leaveCommunityWhilstFacingEnemySide();
        break;
      case "justPlace":
        selectedRoute = autonRoutes.placeObject();
        break;
      case "balanceWhileFacingWall":

        ArrayDeque<AutonomousAction> balanceWallActions = new ArrayDeque<>();
        balanceWallActions
            .add(new AutonBalance(MovementDirection.BACKWARDS, robotGyroscope, motors));
        selectedRoute = balanceWallActions;
        break;
      case "balanceWhileFacingField":
        ArrayDeque<AutonomousAction> balanceFieldActions = new ArrayDeque<>();
        balanceFieldActions
            .add(new AutonBalance(MovementDirection.FORWARDS, robotGyroscope, motors));
        selectedRoute = balanceFieldActions;
        break;
      case "manualInCode":

        ArrayDeque<AutonomousAction> manualActions = new ArrayDeque<>();
        // manualActions.add(new AutonMoveForward(6, 2));
        // manualActions.add(new AutonArmCalibrate(true));
        // manualActions.add(new AutonMoveArm(ArmPosition.PLACING_MIDDLE));
        // manualActions.add(new AutonMoveGribber(GribberState.OPENING));
        // manualActions.add(new AutonMultiAction(new
        // AutonMoveArm(ArmPosition.FLIP_CONE),
        // new AutonMoveGribber(GribberState.CLOSING)));
        // manualActions.add(new AutonMoveForward(-3, 1));
        // manualActions.add(new AutonBalance(MovementDirection.BACKWARDS,
        // robotGyroscope));

        // manualActions.add(new AutonBalance(MovementDirection.BACKWARDS, robotGyroscope, motors));
        manualActions.add(new AutonMoveInches(MoveInchesDirection.BACKWARD, 36, motors));
        selectedRoute = manualActions;
        break;

      default:
        selectedRoute = autonRoutes.leaveCommunityWhilstFacingWall();
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
    // wtf???
    if (xboxController.getName().contains("Guitar")) {
      XboxController tmp = xboxController;
      xboxController = guitarXboxController;
      guitarXboxController = tmp;
    }

    // Turn Purple if Cube
    guitarControls.doEveryFrame();
    /*
     * if (guitarControls.lightColor == InputtedGuitarControls.LightColor.CUBE) {
     * Constants.LED_GREEN.set(true); Constants.LED_RED.set(true); Constants.LED_BLUE.set(false); //
     * Turn Yellow-ish Green if Cone } else if (guitarControls.lightColor ==
     * InputtedGuitarControls.LightColor.CONE) { Constants.LED_BLUE.set(true);
     * Constants.LED_RED.set(true); Constants.LED_GREEN.set(false); } else {
     * Constants.LED_BLUE.set(false); Constants.LED_RED.set(false); Constants.LED_GREEN.set(false);
     * }
     */

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

  public void disabledPeriodic() {
    Constants.BALANCING_GAINS.P =
        SmartDashboard.getNumber("Balance Proportion", Constants.BALANCING_GAINS.P);
    Constants.BALANCING_GAINS.I =
        SmartDashboard.getNumber("Balance Integral", Constants.BALANCING_GAINS.I);
    Constants.BALANCING_GAINS.D =
        SmartDashboard.getNumber("Balance Derivative", Constants.BALANCING_GAINS.D);
    Constants.BALANCING_MAX_RPM =
        SmartDashboard.getNumber("Max balancing RPM", Constants.BALANCING_MAX_RPM);


    Constants.BUMPERLESS_ROBOT_GAINS.P =
        SmartDashboard.getNumber("Proportion", Constants.BUMPERLESS_ROBOT_GAINS.P);
    Constants.BUMPERLESS_ROBOT_GAINS.I =
        SmartDashboard.getNumber("Integral", Constants.BUMPERLESS_ROBOT_GAINS.I);
    Constants.BUMPERLESS_ROBOT_GAINS.D =
        SmartDashboard.getNumber("Derivative", Constants.BUMPERLESS_ROBOT_GAINS.D);
    inches = SmartDashboard.getNumber("inches to move", inches);
    Constants.BUMPERLESS_ROBOT_GAINS.PEAK_OUTPUT =
        SmartDashboard.getNumber("Peak Output", Constants.BUMPERLESS_ROBOT_GAINS.PEAK_OUTPUT);
    Constants.ACCELERATION =
        SmartDashboard.getNumber("Max Auton Acceleration", Constants.ACCELERATION);
    Constants.ACTIVATE_PID_DELAY =
        SmartDashboard.getNumber("PID Starting Delay", Constants.ACTIVATE_PID_DELAY);
  }

  AutonMovement resetMovement = null;

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {

    if (testOptions.getSelected().equals("armCalibrate")
        || testOptions.getSelected().equals("armTransport")) {
      System.out.println("calibrating and resetting arm");
      ArrayDeque<AutonomousAction> resetQueue = new ArrayDeque<>();
      resetQueue.add(new AutonArmCalibrate(true, motors));
      if (testOptions.getSelected().equals("armCalibrate")) {
        resetQueue.add(new AutonMoveGribber(GribberState.OPENING, motors));
      }
      resetMovement = new AutonMovement(motors, resetQueue);
    } else if (testOptions.getSelected().equals("testMechanics")) {
      System.out.println("Testing mechanics, check console for test info.");
      MechanicsTest mechanicsTest = new MechanicsTest(motors);
      mechanicsTest.testMechanics();
    } else if (testOptions.getSelected().equals("movePIDManually")) {
      ArrayDeque<AutonomousAction> resetQueue = new ArrayDeque<>();
      resetQueue.add(new AutonMoveInches(MoveInchesDirection.FORWARD, inches, motors));
      resetMovement = new AutonMovement(motors, resetQueue);
    }
    // P - Proportional - How fast it approaches the target
    // I - Integral - Over time, how extra will it push based on how long it's been since we've been
    // close to the target
    // D - Derivative - How quickly we will slow down after we hit the target


  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if (resetMovement != null) {
      resetMovement.autonomousEveryFrame();
    }
    // AutonMoveInches.moveInches(autonMoveInchesDirection, inches, motors);
  }



  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {

  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}

  public void initializeMotionMagicMaster(MotorController motorController, Gains gains) {
    /* Factory default hardware to prevent unexpected behavior */
    motorController.reset();

    /* Configure Sensor Source for Primary PID */
    motorController.setSensorSource();

    /*
     * set deadband to super small 0.001 (0.1 %). The default deadband is 0.04 (4 %)
     */
    motorController.setNeutralDeadband(0.001);

    /**
     * Configure Talon SRX Output and Sensor direction accordingly Invert Motor to have green LEDs
     * when driving Talon Forward / Requesting Positive Output Phase sensor to have positive
     * increment when driving Talon Forward (Green LED)
     */

    /* Set relevant frame periods to be at least as fast as periodic rate */
    motorController.setStatusFramePeriod(10);

    /* Set the peak and nominal outputs */
    motorController.setOutputLimits(0, 0, gains.PEAK_OUTPUT, -gains.PEAK_OUTPUT);

    /* Set Motion Magic gains in slot0 - see documentation */
    motorController.setPID(gains);

    /* Set acceleration and vcruise velocity - see documentation */
    motorController.setMotionSpeed(15000, Constants.ACCELERATION);

    /* Zero the sensor once on robot boot up */
    motorController.setEncoderPosition(0);
  }


}
