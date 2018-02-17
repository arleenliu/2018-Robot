/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team670.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import paths.center.center_baseline;
import paths.center.center_left_switch_side;
import paths.center.center_left_switch_straight;
import paths.center.center_right_switch_side;
import paths.center.center_right_switch_straight;
import paths.left.left_baseline;
import paths.left.left_scale_opposite;
import paths.left.left_scale_side;
import paths.left.left_switch_side;
import paths.right.right_baseline;
import paths.right.right_scale_opposite;
import paths.right.right_scale_side;
import paths.right.right_switch_side;
import paths.right.right_switch_straight;

import org.usfirst.frc.team670.robot.commands.CancelCommand;
import org.usfirst.frc.team670.robot.commands.auto_specific.AutoCube;
import org.usfirst.frc.team670.robot.commands.auto_specific.Delay;
import org.usfirst.frc.team670.robot.commands.drive.Drive;
import org.usfirst.frc.team670.robot.commands.drive.Encoders_Drive;
import org.usfirst.frc.team670.robot.constants.RobotMap;
import org.usfirst.frc.team670.robot.subsystems.Aggregator;
import org.usfirst.frc.team670.robot.subsystems.Climber;
import org.usfirst.frc.team670.robot.subsystems.DriveBase;
import org.usfirst.frc.team670.robot.subsystems.Elevator;
import org.usfirst.frc.team670.robot.subsystems.Intake;

import com.ctre.phoenix.motorcontrol.SensorCollection;

/**
 * @author vsharma
 */

public class Robot extends TimedRobot {
	public static final double width = 33.25, length = 38; // LENGTH AND WIDTH WITH BUMPERS
	public static final Elevator elevator = new Elevator();
	public static final DriveBase driveBase = new DriveBase();
	public static final Intake intake = new Intake();
	public static final Climber climber = new Climber();
	public static PowerDistributionPanel pdp = new PowerDistributionPanel(RobotMap.pdp);

	public static Aggregator sensors;
	public static OI oi;
	
	Command m_autonomousCommand;
	private SendableChooser<Double> autonomousDelay, CubePickup;
	private SendableChooser<String> subMenuRR, subMenuLL, subMenuLR, subMenuRL;
	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		sensors = new Aggregator();
		
		subMenuRR = new SendableChooser<String>();
		subMenuLL = new SendableChooser<String>();
		subMenuRL = new SendableChooser<String>();
		subMenuLR = new SendableChooser<String>();
		autonomousDelay = new SendableChooser<Double>();
		CubePickup = new SendableChooser<Double>();

		autonomousDelay.addDefault("0 Second", 0.0);
		autonomousDelay.addObject("1 Second", 1.0);
		autonomousDelay.addObject("2 Second", 2.0);
		autonomousDelay.addObject("3 Second", 3.0);
		autonomousDelay.addObject("4 Second", 4.0);
		autonomousDelay.addObject("5 Second", 5.0);
		
		CubePickup.addDefault("No Cube Pickup", -1.0);
		CubePickup.addObject("Cube 1", 1.0);
		CubePickup.addObject("Cube 2", 2.0);
		CubePickup.addObject("Cube 3", 3.0);
		CubePickup.addObject("Cube 4", 4.0);
		CubePickup.addObject("Cube 5", 5.0);
		CubePickup.addObject("Cube 6", 6.0);
		
		subMenuLL.addDefault("LL (KEY ONLY)", "left_baseline");
		subMenuLL.addObject("----CENTER----", "left_baseline");
		subMenuLL.addObject("left_baseline", "left_baseline");
		subMenuLL.addObject("left_scale_opposite", "left_scale_opposite");
		subMenuLL.addObject("left_scale_side", "left_scale_side");
		subMenuLL.addObject("left_switch_side", "left_switch_side");
		subMenuLL.addObject("----LEFT----", "left_baseline");
		subMenuLL.addObject("center_baseline", "center_baseline");
		subMenuLL.addObject("center_left_switch_side", "center_left_switch_side");
		subMenuLL.addObject("center_left_switch_straight", "center_left_switch_straight");
		subMenuLL.addObject("center_right_switch_straight", "center_right_switch_straight");
		subMenuLL.addObject("center_right_switch_side", "center_right_switch_side");
		subMenuLL.addObject("----RIGHT----", "left_baseline");
		subMenuLL.addObject("right_baseline", "right_baseline");
		subMenuLL.addObject("right_scale_opposite", "right_scale_opposite");
		subMenuLL.addObject("right_scale_side", "right_scale_side");
		subMenuLL.addObject("right_switch_side", "right_switch_side");
		subMenuLL.addObject("right_switch_straight", "right_switch_straight");
		
		subMenuRR.addDefault("RR (KEY ONLY)", "left_baseline");
		subMenuRR.addObject("----CENTER----", "left_baseline");
		subMenuRR.addObject("left_baseline", "left_baseline");
		subMenuRR.addObject("left_scale_opposite", "left_scale_opposite");
		subMenuRR.addObject("left_scale_side", "left_scale_side");
		subMenuRR.addObject("left_switch_side", "left_switch_side");
		subMenuRR.addObject("----LEFT----", "left_baseline");
		subMenuRR.addObject("center_baseline", "center_baseline");
		subMenuRR.addObject("center_left_switch_side", "center_left_switch_side");
		subMenuRR.addObject("center_left_switch_straight", "center_left_switch_straight");
		subMenuRR.addObject("center_right_switch_straight", "center_right_switch_straight");
		subMenuRR.addObject("center_right_switch_side", "center_right_switch_side");
		subMenuRR.addObject("----RIGHT----", "left_baseline");
		subMenuRR.addObject("right_baseline", "right_baseline");
		subMenuRR.addObject("right_scale_opposite", "right_scale_opposite");
		subMenuRR.addObject("right_scale_side", "right_scale_side");
		subMenuRR.addObject("right_switch_side", "right_switch_side");
		subMenuRR.addObject("right_switch_straight", "right_switch_straight");
		
		subMenuLR.addDefault("LR (KEY ONLY)", "left_baseline");
		subMenuLR.addObject("----CENTER----", "left_baseline");
		subMenuLR.addObject("left_baseline", "left_baseline");
		subMenuLR.addObject("left_scale_opposite", "left_scale_opposite");
		subMenuLR.addObject("left_scale_side", "left_scale_side");
		subMenuLR.addObject("left_switch_side", "left_switch_side");
		subMenuLR.addObject("----LEFT----", "left_baseline");
		subMenuLR.addObject("center_baseline", "center_baseline");
		subMenuLR.addObject("center_left_switch_side", "center_left_switch_side");
		subMenuLR.addObject("center_left_switch_straight", "center_left_switch_straight");
		subMenuLR.addObject("center_right_switch_straight", "center_right_switch_straight");
		subMenuLR.addObject("center_right_switch_side", "center_right_switch_side");
		subMenuLR.addObject("----RIGHT----", "left_baseline");
		subMenuLR.addObject("right_baseline", "right_baseline");
		subMenuLR.addObject("right_scale_opposite", "right_scale_opposite");
		subMenuLR.addObject("right_scale_side", "right_scale_side");
		subMenuLR.addObject("right_switch_side", "right_switch_side");
		subMenuLR.addObject("right_switch_straight", "right_switch_straight");
		
		subMenuRL.addDefault("RL (KEY ONLY)", "left_baseline");
		subMenuRL.addObject("----CENTER----", "left_baseline");
		subMenuRL.addObject("left_baseline", "left_baseline");
		subMenuRL.addObject("left_scale_opposite", "left_scale_opposite");
		subMenuRL.addObject("left_scale_side", "left_scale_side");
		subMenuRL.addObject("left_switch_side", "left_switch_side");
		subMenuRL.addObject("----LEFT----", "left_baseline");
		subMenuRL.addObject("center_baseline", "center_baseline");
		subMenuRL.addObject("center_left_switch_side", "center_left_switch_side");
		subMenuRL.addObject("center_left_switch_straight", "center_left_switch_straight");
		subMenuRL.addObject("center_right_switch_straight", "center_right_switch_straight");
		subMenuRL.addObject("center_right_switch_side", "center_right_switch_side");
		subMenuRL.addObject("----RIGHT----", "left_baseline");
		subMenuRL.addObject("right_baseline", "right_baseline");
		subMenuRL.addObject("right_scale_opposite", "right_scale_opposite");
		subMenuRL.addObject("right_scale_side", "right_scale_side");
		subMenuRL.addObject("right_switch_side", "right_switch_side");
		subMenuRL.addObject("right_switch_straight", "right_switch_straight");
		

		SmartDashboard.putData("Auton Delay", autonomousDelay);
		SmartDashboard.putData("Cube Pickup", CubePickup);
		SmartDashboard.putData("LL", subMenuLL);
		SmartDashboard.putData("RR", subMenuRR);
		SmartDashboard.putData("LR", subMenuLR);
		SmartDashboard.putData("RL", subMenuRL);
	}
	
	public Command parseCommand(String str) {		
		switch(str.toLowerCase()){
			case "left_baseline":
					return new left_baseline();	
			case "left_scale_opposite":
				return new left_scale_opposite();
			case "left_scale_side":
				return new left_scale_side();
			case "left_switch_side":
				return new left_switch_side();
			case "center_baseline":
				return new center_baseline();
			case "center_left_switch_side":
				return new center_left_switch_side();
			case "center_left_switch_straight":
				return new center_left_switch_straight();
			case "center_right_switch_straight":
				return new center_right_switch_straight();
			case "center_right_swtich_side":
				return new center_right_switch_side();
			case "right_baseline":
				return new right_baseline();
			case "right_scale_opposite":
				return new right_scale_opposite();
			case "right_scale_side":
				return new right_scale_side();
			case "right_switch_side":
				return new right_switch_side();
			case "right_switch_straight":
				return new right_switch_straight();
			default: 
				return new left_baseline();		
		}
	}
	
	public Boolean isLeft(String str)
	{
		switch(str.toLowerCase()){
		case "left_baseline":
				return null;	
		case "left_scale_opposite":
			return false;
		case "left_scale_side":
			return true;
		case "left_switch_side":
			return true;
		case "center_baseline":
			return null;
		case "center_left_switch_side":
			return null;
		case "center_left_switch_straight":
			return null;
		case "center_right_switch_straight":
			return null;
		case "center_right_swtich_side":
			return null;
		case "right_baseline":
			return null;
		case "right_scale_opposite":
			return true;
		case "right_scale_side":
			return false;
		case "right_switch_side":
			return false;
		case "right_switch_straight":
			return false;
		default: 
			return null;		
	}	}
	
	
	
	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {	
		
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		String data = DriverStation.getInstance().getGameSpecificMessage(); 
		data = data.substring(0, 2); 
		
		String cmd = "";
		
		if(data.equalsIgnoreCase("RR")) 
			cmd = subMenuRR.getSelected();
		else if(data.equalsIgnoreCase("LL"))
			cmd = subMenuLL.getSelected();
		else if(data.equalsIgnoreCase("LR"))
			cmd = subMenuLR.getSelected();
		else if(data.equalsIgnoreCase("RL"))
			cmd = subMenuRL.getSelected();
		
		m_autonomousCommand = parseCommand(cmd);
		Boolean isL = isLeft(cmd);
		double selectedCube = CubePickup.getSelected();
		
		//RUN THE AUTONOMOUS COMMAND------------------------------
		
		CommandGroup combined = new CommandGroup(); 
		
		combined.addSequential(new Delay(autonomousDelay.getSelected())); 
		combined.addSequential(m_autonomousCommand);
		
		if(selectedCube!=-1.0 && isL != null)
			combined.addSequential(new AutoCube(isL, (int)selectedCube));
		
		if (combined != null)
			combined.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
