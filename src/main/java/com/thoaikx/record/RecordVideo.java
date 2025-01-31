package com.thoaikx.record;

import atu.testrecorder.ATUTestRecorder;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.thoaikx.config.ConfigurationManager.configuration;

public class RecordVideo {

    private static String monteVideoName;
    private static boolean isRecording = false;  // Flag to track recording state

    // ------Record with ATU Test Recorder-----------
    private static ATUTestRecorder atuRecorder;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");

    // ------Start recording with ATU Test Recorder------
    public static synchronized void startRecordATU(String videoName) {

        try {
            if (!"selenium-grid".equals(configuration().target())) {
                atuRecorder = new ATUTestRecorder("./videos",
                        videoName + "-" + dateFormat.format(new Date()), false);
                atuRecorder.start();
                isRecording = true; // Set the flag to true when recording starts
                System.out.println("ATU recording started for: "  + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error starting ATU Test Recorder recording", e);
        }
    }

    // ------Stop recording with ATU Test Recorder------
    public static synchronized void stopRecordATU() {

        try {
            if (!"selenium-grid".equals(configuration().target()) && atuRecorder != null) {
                try {
                    atuRecorder.stop();
                    isRecording = false; // Set the flag to false when recording stops
                } catch (NullPointerException e) {
                    System.out.println("Error stopping recording: " + e.getMessage());
                }
                // Cleanup the recorder instance
                System.out.println("ATU recording stopped." + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error stopping ATU Test Recorder recording", e);
        }
    }

    // ------Check if recording is running------
    public static boolean isRecording() {
        return isRecording; // Return the current state of the recording
    }
}
