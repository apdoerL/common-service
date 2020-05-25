package org.apdoer.common.service.model.po;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * web 用户角色表
 *
 * @author apdoer
 */
@Table(name = "web_role")
@Data
public class RolePo {
    private Integer id;

    private String name;

    private String description;

    private Integer enabled;

    private Date createTime;

}
