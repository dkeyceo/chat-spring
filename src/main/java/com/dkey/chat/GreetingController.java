package com.dkey.chat;

import com.dkey.chat.dao.MessageDao;
import com.dkey.chat.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class GreetingController {
    @Autowired
    private MessageDao messageDao;
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Map model) {
        model.put("name", name);
        return "greeting";
    }
    @GetMapping
    public String main(Map model) {
        Iterable<Message> messages = messageDao.findAll();
        model.put("messages",messages);
        return "main";
    }
    @PostMapping
    public String add(@RequestParam String text, @RequestParam String tag,Map model ){
        Message message = new Message(text, tag);
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
