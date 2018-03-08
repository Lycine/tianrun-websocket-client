package com.channelsoft;

//事件处理handler

import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.*;

public class WebSocketSessionHandler implements StompSessionHandler {
    private StompSession stompSession;

    private String sessionKey;

    public WebSocketSessionHandler(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        System.out.println("连接建立成功");


        this.stompSession = stompSession;
        StompHeaders subscribeHeaders = new StompHeaders();
        subscribeHeaders.setDestination("/topic/agent");
        List<String> agentList = new LinkedList<String>();
        agentList.add("2000");  //座席工号
        agentList.add("2001");
        agentList.add("2002");
        subscribeHeaders.put("agentList", agentList); //设置需要接收哪些座席的事件, 不设置接收所有座席的事件
        //订阅
        stompSession.subscribe(subscribeHeaders, new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return Greeting.class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
//                System.out.println("收到消息: " + o.toString());
                Greeting greeting = (Greeting) o;
                System.out.println(greeting.getContent());
            }
        });


        //每60秒进行一次ping操作
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, 0, 60000);


        stompSession.send("/app/hello", new Greeting("Dave"));
    }

    public void ping() {
        //发送ping消息
        Map<String, String> map = new HashMap<String, String>();
        //传入sessionKey
        map.put("sessionKey", sessionKey);
        stompSession.send("/app/agent/handle/ping", map);
    }

    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        System.out.println("123123");
    }

    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        System.out.println("123123456");
    }

    public Type getPayloadType(StompHeaders stompHeaders) {
        System.out.println("123123444");
        return null;
    }

    public void handleFrame(StompHeaders stompHeaders, Object o) {
        System.out.println("222123123");
    }
}