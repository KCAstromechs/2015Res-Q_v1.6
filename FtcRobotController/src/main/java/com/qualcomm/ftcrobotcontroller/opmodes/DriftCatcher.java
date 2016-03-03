package com.qualcomm.ftcrobotcontroller.opmodes;
/*
    monitors drift when robot at rest
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by Kevin on 12/6/2015.
 */
public class DriftCatcher  extends Thread {
    DriftCatcher(GyroSensor gyro, LinearOpMode callingOpMode) {

    }


    public void run(GyroSensor gyro, LinearOpMode callingOpMode, Boolean init) throws InterruptedException{
        int initial = gyro.getHeading();
        double timeElapsed = 0;
        while (init) {
            sleep(500);
            int current = gyro.getHeading();
            int driftChange = current - initial;
            timeElapsed = System.nanoTime()/1000000000; //convert to seconds by dividing by 1 billion
            double driftRate = timeElapsed / driftChange; //seconds per degree
            callingOpMode.telemetry.addData("Drift Rate", driftRate + " seconds per degree");
            callingOpMode.telemetry.addData("Time", timeElapsed);
            if(driftRate<7d){
                callingOpMode.telemetry.addData("Gyro", "Please reinitialize");
                break;
            }

        }

    }
}
