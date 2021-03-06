package com.auth;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ciprian on 6/8/2016.
 */
@RestController
public class AuthentificationController {

    private static final String REQUEST_FAILED = "FAILED";
    private static final String REQUEST_SUCCESS = "SUCCESS";

    private Map<String,String> users = new HashMap<>();

    @Inject
    private UserDAO userDAO;


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public
    @ResponseBody
    String login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password
    ){
        userDAO.getAll().stream().forEach(user -> users.put(user.getUsername(),user.getPassword()));
        if(users.get(username) != null && users.get(username).equals(password)){
            return REQUEST_SUCCESS;
        } else {
            return REQUEST_FAILED;
        }

    }



}
