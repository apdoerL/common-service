package org.apdoer.common.service.resolver;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.model.vo.UserEnvVo;
import org.apdoer.common.service.util.NetUtils;
import org.springframework.core.MethodParameter;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 客户端环境解析<br/>
 *
 * @author William
 * @since JDK 1.8
 */
@Slf4j
public class UserEnvResovler extends LiteDeviceResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UserEnvVo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest nativewebrequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = nativewebrequest.getNativeRequest(HttpServletRequest.class);
        Cookie[] cookie = request.getCookies();
        Locale locale = null;
        if (cookie != null) {
            for (Cookie ck : cookie) {
                if ("locale".equalsIgnoreCase(ck.getName())) {
                    try {
                        String[] langArr = ck.getValue().split("_");
                        locale = new Locale(langArr[0], langArr[1]);
                    } catch (Exception e) {
                        log.error("parse locale error.", e);
                        locale = Locale.CHINA;
                    }
                    break;
                }
            }
        } else {
            locale = Locale.CHINA;
        }

        UserEnvVo userEnvVo = new UserEnvVo();
        userEnvVo.setDevice(super.resolveDevice(request));
        userEnvVo.setUserIp(NetUtils.getIpAdrress(request));
        userEnvVo.setLocale(locale);
        return userEnvVo;
    }

}
