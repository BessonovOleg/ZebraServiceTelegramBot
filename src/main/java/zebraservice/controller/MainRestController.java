package zebraservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zebraservice.model.TelegramUser;
import zebraservice.repositories.TelegramUserRepository;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/rest")
public class MainRestController {
    @Autowired
    private TelegramUserRepository userRepository;

    @RequestMapping("/getusers")
    public List<TelegramUser> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/updateuser")
    public void updateUser(@RequestBody TelegramUser usr) {
        userRepository.save(usr);
    }

}
