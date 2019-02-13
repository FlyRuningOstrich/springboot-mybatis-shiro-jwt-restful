package server.config.security.session;

import org.springframework.stereotype.Component;

@Component
public class HttpSessionIdHolder {
    private final ThreadLocal<String> SESSIONID = new ThreadLocal<>();

    String getSessionId() {
        return SESSIONID.get();
    }

    public  String getAndRemoveSessionId() {
        String tmp = SESSIONID.get();
        SESSIONID.remove();
        return tmp;
    }

    public void setSessionId(String sessionId) {
        SESSIONID.set(sessionId);
    }

    public  void removeSessionId() {
        SESSIONID.remove();
    }
}
