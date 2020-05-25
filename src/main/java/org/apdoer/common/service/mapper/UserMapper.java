package org.apdoer.common.service.mapper;


import org.apdoer.common.service.model.po.UserPo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface UserMapper extends BaseMapper<UserPo> {


    public int updateUserPassword(@Param("userId") Integer userId, @Param("password") String password, @Param("loginPassGrade") Integer login_pass_grade);

    public int updateById(UserPo userPo);

    public List<UserPo> querySelect(UserPo userPo);


    public List<UserPo> queryUserInfo(UserPo userPo);


    public UserPo getUserById(@Param("userId") Integer userId);

    public List<Integer> getNoWalletUserList();

    public List<Integer> queryAllUser();
}