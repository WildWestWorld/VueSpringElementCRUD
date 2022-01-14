package com.example.demo.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/imserver/{username}")
@Component
public class WebSocketServer {
    //初始化LoggerFactory对象 初始化日志  LoggerFactory.getLogger(xx.class) xx.class就是要加入日志功能的类
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 记录当前在线连接数
     */
    //ConcurrentHashMap是并发效率更高的Map，用来替换其他线程安全的Map容器，比如Hashtable和Collections.synchronizedMap。
    //实际上，并发执行时，线程安全的容器只能保证自身的数据不被破坏，但无法保证业务的行为是否正确。
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    //	当打开连接后触发的函数
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        //map中放入传入进来的username和session
        //键名是username，键值是session
        //后面我们会根据这里填写的username找到放在这里的session
        sessionMap.put(username, session);
        //此时的sessionMap为{lijiekai1998:"asdasdasdasdasdasd"}

        //日志输出 log.info里面的{} = 后面写入的值 sessionMap.size()=map里面有的大小 利用该大小就能确定当前在线的人数
        //示例: 有新用户加入，username=lijiekai1998, 当前在线人数为：1
        log.info("有新用户加入，username={}, 当前在线人数为：{}", username, sessionMap.size());
        //新建一个Json格式的对象
        JSONObject result = new JSONObject();
        //新建一个Json格式的数组
        JSONArray array = new JSONArray();
        //对象放入一个数组，键名是users，键值是array  现在的Json格式对象result = {user:[]}
        result.set("users", array);

        //sessionMap.keySet() 相当于map的一个Key的数列
        //循环出来的key 其实是我们之前放入的username
        for (Object key : sessionMap.keySet()) {
            //新建一个Json格式的对象
            JSONObject jsonObject = new JSONObject();
            //对象中放入键名username 键值为key  key其实是我们前面放入的username
            jsonObject.set("username", key);
            //名字是users的数组中放入我们刚刚创建的对象
            //此时的array：["username":key的值] key的值=键名  键名=我们传入的username
            array.add(jsonObject);
        }
        //举例：循环完毕的 result ={"users":[{"username":"qq380686356"},{"username":"lijiekai1998"}]}



        //这个是我们自己自定义的方法sendAllMessage 在组下面
        //JSONUtil.toJsonStr(result)=将result变成字符串格式
        sendAllMessage(JSONUtil.toJsonStr(result));  // 后台发送消息给所有的客户端
    }
    /**
     * 服务端发送消息给所有客户端
     */
    //这个message是上面我们自己弄的那个  result ={"users":[{"username":"qq380686356"},{"username":"lijiekai1998"}]}
    private void sendAllMessage(String message) {
        try {

            //此时的sessionMap为{lijiekai1998:"asdasdasdasdasdasd"}
            //sessionMap.values()="asdasdasdasdasdasd"
            for (Session session : sessionMap.values()) {


              //session.getId 里面的ID就是0,1,2,3,4.....
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                //发送消息
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }


    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     * @param message 客户端发送过来的消息
     */
    //此时的message就是前端传进来的数据了
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("服务端收到用户username={}的消息:{}", username, message);
        //解码前端传进来的数据
        JSONObject obj = JSONUtil.parseObj(message);
        //getStr，获得键值 text的键值就是我们输入的内容
        String toUsername = obj.getStr("to"); // to表示发送给哪个用户，比如 admin
        String text = obj.getStr("text"); // 发送的消息文本  hello
        // {"to": "admin", "text": "聊天文本"}

        //从我们之前弄的定义的sessionMap利用键名user取出键值session
        Session toSession = sessionMap.get(toUsername);
        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}

            //创建一个新的json格式对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", username);  // from 是 zhang
            jsonObject.set("text", text);  // text 同上面的text

            this.sendMessage(jsonObject.toString(), toSession);
            log.info("发送给用户username={}，消息：{}", toUsername, jsonObject.toString());
        } else {
            log.info("发送失败，未找到用户username={}的session", toUsername);
        }
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            //核心代码，把前端传入的信息发送
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        //sessionMapper移除键名为{username}的内容
        sessionMap.remove(username);

        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", username, sessionMap.size());
    }



    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }




}

