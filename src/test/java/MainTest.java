import com.alan.text.Translator;
import com.alan.util.Output;
import com.alan.video.FFmpegCapturer;

public class MainTest {


    public void translate() {
        String run = new Translator().run("i don`t know who i am");
        Output.print(run);
    }


    public void record() {
        FFmpegCapturer fFmpegCapturer = new FFmpegCapturer();
        fFmpegCapturer.addLog().addVideoDevice("USB").setOutput("F:\\Alan\\Videos\\Mine\\ffmpeg_record.mp4");
        fFmpegCapturer.runWithInput();
    }


}
