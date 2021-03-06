/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

public class teleopState extends OpMode {

	AstroRobotBaseInterface robotBase;

	public teleopState() {

	}


	@Override
	public void init() {

		robotBase = new RobotBaseState(hardwareMap);
		robotBase.initializeServos();
		robotBase.setRightHookPosition(0);
		robotBase.setLeftHookPosition(1);

	}
	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left an right sticks
		 *
		 *
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);

		// write the values to the drive motors
		robotBase.setRightPower(right);
		robotBase.setLeftPower(left);

		//change position of hooks on front
		//1st driver
		if(gamepad1.a) { //down
			robotBase.setRightHookDown();
			robotBase.setLeftHookDown();
			//robotBase.setLeftHookPosition(0.1);
			//robotBase.setRightHookPosition(0.9);
		}
		if(gamepad1.b){ // up
			robotBase.setRightHookUp();
			robotBase.setLeftHookUp();
			//robotBase.setLeftHookPosition(1);
			//robotBase.setRightHookPosition(0);
		}
		//second Driver
		if(gamepad2.a) { //down
			robotBase.setRightHookDown();
			robotBase.setLeftHookDown();
			//robotBase.setLeftHookPosition(0.1);
			//robotBase.setRightHookPosition(0.9);
		}
		if(gamepad2.b){ // up
			robotBase.setRightHookUp();
			robotBase.setLeftHookUp();
			//robotBase.setLeftHookPosition(1);
			//robotBase.setRightHookPosition(0);
		}


		//gamepad2
		float right2 = gamepad2.right_stick_y;
		float left2 = gamepad2.left_stick_y;

		//deadzones for secondary driver
		if (Math.abs(left2)<0.1){
			left2=0.0f;
		}
		if (Math.abs(right2)<0.1){
			right2=0;
		}

		if (gamepad2.right_bumper){
			right2 /= 5.0;
		}

		if (gamepad2.left_bumper){
			right2 /= 5.0;
		}

		if(gamepad2.x){
			robotBase.setMjolnirDown();
		}

		if(gamepad2.y){
			robotBase.setMjolnirUp();
		}

		if(gamepad1.left_bumper){
			robotBase.setLeftZiplineUp();
		}

		if(gamepad1.right_bumper){
			robotBase.setRightZiplineUp();
		}

		if(gamepad1.left_trigger>0.75){
			robotBase.setLeftZiplineDown();
		}

		if(gamepad1.right_trigger>0.75) {
			robotBase.setRightZiplineDown();
		}

		if(gamepad1.dpad_down){
			robotBase.updateDrawerSlide(0.75f);
		} else if (gamepad1.dpad_up) {
			robotBase.updateDrawerSlide(-0.75f);
		} else {
			robotBase.updateDrawerSlide(left2);
		}

		robotBase.updateWinch(-right2);

	}

	@Override
	public void stop() {

	}
	
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);

		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}

		// return scaled value.
		return dScale;
	}

}
