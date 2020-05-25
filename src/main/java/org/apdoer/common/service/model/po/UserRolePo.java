package org.apdoer.common.service.model.po;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "web_user_role")
public class UserRolePo implements Serializable {

    private static final long serialVersionUID = -689653344194507603L;

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private Integer enabled;

    private Date createTime;

}
