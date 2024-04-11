package cn.mikulink.rabbitchat.utils;

import org.junit.Test;

/**
 * created by MikuNyanya on 2024/4/12 3:54
 * For the Reisen
 */
public class PageUtilTest {

    @Test
    public void test(){
        try{
            int page  = 3;
            int pageSize = 10;

            int start = PageUtil.getLimit(page,pageSize);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
