package com.hollysys.smartfactory.kafkaproducer.dao;

import com.hollysys.smartfactory.kafkaproducer.entity.Producer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (Producer)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-28 13:39:42
 */
@Mapper
public interface ProducerDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Producer queryById(Integer id);


    List<Producer> queryByName(String name);

    /**
     * 查询指定行数据
     *
     * @return 对象列表
     */
    List<Producer> queryAll();

    /**
     * 统计总行数
     *
     * @param producer 查询条件
     * @return 总行数
     */
    long count(Producer producer);

    /**
     * 新增数据
     *
     * @param producer 实例对象
     * @return 影响行数
     */
    int insert(Producer producer);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Producer> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Producer> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Producer> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Producer> entities);

    /**
     * 修改数据
     *
     * @param producer 实例对象
     * @return 影响行数
     */
    int update(Producer producer);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    void alterTable(Integer id);

    int deleteByName(String name);


    List<Producer> queryByPro(Producer producer);
}

