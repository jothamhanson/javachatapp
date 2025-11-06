package javachatappp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignupController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    
    MongoCollection<Document> collection;
    private  String usernameinput;
    private  String passwordinput;


   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }

    public  void login() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
           // e.printStackTrace();
        }
    }   

    public void signup() {
        usernameinput = username.getText();
        passwordinput = password.getText();

        if (usernameinput.isEmpty() || passwordinput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Username and Password cannot be empty.");
            alert.showAndWait();
        } else if (passwordinput.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters long.");
            alert.showAndWait();
        }else  {
            MongoDatabase database = databaseConnect.getDatabase("userauth"); 
            collection = database.getCollection("users");
             String hashedPassword = BCrypt.hashpw(passwordinput, BCrypt.gensalt());
            Document user = new Document("username", usernameinput)
                    .append("hashedpassword", hashedPassword);
            passwordinput = null;
            password.clear();
            Document existingUser = collection.find(new Document("username", usernameinput)).first();
            if (existingUser != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Signup Error");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists. Please choose a different username.");
                alert.showAndWait();
                return;   
            }
            collection.insertOne(user);
            addnewuser(usernameinput);
            try {
                App.setRoot("login");
            } catch (IOException e) {
               // e.printStackTrace();
            }
        }
    } 
    public void addnewuser(String username) {
        MongoDatabase database = databaseConnect.getDatabase("userauth");
        collection = database.getCollection("chatusers");
        Document user = new Document("username", username);
        collection.insertOne(user);
    }
}
