package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    @Select("SELECT * FROM user_infos WHERE email = #{email}")
    User findByEmail(String email);

    @Insert("INSERT INTO user_infos (email, password, userInfo) VALUES(#{email}, #{password}, #{userInfo})")
    //@Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insertUser(String email, String password, String role);
    @Update("UPDATE user_infos SET password = #{password} WHERE email = #{email}")
    int updatePassword(String email, String password);
}
