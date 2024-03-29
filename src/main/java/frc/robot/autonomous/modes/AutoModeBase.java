package frc.robot.autonomous.modes;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.autonomous.tasks.Task;
import frc.robot.subsystems.Drivetrain;

public abstract class AutoModeBase {
  private ArrayList<Task> m_tasks;

  public AutoModeBase() {
    m_tasks = new ArrayList<>();

    // Reset the gyro and set the starting position
    Pose2d startingPosition = getStartingPosition();
    Drivetrain m_drive = Drivetrain.getInstance();
    // m_drive.setGyroAngleAdjustment(startingPosition.getRotation().getDegrees());
    m_drive.resetOdometry(startingPosition);
  }

  public Task getNextTask() {
    // Pop the first task off the list and return it
    try {
      return m_tasks.remove(0);
    } catch (IndexOutOfBoundsException ex) {
      return null;
    }
  }

  public void queueTask(Task task) {
    m_tasks.add(task);
  }

  public abstract void queueTasks();

  public abstract Pose2d getRedStartingPosition();

  private Pose2d getBlueStartingPosition() {
    Rotation2d blueStartingRotation = Rotation2d.fromDegrees(getRedStartingPosition().getRotation().getDegrees() - 180);

    Translation2d blueStartingTranslation = new Translation2d(
        Constants.Field.k_width - getRedStartingPosition().getX(),
        getRedStartingPosition().getY());

    return new Pose2d(blueStartingTranslation, blueStartingRotation);
  };

  private Pose2d getStartingPosition() {
    // TODO: Deal with this later
    DriverStation.Alliance alliance = DriverStation.getAlliance().get();

    if (alliance == DriverStation.Alliance.Blue) {
      return getBlueStartingPosition();
    } else {
      return getRedStartingPosition();
    }
  }

  // public static PathPlannerTrajectory
  // transformTrajectoryForAlliance(PathPlannerTrajectory trajectory) {
  // return trajectory;

  // if (DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
  // return trajectory;
  // }

  // // Flip the trajectory for blue
  // List<State> transformedStates = new ArrayList<>();

  // // for (State s : trajectory.getStates()) {
  // // State state = (PathPlannerState) s;

  // // transformedStates.add(transformStateForAlliance(state));
  // // }

  // return new PathPlannerTrajectory(
  // transformedStates,
  // trajectory.getMarkers(),
  // trajectory.getStartStopEvent(),
  // trajectory.getEndStopEvent(),
  // trajectory.fromGUI);
  // }

  // public static PathPlannerState transformStateForAlliance(PathPlannerState
  // state) {
  // // Create a new state so that we don't overwrite the original
  // PathPlannerState transformedState = new PathPlannerState();

  // Translation2d transformedTranslation = new Translation2d(
  // Constants.Field.k_width - state.poseMeters.getX(), state.poseMeters.getY());

  // Rotation2d transformedHeading =
  // Rotation2d.fromDegrees(state.poseMeters.getRotation().getDegrees() + 180);
  // Rotation2d transformedHolonomicRotation =
  // Rotation2d.fromDegrees(state.holonomicRotation.getDegrees() + 180);

  // transformedState.timeSeconds = state.timeSeconds;
  // transformedState.velocityMetersPerSecond = state.velocityMetersPerSecond;
  // transformedState.accelerationMetersPerSecondSq =
  // state.accelerationMetersPerSecondSq;
  // transformedState.poseMeters = new Pose2d(transformedTranslation,
  // transformedHeading);
  // transformedState.angularVelocityRadPerSec = -state.angularVelocityRadPerSec;
  // transformedState.holonomicRotation = transformedHolonomicRotation;
  // transformedState.holonomicAngularVelocityRadPerSec =
  // -state.holonomicAngularVelocityRadPerSec;
  // // transformedState.curveRadius = -state.curveRadius;
  // transformedState.curvatureRadPerMeter = -state.curvatureRadPerMeter;
  // // transformedState.deltaPos = state.deltaPos;

  // return transformedState;
  // }
}
