/* Copyright (c) 2015 Qualcomm Technologies Inc

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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Base Drive code
 * Red side of field
 * far right corner
 */

public class RedPos1Camera extends LinearOpMode {

    AstroRobotBaseInterface robotBase;

    //Drive Constants
    private static final double klongDrive = 97;
    private static final double kClearWall = 5.5;
    private static final double kSlowApproach = 30;


    @Override
    public void runOpMode() throws InterruptedException {

        robotBase = new RobotBaseState(hardwareMap,this);
        robotBase.initializeServos();
        robotBase.calibrateGyro();
        telemetry.addData("Gyro", "Calibrated");
        robotBase.cameraSetup();
        telemetry.addData("Ready to run", "Gyro and Camera are Ready");


        waitForStart();

        robotBase.gyroResetZaxisIntegrator();

        robotBase.driveStraight(kClearWall, 1, 0, 1.0f); //clears wall
        robotBase.turn(325, 1.0f); //turns 45 degrees
        robotBase.driveStraight(klongDrive, 1, 315, 1.0f); // long drive down the field
        robotBase.turn(280, 1.0f); // turns towards safety beacon
        sleep(500);
        robotBase.snapPic();
        while(!robotBase.get_cameraProcessDone()){
            sleep(10);
        }
        telemetry.addData("Red Pixel Total:", robotBase.get_RedTotal());
        telemetry.addData("Blue Pixel Total:", robotBase.get_BlueTotal());
        if(robotBase.get_RedTotal()>20 && robotBase.get_BlueTotal()>robotBase.get_RedTotal()) {
            robotBase.setPushDown();
            if (robotBase.get_yBlueAvg() > robotBase.get_yRedAvg()) {
                System.out.println("Blue Side: " + "Left");
                System.out.println("Red Side: " + "Right");
                System.out.println("==>   Blue | Red      avgBlueY:" + robotBase.get_yBlueAvg() + " avgRedY:" + robotBase.get_yRedAvg());
                robotBase.driveStraight(kSlowApproach, 0.5, 274, 1.0f); //approaches safety beacon
            } else {
                System.out.println("Blue Side: " + "Right");
                System.out.println("Red Side: " + "Left");
                System.out.println("==>   Red | Blue      avgBlueY:" + robotBase.get_yBlueAvg() + " avgRedY:" + robotBase.get_yRedAvg());
                robotBase.driveStraight(kSlowApproach - 2.5, 0.5, 266, 1.0f); //approaches safety beacon
                robotBase.setDriveReverse();
                sleep(1000);
                robotBase.driveStraight(kSlowApproach - 2.5, 0.5, 266, 1.0f); //backs away from beacon
                robotBase.setDriveForward();
                robotBase.setPushUp();
                robotBase.turn(270, 1);
                robotBase.driveStraight(kSlowApproach, 0.5, 274, 1.0f); //approaches safety beacon
            }


            robotBase.hammerTime();

        } else {
            robotBase.driveStraight(kSlowApproach-6, 0.5, 270, 1.0f); //approaches safety beacon
        }

    }
}

//Trial 1: Normal: blue 78 red 22
//Trial 2: No Light Red Beacon: blue 0 red 0
//Trial 3: Covering Up Blue: blue 71 red 206
//Trial 4: Same as 3: blue 127 red 232
//Trial 5: Same as 3: blue 115 red 44
//Trial 6: Same as 3: blue 119 red 216
//Trial 7: Normal: blue 678 red 231
//Trial 8: No Light Blue Beacon: blue 0 red 0
//Trial 9: No Light Red Beacon: blue 0 red 0
//Trial 10: Reset During Picture Take: blue 10 red 0
//Trial 11: Yellow: blue 0 red 0

//CODE UPDATE WITH FIRST THRESHOLDS OF RED > 20 AND BLUE > RED

//Trial 1: No Light Blue Beacon: blue 0 red 0: Did correct thing: YAY!
//Trial 2: Normal: blue 659 red 235: Did correct thing: YAY!
//Trial 3: Normal: blue 732 red 262: Did correct thing: YAY!
//Trial 4: Offset gyro starting point: blur 872 red 260: Did correct thing: YAY!
//Trial 5: Normal: blue 1143 red 415: Did correct thing: YAY!