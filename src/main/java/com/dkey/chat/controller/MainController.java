package com.dkey.chat.controller;

import com.dkey.chat.dao.MessageDao;
import com.dkey.chat.model.Message;
import com.dkey.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private MessageDao messageDao;
    @GetMapping("/")
    public String greeting(Map model) {
        return "greeting";
    }
    @GetMapping("/main")
    public String main(Map model) {
        Iterable<Message> messages = messageDao.findAll();
        model.put("messages",messages);
        return "main";
    }
    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String text, @RequestParam String tag, Map model ){
        Message message = new Message(text, tag,user);
        messageDao.save(message);
        Iterable<Message> messages = messageDao.findAll();
        model.put("messages",messages);
        return "main";
    }
    @PostMapping("filter")
    public String add(@RequestParam String filter,Map model ){
        Iterable<Message> messages;
        if(filter!=null && !filter.isEmpty()) {
            messages = messageDao.findByTag(filter);
        }else{
            messages = messageDao.findAll();
        }
        model.put("messages",messages);
        return "main";
    }

}