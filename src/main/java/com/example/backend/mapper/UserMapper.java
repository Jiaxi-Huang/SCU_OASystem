package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    @Select("SELECT * FROM user_infos WHERE email = #{email}")
    User findByEmail(String email);

    @Insert("INSERT INTO user_infos (email, password, role) VALUES(#{email}, #{password}, #{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    //返回int,boolean等值的时候，尽量表明正常的情况返回什么值
    //int insertUser(String email, String password, String role)这样是不会正常返回的
    int insertUser(String email, String password, String role);

    @Update("UPDATE user_infos SET password = #{password} WHERE email = #{email}")
    int updatePassword(String email, String password);
}
