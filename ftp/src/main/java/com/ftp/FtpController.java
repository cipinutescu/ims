package com.ftp;

import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ciprian on 6/8/2016.
 */
@RestController
public class FtpController {

    public List<Message> files;

    @PostConstruct
    public void init(){
        files = new ArrayList<>();
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public
    @ResponseBody
    Message uploadFile(@RequestBody String fileUrl,
                       @RequestParam(value = "user", defaultValue = "") String user) throws Exception {
        String time = HttpUtils.sendGet("http://localhost:8889/getTimestamp");

        String file = "";
        BufferedReader br = null;
        FileReader fr = null;

        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(fileUrl);
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                file += sCurrentLine;
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }



        files.add(new Message(user,file,time));
        return new Message(user,"Upload complete : http://localhost:8889/downloadFile",time);
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public
    @ResponseBody
    String downloadFile() throws Exception {
        return files.get(files.size()-1).getMessage();
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
