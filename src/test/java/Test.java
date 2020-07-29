import com.alan.cmd.FFmpegCmd;
import com.alan.output.Output;
import org.json.JSONArray;

public class Test {
    public static void main(String args[]) {
        System.out.println("this is test class举起手来 HD1280高清国语中字.mp4");

        String json = "[ is test dd class,bb,cc,{dd:ee}]";
        JSONArray objects = new JSONArray(json);
        String dd = objects.getJSONObject(3).getString("dd");
        new Output(dd);
    }
}
