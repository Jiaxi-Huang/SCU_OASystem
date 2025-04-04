package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Folder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FolderMapper extends BaseMapper<Folder> {

    @Insert("INSERT INTO folders ( user_id, title, pid, department, is_shared)" +
            " VALUES( #{user_id}, #{title}, #{pid}, #{department}, #{isShared} )")
    int createFolder( int user_id,String department, String title, Integer pid,Integer isShared);

    @Select(" SELECT * FROM folders WHERE user_id = #{userId}")
    List<Folder> getFolderByUserId(int userId);

    @Update(" UPDATE folders SET title = #{title},user_id = #{userId}, update_time = NOW() WHERE id = #{id}")
    int modifyFolder(int id, String title, int userId );

    @Update(" UPDATE folders SET pid = #{pid}, department = #{department}," +
            " user_id = #{userId},is_shared = #{isShared}, update_time = NOW() WHERE id = #{id}")
    int moveFolder(int id, int pid, String department,int userId,Integer isShared );

    @Delete("DELETE FROM folders WHERE id = #{id}")
    int delFolder(Integer id);

    @Select(" SELECT * FROM folders WHERE id = #{id}")
    Folder judgeFolder(int id);

    @Select(" SELECT * FROM folders")
    List<Folder> getFolder();

    @Update(" UPDATE folders SET department = #{department},is_shared = #{isShared}, user_id = #{userId} WHERE id = #{currentFolderId}")
    int updateFolderDepartmentAndShared(int currentFolderId, String department, int isShared, int userId);
}
