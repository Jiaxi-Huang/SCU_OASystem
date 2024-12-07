package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder> {

    @Insert("INSERT INTO folders ( user_id, title, pid, department)" +
            " VALUES( #{user_id}, #{title}, #{pid}, #{department} )")
    int createFolder( int user_id,String department, String title, Integer pid);

    @Select(" SELECT * FROM folders WHERE user_id = #{userId}")
    List<Folder> getFolderByUserId(int userId);

    @Update(" UPDATE folders SET title = #{title} WHERE id = #{id}")
    int modifyFolder(int id, String title );

    @Update(" UPDATE folders SET pid = #{pid}, department = #{department},user_id = #{userId} WHERE id = #{id}")
    int moveFolder(int id, int pid, String department,int userId );

    @Delete("DELETE FROM folders WHERE id = #{id}")
    int delFolder(Integer id);

    @Select(" SELECT * FROM folders WHERE id = #{id}")
    Folder judgeFolder(int id);

    @Select(" SELECT * FROM folders")
    List<Folder> getFolder();
}
