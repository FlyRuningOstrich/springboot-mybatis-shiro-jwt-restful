package server.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value = 1)
public class MyCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer socketIOServer;

    @Autowired
    public MyCommandLineRunner(SocketIOServer server) {
        this.socketIOServer = server;
    }

    @Override
    public void run(String... args) {
        socketIOServer.start();
    }
}
