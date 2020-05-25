package org.apdoer.common.service.model.po;

import lombok.*;

import javax.persistence.Table;
import java.util.Date;

/**
 * web 用户权限表
 *
 * @author apdoer
 */
@Table(name = "web_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PermissionPo {
    private Integer id;

    private String value;

    private Integer enabled;

    private String description;

    private Date createTime;


}