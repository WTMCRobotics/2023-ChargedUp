package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.motor.MotorController;
import frc.robot.motor.MotorController.Type;

public final class Constants {
        private Constants() {
        }

        public static final double MAX_ARM_UP_DEGREES = 108;

        public static final int FRONT_LEFT_MOTOR_ID = 20;
        public static final int FRONT_RIGHT_MOTOR_ID = 21;
        public static final int BACK_LEFT_MOTOR_ID = 22;
        public static final int BACK_RIGHT_MOTOR_ID = 23;
        // fix
        public static final int ARM_MOTOR_ID = 24;
        public static final int GRIBBER_MOTOR_ID = 25;

        public static final int bottomArmLimitSwitchID = 0;
        public static DigitalInput bottomArmLimitSwitch;

        public static Translation2d FRONT_LEFT_WHEEL_LOCATION = new Translation2d(10.75, 10.5);
        public static Translation2d FRONT_RIGHT_WHEEL_LOCATION = new Translation2d(10.75, -10.5);
        public static Translation2d BACK_LEFT_WHEEL_LOCATION = new Translation2d(-10.75, 10.5);
        public static Translation2d BACK_RIGHT_WHEEL_LOCATION = new Translation2d(-10.75, -10.5);

        // zero through one
        public static final double ARM_MOVE_UP_SPEED = 1;
        // zero through one
        public static final double ARM_MOVE_DOWN_SPEED = .6;
        /** The speed in which to move the robot while balancing, in m/s */
        public static final double ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION = 0.6;
        public static final double ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION = .275;
        /**
         * The amount of time in seconds the robot has to be unbalanced for for it to
         * change to it's
         * "unbalanced" state
         */
        public static final double BEING_UNBALANCED_DEBOUNCE_TIME = .5;
        /**
         * The amount of time in seconds the robot has to be balanced for for it to be
         * considered
         * balanced
         */
        public static final double BALANCING_DEBOUNCE_TIME = .15;
        /** The margain of error, in degrees, while balancing */
        public static final double BALANCING_MARGAIN_OF_ERROR = 1;
        /** The margain of error while on the platform, in degrees, while balancing */
        public static final double BALANCING_MARGAIN_OF_ERROR_ON_STATION = 5;

        public static final float robotWidth = 32;
        /** the length of the robot in inches */
        public static final float robotLength = 38;

        /**
         * The postition in degrees from the starting position that the arm should go to
         * for placing
         * cubes/cones on the top level
         */
        public static final double ARM_PLACE_TOP_POSITION = 97;
        /**
         * The postition in degrees from the starting position that the arm should go to
         * for placing
         * cubes/cones on the middle level
         */
        public static final double ARM_PLACE_MIDDLE_POSITION = 60;
        /**
         * The postition in degrees from the starting position that the arm should go to
         * intake cubes
         */
        public static final double ARM_INTAKE_POSITION = 25;
        /**
         * The postition in degrees from the starting position that the arm should go to
         * for
         * flipping a cone over
         */
        public static final double ARM_FLIP_CONE_POSITION = 10;
        /**
         * The postition in degrees from the starting position that the arm should go to
         * for picking
         * up objects
         */
        public static final double ARM_PICK_UP_POSITION = 0;

        /**
         * The postition in degrees from the starting position that the arm should go to
         * for picking
         * up objects
         */
        public static final double ARM_POSITION_BUFFER_DEGREES = 4;
        // ##########################################
        // Digital IO related constants
        // ##########################################

        // DIO IDs

        /** Pixy LEDs - red */
        public static final int LED_RED_ID = 0;
        /** Pixy LEDs - green */
        public static final int LED_GREEN_ID = 2;
        /** Pixy LEDs - blue */
        public static final int LED_BLUE_ID = 1;

        // Binary Sensors
        /** Pixy LEDs - red */
        public static final DigitalOutput LED_RED = new DigitalOutput(LED_RED_ID);
        /** Pixy LEDs - green */
        public static final DigitalOutput LED_GREEN = new DigitalOutput(LED_GREEN_ID);
        /** Pixy LEDs - blue */
        public static final DigitalOutput LED_BLUE = new DigitalOutput(LED_BLUE_ID);

        // ##########################################
        // talon related constants and variables
        // ##########################################

        // can bus IDs. Can be found in Phoenix Tuner
        public static final int LEFT_MASTER_ID = 25;
        public static final MotorController.Type LEFT_MASTER_TYPE = Type.Talon;
        public static final int LEFT_SLAVE_ID = 26;
        public static final MotorController.Type LEFT_SLAVE_TYPE = Type.Talon;
        public static final int RIGHT_MASTER_ID = 27;
        public static final MotorController.Type RIGHT_MASTER_TYPE = Type.Talon;
        public static final int RIGHT_SLAVE_ID = 28;
        public static final MotorController.Type RIGHT_SLAVE_TYPE = Type.Talon;
        public static final int INTAKE_ID = 8;
        public static final MotorController.Type INTAKE_TYPE = Type.SparkMax;
        public static final int LIFT_ID = 9;
        public static final MotorController.Type LIFT_TYPE = Type.SparkMax;
        public static final int TURRET_ROTATION_ID = 5;
        public static final MotorController.Type TURRET_ROTATION_TYPE = Type.SparkMax;
        public static final int TURRET_SHOOTER_ID = 4;
        public static final MotorController.Type TURRET_SHOOTER_TYPE = Type.SparkMax;
        public static final int TURRET_INTAKE_ID = 3;
        public static final MotorController.Type TURRET_INTAKE_TYPE = Type.SparkMax;
        public static final int TURRET_HOOD_ID = 2;
        public static final MotorController.Type TURRET_HOOD_TYPE = Type.SparkMax;

        /**
         * the number of ticks in a full rotation (Talon only - Spark stores it onboard)
         */
        public static final int encoderRotation = 4096;

        // talon config

        /** Which PID slot to pull gains from */
        public static final int SLOT_IDX = 0;
        /** Which PID loop to pull gains from */
        public static final int PID_LOOP_IDX = 0;
        /** amount of time in ms to wait for confirmation */
        public static final int TIMEOUT_MS = 30;

        // PID constants
        public static final Gains PRACTICE_ROBOT_GAINS = new Gains(0.2, 0.00035, 1.5, 0.2, 0, 1.0);
        public static final Gains COMPETITION_ROBOT_GAINS = new Gains(0.35, 0.00001, 100, 0.2, 0, 1.0);
        public static final Gains PRACTICE_ROTATION_GAINS = new Gains(0.004, 0.003, 0.001, 0.0, 0, 0.0);
        public static final Gains COMPETITION_ROTATION_GAINS = new Gains(0.06, 0.003, 0.001, 0.0, 0, 0.0);
        public static final Gains TURRET_ROTATION_GAINS = new Gains(0.002, 0, 0, 0, 0, 1.0);
        public static final Gains TURRET_HOOD_GAINS = new Gains(3, 0.005, 0.003, 0, 0, 1);
        public static final Constraints ROTATIONAL_GAIN_CONSTRAINTS = new Constraints(Double.POSITIVE_INFINITY, 20); // m/s
                                                                                                                     // and
                                                                                                                     // m/s^2

        // ##########################################
        // intake and popper related constants and variables
        // ##########################################

        /* the maximum number of balls that can be held */
        public static final int MAX_BALLS = 3;

        // the speed of the intake motor. Accepts values between 1 and -1.
        public static final double INTAKE_SPEED_IN = 1;
        public static final double INTAKE_SPEED_OUT = -1;

        // the speed of the turret intake motor. Accepts values between 0 and 1.
        public static final double TURRET_INTAKE_SPEED = 1;

        // the speed of the turret hood motor. Accepts values between 0 and 1.
        public static final double TURRET_HOOD_SPEED = 0.3;

        // the speed of the lift motor. Accepts values between 0 and 1.
        public static final double LIFT_SPEED = 0.75;

        /** the number of cycles that a ball interrupts the sensor for when passing */
        public static final int INTAKE_COUNTER_COUNT_TIME = 3;
        /** the number of cycles that constitutes a popper jam */
        public static final int POPPER_COUNTER_JAM_TIME = 20;

        public static final double TURRET_ROTATION_ANGLE = 9.33;
        public static final double TURRET_ROTATION_SPEED = 0.5;

        // Controller button IDs

        /** the mapping of the start button on a xbox controller */
        public static final int START = 7;
        /** the mapping of the select button on a xbox controller */
        public static final int SELECT = 8;
        /** the mapping of the A button on a xbox controller */
        public static final int A_BUTTON = 1;
        /** the mapping of the A button on a xbox controller */
        public static final int B_BUTTON = 2;
        /** the mapping of the A button on a xbox controller */
        public static final int X_BUTTON = 3;
        /** the mapping of the A button on a xbox controller */
        public static final int Y_BUTTON = 4;
        /** the mapping of the right shoulder on a xbox controller */
        public static final int R_STICK = 10;
        /** the mapping of the left shoulder on a xbox controller */
        public static final int L_STICK = 9;
        /** the mapping of the right shoulder on a xbox controller */
        public static final int R_SHOULDER = 6;
        /** the mapping of the left shoulder on a xbox controller */
        public static final int L_SHOULDER = 5;

        // ##########################################
        // Pneumatics related constants
        // ##########################################

        public static final int PCM_DRAWBRIDGE_IN = 1;
        public static final int PCM_DRAWBRIDGE_OUT = 0;
        public static final int PCM_RATCHET = 2;

        public static final String[] galacticSearchNames = { "Red A", "Blue A", "Red B", "Blue B" };
}
