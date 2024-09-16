package com.one.frontend.controller;

import com.one.frontend.model.GachaMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GachaWebSocketController {

    @MessageMapping("/draw")
    @SendTo("/topic/lottery")
    public GachaMessage sendLotteryMessage(GachaMessage message) throws Exception {
        // 在这里可以加入逻辑，判断是否中奖
        return message;
    }
}
