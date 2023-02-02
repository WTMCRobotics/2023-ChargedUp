package frc.robot;

import java.text.DecimalFormat;
import java.util.HashMap;

import javax.xml.crypto.dsig.Transform;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagPoseEstimate;
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
    private AprilTagDetection[]  allAprilTags;
    private Thread m_visionThread;
    private HashMap<Integer, Integer> aprilConfidence;
    private HashMap<Integer, Boolean> hasDetectedLastFrame;
    private DecimalFormat df = new DecimalFormat("#.##");

    private final double ALIGNMENT_MARGIAN = 12;//The margian that the center of an April tag can be, to be counted as centerd
    private final double ROTATION_MARGIAN = 2;//The acceptable difference between the verticles lines lengths on the AprilTags
    //private final double SLOW_MARGIAN;
    private final int CAMERA_WIDTH = 640; //Camera resolution Width
    private final int CAMERA_HEIGHT = 480; //Camera resolution Height
    
    public void start() {
        SmartDashboard.putString("Testing_Thingy", "Working!");
    SmartDashboard.updateValues();

    System.out.println("sysout test");
    m_visionThread =
        new Thread(
            () -> {
              System.out.println("Thead started");
              // Get the UsbCamera from CameraServer
              UsbCamera camera = CameraServer.startAutomaticCapture();
            
              // Set the resolution
              camera.setResolution(CAMERA_WIDTH, CAMERA_HEIGHT);

              // Get a CvSink. This will capture Mats from the camera
              CvSink cvSink = CameraServer.getVideo();
              // Setup a CvSource. This will send images back to the Dashboard
              CvSource outputStream = CameraServer.putVideo("Rectangle", CAMERA_WIDTH, CAMERA_HEIGHT);

          

              // Mats are very memory expensive. Lets reuse this Mat.
              Mat mat = new Mat();
              Mat greyMat = new Mat();
              Scalar red = new Scalar(0, 0, 255);
              Scalar green = new Scalar(0, 255, 0);
              Scalar yellow = new Scalar(0, 255, 255);

              double halfCameraWidth = CAMERA_WIDTH / 2; //Avoids re-doing math
              
              Config estimatorConfig = new Config(0.1524, 84, 61.9, 320, 240);
              AprilTagPoseEstimator estimator = new AprilTagPoseEstimator(estimatorConfig);
              Transform3d poseEstimate;

              aprilDetector = new AprilTagDetector();
              //aprilDetector.addFamily("tag16h5");
              aprilDetector.addFamily("tag16h5", 0);

              aprilConfidence = new HashMap<>();
              hasDetectedLastFrame = new HashMap<>();
              for (int i = 0; i < 29; i ++) {
                aprilConfidence.put(i, 0);
                hasDetectedLastFrame.put(i, false);
              }

              //Get the nearest April tag, to hone in on
              int targetedAprilTag = -1; //This is the closest AprilTag, we will now hone in on it. We will declare this later on
              int framesWithoutClosestTag = 0; //This will count how many frames in a row the closest april tag is out of frames for, if it reaches a threshold, it will find a new april tag.
              //Example: If our team robot pushes us out of the way, then we will lose sight of the april tag, and we won't be able to oriente towards it. 
              //Later, we will find the new closest aprilTag if this happens

              
              // This cannot be 'true'. The program will never exit if it is. This
              // lets the robot stop this thread when restarting robot code or
              // deploying.
              System.out.println("Intia ltest");
              while (!Thread.interrupted()) {
                // Tell the CvSink to grab a frame from the camera and put it
                // in the source mat.  If there is an error notify the output.
                if (cvSink.grabFrame(mat) == 0) {
                  // Send the output the error.
                  outputStream.notifyError(cvSink.getError());
                  // skip the rest of the current iteration
                  continue;
                }
                //System.out.println("Test 2");
                //aprilTag = aprilDetector.detect(mat)[0];
                Imgproc.cvtColor(mat, greyMat, Imgproc.COLOR_RGB2GRAY);

                allAprilTags = aprilDetector.detect(greyMat);
                if (allAprilTags == null)  {
                  SmartDashboard.putNumber("Detected April Tags", 0);
                  System.out.println("Detected yep, there are "+allAprilTags.length);
                } else
                  SmartDashboard.putNumber("Detected April Tags", allAprilTags.length);

                Imgproc.circle(mat, 
                new Point(0, 0),//Circle the center point
                8, red);

                Imgproc.line(mat, new Point(halfCameraWidth - ALIGNMENT_MARGIAN, 0), new Point(halfCameraWidth - ALIGNMENT_MARGIAN, CAMERA_HEIGHT), yellow);
                Imgproc.line(mat, new Point(halfCameraWidth + ALIGNMENT_MARGIAN, 0), new Point(halfCameraWidth + ALIGNMENT_MARGIAN, CAMERA_HEIGHT), yellow);
                // Put a rectangle Around the AprilTag
                
                if (targetedAprilTag == -1 || framesWithoutClosestTag > 5) {
                  targetedAprilTag = getClosestAprilTag(aprilDetector.detect(greyMat));
                }
                
                framesWithoutClosestTag++; //Increase the frames without april tag, if it actually is detected, it will be set to 0 later
                
                aprilTagLoop:
                for (AprilTagDetection loopAprilTag : allAprilTags) {
                  //System.out.println("There is an apriltag being looped!");
                  if (loopAprilTag != null) {
                    if (aprilConfidence.get(loopAprilTag.getId()) == null) {
                        continue aprilTagLoop; //I don't want to continue the entire thread loop
                    }
                    if (aprilConfidence.get(loopAprilTag.getId()) == 0) {//Low confidence
                      //System.out.println("AprilTag Detected! Confidence: LOW");

                      aprilConfidence.put(loopAprilTag.getId(), 1); //Saying that: last frame, the april tag was detected as Low confidence
                      hasDetectedLastFrame.put(loopAprilTag.getId(), true);//Saying that the detected april tag was actualy found this frame

                      drawAprilTagOutlines(mat, loopAprilTag, red);//Draws the box, center point, and text on camera display, look below for more info
                    } else if (aprilConfidence.get(loopAprilTag.getId()) == 1) {//Medium confidence
                      //System.out.println("AprilTag Detected! Confidence: MEDIUM");

                      aprilConfidence.put(loopAprilTag.getId(), 2); //Saying that: last frame, the april tag was detected as Medium confidence
                      hasDetectedLastFrame.put(loopAprilTag.getId(), true);//Saying that the detected april tag was actualy found this frame
                    
                      drawAprilTagOutlines(mat, loopAprilTag, yellow);//Draws the box, center point, and text on camera display, look below for more info
                    
                    } else if (aprilConfidence.get(loopAprilTag.getId()) > 1) {//High confidence
                      //System.out.println("AprilTag Detected! Confidence: HIGH");


                      aprilConfidence.put(loopAprilTag.getId(), 3); //Saying that: last frame, the april tag was detected as High confidence
                      hasDetectedLastFrame.put(loopAprilTag.getId(), true); //Saying that the detected april tag was actualy found this frame
                      SmartDashboard.putString("April Tag "+loopAprilTag.getId(), //Adding the info to the SmartDashBoard
                      //new String[]{
                        //"Top Left Corner: "+loopAprilTag.getCornerX(0)+", "+loopAprilTag.getCornerY(0),
                        //"Top Right Corner: "+loopAprilTag.getCornerX(1)+", "+loopAprilTag.getCornerY(1),
                        //"Bottom Left Corner: "+loopAprilTag.getCornerX(2)+", "+loopAprilTag.getCornerY(2),
                        //"Bottom Right Corner: "+loopAprilTag.getCornerX(3)+", "+loopAprilTag.getCornerY(3),
                        "Center: "+df.format(loopAprilTag.getCenterX())+", "+df.format(loopAprilTag.getCenterY())+
                        " AprilTag ID: "+loopAprilTag.getId()
                        //}
                      );
                      if (SmartDashboard.isPersistent("April Tag "+loopAprilTag.getId()))
                        SmartDashboard.clearPersistent("April Tag "+loopAprilTag.getId());
                      if (loopAprilTag.getId() == targetedAprilTag) { //If the detected AprilTag is the one we are Targeting
                        framesWithoutClosestTag = 0;
                        drawAprilTagOutlines(mat, loopAprilTag, new Scalar(194, 25, 166));//This draws the Target AprilTag in Purple
                        //System.out.println("Rotation: "+neededRotation(loopAprilTag));
                        SmartDashboard.putString("Needed Rotation", neededRotation(loopAprilTag));
                        SmartDashboard.putString("Needed Movement", neededMovement(loopAprilTag));

                        poseEstimate = estimator.estimate(loopAprilTag);
                        SmartDashboard.putNumber("X Rotation Deg", Math.toDegrees(poseEstimate.getRotation().getX()));
                        SmartDashboard.putNumber("Y Rotation Deg", Math.toDegrees(poseEstimate.getRotation().getY()));
                        SmartDashboard.putNumber("Z Rotation Deg", Math.toDegrees(poseEstimate.getRotation().getZ()));
                        SmartDashboard.putNumber("All Rotation Deg", Math.toDegrees(poseEstimate.getRotation().getAngle()));
                        SmartDashboard.putNumber("Alt X Deg", Math.toDegrees(poseEstimate.getX()));
                        SmartDashboard.putNumber("Alt Y Deg", Math.toDegrees(poseEstimate.getY()));
                        SmartDashboard.putNumber("Alt Z Deg", Math.toDegrees(poseEstimate.getZ()));
                      } else {
                        drawAprilTagOutlines(mat, loopAprilTag, green);//An Un-targeted confident april tag is drawn in green
                      }
                    }
                  }
                }

                for (int i = 0; i < 29; i ++) {
                  if (!hasDetectedLastFrame.get(i)) //If the loop april tag was not detected in the last frame
                    if (aprilConfidence.get(i) > 1) { //Checking if it has a confidence value
                      aprilConfidence.put(i, (aprilConfidence.get(i) - 1)); //If it was, lower the confidence, as it wasn't detected this frame
                      if (aprilConfidence.get(i) < 1) //Checking if it has any confidence left
                        SmartDashboard.putString("April Tag "+i, null); //If not, remove it from the dashboard, as it's probably not in view anymore
                      }
                  hasDetectedLastFrame.put(i, false);//Reset the hasDetected
                }

                outputStream.putFrame(mat); // Give the output stream a new image to display
                SmartDashboard.updateValues();
                //System.out.println("Updaing values!");
              }
            });

    m_visionThread.setDaemon(true);
    m_visionThread.start();
  }

  private String neededRotation(AprilTagDetection tag) { //returns a string of the rotation needed to align the robot to the AprilTag
    double lineLengthDifference = (getLineLength(tag, 3, 0) - getLineLength(tag, 1, 2));
    if (lineLengthDifference >= -ROTATION_MARGIAN && lineLengthDifference <= ROTATION_MARGIAN) {
      return "None"; //Can be replaced with 0 for a more machine readable format
    } else if (lineLengthDifference > 0) {
      return "Left";// This can be replaced by a -1 for a more machine readable for, also, I have no idea if this is left or right, just a placeholder
    } else {
      return "Right"; //AKA. 1, this might also be wrong becaise at the time of writing, I can't test whether it is left or right
    }
  }

  private String neededMovement(AprilTagDetection tag) { //Returns a string with the required action needed to move the robot for alignment
    double CenterMinusTagCenterDifference = ((CAMERA_WIDTH / 2) - tag.getCenterX());
    if (CenterMinusTagCenterDifference >= -12 && CenterMinusTagCenterDifference <= ALIGNMENT_MARGIAN) {
      return "None"; //Can be replaced with 0 for a more machine readable format
    } else if (CenterMinusTagCenterDifference > 0) {
      return "Left";// This can be replaced by a -1 for a more machine readable for, also, I have no idea if this is left or right, just a placeholder
    } else {
      return "Right"; //AKA. 1, this might also be wrong
    }
  }

  private int getClosestAprilTag(AprilTagDetection[] tags) {
    int closestAprilID = -1;
    double closestDistance = 0;
    double distanceAway;
    if (tags == null || tags.length < 1) {
      SmartDashboard.putNumber("Targeted April Tag", -1);
      return -1;
    }
    for (AprilTagDetection tag: tags) {
      distanceAway = (getLineLength(tag, 3, 0) + getLineLength(tag, 1, 2)) / 2; //Averages both verticle line lengths. Bigger Number = Closer
      if (distanceAway > closestDistance) {
        closestDistance = distanceAway;
        closestAprilID = tag.getId();
      }
    }
    SmartDashboard.putNumber("Targeted April Tag", closestAprilID);
    return closestAprilID;
  }

  private double getLineLength(AprilTagDetection tag, int corner1, int corner2){                            //w    __
    //Basic pythagoras theorem for finding the Height of lines, is basccily only helpful for verticle lines | |✅  __ ❌
    return Math.sqrt( //Get the square root of:
      Math.pow( //Square the number
        (tag.getCornerX(corner1) - tag.getCornerX(corner2)), 2) //Get the X difference between corner 1 and corner 2
        + //Add squared Y differences and sqaured X differences together, like A^2 + b^2
        Math.pow(//Square the number
          (tag.getCornerY(corner1) - tag.getCornerY(corner2)) //Get the Y difference between corner 1 and corner 2
        , 2));
  }

  private void drawAprilTagOutlines(Mat mat, AprilTagDetection tag, Scalar color) {

    /*Imgproc.rectangle( //Draw the rectangle outlining the enture AprilTag
      mat, new Point(tag.getCornerX(1), tag.getCornerY(1)),
      new Point(tag.getCornerX(3), tag.getCornerX(3)), color,
      5);*/

      //SmartDashboard.putString("Tag "+tag.getId()+" Line Length 1", df.format(Math.sqrt( Math.pow((tag.getCornerX(3) - tag.getCornerX(0)), 2) + Math.pow((tag.getCornerY(3) - tag.getCornerY(0)), 2)) ));
      //SmartDashboard.putString("Tag "+tag.getId()+" Line Length 2", df.format(Math.sqrt( Math.pow((tag.getCornerX(1) - tag.getCornerX(2)), 2) + Math.pow((tag.getCornerY(1) - tag.getCornerY(2)), 2))));

      Imgproc.line(mat, 
      new Point(tag.getCornerX(0), tag.getCornerY(0)), 
      new Point(tag.getCornerX(1), tag.getCornerY(1))
      , color, 5);
      Imgproc.line(mat, 
      new Point(tag.getCornerX(1), tag.getCornerY(1)), 
      new Point(tag.getCornerX(2), tag.getCornerY(2))
      , new Scalar(255, 255, 255), 5);
      Imgproc.line(mat, 
      new Point(tag.getCornerX(2), tag.getCornerY(2)), 
      new Point(tag.getCornerX(3), tag.getCornerY(3))
      , color, 5);
      Imgproc.line(mat, 
      new Point(tag.getCornerX(3), tag.getCornerY(3)), 
      new Point(tag.getCornerX(0), tag.getCornerY(0))
      , new Scalar(255, 255, 255), 5);

      

    Imgproc.circle(mat, 
    new Point(tag.getCenterX(), tag.getCenterY())//Circle the center point
    , 6, color);

    Imgproc.putText(mat, "ID: "+String.valueOf(tag.getId()), 
    new Point(tag.getCornerX(2), tag.getCornerY(2)) //Place text by one of the corners with ID
    , Imgproc.FONT_HERSHEY_SIMPLEX, 2,
           color, 7);
    }

    public void stop() {
        m_visionThread.interrupt();
    }
}
