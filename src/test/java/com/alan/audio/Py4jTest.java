package com.alan.audio;

import org.junit.Test;
import py4j.ClientServer;

public class Py4jTest {
    @Test
    public void close() {
        RosaPy4j rosaPy4j = new RosaPy4j();
        ClientServer clientServer = new ClientServer(null);
        Client client = (Client) clientServer.getPythonServerEntryPoint(new Class[]{Client.class});
        client.say_hello(4,"alan");
        try {
            client.shutdown();
        } catch (Exception e) {
        }
    }
}
