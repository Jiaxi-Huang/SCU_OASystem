package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    @Select("SELECT * FROM user_infos")
    List<User> findAllUser();
    @Select("SELECT * FROM user_infos WHERE department = #{department}")
    List<User> findDepartmentUser(String department);
    @Select("SELECT * FROM user_infos WHERE user_id = #{user_id}")
    User findByUserId(int user_id);
    @Select("SELECT * FROM user_infos WHERE email = #{email}")
    User findByEmail(String email);

    @Insert("INSERT INTO user_infos (email, password, role) VALUES(#{email}, #{password}, #{role})")
    //@Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insertUser(String email, String password, String role);
    @Update("UPDATE user_infos SET password = #{password} WHERE email = #{email}")
    int updatePassword(String email, String password);
    @Update("UPDATE user_infos SET username = #{username},phone = #{phone},intro= #{intro} WHERE user_id = #{user_id}")
    int updateUserInfo(String username, String phone,String intro, int user_id);
}
