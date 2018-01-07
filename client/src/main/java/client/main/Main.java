package client.main;


import client.comm.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    static String sentText;
    static Thread mainThread;
    static Object staticObject = new Object();
    static boolean inWindow = false;

    static class ChatFrame extends JFrame implements Observer {

        public JTextArea textArea;
        private JTextField inputTextField;
        private JButton sendButton;

        public ChatFrame() {
            buildGUI();
        }

        /** Builds the user interface */
        private void buildGUI() {
            this.setPreferredSize(new Dimension(600,400));
            textArea = new JTextArea(40, 80);
            textArea.setEditable(false);
            textArea.setLineWrap(true);

            Font font = new Font("Verdana", Font.BOLD, 15);
            textArea.setFont(font);
            add(new JScrollPane(textArea), BorderLayout.CENTER);

            Box box = Box.createHorizontalBox();
            add(box, BorderLayout.SOUTH);
            inputTextField = new JTextField();
            inputTextField.setFont(font);

            sendButton = new JButton("Send");
            box.add(inputTextField);
            box.add(sendButton);

            // Action for the inputTextField and the goButton
            ActionListener sendListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String str = inputTextField.getText();
                    inputTextField.selectAll();
                    inputTextField.requestFocus();
                    inputTextField.setText("");
                    if(!inWindow)
                        textArea.append(str + "\n");
                    sentText = str;
                    synchronized (staticObject) {
                        staticObject.notify();
                    }
                }
            };
            inputTextField.addActionListener(sendListener);
            sendButton.addActionListener(sendListener);
        }

        /** Updates the UI depending on the Object argument */
        public void update(Observable o, Object arg) {
            final Object finalArg = arg;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    textArea.append(finalArg.toString());
                    textArea.append("\n");
                }
            });
        }
    }

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    public static void main(String[] args) throws Exception {

        ChatFrame chatFrame = new ChatFrame();
        chatFrame.setTitle("My chat app");
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.pack();
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setResizable(false);
        chatFrame.setVisible(true);

        boolean loginOption = false;
        boolean registerOption = false;
        final List<Message> messagesReceived = new ArrayList<>();
        List<Message> messagesShown = new ArrayList<>();

        mainThread = Thread.currentThread();
        while(true){

            //System.out.println("Welcome to Chat Room.");
            chatFrame.textArea.append("Welcome to Chat Room.\n");
            //System.out.println("Enter : \n 1.For login\n 2.For registration");
            chatFrame.textArea.append("Enter : \n 1.For login\n 2.For registration\n");
            //Scanner scanner = new Scanner(System.in);
            //String option = scanner.next();
            synchronized (staticObject) {
                staticObject.wait();
            }
            String option = sentText;
            if(option.contains("1")){
                loginOption = true;
                break;
            } else if(option.contains("2")){
                registerOption = true;
                break;
            } else {
                //System.out.println("No valid option entered!");
                chatFrame.textArea.append("No valid option entered!\n");
            }
        }

        if(registerOption){

            //System.out.println("Registration page\n\nPlease specified your credentials : ");
            //System.out.print("Username : ");
            chatFrame.textArea.append("Registration page\n\nPlease specified your credentials : \n");
            chatFrame.textArea.append("Username : ");
            //Scanner scanner = new Scanner(System.in);
            //String username = scanner.next();
            synchronized (staticObject) {
                staticObject.wait();
            }
            String username = sentText;

            //System.out.print("Password : ");
            chatFrame.textArea.append("Password : ");
            //String password = scanner.next();
            synchronized (staticObject) {
                staticObject.wait();
            }
            String password = sentText;
            String responseFromRegistrationService = null;
            try {
                responseFromRegistrationService = HttpUtils.sendGet("http://localhost:8889/register?username="+username+"&password="+password);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(responseFromRegistrationService != null && responseFromRegistrationService.equals("SUCCESS")){
                //System.out.println("Registration was successful !");
                chatFrame.textArea.append("Registration was successful !\n");
                loginOption = true;
            }
        }

        if(loginOption){
            //System.out.println("Login page \n\nPlease specified your credentials : ");
            chatFrame.textArea.append("Login page \n\nPlease specified your credentials : \n");
            //System.out.print("Username : ");
            chatFrame.textArea.append("Username :");
            //Scanner scanner = new Scanner(System.in);
            //String username = scanner.next();
            synchronized (staticObject) {
                staticObject.wait();
            }
            String username = sentText;


            //System.out.print("Password : ");
            chatFrame.textArea.append("Password: ");
            //String password = scanner.next();
            synchronized (staticObject) {
                staticObject.wait();
            }
            String password = sentText;

            String responseFromRegistrationService = null;
            try {
                responseFromRegistrationService = HttpUtils.sendGet("http://localhost:8889/login?username="+username+"&password="+password);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(responseFromRegistrationService != null && responseFromRegistrationService.equals("SUCCESS")){
                //System.out.println("Authentification was successful !");
                chatFrame.textArea.append("Authentification was successful !\n");
                inWindow = true;
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
                        //System.out.println("Too many messges to show ! Archieved messages at address : "+ newMessages.get(0).getMessage());
                        chatFrame.textArea.append("Too many messges to show ! Archieved messages at address : "+ newMessages.get(0).getMessage() + "\n");
                        for(int i=0; i< Integer.valueOf(newMessages.get(0).getTimestamp());i++){
                            messagesReceived.add(new Message());
                        }

                    } else {
                        for (Message message : newMessages) {
                            //System.out.println("{ " + message.getUsername() + "  } : { " + message.getTimestamp() + " } : " + message.getMessage());
                            chatFrame.textArea.append("{ " + message.getUsername() + "  } : { " + message.getTimestamp() + " } : " + message.getMessage() + "\n");
                            messagesShown.add(message);
                        }
                    }
                },0,2, TimeUnit.SECONDS);

                scheduler.scheduleAtFixedRate((Runnable) () -> {
                    synchronized (staticObject) {
                        try {
                            staticObject.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String message = sentText;

                    if (message.equals("exit")) {
                        //System.out.println("bye bye!");
                        chatFrame.textArea.append("Bye bye!\n");
                        System.exit(0);
                    }
                    try {
                        String status = HttpUtils.sendPost("http://localhost:8889/sendMessage?user="+username,message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },0,1,TimeUnit.SECONDS);
//                while(true) {
                    //scanner = new Scanner(System.in).useDelimiter("\\n");
                    //String message = scanner.next();
//                                 }
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
