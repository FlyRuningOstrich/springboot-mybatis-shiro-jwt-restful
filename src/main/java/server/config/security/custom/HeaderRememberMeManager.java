package server.config.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.AbstractRememberMeManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HeaderRememberMeManager extends AbstractRememberMeManager {
    private static final String DEFAULT_REMEMBER_ME_HEADER_NAME = "Remember-Me";

    //修改rememberMe信息放置header中
    protected void rememberSerializedIdentity(Subject subject, byte[] serialized) {
        HttpServletResponse response = WebUtils.getHttpResponse(subject);
        String base64 = Base64.encodeToString(serialized);
        // 设置 rememberMe 信息到 response header 中
        response.setHeader(DEFAULT_REMEMBER_ME_HEADER_NAME, base64);
    }

    //修改读取remenmberMe改为由header中读取
    protected byte[] getRememberedSerializedIdentity(SubjectContext subjectContext) {
        WebSubjectContext wsc = (WebSubjectContext) subjectContext;
        HttpServletRequest request = WebUtils.getHttpRequest(wsc);
        String base64 = request.getHeader(DEFAULT_REMEMBER_ME_HEADER_NAME);
        if (base64 != null) {
            base64 = this.ensurePadding(base64);
            return Base64.decode(base64);
        } else {
            return null;
        }
    }

    //一下方法为销毁rememberMe信息，改为header中存储后，后台不再控制此功能
    protected void forgetIdentity(Subject subject) {
    }

    public void forgetIdentity(SubjectContext subjectContext) {
    }

    private String ensurePadding(String base64) {
        int length = base64.length();
        if (length % 4 != 0) {
            StringBuilder sb = new StringBuilder(base64);
            for (int i = 0; i < length % 4; ++i) {
                sb.append('=');
            }
            base64 = sb.toString();
        }
        return base64;
    }
}
