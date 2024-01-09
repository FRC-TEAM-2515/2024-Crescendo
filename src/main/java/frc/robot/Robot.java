// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controls.controllers.DriverController;
import frc.robot.controls.controllers.OperatorController;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Compressor;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Subsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Controller
  private final DriverController m_driverController = new DriverController(0, true, true);
  private final OperatorController m_operatorController = new OperatorController(1, true, true);

  private final SlewRateLimiter m_speedLimiter = new SlewRateLimiter(3); // 3 seconds to go from 0.0 to 1.0
  private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(3); // 3 seconds to go from 0.0 to 1.0

  // Robot subsystems
  private List<Subsystem> m_allSubsystems = new ArrayList<>();
  private final Intake m_intake = Intake.getInstance();
  private final Compressor m_compressor = Compressor.getInstance();
  private final Drivetrain m_drive = Drivetrain.getInstance();
  private final Shooter m_shooter = Shooter.getInstance();
  private final Climber m_climber = Climber.getInstance();

  /**
   * This function is run when the robot is first started up.
   */
  @Override
  public void robotInit() {
    m_allSubsystems.add(m_intake);
    m_allSubsystems.add(m_compressor);
    m_allSubsystems.add(m_drive);
    m_allSubsystems.add(m_shooter);
    m_allSubsystems.add(m_climber);
  }

  @Override
  public void robotPeriodic() {
    m_allSubsystems.forEach(subsystem -> subsystem.periodic());
    m_allSubsystems.forEach(subsystem -> subsystem.writePeriodicOutputs());
    m_allSubsystems.forEach(subsystem -> subsystem.outputTelemetry());
    m_allSubsystems.forEach(subsystem -> subsystem.writeToLog());
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  double speed = 0;

  @Override
  public void teleopPeriodic() {
    // Get the x speed. We are inverting this because Xbox controllers return
    // negative values when we push forward.
    double xSpeed = -m_speedLimiter.calculate(-m_driverController.getForwardAxis()) *
        Drivetrain.kMaxSpeed;

    // Get the rate of angular rotation. We are inverting this because we want a
    // positive value when we pull to the left (remember, CCW is positive in
    // mathematics). Xbox controllers return positive values when you pull to
    // the right by default.
    m_drive.slowMode(m_driverController.getWantsSlowMode());
    // m_drive.speedMode(m_driverController.getWantsSpeedMode());
    double rot = -m_rotLimiter.calculate(-m_driverController.getTurnAxis()) *
        Drivetrain.kMaxAngularSpeed;

    m_drive.drive(xSpeed, rot);

    // if (m_driverController.getShooterAxis() > 0.1) {
    // // m_shooter.setSpeed(m_driverController.getShooterAxis());
    // m_shooter.setSpeed(0.15);
    // } else {
    // m_shooter.stopShooter();
    // }

    // Shooter fixed speed
    // if (m_driverController.getRawButton(1)) {
    // m_shooter.setSpeed(0.10);
    // } else if (m_driverController.getRawButton(2)) {
    // m_shooter.setSpeed(0.15);
    // } else if (m_driverController.getRawButton(3)) {
    // m_shooter.setSpeed(0.20);
    // } else if (m_driverController.getRawButton(4)) {
    // m_shooter.setSpeed(0.80);
    // } else {
    // m_shooter.setSpeed(0);
    // }

    // Shooter variable speed
    if (m_driverController.getWantsMoreSpeed()) {
      speed += .01;
    } else if (m_driverController.getWantsLessSpeed()) {
      speed -= .01;
    } else if (m_driverController.getWantsShooterStop()) {
      speed = 0;
    }
    speed = MathUtil.clamp(speed, -1, 1);
    m_shooter.setSpeed(speed);

    // Intake
    if (m_driverController.getWantsFullIntake()) {
      m_intake.goToGround();
    } else if (m_driverController.getWantsIntake()) {
      if (m_intake.getIntakeHasNote()) {
        m_intake.pulse();
      } else {
        m_intake.intake();
      }
    } else if (m_driverController.getWantsEject()) {
      m_intake.eject();
    } else if (m_driverController.getWantsSource()) {
      m_intake.goToSource();
    } else if (m_driverController.getWantsStow()) {
      m_intake.goToStow();
    } else {
      m_intake.stopIntake();
    }

    // Climber
    if (m_operatorController.getWantsClimberClimb()) {
      m_climber.climb();
    } else if (m_operatorController.getWantsClimberRelease()) {
      m_climber.release();
    } else if (m_operatorController.getWantsClimberTiltLeft()) {
      m_climber.tiltLeft();
    } else if (m_operatorController.getWantsClimberTiltRight()) {
      m_climber.tiltRight();
    }
  }

  @Override
  public void disabledInit() {
    speed = 0;
    m_allSubsystems.forEach(subsystem -> subsystem.stop());
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void disabledExit() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
