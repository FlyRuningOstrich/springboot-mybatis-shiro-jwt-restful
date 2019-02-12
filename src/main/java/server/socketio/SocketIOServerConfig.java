package server.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.config.security.RedisSessionDao;
import server.config.security.ShiroSessionTool;

@Configuration
public class SocketIOServerConfig {
    private static String hostname;
    private static int port;
    private final ShiroSessionTool shiroSessionTool;

    @Autowired
    public SocketIOServerConfig(ShiroSessionTool shiroSessionTool) {
        this.shiroSessionTool = shiroSessionTool;
    }

    @Value("${socketio.host}")
    public void setHostname(String a) {
        hostname = a;
    }

    @Value("${socketio.port}")
    public void setPort(int a) {
        port = a;
    }

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        config.setAuthorizationListener(data -> {
            String token = data.getSingleUrlParam("Authorization");
            boolean result = false;
            try {
                result = shiroSessionTool.checkSession(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        });
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
