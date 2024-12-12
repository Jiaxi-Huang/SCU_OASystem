package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.authedRoutes.Permission;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Insert("INSERT INTO user_permission_infos (user_id,permissions) VALUES(#{user_id}, #{permissions})")
    int insertPermission(int user_id, String permissions);
    @Update("UPDATE user_permission_infos SET permissions=#{permissions} WHERE user_id=#{user_id}")
    int updatePermission(int user_id, String permissions);
}
