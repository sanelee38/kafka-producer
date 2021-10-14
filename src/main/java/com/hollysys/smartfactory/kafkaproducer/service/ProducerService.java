package com.hollysys.smartfactory.kafkaproducer.service;

import com.hollysys.smartfactory.kafkaproducer.entity.Producer;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * (Producer)表服务接口
 *
 * @author makejava
 * @since 2021-09-28 13:39:43
 */
public interface ProducerService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Producer queryById(Integer id);
    /**
     * 通过name查询单条数据
     *
     * @param name
     * @return 实例对象
     */
    List<Producer> queryByName(String name);


    List<Producer> queryByPro(Producer producer);
    /**
     * 分页查询
     *
     * @return 查询结果
     */
    List<Producer> findAll();

    /**
     * 新增数据
     *
     * @param producer 实例对象
     * @return 实例对象
     */
    Producer insert(Producer producer);
    /**
     * 批量新增数据
     *
     * @param producers 实例对象
     * @return 实例对象
     */

    @Transactional
    List<Producer> insertBatch(List<Producer> producers);
    /**
     * 修改数据
     *
     * @param producer 实例对象
     * @return 实例对象
     */
    int update(Producer producer);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Transactional
    boolean deleteById(Integer id);

    /**
     * 通过name删除数据
     *
     * @return 是否成功
     */
    @Transactional
    boolean deleteByName(Producer producer);
}
