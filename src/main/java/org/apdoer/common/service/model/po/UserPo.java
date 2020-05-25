package org.apdoer.common.service.model.po;

import lombok.*;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "web_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserPo implements Serializable {
    private static final long serialVersionUID = -2092561749596719704L;

    //自增ID，主键，用户ID
    private Integer id;

    //密码
    private String password;

    //用户名称
    private String username;

    //邮箱
    private String email;

    //用户状态，0：正常，1：锁定，3：注销
    private Integer enabled;

    //注册时间
    private Date createTime;

    //最近登陆时间
    private Date lastLogin;

    //手机号码
    private String phone;

    //谷歌验证码
    private String googleCode;

    //交易密码
    private String tradePassword;

    //登陆密码等级
    private Integer loginPassGrade;

    //是否开启谷歌认证码，1：开启，2：关闭
    private Integer googleValidate;

    //防钓鱼码
    private String avoidFishingCode;

    //交易密码安全策略，1：每次输入密码，2：每两小时输入一次，3：不输入
    private Integer tradeSafe;

    //用户渠道类型 1.web_register：web直接注册 2.web_google：web google登录 3.web_facebook：web facebook登录4.h5_register：h5直接注册 5.h5_google：h5 google登录
    // 6.h5_facebook：h5 facebook登录7.app_register：app直接注册 8.app_google：app google登录 9.app_facebook：app facebook登录
    private String userChannel;

    //上次输入交易日期时间
    private Date lastTradeTime;


}
