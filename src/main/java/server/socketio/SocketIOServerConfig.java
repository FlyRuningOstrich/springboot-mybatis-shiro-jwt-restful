package server.socketio;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOServerConfig {
    private static String hostname;
    private static int port;

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
//        config.setAuthorizationListener(data -> {
////            String token = data.getHttpHeaders().get("Authorization");
//            String token = data.getSingleUrlParam("token");
//            boolean result = false;
//            try {
//                result = JavaJWT.verifyToken(token);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        });

        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}
