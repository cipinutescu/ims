package com.auth;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ciprian on 6/8/2016.
 */
@RestController
public class AuthentificationController {

    private static final String REQUEST_FAILED = "FAILED";
    private static final String REQUEST_SUCCESS = "SUCCESS";

    private Map<String,String> users;

    @PostConstruct
    public void init(){
        users = new HashMap<>();

        users.put("ciprian","ciprian");
        users.put("alex","alex");
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public
    @ResponseBody
    String login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password
    ){
        if(users.get(username) != null && users.get(username).equals(password)){
            return REQUEST_SUCCESS;
        } else {
            return REQUEST_FAILED;
        }

    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public
    @ResponseBody
    String register(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password
    ){
        if(users.containsKey(username)){
            return REQUEST_FAILED;
        } else {
            users.put(username,password);
            return REQUEST_SUCCESS;
        }
    }
}
