package com.alan.audio;

import java.nio.file.Paths;
import java.util.List;

import com.alan.common.util.Output;
import com.alan.common.util.RunCmd;

import py4j.ClientServer;

interface Client {
	String run(String p);

	void shutdown();

	List<List<Double>> audio_time(String file);

	List<String> say_hello(int i, String s);
}

public class RosaPy4j {
	Client client;

	private void runPython() {
		String pyProgram = "G:\\Alan\\Documents\\OneDrive\\Documents\\program\\python\\auto-cut-video\\java_server.py";
		String workDir = Paths.get(pyProgram).getParent().toString();
		Output.print(workDir);
		String cmd = String.format("cmd /c cd %s && python %s", workDir, pyProgram);
		new RunCmd(cmd, 1000, false, true);
	}

	public List<List<Double>> getSpeakClips(String file) {
		List<List<Double>> clips = null;
		RosaPy4j rosa = new RosaPy4j();
		rosa.runPython();
		ClientServer clientServer = new ClientServer(null);
		client = (Client) clientServer.getPythonServerEntryPoint(new Class[]{Client.class});
		clips = client.audio_time(file);
		try {
			client.shutdown();
		} catch (Exception e) {
			String message = e.getMessage();
		}
		clientServer.shutdown();
		if (clips == null) {
			throw new RuntimeException("fail to get audio time clips from python connection");
		}
		return clips;
	}
}
