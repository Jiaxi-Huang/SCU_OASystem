package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Weather;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WeatherMapper extends BaseMapper<Weather> {
    @Select("SELECT * FROM weather WHERE city = #{city} AND date = #{date} LIMIT 1")
    Weather findByCityAndDate(@Param("city") String city, @Param("date") String date);
}
