package frc.robot;

public class MoveInches {


    /**
     * move a distance in s straight line
     * 
     * @param direction Specifies which direction to move to robot in. possible options are FORWARD,
     *        BACKWARD, LEFT, and RIGHT
     * @param inches the distance in inches to move forward (use negative number to go backward)
     * @param motors the motors in which to move
     * @return true when done
     */
    public static boolean moveInches(MoveInchesDirection direction, double inches,
            RobotMotors motors) {
        if (direction == MoveInchesDirection.FORWARD) {
            motors.getFrontLeftMotor().setDistance(inches);
            motors.getBackLeftMotor().setDistance(inches);
            motors.getFrontRightMotor().setDistance(inches);
            motors.getBackRightMotor().setDistance(inches);
        } else if (direction == MoveInchesDirection.BACKWARD) {
            motors.getFrontLeftMotor().setDistance(-inches);
            motors.getBackLeftMotor().setDistance(-inches);
            motors.getFrontRightMotor().setDistance(-inches);
            motors.getBackRightMotor().setDistance(-inches);
        } else if (direction == MoveInchesDirection.LEFT) {
            motors.getFrontLeftMotor().setDistance(-inches);
            motors.getBackLeftMotor().setDistance(inches);
            motors.getFrontRightMotor().setDistance(inches);
            motors.getBackRightMotor().setDistance(-inches);
        } else if (direction == MoveInchesDirection.BACKWARD) {
            motors.getFrontLeftMotor().setDistance(inches);
            motors.getBackLeftMotor().setDistance(-inches);
            motors.getFrontRightMotor().setDistance(-inches);
            motors.getBackRightMotor().setDistance(inches);
        }

        return false;

    }

    public static enum MoveInchesDirection {
        FORWARD, BACKWARD, LEFT, RIGHT
    }


}
