package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.OperationLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    int insert(OperationLog operationLog);
    @Select("SELECT * FROM user_logs")
    List<OperationLog> findAllLogs();
    @Delete("<script>" +
            "DELETE FROM user_logs WHERE id IN " +
            "<foreach item='id' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int deleteLogs(List<Integer> ids);
}
