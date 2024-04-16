package cn.mikulink.rabbitchat;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;

/**
 * created by MikuNyanya on 2024/4/16 18:34
 * For the Reisen
 */
public class UserlogoJsonStrTest {
    @Test
    public void test(){
        List<String> logoValue = new ArrayList<>();
        logoValue.add("IzumiKonata_logo.jpg");
        logoValue.add("logo1.jpg");
        logoValue.add("logo2.jpg");
        logoValue.add("logo3.jpg");
        logoValue.add("rabbit_logo.jpg");


        Map<String,String> map = new HashMap<>();
        map.put("url","");
        map.put("logoList", JSONObject.toJSONString(logoValue));


    }
}
