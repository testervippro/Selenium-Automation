package com.thoaikx.utils;


import org.monte.media.FormatKeys;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.text.Format;

public class VideoRecorder {
    private ScreenRecorder screenRecorder;

    public void startRecording(String videoFilePath) throws Exception {
        File file = new File(videoFilePath);
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        screenRecorder = new ScreenRecorder(gc,
                new Format(FormatKeys.MediaTypeKey, FormatKeys.MediaType.VIDEO, FormatKeys.MimeTypeKey, "avi"),
                null, null, null, file);

        screenRecorder.start();
    }

    public void stopRecording() throws Exception {
        screenRecorder.stop();
    }
}