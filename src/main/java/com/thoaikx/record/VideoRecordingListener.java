package com.thoaikx.record;

import org.testng.ITestContext;
import org.testng.ITestListener;

public class VideoRecordingListener implements ITestListener {

    // ThreadLocal to ensure each thread has its own instance of RecordVideo
    private ThreadLocal<RecordVideo> videoRecorderThreadLocal = ThreadLocal.withInitial(RecordVideo::new);

    @Override
    public void onStart(ITestContext context) {
        // Retrieve the browser name and create a unique video name for each browser
        String browserName = context.getCurrentXmlTest().getParameter("browser");
        if (browserName == null) {
            browserName = "unknown_browser";
        }

        // Start the recording in a separate thread for the specific browser
       RecordVideo.startRecordATU(browserName);
    }

    @Override
    public void onFinish(ITestContext context) {
        // Ensure that video recording stops after all tests are finished
        try {
            // Retrieve the thread-local instance of RecordVideo
            RecordVideo videoRecorder = videoRecorderThreadLocal.get();

            // Stop recording only if it's actively recording
            if (videoRecorder.isRecording()) {
                videoRecorder.stopRecordATU();  // Stop recording after all tests are finished for all browsers
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cleanup thread-local reference after test completion
            videoRecorderThreadLocal.remove();
        }
    }
}
