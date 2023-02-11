package frc.robot;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagPoseEstimator;
import edu.wpi.first.apriltag.AprilTagPoseEstimator.Config;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    private AprilTagDetector aprilDetector;
    private AprilTagDetection[] allAprilTags;
    private Thread m_visionThread;
    private HashMap<Integer, Integer> aprilConfidence;
    private HashMap<Integer, Boolean> hasDetectedLastFrame;
    private DecimalFormat df = new DecimalFormat("#.##");

    /** The margin that the center of an April tag can be, to be counted as centered */
    private final double ALIGNMENT_MARGIAN = 12;
    /** The acceptable difference between the verticles lines lengths on the AprilTags */
    private final double ROTATION_MARGIAN = 2;

    // private final double SLOW_MARGIAN;
    private final int CAMERA_WIDTH = 640; // Camera resolution Width
    private final int CAMERA_HEIGHT = 480; // Camera resolution Height

    public void start() {
        SmartDashboard.putString("Testing_Thingy", "Working!");
        SmartDashboard.updateValues();

        System.out.println("sysout test");
        m_visionThread = new Thread(() -> {
            System.out.println("Thead started");
            // Get the UsbCamera from CameraServer
            UsbCamera camera = CameraServer.startAutomaticCapture();

            // Set the resolution
            camera.setResolution(CAMERA_WIDTH, CAMERA_HEIGHT);

            // Get a CvSink. This will capture Mats from the camera
            CvSink cvSink = CameraServer.getVideo();
            // Setup a CvSource. This will send images back to the Dashboard
            CvSource outputStream = CameraServer.putVideo("Rectangle", CAMERA_WIDTH, CAMERA_HEIGHT);



            // Mats are very memory expensive. reusing it.
            Mat mat = new Mat();
            Mat greyMat = new Mat();
            Scalar red = new Scalar(0, 0, 255);
            Scalar green = new Scalar(0, 255, 0);
            Scalar yellow = new Scalar(0, 255, 255);

            double halfCameraWidth = CAMERA_WIDTH / 2; // Avoids re-doing math

            Config estimatorConfig = new Config(0.1524, 84, 61.9, 320, 240);
            AprilTagPoseEstimator estimator = new AprilTagPoseEstimator(estimatorConfig);
            Transform3d poseEstimate;

            aprilDetector = new AprilTagDetector();
            // aprilDetector.addFamily("tag16h5");
            aprilDetector.addFamily("tag16h5", 0);

            aprilConfidence = new HashMap<>();
            hasDetectedLastFrame = new HashMap<>();
            for (int i = 0; i < 29; i++) {
                aprilConfidence.put(i, 0);
                hasDetectedLastFrame.put(i, false);
            }

            // Get the nearest April tag, to hone in on
            /**
             * This is the closest AprilTag, we will now hone in on it. We will declare this later
             * on
             */
            int targetedAprilTag = -1;
            /**
             * This will count how many frames in a row the closest april tag is out of frames for,
             * if it reaches a threshold, it will find a new april tag.
             */
            int framesWithoutClosestTag = 0;
            // Example: If our team robot pushes us out of the way, then we will lose sight of the
            // april tag, and we won't be able to oriente towards it.
            // Later, we will find the new closest aprilTag if this happens


            // This cannot be 'true'. The program will never exit if it is. This
            // lets the robot stop this thread when restarting robot code or
            // deploying.
            System.out.println("Intia ltest");
            while (!Thread.interrupted()) {
                // Tell the CvSink to grab a frame from the camera and put it
                // in the source mat. If there is an error notify the output.
                if (cvSink.grabFrame(mat) == 0) {
                    // Send the output the error.
                    outputStream.notifyError(cvSink.getError());
                    // skip the rest of the current iteration
                    continue;
                }
                // System.out.println("Test 2");
                // aprilTag = aprilDetector.detect(mat)[0];
                Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_RGB2GRAY);

                allAprilTags = aprilDetector.detect(greyMat);
                if (allAprilTags == null) {
                    SmartDashboard.putNumber("Detected April Tags", 0);
                    System.out.println("Detected yep, there are " + allAprilTags.length);
                } else {
                    SmartDashboard.putNumber("Detected April Tags", allAprilTags.length);
                }

                Imgproc.circle(mat, new Point(0, 0), // Circle the center point of camera
                        8, red);

                // Draw lines on camera view to visually show if something is centered
                Imgproc.line(mat, new Point(halfCameraWidth - ALIGNMENT_MARGIAN, 0),
                        new Point(halfCameraWidth - ALIGNMENT_MARGIAN, CAMERA_HEIGHT), yellow);
                Imgproc.line(mat, new Point(halfCameraWidth + ALIGNMENT_MARGIAN, 0),
                        new Point(halfCameraWidth + ALIGNMENT_MARGIAN, CAMERA_HEIGHT), yellow);

                if (targetedAprilTag == -1 || framesWithoutClosestTag > 5) {
                    targetedAprilTag = getClosestAprilTag(aprilDetector.detect(greyMat));
                }

                // Increase the frames without april tag, if it actually is detected, it will be set
                // to 0 later
                framesWithoutClosestTag++;

                aprilTagLoop: for (AprilTagDetection loopAprilTag : allAprilTags) {
                    // System.out.println("There is an apriltag being looped!");
                    if (loopAprilTag != null) {
                        if (aprilConfidence.get(loopAprilTag.getId()) == null) {
                            // I don't want to continue the entire thread loop
                            continue aprilTagLoop;
                        }
                        if (aprilConfidence.get(loopAprilTag.getId()) == 0) {
                            // Low confidence
                            // System.out.println("AprilTag Detected! Confidence: LOW");

                            // Saying that: last frame, the april tag was detected as Low
                            // confidence
                            aprilConfidence.put(loopAprilTag.getId(), 1);
                            // Saying that the detected april tag was actualy found this frame
                            hasDetectedLastFrame.put(loopAprilTag.getId(), true);

                            // Draws the box, center point, and text on camera display, look below
                            // for more info
                            drawAprilTagOutlines(mat, loopAprilTag, red);
                        } else if (aprilConfidence.get(loopAprilTag.getId()) == 1) {
                            // Medium confidence
                            // System.out.println("AprilTag Detected! Confidence: MEDIUM");

                            // Saying that: last frame, the april tag was detected as Medium
                            // confidence
                            aprilConfidence.put(loopAprilTag.getId(), 2);
                            // Saying that the detected april tag was actualy found this frame
                            hasDetectedLastFrame.put(loopAprilTag.getId(), true);

                            // Draws the box, center point, and text on camera display, look below
                            // for more info
                            drawAprilTagOutlines(mat, loopAprilTag, yellow);

                        } else if (aprilConfidence.get(loopAprilTag.getId()) > 1) {
                            // High confidence
                            // System.out.println("AprilTag Detected! Confidence: HIGH");

                            // Saying that: last frame, the april tag was detected as High
                            // confidence
                            aprilConfidence.put(loopAprilTag.getId(), 3);
                            // Saying that the detected april tag was actualy found this frame
                            hasDetectedLastFrame.put(loopAprilTag.getId(), true);
                            // Adding the info to the SmartDashBoard
                            SmartDashboard.putString("April Tag " + loopAprilTag.getId(),
                                    // new String[]{
                                    // "Top Left Corner: "+loopAprilTag.getCornerX(0)+",
                                    // "+loopAprilTag.getCornerY(0),
                                    // "Top Right Corner: "+loopAprilTag.getCornerX(1)+",
                                    // "+loopAprilTag.getCornerY(1),
                                    // "Bottom Left Corner: "+loopAprilTag.getCornerX(2)+",
                                    // "+loopAprilTag.getCornerY(2),
                                    // "Bottom Right Corner: "+loopAprilTag.getCornerX(3)+",
                                    // "+loopAprilTag.getCornerY(3),
                                    "Center: " + df.format(loopAprilTag.getCenterX()) + ", "
                                            + df.format(loopAprilTag.getCenterY())
                                            + " AprilTag ID: " + loopAprilTag.getId()
                            // }
                            );
                            if (SmartDashboard.isPersistent("April Tag " + loopAprilTag.getId()))
                                SmartDashboard.clearPersistent("April Tag " + loopAprilTag.getId());
                            if (loopAprilTag.getId() == targetedAprilTag) {
                                // If the detected AprilTag is the one we are Targeting
                                framesWithoutClosestTag = 0;
                                // This draws the Target AprilTag in Purple
                                drawAprilTagOutlines(mat, loopAprilTag, new Scalar(194, 25, 166));
                                // System.out.println("Rotation: "+neededRotation(loopAprilTag));
                                SmartDashboard.putString("Needed Rotation",
                                        neededRotation(loopAprilTag));
                                SmartDashboard.putString("Needed Movement",
                                        neededMovement(loopAprilTag));

                                poseEstimate = estimator.estimate(loopAprilTag);
                                SmartDashboard.putNumber("X Rotation Deg",
                                        Math.toDegrees(poseEstimate.getRotation().getX()));
                                SmartDashboard.putNumber("Y Rotation Deg",
                                        Math.toDegrees(poseEstimate.getRotation().getY()));
                                SmartDashboard.putNumber("Z Rotation Deg",
                                        Math.toDegrees(poseEstimate.getRotation().getZ()));
                                SmartDashboard.putNumber("All Rotation Deg",
                                        Math.toDegrees(poseEstimate.getRotation().getAngle()));
                                SmartDashboard.putNumber("Alt X Deg",
                                        Math.toDegrees(poseEstimate.getX()));
                                SmartDashboard.putNumber("Alt Y Deg",
                                        Math.toDegrees(poseEstimate.getY()));
                                SmartDashboard.putNumber("Alt Z Deg",
                                        Math.toDegrees(poseEstimate.getZ()));
                            } else {
                                // An Un-targeted confident april tag is drawn in green
                                drawAprilTagOutlines(mat, loopAprilTag, green);
                            }
                        }
                    }
                }

                for (int i = 0; i < 29; i++) {
                    // If the loop april tag was not detected in the last frame
                    if (!hasDetectedLastFrame.get(i)) {
                        // Checking if it has a confidence value
                        if (aprilConfidence.get(i) > 1) {
                            // If it was, lower the confidence, as it wasn't detected this frame
                            aprilConfidence.put(i, (aprilConfidence.get(i) - 1));
                            // Checking if it has any confidence left
                            if (aprilConfidence.get(i) < 1)
                                // If not, remove it from the dashboard, as it's probably not in
                                // view anymore
                                SmartDashboard.putString("April Tag " + i, null);
                        }
                    }
                    hasDetectedLastFrame.put(i, false);// Reset the hasDetected
                }

                outputStream.putFrame(mat); // Give the output stream a new image to display
                SmartDashboard.updateValues();
                // System.out.println("Updaing values!");
            }
        });

        m_visionThread.setDaemon(true);
        m_visionThread.start();
    }

    private String neededRotation(AprilTagDetection tag) {
        // returns a string of the rotation needed to align the robot to the AprilTag
        double lineLengthDifference = (getLineLength(tag, 3, 0) - getLineLength(tag, 1, 2));
        if (lineLengthDifference >= -ROTATION_MARGIAN && lineLengthDifference <= ROTATION_MARGIAN) {
            // Can be replaced with 0 for a more machine readable format
            return "None";
        } else if (lineLengthDifference > 0) {
            // This can be replaced by a -1 for a more machine readable format
            return "Left";
        } else {
            // AKA. 1
            return "Right";
        }
    }

    private String neededMovement(AprilTagDetection tag) {
        // Returns a string with the required action needed to move the robot for alignment
        double CenterMinusTagCenterDifference = ((CAMERA_WIDTH / 2) - tag.getCenterX());
        if (CenterMinusTagCenterDifference >= -12
                && CenterMinusTagCenterDifference <= ALIGNMENT_MARGIAN) {
            // Can be replaced with 0 for a more machine readable format
            return "None";
        } else if (CenterMinusTagCenterDifference > 0) {
            // This can be replaced by a -1 for a more machine readable format
            return "Left";
        } else {
            // AKA. 1
            return "Right";
        }
    }

    /**
     * Gets the (probably) closest april tag to the camera. It accomplishes this by averaging the 2
     * verticle line length on the april tags.
     * 
     * @param tags All of the current april tags in frame
     * @return The ID of the closest apriltag
     */
    private int getClosestAprilTag(AprilTagDetection[] tags) {
        int closestAprilID = -1;
        double closestDistance = 0;
        double distanceAway;
        if (tags == null || tags.length < 1) {
            SmartDashboard.putNumber("Targeted April Tag", -1);
            return -1;
        }
        for (AprilTagDetection tag : tags) {
            // Averages both verticle line lengths. Bigger Number = Closer
            distanceAway = (getLineLength(tag, 3, 0) + getLineLength(tag, 1, 2)) / 2;
            if (distanceAway > closestDistance) {
                closestDistance = distanceAway;
                closestAprilID = tag.getId();
            }
        }
        SmartDashboard.putNumber("Targeted April Tag", closestAprilID);
        return closestAprilID;
    }


    private double getLineLength(AprilTagDetection tag, int corner1, int corner2) {
        // Basic pythagoras theorem for finding the Height of lines, is basicaly only helpful for
        // verticle lines
        return Math.sqrt( // Get the square root of:
                Math.pow( // Square the number
                          // Get the X difference between corner 1 and corner 2
                        (tag.getCornerX(corner1) - tag.getCornerX(corner2)), 2) +
                // Add squared Y differences and sqaured X differences together, like A^2 + b^2
                        Math.pow(// Square the number
                                 // Get the Y difference between corner 1 and corner 2
                                (tag.getCornerY(corner1) - tag.getCornerY(corner2)), 2));
    }

    private void drawAprilTagOutlines(Mat mat, AprilTagDetection tag, Scalar color) {

        // Imgproc.rectangle( //Draw the rectangle outlining the enture AprilTag
        // mat, new Point(tag.getCornerX(1), tag.getCornerY(1)),
        // new Point(tag.getCornerX(3), tag.getCornerX(3)), color,
        // 5);

        // SmartDashboard.putString("Tag "+tag.getId()+" Line Length 1", df.format(Math.sqrt(
        // Math.pow((tag.getCornerX(3) - tag.getCornerX(0)), 2) + Math.pow((tag.getCornerY(3) -
        // tag.getCornerY(0)), 2)) ));
        // SmartDashboard.putString("Tag "+tag.getId()+" Line Length 2", df.format(Math.sqrt(
        // Math.pow((tag.getCornerX(1) - tag.getCornerX(2)), 2) + Math.pow((tag.getCornerY(1) -
        // tag.getCornerY(2)), 2))));

        Imgproc.line(mat, new Point(tag.getCornerX(0), tag.getCornerY(0)),
                new Point(tag.getCornerX(1), tag.getCornerY(1)), color, 5);
        Imgproc.line(mat, new Point(tag.getCornerX(1), tag.getCornerY(1)),
                new Point(tag.getCornerX(2), tag.getCornerY(2)), new Scalar(255, 255, 255), 5);
        Imgproc.line(mat, new Point(tag.getCornerX(2), tag.getCornerY(2)),
                new Point(tag.getCornerX(3), tag.getCornerY(3)), color, 5);
        Imgproc.line(mat, new Point(tag.getCornerX(3), tag.getCornerY(3)),
                new Point(tag.getCornerX(0), tag.getCornerY(0)), new Scalar(255, 255, 255), 5);



        Imgproc.circle(mat, new Point(tag.getCenterX(), tag.getCenterY())// Circle the center point
                , 6, color);

        Imgproc.putText(mat, "ID: " + String.valueOf(tag.getId()),
                new Point(tag.getCornerX(2), tag.getCornerY(2)) // Place text by one of the corners
                                                                // with ID
                , Imgproc.FONT_HERSHEY_SIMPLEX, 2, color, 7);
    }

    /**
     * Foribly stops the vision thread completly
     */
    public void stop() {
        m_visionThread.interrupt();
    }
}
