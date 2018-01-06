package client.main;


import client.comm.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws Exception {

        boolean loginOption = false;
        boolean registerOption = false;
        final List<Message> messagesReceived = new ArrayList<>();
        List<Message> messagesShown = new ArrayList<>();
        while(true){

            System.out.println("Welcome to Chat Room.");
            System.out.println("Enter : \n 1.For login\n 2.For registration");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.next();
            if(option.contains("1")){
                loginOption = true;
                break;
            } else if(option.contains("2")){
                registerOption = true;
                break;
            } else {
                System.out.println("No valid option entered!");
            }
        }

        if(registerOption){

            System.out.println("Registration page\n\nPlease specified your credentials : ");
            System.out.print("Username : ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.next();
            System.out.print("Password : ");
            String password = scanner.next();
            String responseFromRegistrationService = null;
            try {
                responseFromRegistrationService = HttpUtils.sendGet("http://localhost:8889/register?username="+username+"&password="+password);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(responseFromRegistrationService != null && responseFromRegistrationService.equals("SUCCESS")){
                System.out.println("Registration was successful !");
                loginOption = true;
            }
        }

        if(loginOption){
            System.out.println("Login page \n\nPlease specified your credentials : ");
            System.out.print("Username : ");
            Scanner scanner = new Scanner(System.in);
            String username = scanner.next();
            System.out.print("Password : ");
            String password = scanner.next();
            String responseFromRegistrationService = null;
            try {
                responseFromRegistrationService = HttpUtils.sendGet("http://localhost:8889/login?username="+username+"&password="+password);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(responseFromRegistrationService != null && responseFromRegistrationService.equals("SUCCESS")){
                System.out.println("Authentification was successful !");

                scheduler.scheduleAtFixedRate((Runnable) () -> {
                    String messages = null;
                    try {
                        messages = HttpUtils.sendGet("http://localhost:8889/receiveMessages?index="+messagesReceived.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    List<Message> newMessages = null;
                    try {
                        newMessages = Arrays.asList(new ObjectMapper().readValue(messages,Message[].class));
                        messagesReceived.addAll(newMessages);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(newMessages.size() == 1 && newMessages.get(0).getUsername().equals("Archiever")){
                        messagesReceived.clear();
                        System.out.println("Too many messges to show ! Archieved messages at address : "+ newMessages.get(0).getMessage());
                        for(int i=0; i< Integer.valueOf(newMessages.get(0).getTimestamp());i++){
                            messagesReceived.add(new Message());
                        }

                    } else {
                        for (Message message : newMessages) {
                            System.out.println("{ " + message.getUsername() + "  } : { " + message.getTimestamp() + " } : " + message.getMessage());
                            messagesShown.add(message);
                        }
                    }
                },0,2, TimeUnit.SECONDS);


                while(true){
                    scanner = new Scanner(System.in).useDelimiter("\\n");
                    String message = scanner.next();
                    if(message.equals("exit")){
                        System.out.println("bye bye!");
                        break;
                    }
                    String status = HttpUtils.sendPost("http://localhost:8889/sendMessage?user="+username,message);
                }
            }
        }

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
