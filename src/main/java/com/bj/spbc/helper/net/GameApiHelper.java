package com.bj.spbc.helper.net;

import com.bj.spbc.constant.GameApiConst;
import com.bj.spbc.constant.UrlConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Repository
public
class GameApiHelper {

    private static final String ROUTE_FIELD = "route";

    @Autowired
    private HttpHelper httpHelper;

    public String getUserInfo(Integer userId) {
        MultiValueMap<String, String> args = new LinkedMultiValueMap<>();
        args.add("userId", userId.toString());

        String url = UrlConst.ADMIN_API + "?" + ROUTE_FIELD + "=" + GameApiConst.GET_USER_INFO;

        return httpHelper.post(url, args);
    }

}
