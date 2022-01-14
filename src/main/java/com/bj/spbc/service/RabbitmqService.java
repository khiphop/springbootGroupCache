package com.bj.spbc.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Repository
public class RabbitmqService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    Logger logger = LoggerFactory.getLogger(getClass());


    public void produce(String ex, String rk, Map<String, Object> data)
    {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        data.put("mid", messageId);
        data.put("ct", createTime);

        String jsonMsg = JSON.toJSONString(data);

        logger.info("produce_ready | msg:" + jsonMsg);

        rabbitTemplate.convertAndSend(ex, rk, jsonMsg);
    }

}
