package zebraservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zebraservice.bot.ZebraServiceBot;
import zebraservice.model.TelegramMessageEntity;
import zebraservice.model.TelegramUser;
import zebraservice.repositories.TelegramMessageRepository;
import zebraservice.repositories.TelegramUserRepository;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/rest")
public class MainRestController {
    @Autowired
    private TelegramUserRepository userRepository;

    @Autowired
    private ZebraServiceBot zebraServiceBot;

    @Autowired
    private TelegramMessageRepository messageRepository;

    @RequestMapping("/getusers")
    public List<TelegramUser> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/updateuser")
    public void updateUser(@RequestBody TelegramUser usr) {
        userRepository.save(usr);
    }

    @RequestMapping("/getadmins")
    public List<TelegramUser> getAdmins(){
        return userRepository.findByIsAdminTrue();
    }

    @PostMapping("/sendmessagetoall")
    public void sendMesageToAll(@RequestParam(name = "msg") String message){
        if(message != null && !message.isEmpty()){
            zebraServiceBot.sendMessageToAllUsers(message);
        }
    }

    @PostMapping("/clearhistory")
    public void clearHistory(){
        messageRepository.deleteAll();
    }

    @RequestMapping("/getmessages")
    public List<TelegramMessageEntity> getAllMessages(){
        return messageRepository.findAllByOrderByDateEvent();
    }
}
