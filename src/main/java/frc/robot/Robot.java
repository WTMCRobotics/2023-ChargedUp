// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.motor.*;

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
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private MotorController frontLeft;
  private MotorController frontRight;
  private MotorController backLeft;
  private MotorController backRight;
  private XboxController xboxController;
  private Vision robotVision;

  private boolean slowMode = false;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);


    frontLeft = MotorControllerFactory.create(this, 30, MotorController.Type.Talon); //Assuming the motors are talons, if not, switch to Spark
    frontRight = MotorControllerFactory.create(this, 29, MotorController.Type.Talon);
    backLeft = MotorControllerFactory.create(this, 22, MotorController.Type.Talon);
    backRight = MotorControllerFactory.create(this, 32, MotorController.Type.Talon);
    xboxController = new XboxController(0);

    frontLeft.setBrakeMode(true);
    frontRight.setBrakeMode(true);
    backLeft.setBrakeMode(true);
    backRight.setBrakeMode(true);

    //This is needed for the mecanum math
    //backRight.setInverted(true);
    //frontRight.setInverted(true);

    robotVision = new Vision();
    robotVision.start();
    //robotVision.stop(); //This should stop the vision system

  }



  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  public double getMotorPower(double x, double y, double turnAmount, double scaleDownFactor, int motorId) {
    if (slowMode) {
      //If slow mode is enabled, increase the scale factor by 2, which will make it twice as slow
      scaleDownFactor *= 4;
    }
    switch (motorId) {
      case Constants.FRONT_LEFT_MOTOR_ID:
        return (y + x + turnAmount) / scaleDownFactor;
      case Constants.FRONT_RIGHT_MOTOR_ID:
        return -(y - x - turnAmount) / scaleDownFactor;
      case Constants.BACK_RIGHT_MOTOR_ID:
        return -((y + x - turnAmount) / scaleDownFactor);
      case Constants.BACK_LEFT_MOTOR_ID:
        return (y - x + turnAmount) / scaleDownFactor;
      default:
        return 0;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}


  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //These values can be anywhere from -1 to 1
    //The left joystick values will determine the direction that the robot will strafe (crabwalk), these values will not rotate the robot
    double leftXboxJoystickY = xboxController.getLeftY();
    double leftXboxJoystickX = xboxController.getLeftX(); //* 1.1 we might need to multiply by a bit more than one, if we want to counteract imperpefect strafing
    //This value dictates how much the robot should rotate, higher value = more rotation
    double turnAmount = xboxController.getRightX();
    //System.out.println("Right bumber is "+xboxController.getRightTriggerAxis());

    if (xboxController.getAButtonPressed()) {
      slowMode = !slowMode;
    }

    //This value determines how fast we should strafe, it is gotten by doing the pythagoras theorem
     /*                                                      . - Left Joystick Y value
     * The length of this line determins the strafe speed - /|
     *                                                    /  |
     *                         Left Joystick X value - ./____|. - Joystick center (0,0)     
     */
    //Unused for now, needs testing to implement
    double magnitude = Math.sqrt((Math.pow(leftXboxJoystickX, 2)) + (Math.pow(leftXboxJoystickY, 2)));


    /*
    The scale factor is the number we need to divide everything thing by to ensure all of our values are less than 1 while keeping the same ratio.
    For example, say we had motor powers of 0.9, 1.0, 1.1, and 1.2. The top 3 values would all be the same, as motor power can only go up 1.
    The scale factor factor makes sure that the values all keep the same ratio (in this example: 9:10:11:12).
    It does this by making the largest value equal to 1, and scaling the other values down proportionally
    */
    double scaleDownFactor = Math.max(Math.abs(leftXboxJoystickY) + Math.abs(leftXboxJoystickX) + Math.abs(turnAmount), 1);
    //to appease Colin:
    //double scaleFactorNeededToMultipleyeverythingByToEnsureThatEveryhingIsLessThanOneWhileStillMaintainingProportionalToEachOther = Math.max(Math.abs(leftXboxJoystickY) + Math.abs(leftXboxJoystickX) + Math.abs(turnAmount), 1);
    // double frontLeftPower = (leftXboxJoystickY + leftXboxJoystickX + turnAmount) / scaleDownFactor;
    // double backLeftPower = (leftXboxJoystickY - leftXboxJoystickX + turnAmount) / scaleDownFactor;
    // double frontRightPower = -((leftXboxJoystickY - leftXboxJoystickX - turnAmount) / scaleDownFactor);
    // double backRightPower = -((leftXboxJoystickY + leftXboxJoystickX - turnAmount) / scaleDownFactor);
    
    double frontLeftPower = getMotorPower(leftXboxJoystickX, leftXboxJoystickY, turnAmount, scaleDownFactor, Constants.FRONT_LEFT_MOTOR_ID);
    double frontRightPower = getMotorPower(leftXboxJoystickX, leftXboxJoystickY, turnAmount, scaleDownFactor, Constants.FRONT_RIGHT_MOTOR_ID);
    double backRightPower = getMotorPower(leftXboxJoystickX, leftXboxJoystickY, turnAmount, scaleDownFactor, Constants.BACK_RIGHT_MOTOR_ID);
    double backLeftPower = getMotorPower(leftXboxJoystickX, leftXboxJoystickY, turnAmount, scaleDownFactor, Constants.BACK_LEFT_MOTOR_ID);

    frontLeft.setPercentOutput(frontLeftPower);
    backLeft.setPercentOutput(backLeftPower);
    frontRight.setPercentOutput(frontRightPower);
    backRight.setPercentOutput(backRightPower);
    

    /*Self written code using math from website, doesn't really work 
    //Code written using math from https://seamonsters-2605.github.io/archive/mecanum/
    double XboxLeftStickY = xboxController.getLeftY();
    double XboxLeftStickX = xboxController.getLeftX();
    //This is amount that the robot will turn
    double turnAmount = xboxController.getRightX();
    //This is the direction that the robot will STRAFE
    double direction = Math.atan2(XboxLeftStickY, XboxLeftStickX); 
    //This will determine how fast we should strafe in that direction using the Pythagorean theorem
    double magnitude = Math.sqrt((Math.pow(XboxLeftStickX, 2)) + (Math.pow(XboxLeftStickY, 2)));

    //TOD: Explain this, figure out what it does later.
    //Basically a scale factor for the motor power, to make sure all values are under 100
    double thingToDivideBy = Math.max((Math.abs(XboxLeftStickX) + Math.abs(XboxLeftStickY) + Math.abs(turnAmount)), 1);

    //The motors can only go up to 100 power, so if magnitude was 1.2, it would tell the motors to move at 120% power, which isn't possible
    double frontRightPower = (Math.sin(direction-(1/4)*Math.PI) * magnitude + turnAmount) / thingToDivideBy;
    //Reversing the motor as it is needed for math
    double backLeftPower = -frontRightPower; 
    double backRightPower = (Math.sin(direction+(1/4)*(Math.PI)) * magnitude + turnAmount) / thingToDivideBy;
   //Reversing the motor as it is needed for math
    double frontLeftPower = -backLeftPower;

    frontLeft.set(frontLeftPower);
    backLeft.set(backLeftPower);
    frontRight.set(frontRightPower);
    backRight.set(backRightPower);
    */
    
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
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
