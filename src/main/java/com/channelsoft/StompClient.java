package com.channelsoft;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.jetty.JettyWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.util.ArrayList;
import java.util.List;

public class StompClient {

    public static void main(String[] args) {
        System.out.println("hello");
//        //1. 权限验证
//        String apiUrl = "http://api-2.cticloud.cn/interface/v10/enterpriseLogin/authenticate?validateType=1&departmentId=BM0000001&enterpriseId=&timestamp=1480736832&sign=7200afb3676f010313b60c9a94518a1c";
//        String sessionKey = "";
//        String agentGateWayUrl = "";
//        CloseableHttpClient httpClient = HttpClients.custom().build();
//        HttpGet httpGet = new HttpGet(apiUrl);
//        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000)
//                .setConnectTimeout(2000).build();
//        httpGet.setConfig(requestConfig);
//        try {
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            String result = EntityUtils.toString(response.getEntity());
//            if (result != null && result.length() > 0) {
//                JSONObject jsonObject = new JSONObject(result);
//                if (jsonObject.getInt("result") == 0) {  //返回成功
//                    sessionKey = jsonObject.getString("sessionKey");
//                    agentGateWayUrl = jsonObject.getString("agentGateWayUrl");
//                }
//            }
//        } catch (Exception e) {
//            //error happen
//        }
        System.out.println("hello");

        //2. 权限验证成功, 建立连接
        List<Transport> transports = new ArrayList<Transport>();  //设置协议
        transports.add(new WebSocketTransport(new JettyWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        SockJsClient webSocketClient = new SockJsClient(transports);
        webSocketClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String agentGateWayUrl = "ws://localhost:8080/agent";
        String sessionKey = "201803081424";
//        String webSocketUrl = "ws://" + agentGateWayUrl + "/agent?sessionKey=" + sessionKey;
        String webSocketUrl = agentGateWayUrl + "?sessionKey=" + sessionKey;
        StompSessionHandler sessionHandler = new WebSocketSessionHandler(sessionKey);  //定义session handler
        stompClient.connect(webSocketUrl, sessionHandler);  //建立连接
    }
}