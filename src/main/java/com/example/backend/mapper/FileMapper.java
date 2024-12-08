package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<Files> {
    @Select(" SELECT * FROM files WHERE user_id = #{userId}")
    List<Files> getFileByUserId(int userId);

    @Update(" UPDATE files SET dir_id = #{dirId}, user_id=#{userId}, department=#{department} WHERE id = #{id}")
    int moveFile(int id, Integer dirId,Integer userId,String department);

    @Delete("DELETE FROM files WHERE id = #{id}")
    int delFolder(int id);

    @Update(" UPDATE files SET remark = #{remark}, user_id=#{userId} WHERE id = #{id}")
    int remarkFile(int id, String remark, Integer userId);

    @Insert("INSERT INTO files ( file_name, ext, size, dir_id, user_id, url, department )" +
            " VALUES( #{fileName}, #{ext}, #{size}, #{dirId}, #{userId}, #{url}, #{department} )")
    int uploadFile(String fileName, String ext, String size, Integer dirId, String userId,String url,String department);

    @Select(" SELECT * FROM files")
    List<Files> getFile();
}
