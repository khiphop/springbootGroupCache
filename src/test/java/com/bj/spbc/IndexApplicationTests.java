package com.bj.spbc;

import com.bj.spbc.helper.config.AmqpHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class IndexApplicationTests {


    @Autowired
    AmqpHelper amqpHelper;

    @Test
    void contextLoads() {
        getAmqpHost();
    }


    public void getAmqpHost() {
        String host = amqpHelper.getHost();

        System.out.println(host);
    }

    public void post() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://127.0.0.1:8001";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("email", "123@qq.com");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println(response.getBody());
    }

    public void map() {
        Map<String, Object> map = new HashMap<String, Object>();//创建Map对象，Object是所有类型的父类
        map.put("publish", "publish_val");//存储key和value
        map.put("status", "status_val");
        map.get("publish");//获取相应key的值

        System.out.println(map.get("publish"));
        System.out.println(map);
    }
}
