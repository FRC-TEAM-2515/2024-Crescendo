package frc.robot;

import edu.wpi.first.math.util.Units;

public class Constants {
  // PCM
  public static final int kPCMId = 0;
  public static final int kIntakeSolenoidForwardId = 2;

  // DIO

  // Intake
  public static final int kIntakeMasterId = 14;

  // Elevator
  public static final int kElevatorPivotMotorId = 10;
  public static final int kElevatorExtensionMotorId = 9;

  // Intake
  public static final int kIntakeMotorId = 11;

  // Shooter
  public static final int kShooterLeftMotorId = 12;
  public static final int kShooterRightMotorId = 13;

  // Drivetrain
  public static final int kDrivetrainFLMotorId = 5;
  public static final int kDrivetrainBLMotorId = 6;
  public static final int kDrivetrainFRMotorId = 7;
  public static final int kDrivetrainBRMotorId = 8;

  // Pivot set points
  public static final double kPivotGroundCount = 0;
  public static final double kPivotScoreCount = -55;
  public static final double kPivotPreScoreCount = -65;
  public static final double kPivotStowCount = -120;

  // Extension set points
  public static final double kExtensionStowCount = 0;
  public static final double kExtensionMidGoalCount = 34;
  public static final double kExtensionHighGoalCount = 72;

  public static class Field {
    // All dimensions from Figure 5-16 in the manual
    // public static final double k_lowGoalX = 22.75; // Inches
    // public static final double k_lowGoalHeight = 34; // Inches

    // public static final double k_highGoalX = 39.75; // Inches
    // public static final double k_highGoalHeight = 46; // Inches

    public static final double k_width = Units.feetToMeters(54.0);
    public static final double k_length = Units.feetToMeters(27.0);
  }
}
