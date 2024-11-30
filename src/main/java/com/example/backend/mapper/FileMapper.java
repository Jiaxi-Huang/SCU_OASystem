package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<Files> {
    @Select(" SELECT * FROM files WHERE user_id = #{userId}")
    List<Files> getFileByUserId(int userId);

    @Update(" UPDATE files SET dir_id = #{dirId} WHERE id = #{id}")
    int moveFile(int id, Integer dirId);

    @Delete("DELETE FROM files WHERE id = #{id}")
    int delFolder(int id);

    @Update(" UPDATE files SET remark = #{remark} WHERE id = #{id}")
    int remarkFile(int id, String remark);

    @Insert("INSERT INTO files ( file_name, ext, size, dir_id, user_id, url )" +
            " VALUES( #{fileName}, #{ext}, #{size}, #{dirId}, #{userId}, #{url} )")
    int uploadFile(String fileName, String ext, String size, Integer dirId, String userId,String url);
}
