package cn.mikulink.rabbitchat.websocket;

import cn.mikulink.rabbitchat.entity.db.ChatRecordInfo;
import cn.mikulink.rabbitchat.entity.db.UsersInfo;
import cn.mikulink.rabbitchat.entity.param.ChatRecordParam;
import cn.mikulink.rabbitchat.entity.response.MethodReInfo;
import cn.mikulink.rabbitchat.entity.response.chat.ChatRecordVo;
import cn.mikulink.rabbitchat.service.ChatRecordService;
import cn.mikulink.rabbitchat.service.UsersService;
import cn.mikulink.rabbitchat.utils.DateUtil;
import cn.mikulink.rabbitchat.utils.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MikuLink created in 2024/2/16 5:45
 * For the Reisen
 * <p>
 * WebStocket操作类
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数，线程安全的类。
     */
    private static AtomicInteger onlineSessionClientCount = new AtomicInteger(0);

    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Session> onlineSessionClientMap = new ConcurrentHashMap<>();

    private static UsersService usersService;

    @Autowired
    public void setUsersService(UsersService usersService) {
        WebSocketServer.usersService = usersService;
    }

    private static ChatRecordService chatRecordService;

    @Autowired
    public void setUsersService(ChatRecordService chatRecordService) {
        WebSocketServer.chatRecordService = chatRecordService;
    }

    /**
     * 连接建立成功调用的方法。由前端<code>new WebSocket</code>触发
     *
     * @param sid     每次页面建立连接时传入到服务端的id，比如用户id等。可以自定义。
     * @param session 与某个客户端的连接会话，需要通过它来给客户端发送消息
     */
    @OnOpen
    public void onOpen(@PathParam("sid") String sid, Session session) {
        /**
         * session.getId()：当前session会话会自动生成一个id，从0开始累加的。
         */
        log.info("客户端连接建立中：sid:{},sessionId:{}", sid, session.getId());
        //检测sid是否合法 检测sid与会话权限是否对等 若检测不通过则拒绝连接
        Map<String, List<String>> params = session.getRequestParameterMap();
        List<String> chatAuthParam = params.get("chatAuth");
        if (null == chatAuthParam || chatAuthParam.size() <= 0) {
            //未携带会话权限
            return;
        }
        String chatAuth = chatAuthParam.get(0);
        MethodReInfo reInfo = usersService.chatAuthCheck(sid, chatAuth);
        if (!reInfo.isSucess()) {
            log.info("会话授权检验失败，拒绝客户端的连接 sid:{},sessionId:{},chatAuth:{}", sid, session.getId(), chatAuth);
            return;
        }

        //身份验证完成后 保存会话 并向客户端回执连接建立成功
        //覆盖掉已存在的session 并通知重复登录
        if (onlineSessionClientMap.get(sid) != null) {
            //通知用户已顶掉该账号的当前登录状态
            log.info("用户重复登录，sid:{}", sid);
        } else {
            //在线数加1
            onlineSessionClientCount.incrementAndGet();
        }
        onlineSessionClientMap.put(sid, session);

        log.info("连接建立成功，当前在线数为：{},开始监听新连接：session_id: {}， sid: {}", onlineSessionClientCount, session.getId(), sid);

        WebSocketMessage message = new WebSocketMessage(chatAuth, 0, "0", null, null, sid, null,
                "SUCCESS", DateUtil.toString(new Date()), "与服务器连接成功");
        //向客户端通知连接成功
        sendToOne(message);
    }

    /**
     * 连接关闭调用的方法。由前端<code>socket.close()</code>触发
     *
     * @param sid     客户端sid
     * @param session 与某个客户端的连接会话，需要通过它来给客户端发送消息
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid, Session session) {
        //onlineSessionIdClientMap.remove(session.getId());
        // 从 Map中移除
        onlineSessionClientMap.remove(sid);

        //在线数减1
        onlineSessionClientCount.decrementAndGet();
        log.info("连接关闭成功，当前在线数为：{},关闭该连接信息：session_id: {}， sid: {}", onlineSessionClientCount, session.getId(), sid);
    }

    /**
     * 收到客户端消息后调用的方法。由前端<code>socket.send</code>触发
     * * 当服务端执行toSession.getAsyncRemote().sendText(xxx)后，前端的socket.onmessage得到监听。
     *
     * @param message 客户端消息
     * @param session 与某个客户端的连接会话，需要通过它来给客户端发送消息
     */
    @OnMessage
    public void onMessage(@PathParam("sid") String sid, String message, Session session) {
        log.info("服务端收到客户端消息 sid:{}, message:{}", sid, message);
        WebSocketMessage msgInfo = JSONObject.parseObject(message, WebSocketMessage.class);
        //检查授权码
        MethodReInfo methodReInfo = usersService.chatAuthCheck(sid, msgInfo.getChatAuth());
        if (!methodReInfo.isSucess()) {
            log.info("接收到无授权的通讯 sid:{}, auth:{}, message:{}", sid, msgInfo.getChatAuth(), message);
            return;
        }

//        WebSocketMessage returnMsg = new WebSocketMessage(msgInfo.getChatAuth(), 1, "0",
//                null, sid, null,"SUCCESS", DateUtil.toString(new Date()), "返回消息测试");

        //业务分流
        if (msgInfo.getMessageSendType() == 3) {
            //获取用户列表
            sendUserList(msgInfo);
        }

        if (msgInfo.getMessageSendType() == 4) {
            //获取用户信息
            sendUserInfo(msgInfo);
        }

        if (msgInfo.getMessageSendType() == 5) {
            //获取历史消息
            sendChatRecordHistory(msgInfo);
        }

        //消息
        if (msgInfo.getMessageSendType() == 1 || msgInfo.getMessageSendType() == 2) {
            sendMessage(msgInfo);
        }
    }

    /**
     * 发生错误调用的方法
     *
     * @param session 与某个客户端的连接会话，需要通过它来给客户端发送消息
     * @param error   异常
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket发生错误.sessionId:{}", session.getId(), error);
    }

    /**
     * 群发消息
     *
     * @param message 消息
     */
    private void sendToAll(String message) {
        // 遍历在线map集合
        onlineSessionClientMap.forEach((onlineSid, toSession) -> {
            log.info("服务端群发消息:toSid:{}, message:{}", onlineSid, message);
            toSession.getAsyncRemote().sendText(message);
        });
    }

    /**
     * 指定发送消息
     *
     * @param messageInfo 消息内容
     */
    private void sendToOne(WebSocketMessage messageInfo) {
        String message = JSONObject.toJSONString(messageInfo);
        // 通过sid查询map中是否存在
        Session toSession = onlineSessionClientMap.get(messageInfo.getToUid());
        if (toSession == null) {
            log.warn("消息发送失败，sid不存在,该用户当前不在线，message:{}", message);
            return;
        }
        // 异步发送
        log.info("向客户端发送消息,toUid:{},message:{}", messageInfo.getToUid(), message);
        toSession.getAsyncRemote().sendText(message);
        /*
        // 同步发送
        try {
            toSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息失败，WebSocket IO异常");
            e.printStackTrace();
        }*/
    }


    private void sendToGroup(WebSocketMessage messageInfo) {
        String message = JSONObject.toJSONString(messageInfo);

        //获取该群组人员列表

        //发送给session在线群员


        for (String uid : onlineSessionClientMap.keySet()) {
            if (uid == null) {
                continue;
            }
            // 异步发送
            log.info("群组消息，向客户端发送消息,toUid:{},message:{}", uid, message);
            onlineSessionClientMap.get(uid).getAsyncRemote().sendText(message);
        }
    }

    private void sendMessage(WebSocketMessage msgInfo) {
        //消息先落库 然后异步发送
        try {
            ChatRecordInfo info = new ChatRecordInfo();
            info.setCreateTime(new Date());
            info.setType(0);
            info.setStatus(0);
            Long fromUid = NumberUtil.toLong(msgInfo.getFromUid());
            info.setFromId(fromUid);
            info.setToId(NumberUtil.toLong(msgInfo.getToUid()));
            info.setContent(msgInfo.getMessage());
            info.setReadStatus(0);
            info.setReadTime(null);
            chatRecordService.create(info);

        } catch (Exception ex) {
            log.error("消息保存异常", ex);
        }


        if (msgInfo.getMessageSendType() == 1) {
            //发送到用户
            msgToUser(msgInfo);
        }


        if (msgInfo.getMessageSendType() == 2) {
            //发送到群组
            msgToGroup(msgInfo);
        }
    }

    //发送消息给用户
    private void msgToUser(WebSocketMessage msgInfo) {
        //是否互为联系人

        //对目标人发送消息
        UsersInfo usersInfo = usersService.getById(NumberUtil.toLong(msgInfo.getFromUid()));

        if (null == usersInfo) {
            log.error("用户消息异常，用户不存在,uid:{}", msgInfo.getFromUid());
            return;
        }

        WebSocketMessage sendMsg = new WebSocketMessage(null, 1, String.valueOf(msgInfo.getFromUid()), usersInfo.getName(),
                null, String.valueOf(msgInfo.getToUid()), null, "SUCCESS", DateUtil.toString(new Date()),
                msgInfo.getMessage());
        this.sendToOne(sendMsg);
    }

    //发送消息到群组
    private void msgToGroup(WebSocketMessage msgInfo) {
        UsersInfo usersInfo = usersService.getById(NumberUtil.toLong(msgInfo.getFromUid()));
        msgInfo.setFromUserImg(usersInfo.getUserImg());
        msgInfo.setFromUname(usersInfo.getName());
        //发送给群组
        sendToGroup(msgInfo);
    }

    //返回用户列表
    private void sendUserList(WebSocketMessage msgInfo) {
        List<UsersInfo> usersInfos = usersService.getAll();

        WebSocketMessage sendMsg = new WebSocketMessage(null, 3, "0", null,
                null, String.valueOf(msgInfo.getFromUid()), null, "SUCCESS", DateUtil.toString(new Date()),
                JSONObject.toJSONString(usersInfos));
        this.sendToOne(sendMsg);
    }

    private void sendUserInfo(WebSocketMessage msgInfo) {
        UsersInfo usersInfo = usersService.getById(NumberUtil.toLong(msgInfo.getFromUid()));

        WebSocketMessage sendMsg = new WebSocketMessage(null, 4, "0", null,
                null, String.valueOf(msgInfo.getFromUid()), null, "SUCCESS", DateUtil.toString(new Date()),
                JSONObject.toJSONString(usersInfo));
        this.sendToOne(sendMsg);
    }

    //返回用户列表
    private void sendChatRecordHistory(WebSocketMessage msgInfo) {
        ChatRecordParam param = JSONObject.parseObject(msgInfo.getMessage(),ChatRecordParam.class);

        List<ChatRecordInfo> list = chatRecordService.getChatRecordHistory(param);
        Map<Long,UsersInfo> tempMap = new HashMap<>();

        List<ChatRecordVo> result = new ArrayList<>();
        for (ChatRecordInfo chatRecordInfo : list) {
            UsersInfo usersInfo = null;
            if(tempMap.containsKey(chatRecordInfo.getFromId())){
                usersInfo = tempMap.get(chatRecordInfo.getFromId());
            }else{
                usersInfo = usersService.getById(chatRecordInfo.getFromId());
                tempMap.put(chatRecordInfo.getFromId(),usersInfo);
            }
            if(null == usersInfo){
                continue;
            }

            ChatRecordVo vo = new ChatRecordVo();
            BeanUtils.copyProperties(chatRecordInfo,vo);
            vo.setFromUserName(usersInfo.getName());
            vo.setFromUserImg(usersInfo.getUserImg());

            result.add(vo);
        }

        WebSocketMessage sendMsg = new WebSocketMessage(null, 5, "0", null,
                null, String.valueOf(msgInfo.getFromUid()), null, "SUCCESS", DateUtil.toString(new Date()),
                JSONObject.toJSONString(result));
        this.sendToOne(sendMsg);
    }
}
