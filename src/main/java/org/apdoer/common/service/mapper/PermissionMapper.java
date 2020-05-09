package org.apdoer.common.service.mapper;

import org.apdoer.common.service.common.BaseMapper;
import org.apdoer.common.service.model.po.PermissionPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<PermissionPo> {

    List<PermissionPo> getPermissionList(@Param("userId") Integer userId);
}