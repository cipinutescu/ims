package com.arch;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ciprian on 6/8/2016.
 */
@RestController
public class ArchivationController {

    List<Message> messages = new ArrayList<>();

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/archMessages", method = RequestMethod.POST)
    public
    @ResponseBody
    void archieveMesssages(
            @RequestBody List<Message> messagesRecv
    ) throws IOException {
        messages.addAll(messagesRecv);
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/retrieveArchMessages", method = RequestMethod.GET)
    public
    @ResponseBody
    String retrieveArchMessages() throws IOException {
        String time = HttpUtils.sendGet("http://localhost:8889/getTimestamp");
        String s = "Time of archivation : " + time + "\n";
        for(Message message : messages){
            s = s  + "{ " + message.getUsername() + "  } : { " + message.getTimestamp() + " } : " + message.getMessage() + "\n";
        }
        return s;
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
