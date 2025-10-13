package javachatappp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class chatController implements Initializable {

    @FXML
    private Label settings;

    @FXML 
    private Button sendbutton;

    @FXML 
    private TextField textbaox;

    @FXML 
    private VBox chatbody;

    @FXML 
    private ScrollPane chatScrollPane; 

    @FXML 
    private VBox scrollchat;   

    @FXML 
    private Label username;

    @FXML 
    private ListView<User> userList;

    private ObservableList<User> users = FXCollections.observableArrayList();
    private final App app = new App();
    private final databaseConnect db = new databaseConnect();

    private String currentChatroom;
    private String lasttimestamp;
    private Thread poller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatbody.setFillWidth(true);
        chatScrollPane.setFitToWidth(true);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        loadUserList();

        userList.setOnMouseClicked(event -> handleUserSelection());
    }

    private void loadUserList() {
        Task<ObservableList<User>> loadTask = new Task<>() {
            @Override
            protected ObservableList<User> call() {
                MongoDatabase database = db.getDatabase("userauth");
                MongoCollection<Document> collection = database.getCollection("chatusers");
                List<User> fetchedUsers = collection.find().map(doc -> new User(doc.getString("username"))).into(new ArrayList<>());

                fetchedUsers.removeIf(u -> u.getName().equals(LoginController.usernameinput));
                return FXCollections.observableArrayList(fetchedUsers);
            }
        };

        loadTask.setOnSucceeded(e -> {
            users.setAll(loadTask.getValue());
            userList.setItems(users);
            userList.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);
                    if (empty || user == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setGraphic(new usertab(user.getName(), null).getView());
                        setText(null);
                    }
                }
            });
        });

        loadTask.setOnFailed(e -> loadTask.getException().printStackTrace());

        Thread thread = new Thread(loadTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void handleUserSelection() {
        User selected = userList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        chatbody.setVisible(true);
        username.setText(selected.getName());
        chatbody.requestFocus();
        app.initHandlers((Scene) userList.getScene(), chatbody);

        Message message = new Message(textbaox.getText(), LoginController.usernameinput, timestamp());
        currentChatroom = message.createchatroom(LoginController.usernameinput, selected.getName());
        lasttimestamp = "";

        if (poller != null && poller.isAlive()) poller.interrupt();
        Task<List<Message>> loadHistory = new Task<>() {
            @Override
            protected List<Message> call() {
                MongoDatabase messagesDb = db.getDatabase("messages");
                MongoCollection<Document> chatCollection = messagesDb.getCollection(currentChatroom);
                return chatCollection.find().map(doc -> new Message(
                        doc.getString("content"),
                        doc.getString("sender"),
                        doc.getString("timestamp")
                )).into(new ArrayList<>());
            }
        };

        loadHistory.setOnSucceeded(e -> {
            List<Message> messages = loadHistory.getValue();
            scrollchat.getChildren().clear();
            for (Message m : messages) {
                textbubble bubble = new textbubble(m.getText(), m.getTimestamp(), m.getSender());
                scrollchat.getChildren().add(bubble.render(chatScrollPane, LoginController.usernameinput));
            }
            if (!messages.isEmpty()) {
                lasttimestamp = messages.stream()
                        .map(Message::getTimestamp)
                        .max(String::compareTo)
                        .orElse(timestamp());
            }
            scrollbottom();
            startPoller();
        });

        loadHistory.setOnFailed(e -> loadHistory.getException().printStackTrace());

        Thread t = new Thread(loadHistory);
        t.setDaemon(true);
        t.start();
    }

    private void startPoller() {
        poller = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
                if (currentChatroom == null || lasttimestamp == null) continue;

                MongoDatabase messagesDb = db.getDatabase("messages");
                MongoCollection<Document> chatCollection = messagesDb.getCollection(currentChatroom);
                List<Message> newMessages = chatCollection.find(Filters.gt("timestamp", lasttimestamp))
                        .map(doc -> new Message(
                                doc.getString("content"),
                                doc.getString("sender"),
                                doc.getString("timestamp")
                        )).into(new ArrayList<>());

                if (!newMessages.isEmpty()) {
                    Platform.runLater(() -> {
                        String max = lasttimestamp;
                        for (Message m : newMessages) {
                            if (!m.getSender().equals(LoginController.usernameinput)) {
                                textbubble bubble = new textbubble(m.getText(), m.getTimestamp(), m.getSender());
                                scrollchat.getChildren().add(bubble.render(chatScrollPane, LoginController.usernameinput));
                            }
                            if (m.getTimestamp().compareTo(max) > 0) {
                                max = m.getTimestamp();
                            }
                        }
                        lasttimestamp = max;
                        scrollbottom();
                    });
                }
            }
        });
        poller.setDaemon(true);
        poller.start();
    }

    private void scrollbottom() {
        if (chatScrollPane != null) {
            Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
        }
    }

    @FXML
    private void settings(MouseEvent event) {
        try {
            App.setRoot("settings");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void sendbutton(ActionEvent event) {
        String textmessage = textbaox.getText().trim();
        if (textmessage.isEmpty()) return;

        Message message = new Message(textmessage, LoginController.usernameinput, timestamp());
        message.createchatroom(LoginController.usernameinput, username.getText());

        textbubble bubble = new textbubble(textmessage, message.getTimestamp(), LoginController.usernameinput);
        scrollchat.getChildren().add(bubble.render(chatScrollPane, LoginController.usernameinput));

        message.toMap();
        message.todb();
        textbaox.clear();

        if (lasttimestamp == null || message.getTimestamp().compareTo(lasttimestamp) > 0) {
            lasttimestamp = message.getTimestamp();
        }

        scrollbottom();
    }

    private String timestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
