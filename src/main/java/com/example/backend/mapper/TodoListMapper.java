package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.meeting.MeetingWithAdderId;
import com.example.backend.entity.meeting.SelectorProviderGy;
import com.example.backend.entity.todoList.TodoRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface TodoListMapper extends BaseMapper<TodoRecord> {

    @Select("SELECT * FROM usertodo")
    List<TodoRecord> getAll();

    @Update("UPDATE usertodo " +
            "SET todo_title = #{todo_title} , todo_ctnt = #{todo_ctnt}," +
            "todo_fin = #{todo_fin},  todo_crt =  #{todo_crt}, todo_ddl=#{todo_ddl}" +
            " WHERE user_id = #{user_id} and todo_id = #{todo_id}")
            //Where另起一行前面要放个空格啊，不能放在前一行后面，不知道为什么啊
    int updateTodoRecord(int user_id, int todo_id, int adder_id, String todo_title,
                         String todo_ctnt, String todo_fin, String todo_crt, String todo_ddl);


    @Insert("INSERT INTO usertodo (todo_title, todo_ctnt, todo_fin, todo_crt, todo_ddl, user_id, adder_id)" +
            " VALUES(#{todo_title}, #{todo_ctnt}, #{todo_fin}, #{todo_crt}, #{todo_ddl}, #{user_id}, #{adder_id})")
    int insertTodoRecord(int user_id, int todo_id, int adder_id, String todo_title,
                         String todo_ctnt, String todo_fin, String todo_crt, String todo_ddl);

    @Delete("DELETE from usertodo WHERE todo_id = #{todo_id}")
    int deleteRecord(int todo_id);


    @Select("SELECT * FROM usertodo WHERE user_id = #{user_id}")
    List<TodoRecord> getRecordsByUserId(int user_id);

    @SelectProvider(type = SelectorProviderGy.class, method = "buildTodoSearchQuery")
    List<TodoRecord> searchByFieldAndKeyPersonal(String field, String key, int user_id);

    @Select("SELECT count(*) FROM usertodo")
    Integer getTotal();

    @Select("SELECT count(*) FROM usertodo WHERE todo_fin = '未完成' and adder_id = user_id")
    Integer getUnfinSelf();

    @Select("SELECT count(*) FROM usertodo WHERE todo_fin = '已完成' and adder_id = user_id")
    Integer getFinishedSelf();

    @Select("SELECT count(*) FROM usertodo WHERE todo_fin = '未完成' and adder_id != user_id")
    Integer getUnfinMana();

    @Select("SELECT count(*) FROM usertodo WHERE todo_fin = '已完成' and adder_id != user_id")
    Integer getFinishedMana();
}
