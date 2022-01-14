package com.bj.spbc.bean;

import com.bj.spbc.helper.groupcache.NodeDispatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = { "classpath:config/node.properties" })
public class NodeDispatchBean {

    public NodeDispatch instance = null;

    @Value("${node.ip}")
    private String ip;

    @Value("${node.port}")
    private Integer port;

    @Bean
    public NodeDispatch getIns() {
        if (instance != null) {
            return instance;
        }

        instance = new NodeDispatch();
        instance.registerSelfNode(ip, port);

        return instance;
    }



}
