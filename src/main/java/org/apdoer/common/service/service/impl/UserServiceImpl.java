package org.apdoer.common.service.service.impl;

import org.apdoer.common.service.code.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.mapper.*;
import org.apdoer.common.service.model.po.*;
import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ExceptionBuildUtils;
import org.apdoer.common.service.util.InviteCodeUtil;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.apdoer.common.service.service.UserService;

import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserCodePoMapper userCodePoMapper;

    /***
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    @Cacheable(value = "USER_NAME", key = "'USER_NAME_'+#username")
    public UserPo getUserByUsername(String username) {
        try {
            UserPo user = new UserPo();
            user.setUsername(username);
            return this.userMapper.selectOne(user);
        } catch (Exception e) {
            log.error("");
            throw ExceptionBuildUtils.buildUnTradeException(ExceptionCode.QUERY_USER_INFO_EXCEPTION_CODE.getCode(), "", e);
        }
    }

    /***
     * 更新user
     *
     * @param userPo
     * @return
     */
    @Override
    public int updateUserbById(UserPo userPo) {
        try {
            if (null != userPo.getId()) {
                userServiceImpl.redisCacheEvictUserById(userPo.getId());
            }
            if (StringUtils.isNotBlank(userPo.getUsername())) {
                userServiceImpl.redisCacheEvictUserByName(userPo.getUsername());
            }
            return userMapper.updateById(userPo);
        } catch (Exception e) {
            log.error("");
            throw ExceptionBuildUtils.buildUnTradeException(ExceptionCode.QUERY_USER_INFO_EXCEPTION_CODE.getCode(), "", e);
        }
    }


    @Override
    public UserPo getUserByEmail(String email) {
        try {
            UserPo user = new UserPo();
            user.setEmail(email);
            return this.userMapper.selectOne(user);
        } catch (Exception e) {

            throw ExceptionBuildUtils.buildUnTradeException(ExceptionCode.QUERY_USER_INFO_EXCEPTION_CODE.getCode(), "", e);
        }

    }


    @Override
    public UserPo getUserByPhone(String phone) {
        try {
            UserPo user = new UserPo();
            user.setPhone(phone);
            return this.userMapper.selectOne(user);
        } catch (Exception e) {

            throw ExceptionBuildUtils.buildUnTradeException(ExceptionCode.QUERY_USER_INFO_EXCEPTION_CODE.getCode(), "", e);
        }

    }


    @Override
    public ResultVo save(UserPo user) {
        try {
            this.userMapper.insertSelective(user);
            UserPo userPo = this.getUserByUsername(user.getUsername());
            RolePo rolePo = new RolePo();
            //初始注册用户角色为 NORMAL_USER
            rolePo.setName("normal");
            RolePo role = this.roleMapper.selectOne(rolePo);
            UserRolePo userRolePo = new UserRolePo();
            userRolePo.setRoleId(role.getId());
            userRolePo.setUserId(userPo.getId());
            this.userRoleMapper.insertSelective(userRolePo);

            //插入邀请码
            UserCodePo codePo = new UserCodePo();
            codePo.setUserId(userPo.getId());
            codePo.setCreateTime(user.getCreateTime());
            codePo.setInviteCode(InviteCodeUtil.mapToCode(userPo.getId()));
            this.userCodePoMapper.insertSelective(codePo);
            return new ResultVo(ExceptionCode.SUCCESS.getCode(), "注册用户成功", userPo.getId());
        } catch (Exception e) {

            return ResultVoBuildUtils.buildResultVo(ExceptionCode.FIAL.getCode(), "注册失败");
        }
    }

    @Override
    public ResultVo updatePass(UserPo user, String newPassword) {
        try {
            if (null != user.getId()) {
                userServiceImpl.redisCacheEvictUserById(user.getId());
            }
            if (StringUtils.isNotBlank(user.getUsername())) {
                userServiceImpl.redisCacheEvictUserByName(user.getUsername());
            }
            this.userMapper.updateUserPassword(user.getId(), newPassword, user.getLoginPassGrade());
            return ResultVoBuildUtils.buildResultVo(ExceptionCode.SUCCESS.getCode(), "修改密码成功");
        } catch (Exception e) {


            return ResultVoBuildUtils.buildResultVo(ExceptionCode.FIAL.getCode(), "修改密码失败");
        }
    }


    @Override
    @Cacheable(value = "PERMISSION", key = "'PERMISSION_'+#userId.toString()")
    public List<PermissionPo> getPermissionList(Integer userId) {
        try {
            return this.permMapper.getPermissionList(userId);
        } catch (Exception e) {
            throw ExceptionBuildUtils.buildUnTradeException(
                    ExceptionCode.QUERY_USER_PERMISSION_LIST_EXCEPTION_CODE.getCode(), "", e);
        }
    }


    @Override
    @Cacheable(value = "USER_ID", key = "'USER_ID_'+#userId.toString()")
    public UserPo getUserById(Integer userId) {
        try {
            UserPo po = this.userMapper.getUserById(userId);
            return po;
        } catch (Exception e) {

            throw ExceptionBuildUtils.buildUnTradeException(ExceptionCode.QUERY_USER_INFO_EXCEPTION_CODE.getCode(), "", e);
        }
    }

    @CacheEvict(value = "USER_NAME", key = "'USER_NAME_'+#username")
    public void redisCacheEvictUserByName(String username) {
    }

    @CacheEvict(value = "USER_ID", key = "'USER_ID_'+#userId.toString()")
    public void redisCacheEvictUserById(Integer userId) {
    }
}
