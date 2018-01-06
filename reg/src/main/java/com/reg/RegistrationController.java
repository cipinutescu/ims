package com.reg;

import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ciprian on 6/8/2016.
 */
@RestController
public class RegistrationController {

    private static final String REQUEST_FAILED = "FAILED";
    private static final String REQUEST_SUCCESS = "SUCCESS";

    private Map<String,String> users = new HashMap<>();

    @Inject
    private UserDAO2 userDAO;



    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public
    @ResponseBody
    String register(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password
    ){
        userDAO.getAll().stream().forEach(user -> users.put(user.getUsername(),user.getPassword()));
        if(users.containsKey(username)){
            return REQUEST_FAILED;
        } else {
            users.put(username,password);
            userDAO.create(new User2(username,password));
            return REQUEST_SUCCESS;
        }
    }
}
