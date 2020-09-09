import com.alan.text.YoudaoTranslation;
import com.alan.util.Output;
import com.alan.video.FFmpegCapturer;
import org.junit.Test;

public class MainTest {


    @Test
    public void translate() {
        String run = YoudaoTranslation.run("i dont kown who i am");
        Output.print(run);
    }

    public void record() {
        FFmpegCapturer fFmpegCapturer = new FFmpegCapturer();
        fFmpegCapturer.addLog().addVideoDevice("USB").setOutput("F:\\Alan\\Videos\\Mine\\ffmpeg_record.mp4");
        fFmpegCapturer.runWithInput();
    }

}
