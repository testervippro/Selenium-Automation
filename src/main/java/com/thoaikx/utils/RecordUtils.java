package com.thoaikx.utils;



import atu.testrecorder.ATUTestRecorder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordUtils {

  public static ATUTestRecorder recorder;
  static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
  static Date date = new Date();

  static  String videoFolderPath = Path.of(System.getProperty("user.dir"), "video").toString();



  public static void startRecordATU(String videoName) throws Exception {
    Files.createDirectories(Path.of(videoFolderPath));
    recorder = new ATUTestRecorder(videoFolderPath, videoName + "-" + dateFormat.format(date), false);
    recorder.start();
  }

  public static void stopRecordATU() throws Exception {
    recorder.stop();
  }
}
