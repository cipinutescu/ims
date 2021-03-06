package com.chat;

import antlr.debug.MessageAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by Ciprian on 6/8/2016.
 */
@RestController
public class ChatController {

    public List<Message> messages;

    @PostConstruct
    public void init(){
        messages = new ArrayList<>();
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public
    @ResponseBody
    String sendMessage(@RequestBody String message,
                       @RequestParam(value = "user", defaultValue = "") String user) throws Exception {
        String time = HttpUtils.sendGet("http://localhost:8889/getTimestamp");
        if(message.contains("upload file")){
            StringTokenizer st = new StringTokenizer(message,"->");
            st.nextToken();
            Message mess = new ObjectMapper()
                    .readValue(HttpUtils.sendPost("http://localhost:8889/uploadFile?user="+user,st.nextToken()),Message.class);
            messages.add(new Message(user,mess.getMessage(),mess.getTimestamp()));
            return "OK";
        } else {
            messages.add(new Message(user, message, time));
            return "OK";
        }
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/receiveMessages", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Message> receiveMessage(@RequestParam(value = "index", defaultValue = "") Integer index) throws Exception {
        if(messages.size() - index > 5){
            HttpUtils.sendPost("http://localhost:8889/archMessages",messages.toString());
            String url = "http://localhost:8889/retrieveArchMessages";
            return Collections.singletonList(new Message("Archiever",url,String.valueOf(messages.size())));
        }

        return messages.subList(index,messages.size());
    }

    public static class Message{
        private String username;
        private String message;
        private String timestamp;

        public Message() {
        }

        public Message(String username, String message, String timestamp) {
            this.username = username;
            this.message = message;
            this.timestamp = timestamp;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"username\":\"" + username + '\"' +
                    ",\"message\":\"" + message + '\"' +
                    ",\"timestamp\":\"" + timestamp + '\"' +
                    '}';
        }
    }
}
