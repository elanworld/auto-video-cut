import com.alan.common.util.Output;
import com.alan.common.web.tans.YoudaoTranslator;
import com.alan.video.FFmpegCapture;

public class MainTest {

	public void translate() {
		String run = new YoudaoTranslator().tans("i don`t know who i am");
		Output.print(run);
	}

	public void record() {
		FFmpegCapture fFmpegCapture = new FFmpegCapture();
		fFmpegCapture.addLog().addVideoDevice("USB").setOutput("F:\\Alan\\Videos\\Mine\\ffmpeg_record.mp4");
		fFmpegCapture.runWithInput();
	}

}
