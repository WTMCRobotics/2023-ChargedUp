// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
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


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);


    frontLeft = new Talon(0); //Assuming the motors are talons, if not, switch to Spark
    frontRight = new Talon(1);
    backLeft = new Talon(2);
    backRight = new Talon(3);
    xboxController = new XboxController(0);


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

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
 
    //Code written using math from https://seamonsters-2605.github.io/archive/mecanum/
    double XboxLeftStickY = xboxController.getLeftY();
    double XboxLeftStickX = xboxController.getLeftX();
    //This is amount that the robot will turn
    double turnAmount = xboxController.getRightX();
    //This is the direction that the robot will STRAFE
    double direction = Math.atan2(XboxLeftStickY, XboxLeftStickX); 
    //This will determine how fast we should strafe in that direction using the Pythagorean theorem
    double magnitude = Math.sqrt((Math.pow(XboxLeftStickX, 2)) + (Math.pow(XboxLeftStickY, 2)));

    //TODO: Explain this, figure out what it does later.
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




    /* CODE COPIED FROM INTERNET: (and changed to work with wpilib)
    double y = xboxController.getLeftY(); // Remember, this is reversed!
    double x = xboxController.getLeftX() * 1.1; // Counteract imperfect strafing
    double rx = xboxController.getRightX();

    // Denominator is the largest motor power (absolute value) or 1
    // This ensures all the powers maintain the same ratio, but only when
    // at least one is out of the range [-1, 1]
    double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
    double frontLeftPower = (y + x + rx) / denominator;
    double backLeftPower = (y - x + rx) / denominator;
    double frontRightPower = -((y - x - rx) / denominator);
    double backRightPower = -((y + x - rx) / denominator);

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
