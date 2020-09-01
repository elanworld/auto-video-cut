package com.alan.audio;


import com.alan.util.Output;
import com.alan.util.RunCmd;
import py4j.ClientServer;
import py4j.GatewayServer;

import java.nio.file.Paths;
import java.util.List;

interface Client {
    public String run(String p);

    public void shutdown();

    public List<List<Double>> audio_time(String file);

    public List<String> say_hello(int i, String s);
}


public class RosaPy4j {


    private void runPython() {
        String pyProgram = "G:\\Alan\\Documents\\OneDrive\\Documents\\program\\python\\auto-cut-video\\java_server.py";
        String workDir = Paths.get(pyProgram).getParent().toString();
        Output.print(workDir);
        String cmd = String.format("cmd /c cd %s && python %s", workDir, pyProgram);
        new RunCmd(cmd, 100, false, true);
    }

    public List<List<Double>> getSpeakClips(String file) {
        List<List<Double>> clips = null;
        RosaPy4j rosa = new RosaPy4j();
        rosa.runPython();
        ClientServer clientServer = new ClientServer(null);
        try {
            Client client = (Client) clientServer.getPythonServerEntryPoint(new Class[]{Client.class});
            clips = client.audio_time(file);
            client.shutdown();
        } catch (Exception e) {
            String message = e.getMessage();
            e.printStackTrace();
            if (message.matches(".*127.0.0.1:25333.*"))
                new RunCmd("taskkill /f /im python.exe");
            // throw new RuntimeException(e.getMessage());
        }
        clientServer.shutdown();
        if (clips == null)
            throw new RuntimeException("fail to get audio time clips from python connection");
        return clips;
    }
}