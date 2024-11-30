package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder> {

    @Insert("INSERT INTO folders ( user_id, title, pid )" +
            " VALUES( #{user_id}, #{title}, #{pid} )")
    int createFolder( int user_id, String title, Integer pid);

    @Select(" SELECT * FROM folders WHERE user_id = #{userId}")
    List<Folder> getFolderByUserId(int userId);

    @Update(" UPDATE folders SET title = #{title} WHERE id = #{id}")
    int modifyFolder(int id, String title );

    @Update(" UPDATE folders SET pid = #{pid} WHERE id = #{id}")
    int moveFolder(int id, int pid );

    @Delete("DELETE FROM folders WHERE id = #{id}")
    int delFolder(Integer id);
}
