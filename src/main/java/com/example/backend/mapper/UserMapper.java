package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    @Select("SELECT * FROM user_infos")
    List<User> findAllUser();
    @Select("SELECT * FROM user_infos WHERE department = #{department}")
    List<User> findDepartmentUser(String department);
    @Select("SELECT * FROM user_infos WHERE user_id = #{user_id}")
    User findByUserId(int user_id);
    @Select("SELECT * FROM user_infos LEFT JOIN user_avatar ON user_infos.user_id=user_avatar.user_id WHERE email = #{email}")
    User findByEmail(String email);
    @Select("SELECT * FROM user_infos WHERE wechat_user_id=#{openid}")
    User findByOpenid(String openid);

    @Insert("INSERT INTO user_infos (email, password, role) VALUES(#{email}, #{password}, #{role})")
    //@Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insertUser(String email, String password, String role);
    @Insert("INSERT INTO user_infos (username, password, department, role) VALUES(#{username}, #{password}, #{department}, #{role})")
    int adminInsertUser(String username, String password, String department, String role);
    @Update("UPDATE user_infos SET wechat_user_id=#{openid} WHERE phone=#{phone}")
    int bindOpenidByPhone(String openid,String phone);
    @Update("UPDATE user_infos SET password = #{password} WHERE email = #{email}")
    int updatePassword(String email, String password);
    @Update("UPDATE user_infos SET username = #{username},phone = #{phone},intro= #{intro} WHERE user_id = #{user_id}")
    int updateUserInfo(String username, String phone,String intro, int user_id);
    @Update("UPDATE user_infos SET username = #{username}, department=#{department}, role=#{role} WHERE user_id = #{user_id}")
    int adminUpdateUserInfo(String username, String department, String role, int user_id);
    @Update("UPDATE user_infos SET password = #{password} WHERE user_id = #{user_id}")
    int updatePersonalPassword(int user_id,String password);
    @Update("UPDATE user_infos SET email = #{newEmail} WHERE user_id = #{user_id} and email=#{oldEmail}")
    int updatePersonalEmail(int user_id,String oldEmail,String newEmail);
    @Delete("DELETE from user_infos WHERE user_id =#{user_id}")
    int adminDeleteUser(int user_id);
}
