package cn.mikulink.rabbitchat.utils;

import org.junit.Test;

/**
 * MikuLink created in 2024/2/18 5:47
 * For the Reisen
 */
public class Des3UtilTest {
    String key = "IE37Ih9GfBM8oKajGMEGE37u";

    @Test
    public void enCode() {
        String str = "123asdsa";

        String enStr = Des3Util.encrypt(key, str);

        System.out.println(enStr);
    }

    @Test
    public void deCode() {
        String code = "FjMmc0CNlzVftt+5b2Dar8dRw7VJlsA0KYvrENKBczC/xBoiAh0tpw==";

        String str = Des3Util.decrypt(key, code);

        System.out.println(str);
    }
}
