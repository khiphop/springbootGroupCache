package com.bj.spbc.controller.core;

import com.bj.spbc.helper.net.GameApiHelper;
import com.bj.spbc.service.RabbitmqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HealthController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private GameApiHelper gameApiHelper;

    @Autowired
    private RabbitmqService rabbitmqService;

    @ResponseBody
    @RequestMapping("/health")
    public Map<String, Object> get(@RequestParam(name = "user_id") Integer userId) throws IOException
    {
        Map<String, Object> ret = new HashMap<>();

        logger.info("user_id:" + userId);

        // Redirect
        if (1 == 2) {
            response.sendRedirect("https://www.zhihu.com/search?type=content&q=" + userId);
        }

        // cache query
        /*String cache = userCache.get(userId);
        ret.put("cache",cache);*/

        // http
        String httpRes = gameApiHelper.getUserInfo(userId);
        ret.put("http_res",httpRes);

        // rabbitMq Produce
        Map<String, Object> msgData = new HashMap<String, Object>();
        msgData.put("userId", userId.toString());
        rabbitmqService.produce("test", "test", msgData);

        return ret;
    }

}