package org.apdoer.common.service.service;


import org.apdoer.common.service.model.po.PermissionPo;
import org.apdoer.common.service.model.po.UserPo;
import org.apdoer.common.service.model.vo.ResultVo;

import java.util.List;


public interface UserService {

    /***
     * 根据用户名查找用户
     * @param username
     * @return
     */
    UserPo getUserByUsername(String username);


    /***
     * 更新user
     * @param userPo
     * @return
     */
    int updateUserbById(UserPo userPo);

    /***
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    UserPo getUserByEmail(String email);

    /***
     * 根据手机号查找用户
     * @param phone
     * @return
     */
    UserPo getUserByPhone(String phone);

    /***
     * 注册用户
     * @param user
     */
    ResultVo save(UserPo user);

    /***
     *
     * @param user
     * @param newPassword
     * @return
     */
    ResultVo updatePass(UserPo user, String newPassword);

    /***
     * 获取用户权限
     * @param userId
     * @return
     */
    List<PermissionPo> getPermissionList(Integer userId);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserPo getUserById(Integer userId);
}
