package com.example.backend.services;

import com.example.backend.annotation.LogOperationWithId;
import com.example.backend.entity.OperationLog;
import com.example.backend.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;
    @LogOperationWithId(value = "查询操作日志", idParamIndex = 0)
    public List<OperationLog> logList(int user_id){
        try{
            return operationLogMapper.findAllLogs();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @LogOperationWithId(value = "删除操作日志", idParamIndex = 0)
    public int logDelete(int user_id,List<Integer> ids){
        try{
            return operationLogMapper.deleteLogs(ids);
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
