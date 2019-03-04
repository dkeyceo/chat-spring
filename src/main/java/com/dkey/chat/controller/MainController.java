package com.dkey.chat.controller;

import com.dkey.chat.dao.MessageDao;
import com.dkey.chat.model.Message;
import com.dkey.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private MessageDao messageDao;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/")
    public String greeting(Map model) {
        return "greeting";
    }
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = messageDao.findAll();
        if(filter!=null && !filter.isEmpty()) {
            messages = messageDao.findByTag(filter);
        }else{
            messages = messageDao.findAll();
        }
        model.addAttribute("messages",messages);
        model.addAttribute("filter", filter);
        return "main";
    }
    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Message message,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam("file") MultipartFile file) throws IOException {
        message.setAuthor(user);
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);
        }else {
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                message.setFilename(resultFilename);
            }
            model.addAttribute("message", null);
            messageDao.save(message);
        }
        Iterable<Message> messages = messageDao.findAll();
        model.addAttribute("messages",messages);
        return "main";
    }

}
