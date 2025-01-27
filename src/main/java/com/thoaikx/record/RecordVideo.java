package com.thoaikx.record;

import atu.testrecorder.ATUTestRecorder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordVideo {

    // ------Record with ATU library-----------
    public static ATUTestRecorder recorder;
    static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    static Date date = new Date();

    public static void startRecordATU(String videoName) throws Exception {
        recorder = new ATUTestRecorder("./test-recordings/", videoName + "-" + dateFormat.format(date), false);
        recorder.start();
    }

    public static void stopRecordATU() throws Exception {
        recorder.stop();
    }
}


