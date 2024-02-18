package cn.mikulink.rabbitchat.utils;

import org.junit.Test;

/**
 * MikuLink created in 2024/2/18 5:44
 * For the Reisen
 */
public class RandomUtilTest {

    @Test
    public void randStr(){
        String str = RandomUtil.randStr(24);

        System.out.println(str);
    }
}
