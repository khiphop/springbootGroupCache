package com.bj.spbc.controller.kache;

import com.bj.spbc.bean.NodeDispatchBean;
import com.bj.spbc.helper.groupcache.NodeDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NodeDispatchBean nodeDispatchBean;

    public Map<String, Object> returnFormat(int code , String msg, String data){
        Map<String, Object> ret = new HashMap<>();

        if (code == 0) {
            code = 200;
        }

        if (Objects.equals(msg, "")) {
            msg = "success";
        }

        ret.put("code", code);
        ret.put("msg", msg);
        ret.put("data", data);

        return ret;
    }


    @ResponseBody
    @RequestMapping("/cache/{group}/{key}")
    public Map<String, Object> get(
            @PathVariable String  group,
            @PathVariable String  key
    ) throws InterruptedException {
        NodeDispatch nodeDispatch = nodeDispatchBean.getIns();

        return returnFormat(200, "success", nodeDispatch.getHandler(group, key, false));
    }


    @ResponseBody
    @RequestMapping("/inner/{group}/{key}")
    public Map<String, Object> innerGet(
            @PathVariable String  group,
            @PathVariable String  key
    ) throws InterruptedException {
        NodeDispatch nodeDispatch = nodeDispatchBean.getIns();

        return returnFormat(200, "success", nodeDispatch.getHandler(group, key, true));
    }

    @ResponseBody
    @RequestMapping(value = {"/cache"}, method = {RequestMethod.POST})
    public Map<String, Object> set(
            @RequestParam(name = "group") String group,
            @RequestParam(name = "key") String key,
            @RequestParam(name = "val") String val
    ) throws InterruptedException {
        NodeDispatch nodeDispatch = nodeDispatchBean.getIns();
        nodeDispatch.setHandler(group, key, val, false);

        return returnFormat(200, "success", "");
    }

    @ResponseBody
    @RequestMapping(value = {"/inner"}, method = {RequestMethod.POST})
    public Map<String, Object> innerSet(
            @RequestParam(name = "group") String group,
            @RequestParam(name = "key") String key,
            @RequestParam(name = "val") String val
    ) throws InterruptedException {
        System.out.println(group);
        System.out.println(key);
        System.out.println(val);
        NodeDispatch nodeDispatch = nodeDispatchBean.getIns();
        nodeDispatch.setHandler(group, key, val, true);

        return returnFormat(200, "success", "");
    }

    @ResponseBody
    @RequestMapping(value = {"/node/{ip}/{port}"}, method = {RequestMethod.POST})
    public Map<String, Object> registerNode(
            @PathVariable String ip,
            @PathVariable String port
    ) throws InterruptedException {
        NodeDispatch nodeDispatch = nodeDispatchBean.getIns();

        nodeDispatch.registerNode(ip + ":" + port);

        return returnFormat(200, "success", "");
    }

}