package org.example;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 表示这是一个 MyBatis 的 Mapper 接口，用于执行数据库操作
public interface AdminMapper extends BaseMapper<Admin> {
}
