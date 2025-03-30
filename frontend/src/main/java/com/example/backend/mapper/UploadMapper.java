package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Avatar;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface UploadMapper extends BaseMapper<Avatar> {
    @Select("SELECT * FROM user_avatar WHERE user_id = #{user_id}")
    Avatar findAvatarByUserId(int user_id);
    @Update("UPDATE user_avatar SET avatar = #{avatar} WHERE user_id = #{user_id}")
    int updatePersonalAvatar(int user_id,String avatar);
    @Insert("INSERT user_avatar (user_id,avatar) VALUES(#{user_id},#{avatar})")
    int insertPersonalAvatar(int user_id,String avatar);
}
