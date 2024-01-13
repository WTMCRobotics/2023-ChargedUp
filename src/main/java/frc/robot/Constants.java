package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public final class Constants {
        private Constants() {}

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
        public static final double ARM_MOVE_DOWN_SPEED = .7;
        /** The speed in which to move the robot while balancing, in m/s */
        public static final double ROBOT_SPEED_WHILE_BALANCING_BEFORE_CHARGE_STATION = 0.55;
        public static final double ROBOT_SPEED_WHILE_BALANCING_ON_CHARGE_STATION = .275;
        /**
         * The amount of time in seconds the robot has to be unbalanced for for it to change to it's
         * "unbalanced" state
         */
        public static final double BEING_UNBALANCED_DEBOUNCE_TIME = .5;

        public static final double BALANCING_ACTIVATE_PID_MARGIN_OF_ERROR = 3;
        /**
         * The amount of time in seconds the robot has to be balanced for for it to be considered
         * balanced
         */
        public static final double BALANCING_DEBOUNCE_TIME = .15;
        /** The margain of error, in degrees, while balancing */
        public static final double BALANCING_MARGIN_OF_ERROR = 8;
        /** The margain of error while on the platform, in degrees, while balancing */
        public static final double BALANCING_MARGIN_OF_ERROR_ON_STATION = 6;
        public static double BALANCING_MAX_RPM = 400; // Unless the calculated value is more than 1
        public static double ACTIVATE_PID_DELAY = 1;

        public static final float robotWidth = 32;
        /** the length of the robot in inches */
        public static final float robotLength = 38;

        /**
         * The postition in degrees from the starting position that the arm should go to for placing
         * cubes/cones on the top level
         */
        public static final double ARM_PLACE_TOP_POSITION = 97;
        /**
         * The postition in degrees from the starting position that the arm should go to for placing
         * cubes/cones on the middle level
         */
        public static final double ARM_PLACE_MIDDLE_POSITION = 60;
        /**
         * The postition in degrees from the starting position that the arm should go to intake
         * cubes
         */
        public static final double ARM_INTAKE_POSITION = 25;
        /**
         * The postition in degrees from the starting position that the arm should go to for
         * flipping a cone over
         */
        public static final double ARM_FLIP_CONE_POSITION = 26.6308;
        /**
         * The postition in degrees from the starting position that the arm should go to for picking
         * up objects
         */
        public static final double ARM_PICK_UP_POSITION = 0;

        /**
         * The position in degrees from the starting position that the arm should go to for picking
         * up objects
         */
        public static final double ARM_POSITION_BUFFER_DEGREES = 4;
        // ##########################################
        // Digital IO related constants
        // ##########################################


        public final static double WHEEL_CIRCUMFERENCE_INCHES = 8 * Math.PI;
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
        public static final double MARGIN_OF_ERROR_INCHES = 1;
        public static double ACCELERATION = 400;
        public static final Gains ARM_GAINS = new Gains(.025, 0.0001, 0.0, 0.0, 0, 1);
        public static final Gains BALANCING_GAINS = new Gains(.015, 0.00001, 0.0, 0.0, 0, .75);
        public static final Gains PRACTICE_BALANCING_GAINS =
                        new Gains(.0205, 0.00001, 0.0, 0.0, 0, .75);
        public static final Gains BUMPERLESS_ROBOT_GAINS = new Gains(0.75, 0, 0, 0, 0, 1);
        public static final Gains WITH_BUMPER_ROBOT_GAINS =
                        new Gains(0.35, 0.00001, 100, 0.2, 0, 1.0);
        public static final Gains BUMPERLESS_ROTATION_GAINS =
                        new Gains(0.004, 0.003, 0.001, 0.0, 0, 0.0);
        public static final Gains WITH_BUMPER_ROTATION_GAINS =
                        new Gains(0.06, 0.003, 0.001, 0.0, 0, 0.0);
        public static final Constraints ROTATIONAL_GAIN_CONSTRAINTS =
                        new Constraints(Double.POSITIVE_INFINITY, 20); // m/s

        // and
        // m/s^2
}
