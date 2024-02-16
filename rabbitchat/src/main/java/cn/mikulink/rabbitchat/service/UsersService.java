package cn.mikulink.rabbitchat.service;

import cn.mikulink.rabbitchat.entity.db.UsersInfo;
import cn.mikulink.rabbitchat.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MikuLink created in 2024/2/16 5:18
 * For the Reisen
 * <p>
 * 用户信息服务
 */
@Service
public class UsersService {
    @Autowired
    private UsersMapper mapper;

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    public List<UsersInfo> getAll() {
        return mapper.getAll();

    }
}
