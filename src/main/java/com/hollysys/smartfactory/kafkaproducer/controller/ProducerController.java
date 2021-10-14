package com.hollysys.smartfactory.kafkaproducer.controller;

import com.alibaba.fastjson.JSON;
import com.hollysys.smartfactory.kafkaproducer.entity.Producer;
import com.hollysys.smartfactory.kafkaproducer.entity.resp.ResultStatus;
import com.hollysys.smartfactory.kafkaproducer.entity.resp.ReturnInfo;
import com.hollysys.smartfactory.kafkaproducer.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author : lizhi
 * @Date : Created in 2021/9/28 13:44
 * @Description :
 */
@RestController
@RequestMapping("/producer")
public class ProducerController extends BaseController{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProducerService producerService;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping("/All")
    public ResultStatus All(){
        List<Producer> producerList = producerService.findAll();
        if (producerList != null){
            return success(ReturnInfo.QUERY_SUCCESS_MSG,producerList);
        }else {
            return error(ReturnInfo.QUERY_FAIL_MSG);
        }
    }

    @GetMapping("/query/{id}")
    public ResultStatus selectById(@PathVariable Integer id){
        Producer producer = producerService.queryById(id);
        if (producer != null){
            return success(ReturnInfo.QUERY_SUCCESS_MSG,producer);
        }else {
            return error(ReturnInfo.QUERY_FAIL_MSG);
        }
    }

    @GetMapping("/queryByName/{name}")
    public ResultStatus selectByName(@PathVariable String name){
        List<Producer> producer = producerService.queryByName(name);
        if (producer != null){
            return success(ReturnInfo.QUERY_SUCCESS_MSG,producer);
        }else {
            return error(ReturnInfo.QUERY_FAIL_MSG);
        }
    }

    @GetMapping("/queryByPro")
    public ResultStatus selectByPro(@RequestBody Producer producer){
        List<Producer> producers = producerService.queryByPro(producer);
        if (producers != null){
            return success(ReturnInfo.QUERY_SUCCESS_MSG,producers);
        }else {
            return error(ReturnInfo.QUERY_FAIL_MSG);
        }
    }

    @PostMapping("/add")
    public ResultStatus add(@RequestBody Producer producer){
        producerService.insert(producer);
        Producer query = producerService.queryById(producer.getId());
        if (query!=null){
            List<Producer> producerList = new ArrayList<>();
            producerList.add(query);
            kafkaSend(producerList,"add");
            log.info(ReturnInfo.SAVE_SUCCESS_MSG,query);
            return success(ReturnInfo.SAVE_SUCCESS_MSG,query);
        }else {
            log.error(ReturnInfo.SAVE_FAIL_MSG);
            return error(ReturnInfo.SAVE_FAIL_MSG);
        }
    }


    @PostMapping("/insertBatch")
    public ResultStatus insertBatch(@RequestBody List<Producer> producers){
        List<Producer> producerList = producerService.insertBatch(producers);
        if (producerList!=null){
            kafkaSend(producerList,"insertBatch");
            log.info(ReturnInfo.SAVE_SUCCESS_MSG,producerList);
            return success(ReturnInfo.SAVE_SUCCESS_MSG,producerList);
        }else {
            log.error(ReturnInfo.SAVE_FAIL_MSG);
            return error(ReturnInfo.SAVE_FAIL_MSG);
        }
    }

    @PostMapping("/update")
    public ResultStatus update(@RequestBody Producer producer){
        Date date = new Date();
        producer.setUpdateTime(date);
        int updateResult = producerService.update(producer);
        if (updateResult>0){
            List<Producer> producerList = new ArrayList<>();
            producerList.add(producer);
            kafkaSend(producerList,"update");
            log.info(ReturnInfo.UPDATE_SUCCESS_MSG,producer);
            return success(ReturnInfo.UPDATE_SUCCESS_MSG,producer);
        }else {
            log.error(ReturnInfo.UPDATE_FAIL_MSG);
            return error(ReturnInfo.UPDATE_FAIL_MSG);
        }
    }

    @GetMapping("/deleteById/{id}")
    public ResultStatus deleteById(@PathVariable Integer id){
        boolean res = producerService.deleteById(id);
        Producer producer = new Producer();
        producer.setId(id);
        if (res){
            List<Producer> producerList = new ArrayList<>();
            producerList.add(producer);
            kafkaSend(producerList,"deleteById");
            log.info(ReturnInfo.DEL_SUCCESS_MSG);
            return success(ReturnInfo.DEL_SUCCESS_MSG);
        }else {
            log.error(ReturnInfo.DEL_FAIL_MSG);
            return error(ReturnInfo.DEL_FAIL_MSG);
        }
    }

    @GetMapping("/delete")
    public ResultStatus delete(@RequestBody Producer producer){
        boolean res = producerService.deleteByName(producer);
        if (res){
            List<Producer> producerList = new ArrayList<>();
            producerList.add(producer);
            kafkaSend(producerList,"delete");
            log.info(ReturnInfo.DEL_SUCCESS_MSG);
            return success(ReturnInfo.DEL_SUCCESS_MSG);
        }else {
            log.error(ReturnInfo.DEL_FAIL_MSG);
            return error(ReturnInfo.DEL_FAIL_MSG);
        }
    }

    @RequestMapping("/ip")
    public String getIp() throws UnknownHostException {
        System.out.println("log:----------------------->ip");
        String ip = InetAddress.getByName("ip").toString();
        System.out.println(ip);
        return ip;
    }

    public void kafkaSend(List<Producer> producerList,String message){
        ResultStatus resultStatus = new ResultStatus();
        resultStatus.setData(producerList);
        resultStatus.setMessage(message);
        resultStatus.setStatus(0);
        String producerString = JSON.toJSONString(resultStatus);
        try {
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("kafka-topic", producerString);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("kafka-topic - 生产者 发送消息失败：" + throwable.getMessage());
                    System.err.println("kafka-topic - 生产者 发送消息失败：" + throwable.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                    log.info("kafka-topic - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                    System.out.println("kafka-topic - 生产者 发送消息成功：" + stringObjectSendResult.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
