package cn.mikulink.rabbitchat.mapper;

import cn.mikulink.rabbitchat.entity.db.UsersInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * MikuLink created in 2024/2/16 5:05
 * For the Reisen
 * <p>
 * mapper容器
 * 用户信息
 */
@Mapper
public interface UsersMapper {
    List<UsersInfo> getAll();
}
