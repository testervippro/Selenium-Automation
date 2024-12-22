package com.thoaikx.ultis;

import atu.testrecorder.ATUTestRecorder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordUtils {

  public static ATUTestRecorder recorder;
  static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
  static Date date = new Date();

  static  String videoFolderPath = System.getProperty("user.dir") + File.separator+ "video";

  public static void startRecordATU(String videoName) throws Exception {
    Files.createDirectories(Path.of(videoFolderPath));
    recorder = new ATUTestRecorder(videoFolderPath, videoName + "-" + dateFormat.format(date), false);
    recorder.start();
  }

  public static void stopRecordATU() throws Exception {
    recorder.stop();
  }
}
