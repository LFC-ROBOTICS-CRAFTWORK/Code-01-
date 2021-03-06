/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;




/**
 * This OpMode uses the common HardwareK9bot class to define the devices on the robot.
 * All device access is managed through the HardwareK9bot class. (See this class for device names)
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a basic Tank Drive Teleop for the K9 bot
 * It raises and lowers the arm using the Gampad Y and A buttons respectively.
 * It also opens and closes the claw slowly using the X and B buttons.
 *
 * Note: the configuration of the servos is such that
 * as the arm servo approaches 0, the arm position moves up (away from the floor).
 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="CraftworkTeleOp: Telop Control", group="Craftwork")

public class CraftworkTeleOp extends LinearOpMode {

    /* Declare OpMode members. */
    CraftworkHardware   robot           = new CraftworkHardware();              // Use a Craftwork'shardware
    
  
    
    
    
    double          armPosition     = robot.ARM_HOME;                   // Servo safe position
    double          clawPosition    = robot.CLAW_HOME;                  // Servo safe position
    final double    CLAW_SPEED      = 0.01 ;                            // sets rate to move servo
    final double    ARM_SPEED       = 0.01 ;                            // sets rate to move servo
    
    @Override
    public void runOpMode() {
        /*Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
         
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)

        while (opModeIsActive()) {

            controlWheels();
            controlArms();
            //controlClaws();

            telemetry.update();
        

            // Pause for 60 mS each cycle = update 45 times a second.
            idle();
        }
    }
    
    private void controlWheels() {
        // right trigger - move forward
        // left trigger - move backwards
        // left and right simultaneous negates each other
        float throttle = gamepad1.right_trigger - gamepad1.left_trigger;
        // left stck for changing directions
        float turn = gamepad1.left_stick_x;
        
        float left = (throttle != 0)? throttle + turn : 0.0f;
        float right = (throttle != 0)? throttle - turn : 0.0f;
        robot.leftDrive.setPower(Range.clip(left, -1.0, 1.0));
        robot.rightDrive.setPower(Range.clip(right, -1.0, 1.0));
        
        telemetry.addData("leftDrive",  "%.2f", left);
        telemetry.addData("rightDrive", "%.2f", right);
    }
    
    private void controlArms() {
        // Use gamepad Y & A raise and lower the arm
        if (gamepad1.a)
            armPosition += ARM_SPEED;
        else if (gamepad1.y)
            armPosition -= ARM_SPEED;
            
        armPosition  = Range.clip(armPosition, robot.ARM_MIN_RANGE, robot.ARM_MAX_RANGE);
        robot.arm.setPosition(armPosition);
        
        telemetry.addData("arm",   "%.2f", armPosition);
    }
    
    private void controlClaws() {
        // Use gamepad X & B to open and close the claw
        if (gamepad1.x)
            clawPosition += CLAW_SPEED;
        else if (gamepad1.b)
            clawPosition -= CLAW_SPEED;
        
        clawPosition = Range.clip(clawPosition, robot.CLAW_MIN_RANGE, robot.CLAW_MAX_RANGE);
        robot.claw.setPosition(clawPosition);
        
        telemetry.addData("claw",  "%.2f", clawPosition);
    }
}
