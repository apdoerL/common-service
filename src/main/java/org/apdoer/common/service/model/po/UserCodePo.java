package org.apdoer.common.service.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 用户邀请码
 * @Author: yang
 * @Date: 18/11/15/0015 21:42
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "web_user_code")
public class UserCodePo implements Serializable {
    private static final long serialVersionUID = 7138318151763260920L;
    /**
     * 用户id
     */
    @Id
    private Integer userId;

    /**
     * 用户邀请码
     */
    private String inviteCode;

    /**
     * 注册时间
     */
    private Date createTime;


}