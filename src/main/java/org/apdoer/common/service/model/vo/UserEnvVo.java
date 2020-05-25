package org.apdoer.common.service.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mobile.device.Device;

import java.io.Serializable;
import java.util.Locale;

/**
 * 用户环境信息<br/>
 * date: 2018年9月15日 下午4:48:34 <br/>
 *
 * @author William
 * @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserEnvVo implements Serializable {
    private static final long serialVersionUID = -8575327472558940263L;
    // 用户locale信息
    private Locale locale;
    // 客户端ip
    private String userIp;
    // 客户端设备信息
    private Device device;

}
