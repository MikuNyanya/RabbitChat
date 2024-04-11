package cn.mikulink.rabbitchat.utils;

/**
 * created by MikuNyanya on 2024/4/12 16:28
 * For the Reisen
 * 分页 用于limit
 */
public class PageUtil {

    public static int getLimit(int page, int pageSize) {
        return (page - 1) * pageSize;
    }
}
