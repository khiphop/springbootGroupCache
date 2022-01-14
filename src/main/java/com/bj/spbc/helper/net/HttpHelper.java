package com.bj.spbc.helper.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Repository
public
class HttpHelper {

    Logger logger = LoggerFactory.getLogger(getClass());

    public String post(String url, MultiValueMap<String, String> args)
    {
        logger.info("http_ready | url:" + url + " | args:" + JSON.toJSONString(args));

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(args, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );

        String rs = response.getBody();
        logger.info("http_res:" + rs);

        return rs;
    }

    public JSONObject get(String url)
    {
        RestTemplate re = new RestTemplate();
        HttpEntity<JSONObject> jsonObject= re.getForEntity(url,JSONObject.class);

        logger.info("http_res:" + jsonObject.getBody());

        return jsonObject.getBody();
    }

}
