package com.thoaikx.record;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.thoaikx.data.changeless.BrowserData;
import com.video.ExtractLibFromJar;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FileUtils;
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;
//import com.video.ExtractLibFromJar;

import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static com.sun.jna.Platform.isLinux;
import static com.sun.jna.Platform.isMac;
import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;


@Log4j2
public class RecorderManager {


    public  enum RECORDTYPE {
        MONTE,
        FFMPEG,
        HEADLESS
    }

    private static  void preCheckOSRecordHeadlessByCaptureFrame() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("linux")) {
                throw  new RuntimeException("Current record headless Dont support linux");

            } else if (os.contains("mac")) {
                System.out.println("Supported OS in Beta: " + os);
            } else {
                System.out.println("Supported OS: " + os);
            }
        } catch (Exception e) {
            System.err.println("Error while checking OS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String DOWNLOADED_FILES_FOLDER = "videos";
    private static ScreenRecorder screenRecorder;
    private static Process ffmpegProcess;
    public static String nameVideo = "default_video";  // Ensures nameVideo is never null
    private static String nameVideoAvi = Path.of("videos", nameVideo + ".avi").toString();
    private static String nameVideoMp4 = Path.of("videos", nameVideo + ".mp4").toString();
    private static String os = System.getProperty("os.name").toLowerCase();
   // private static Logger log = Logger.getLogger(RecorderManager.class.getName());
    private static Path ffmpegPath;
    private static final Path VIDEO_DIRECTORY = Path.of("videos");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

    public  static void  startCaptureFrames() throws IOException, InterruptedException {
       // preCheckOSRecordWhenOsHaveGUI();
        //preCheckOSRecordHeadlessByCaptureFrame();
       // VideoRecord.deleteFile("frames");

        Thread screencastThread = new Thread(() -> {
            try {

                CommandLine cmd = CommandLine.parse(getScreenProgramPath()+" -folder ./frames");
                // Suppress all console output
                OutputStream nullStream = OutputStream.nullOutputStream();
                PumpStreamHandler silentHandler = new PumpStreamHandler(nullStream, nullStream);

                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(silentHandler);
                executor.setExitValues(null); // Accept all exit codes
                executor.execute(cmd);
                log.info("Start capture frames");
            } catch (Exception e) {
                log.error("Screencast process failed: " + e.getMessage());
            }
        });
        screencastThread.setDaemon(true);
        screencastThread.start();
    }

    public static void convertImagesToVideo(String nameVideo) throws IOException, InterruptedException {

       preCheckOSRecordWhenOsHaveGUI();
        preCheckOSRecordHeadlessByCaptureFrame();
        if (!Files.exists(VIDEO_DIRECTORY)) {
            Files.createDirectories(VIDEO_DIRECTORY);
        }
        String timestamp = getTimestamp();

        Path outputFile = VIDEO_DIRECTORY.resolve(nameVideo + "_" + timestamp + ".mp4");


        if(isMac()) {

            VideoRecord.setExecutablePermission(getFfmpegPath());
        }
            CommandLine cmd = new CommandLine(String.valueOf(getFfmpegPath()))
                    .addArgument("-y") // Overwrite output files
                    .addArgument("-framerate")
                    .addArgument("35")
                    .addArgument("-i")
                    .addArgument("frames/screenshot_%06d.png\"")
                    .addArgument("-vf")
                    .addArgument("scale=trunc(iw/2)*2:trunc(ih/2)*2", false) // Ensure even dimensions
                    .addArgument("-c:v")
                    .addArgument("libx264")
                    .addArgument("-pix_fmt")
                    .addArgument("yuv420p")
                    .addArgument(String.valueOf(outputFile));

            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValues(null);

            int exitCode = executor.execute(cmd);
            System.out.println("FFmpeg exited with code: " + exitCode);
        }

    public static void captureScreenshot(WebDriver driver) {

        preCheckOSRecordHeadlessByCaptureFrame();
        // Format timestamp for filename
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        // Define default directory and file path
        String directory = "screenshots";
        String fileName = "screenshot_" + timestamp + ".png";

        try {
            // Ensure directory exists
            File screenshotDir = new File(directory);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Take and save screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(screenshotDir, fileName);
            FileUtils.copyFile(screenshot, destination);

            System.out.println("Screenshot saved to: " + destination.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
    private static  void preCheckOSRecordWhenOsHaveGUI() {
        try {

            if (isLinux()) {
                throw new RuntimeException("Current record headless Dont support linux");
            }
        } catch (Exception e) {
            log.error("Error while checking OS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void startVideoRecording(RECORDTYPE recordType,String baseVideoName) throws Exception {
        preCheckOSRecordWhenOsHaveGUI();
        ExtractLibFromJar.copyToM2Repo();

        // Generate timestamp and output file path
        String timestamp = getTimestamp();
        Path outputFile = VIDEO_DIRECTORY.resolve(baseVideoName + "_" + timestamp + ".mp4");

        nameVideo = baseVideoName + "_" + timestamp + ".mp4";

        // Ensure the video directory exists
        if (!Files.exists(VIDEO_DIRECTORY)) {
            Files.createDirectories(VIDEO_DIRECTORY);
        }

        // Select recording type using Java 17 arrow switch
        switch (recordType) {
            case MONTE -> {
                log.info("Starting MONTE recording ");
                VideoRecord._startRecording(baseVideoName);
                // Implement MONTE recording logic here
            }
            case FFMPEG -> {
                log.info("Starting Ffmpeg recording ");
                _startVideoRecording(baseVideoName);
            }
            default -> throw new IllegalArgumentException("Unsupported recording type: " + recordType);
        }
    }

    public static void stopVideoRecording(RECORDTYPE recordType, boolean hasDeleteAndConvet) throws Exception{
        preCheckOSRecordWhenOsHaveGUI();
        // Select recording type using Java 17 arrow switch
        switch (recordType) {
            case MONTE -> {
                log.info("Stop MONTE recording ");
                VideoRecord._stopRecording(hasDeleteAndConvet);

            }
            case FFMPEG -> {
                log.info("Stop Ffmpeg recording ");
                if (ffmpegProcess == null)
                    return;

                log.info("Stopping FFmpeg recording...");
                try {
                    ffmpegProcess.getOutputStream().write('q');
                    ffmpegProcess.getOutputStream().flush();
                    ffmpegProcess.waitFor();
                } catch (IOException | InterruptedException e) {
                    // log.error("Failed to stop FFmpeg process.", e);
                } finally {
                    ffmpegProcess = null;
                }
            }
            default -> throw new IllegalArgumentException("Unsupported recording type: " + recordType);
        }

    }

    public static void stopVideoRecording(RECORDTYPE recordType) throws Exception {
        // Default to false
        stopVideoRecording(recordType, false);
    }

    public static void convertAviToMp4(String inputVideo,String outputVideo) throws IOException, InterruptedException{

        VideoRecord._convertAviToMp4(inputVideo, outputVideo);
    }


    public static  Path getFfmpegPath (){
        ffmpegPath = os.contains("mac")
                ? Path.of(System.getProperty("user.home"), ".m2", "repository", "selenium-utils", "ffmpeg")
                : Path.of(System.getProperty("user.home"), ".m2", "repository", "selenium-utils", "ffmpeg.exe");

        return  ffmpegPath;
    }

    public static  Path getScreenProgramPath (){
        ffmpegPath = os.contains("mac")
                ? Path.of(System.getProperty("user.home"), ".m2", "repository", "selenium-utils", "screencast")
                : Path.of(System.getProperty("user.home"), ".m2", "repository", "selenium-utils", "screencast.exe");

        return  ffmpegPath;
    }

    // Get timestamp for filename in format "yyyyMMdd_HHmmss"
    private static String getTimestamp() {
        return dateFormat.format(new Date());
    }


    private static void _startVideoRecording(String baseVideoName) throws IOException, InterruptedException {
        ExtractLibFromJar.copyToM2Repo();

        // Generate timestamp and create output file path
        String timestamp = getTimestamp();
        Path outputFile = VIDEO_DIRECTORY.resolve(baseVideoName + "_" + timestamp + ".mp4");

        // Ensure the video directory exists
        if (!Files.exists(VIDEO_DIRECTORY)) {
            Files.createDirectories(VIDEO_DIRECTORY);
        }

        // Set and validate ffmpeg path
        Path ffmpegPath = getFfmpegPath();
        if (!Files.exists(ffmpegPath)) {
            throw new IOException("FFmpeg executable not found at: " + ffmpegPath);
        }

        // Determine OS and set the appropriate input source
        boolean isMac = os.contains("mac");
        String inputSource = isMac ? "avfoundation" : "gdigrab";
        String inputDevice = isMac ? "1" : "desktop";

        if (isMac) {
            VideoRecord.setExecutablePermission(ffmpegPath);
        }

        // Build the FFmpeg command
        String[] command = {
                ffmpegPath.toString(),
                "-f", inputSource,
                "-framerate", "30",
                "-i", inputDevice,
                "-c:v", "libx264",
                "-preset", "veryfast",
                "-crf", "23",
                "-pix_fmt", "yuv420p",
                "-y", outputFile.toString()
        };

        // Initialize ProcessBuilder
        ProcessBuilder ffmpegBuilder = isMac
                ? new ProcessBuilder(command)
                : new ProcessBuilder("cmd.exe", "/c", String.join(" ", command));

        ffmpegBuilder.redirectErrorStream(true);
        ffmpegProcess = ffmpegBuilder.start();

        // Log FFmpeg output asynchronously
        Process finalFfmpegProcess = ffmpegProcess;
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalFfmpegProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //log.info(line);
                }
            } catch (IOException e) {
                log.error("Error reading FFmpeg output: " + e.getMessage());
            }
        }).start();

        log.info("Video recording started: " + outputFile);
    }


    private static void _stopVideoRecording(Process ffmpegProcess) throws IOException, InterruptedException {
        if (ffmpegProcess == null) return;

        System.out.println("Stopping FFmpeg recording...");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ffmpegProcess.getOutputStream().write('q');
            ffmpegProcess.getOutputStream().flush();
        } else {
            ffmpegProcess.destroy();
        }

        ffmpegProcess.waitFor();
        ffmpegProcess = null;
    }




    // Set up record use Monte
    static class VideoRecord {

        public static void _startRecording(String nameVideo) throws Exception {
            File file = new File(USER_DIR, DOWNLOADED_FILES_FOLDER);

            if (!file.exists()) {
                file.mkdirs();
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            SpecializedScreenRecorder recorder = new SpecializedScreenRecorder(
                    gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24,
                            FrameRateKey, Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, file, nameVideo);

            screenRecorder = recorder;
            nameVideo = recorder.nameVideo;
            nameVideoAvi = Path.of("videos", nameVideo + ".avi").toString();
            nameVideoMp4 = Path.of("videos", nameVideo + ".mp4").toString();

            screenRecorder.start();

            // Move file  to local
            ExtractLibFromJar.copyToM2Repo();
        }

        public static void _stopRecording(boolean hasDeleteAndConvet) throws Exception {
            if (screenRecorder != null) {
                screenRecorder.stop();
            }

            log.info("Convert video success");
            if(hasDeleteAndConvet){
                convertAviToMp4(nameVideoAvi,nameVideoMp4);
                deleteFile(nameVideoAvi);
            }

        }

        private static void readStream(InputStream inputStream) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while (reader.readLine() != null) {
                    // Can log output if needed
                }
            } catch (IOException e) {
                log.error("Error reading stream: " + e.getMessage());
            }
        }
        public static void _convertAviToMp4(String inputFileName, String outputFileName) throws IOException, InterruptedException {
            ffmpegPath = getFfmpegPath();
            if (!Files.exists(ffmpegPath)) {
                throw new IOException("FFmpeg executable not found at: " + ffmpegPath);
            }

            if (!os.contains("win")) {
                setExecutablePermission(ffmpegPath);
            }

            // Updated FFmpeg command
            ProcessBuilder ffmpegBuilder = new ProcessBuilder(
                    ffmpegPath.toString(),
                    "-i", inputFileName,
                    "-c:v", "libx264",
                    "-preset", "medium",
                    "-crf", "18",
                    "-c:a", "aac",
                    "-b:a", "128k",
                    "-pix_fmt","yuv420p",
                    "-movflags", "+faststart", // Ensures file is playable immediately
                    "-y", outputFileName
            );

            //-c:v libx264 -preset medium -crf 18 -c:a aac -b:a 128k -pix_fmt yuv420p -movflags +faststart -y "videos/Test999-20250323_fixed.mp4"
            ffmpegBuilder.redirectErrorStream(true);
            Process ffmpegProcess = ffmpegBuilder.start();

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.submit(() -> readStream(ffmpegProcess.getInputStream()));
            executorService.submit(() -> readStream(ffmpegProcess.getErrorStream()));

            int ffmpegExitCode = ffmpegProcess.waitFor();
            executorService.shutdown();

            if (!executorService.awaitTermination(10L, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

            if (ffmpegExitCode == 0) {
                log.info("Conversion successful: " + outputFileName);
                deleteFile(inputFileName);  // Ensure input file deletion only if conversion succeeds
            } else {
                log.error("Conversion failed with exit code: " + ffmpegExitCode);
            }
        }

        public static void deleteFile(String inputPath) {
            Path path = Path.of(inputPath);
            if (Files.exists(path)) {
                try {
                    if (Files.isDirectory(path)) {
                        // Recursively delete contents
                        try (Stream<Path> walk = Files.walk(path)) {
                            walk.sorted(Comparator.reverseOrder())
                                    .forEach(p -> {
                                        try {
                                            Files.delete(p);
                                            log.info("Deleted: " + p);
                                        } catch (IOException e) {
                                            log.error("Failed to delete: " + p + " - " + e.getMessage());
                                        }
                                    });
                        }
                    } else {
                        Files.delete(path);
                        log.info("Deleted file: " + path);
                    }
                } catch (IOException e) {
                    log.error("Error deleting path: " + e.getMessage(), e);
                }
            } else {
                log.warn("Path does not exist: " + path);
            }
        }

        private static void setExecutablePermission(Path filePath) throws IOException, InterruptedException {
            ProcessBuilder chmodBuilder = new ProcessBuilder("chmod", "+x", filePath.toString());
            Process chmodProcess = chmodBuilder.start();
            int chmodExitCode = chmodProcess.waitFor();

            if (chmodExitCode != 0) {
                throw new IOException("Failed to set execute permission for FFmpeg, exit code: " + chmodExitCode);
            }
        }

    }



    static class  SpecializedScreenRecorder extends ScreenRecorder {

        private String name;
        public  String nameVideo ;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        public SpecializedScreenRecorder(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat, Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
                throws IOException, AWTException {
            super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
            this.name = name;
            nameVideo = name + "-" + dateFormat.format(new Date()) ;

        }

        @Override
        protected File createMovieFile(Format fileFormat) throws IOException {
            if (!movieFolder.exists()) {
                movieFolder.mkdirs();
            } else if (!movieFolder.isDirectory()) {
                throw new IOException("\"" + movieFolder + "\" is not a directory.");
            }

            return new File(movieFolder, name + "-" + dateFormat.format(new Date()) + "." + Registry.getInstance().getExtension(fileFormat));
        }


    }

   public static  class HeadlessUtils {

       public static void startChromeInHeadlessModeMAC() {
          preCheckOSRecordHeadlessByCaptureFrame();
           killProcessOnPort(9200);
           Thread chromeThread = new Thread(() -> {
               String chromePath = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";

               String userDataDir = System.getProperty("user.home") + "/chrome-remote-profile";

               java.util.List<String >command = List.of(
                       chromePath,
                       "--remote-debugging-port=9200",
                       "--no-first-run",
                       "--disable-gpu",
                       "--no-default-browser-check",
                       "--headless=new",
                       "--no-sandbox",
                       "--disable-dev-shm-usage",
                       "--mute-audio"
               );

               try {
                   ProcessBuilder builder = new ProcessBuilder(command);
                   builder.redirectErrorStream(true); // Merge stderr with stdout

                   Process process = builder.start();

                   // Log Chrome output for the first 30 seconds
                   new Thread(() -> {
                       try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                           long startTime = System.currentTimeMillis();
                           String line;
                           while ((line = reader.readLine()) != null) {
                               log.info("[Chrome] {}", line);
                               if (System.currentTimeMillis() - startTime > 30_000) break;
                           }
                       } catch (IOException e) {
                           log.error("Error reading Chrome output: {}", e.getMessage());
                       }
                   }, "Chrome-Logger").start();

                   log.info("Chrome headless started on macOS with remote debugging at port 9222");
                   log.info(" Thread: {}", Thread.currentThread().getName());

               } catch (IOException e) {
                   log.error(" Failed to start Chrome headless: {}", e.getMessage());
               }
           });

           chromeThread.setDaemon(true); // Won’t block JVM exit
           chromeThread.start();
       }

       private  static  void killProcessOnPort(int port) {
           String os = System.getProperty("os.name").toLowerCase();

           try {
               if (os.contains("win")) {
                   // For Windows
                   String command = String.format("cmd /c for /f \"tokens=5\" %%a in ('netstat -aon ^| findstr :%d') do taskkill /f /pid %%a", port);
                   Runtime.getRuntime().exec(command);
                   log.info("Kill port 9200 ");
               } else {
                   // For Unix/Linux/Mac
                   String[] command = {"/bin/sh", "-c", String.format("lsof -ti:%d | xargs kill -9", port)};
                   Runtime.getRuntime().exec(command);
               }

               // Wait a moment to ensure the port is freed
               Thread.sleep(1000);
           } catch (Exception e) {
               System.err.println("Failed to kill process on port " + port);
               e.printStackTrace();
           }
       }

   }

}


