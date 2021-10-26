package com.hollysys.smartfactory.kafkaproducer.service.impl;

import com.hollysys.smartfactory.kafkaproducer.dao.ProducerDao;
import com.hollysys.smartfactory.kafkaproducer.entity.Producer;
import com.hollysys.smartfactory.kafkaproducer.service.ProducerService;
import com.hollysys.smartfactory.kafkaproducer.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * (Producer)表服务实现类
 *
 * @author makejava
 * @since 2021-09-28 13:39:44
 */
@Service("producerService")
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    private ProducerDao producerDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    public static String key = "producers";
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Producer queryById(Integer id) {
        String idKey = "producers_" + id;
        ValueOperations<String, Producer> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(idKey);
        //缓存存在
        if (hasKey) {
            Producer producer = operations.get(idKey);
            System.out.println("获取缓存成功");
            redisUtil.expire(idKey, 600);
            return producer;
        } else {
            //从MySql中获取信息
            Producer producer = producerDao.queryById(id);
            // 插入缓存
            operations.set(idKey, producer);
            System.out.println("插入缓存成功");
            redisUtil.expire(idKey, 600);
            return producer;

        }
//        return this.producerDao.queryById(id);
    }

    @Override
    public List<Producer> queryByName(String name) {
        String nameKey = "producers_"+name;
        ValueOperations<String, List<Producer>> operations = redisTemplate.opsForValue();
        boolean haskey = redisTemplate.hasKey(nameKey);
        //缓存存在
        if (haskey) {
            System.out.println("获取缓存成功");
            List<Producer> producerList = operations.get(key);
            redisUtil.expire(key, 600);
            return producerList;
        } else {
            //从MySql中获取信息
            List<Producer> producerList = producerDao.queryByName(name);
            //插入缓存
            operations.set(nameKey, producerList);
            redisUtil.expire(nameKey, 600);
            System.out.println("插入缓存成功");
            return producerList;
        }
    }

    @Override
    public List<Producer> queryByPro(Producer producer) {
//        if (producer.getId()!=null&&producer.getName()!=null){
//            String idKey = "producers_"+producer.getId();
//            ValueOperations<String, Producer> operations = redisTemplate.opsForValue();
//            String nameKey = "producers_"+producer.getName();
//            ValueOperations<String, List<Producer>> operationlist = redisTemplate.opsForValue();
//            boolean hasKey = redisTemplate.hasKey(nameKey);
//            boolean hasIdKey = redisTemplate.hasKey(idKey);
//
//            if (hasIdKey){
//                Producer producerId = operations.get(idKey);
//                System.out.println("获取缓存成功");
//                redisUtil.expire(idKey, 600);
//                List<Producer> resPro = new ArrayList<>();
//                resPro.add(producerId);
//                return resPro;
//            }else if (hasKey){
//                System.out.println("获取缓存成功");
//                List<Producer> producerList = operationlist.get(key);
//                redisUtil.expire(key, 600);
//                return producerList;
//            }else {
//
//                operationlist.set(nameKey, producers);
//                redisUtil.expire(nameKey, 600);
//                System.out.println("插入缓存成功");
//                return producers;
//            }
//        }else {
//            String ProKey = "producers_Pro";
//            ValueOperations<String, List<Producer>> operationlist = redisTemplate.opsForValue();
//
//        }
        List<Producer> producers = producerDao.queryByPro(producer);
        return producers;
    }

    @Override
    public List<Producer> findAll() {
//        String key = "producers";
        ValueOperations<String, List<Producer>> operations = redisTemplate.opsForValue();
        boolean haskey = redisTemplate.hasKey(key);
        //缓存存在
        if (haskey) {
            System.out.println("获取缓存成功");
            List<Producer> producerList = operations.get(key);
            redisUtil.expire(key, 600);
            return producerList;
        } else {
            //从MySql中获取信息
            List<Producer> producerList = producerDao.queryAll();
            //插入缓存
            operations.set(key, producerList);
            redisUtil.expire(key, 600);
            System.out.println("插入缓存成功");
            return producerList;
        }
//        return producerDao.queryAll();
    }


    /**
     * 新增数据
     *
     * @param producer 实例对象
     * @return 实例对象
     */
    @Override
    public Producer insert(Producer producer) {

        ValueOperations<String, List<Producer>> operations = redisTemplate.opsForValue();
//        String key = "producers";
        List<Producer> producerList = operations.get(key);
        if (producerList != null) {
            boolean haskey = producerList.contains(producer);
            if (!haskey) {
                List<Producer> producerList1 = this.producerDao.queryByName(producer.getName());
                for (int i = 0; i < producerList1.size(); i++) {
                    producerList.add(producerList1.get(i));
                }
                operations.set(key, producerList);
                System.out.println("插入缓存");
                redisUtil.expire(key, 600);
            }
        }
        this.producerDao.insert(producer);
        return producer;
    }

    @Override
    public List<Producer> insertBatch(List<Producer> producers) {
        this.producerDao.insertBatch(producers);
        //批量增加删除缓存
//        String key = "producers";
        redisTemplate.delete(key);
        return producers;
    }


    /**
     * 修改数据
     *
     * @param producer 实例对象
     * @return 实例对象
     */
    @Override
    public int update(Producer producer) {

        ValueOperations<String, List<Producer>> operations = redisTemplate.opsForValue();
//        String key = "producers";
        String idkey = "producers_" + producer.getId();
        List<Producer> producerList = operations.get(key);
        boolean hasKeyId = redisTemplate.hasKey(idkey);
        //缓存存在，更改缓存,然后将修改数据写入缓存
        if (producerList!=null) {
            Producer producer1 = producerList.get(producer.getId() - 1);
            producer1.setItem(producer.getItem());
            producer1.setName(producer.getName());
            producer1.setUpdateTime(producer.getUpdateTime());
            Collections.replaceAll(producerList,producerList.get(producer.getId() - 1),producer1);
            operations.set(key, producerList);
            System.out.println("插入缓存");
            redisUtil.expire(key, 600);
        }
        if (hasKeyId) {
            redisTemplate.delete(idkey);
        }
        int update = this.producerDao.update(producer);
        return update;
    }


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        int ret = this.producerDao.deleteById(id);
        //缓存存在，删除缓存
//        String key = "producers";
        String idKey = "producers_" + id;
        ValueOperations<String, List<Producer>> operations = redisTemplate.opsForValue();
        List<Producer> producerList = operations.get(key);
        boolean hasKeyId = redisTemplate.hasKey(idKey);
        if (producerList != null) {
            for (int i = 0; i < producerList.size(); i++) {
                if (producerList.get(i).getId().equals(id)) {
                    producerList.remove(i);
                    operations.set(key, producerList);
                    System.out.println("插入缓存");
                    redisUtil.expire(key, 600);
                }
            }
        }
        if (hasKeyId) {
            redisTemplate.delete(idKey);
        }
        this.producerDao.alterTable(id);
        return ret > 0;
    }


    @Override
    public boolean deleteByName(Producer producer) {
        List<Producer> producers = this.producerDao.queryByName(producer.getName());
        if (producer==null){
            return false;
        }else {
            int id = producers.get(0).getId();
            int ret = this.producerDao.deleteByName(producer.getName());
//        String key = "producers";
            String idKey = "producers_" + id;
            ValueOperations<String, List<Producer>> operations = redisTemplate.opsForValue();
            List<Producer> producerList = operations.get(key);
            boolean hasKeyId = redisTemplate.hasKey(idKey);
            if (producerList!=null){
                for (int i = 0; i < producerList.size(); i++) {
                    if (Objects.equals(producerList.get(i).getName(), producer.getName())) {
                        producerList.remove(i);
                    }
                }
                redisTemplate.delete(key);
                operations.set(key, producerList);
                redisUtil.expire(key, 600);
            }
            if (hasKeyId) {
                redisTemplate.delete(idKey);
            }
            this.producerDao.alterTable(id);
            return ret > 0;
        }
    }
}
