package javachatappp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



public class LoginController implements Initializable {
   
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    MongoCollection<Document> collection;

    public static String usernameinput;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }

    public void login(ActionEvent event) {
        usernameinput = username.getText();
        String passwordinput = password.getText();
        if (usernameinput.isEmpty() || passwordinput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Username and Password cannot be empty.");
            alert.showAndWait();
            return;
        }
    
        if (passwordinput.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters long.");
            alert.showAndWait();
            return;
        }
    
        databaseConnect databaseConnect = new databaseConnect();
        MongoDatabase database = databaseConnect.getDatabase("userauth"); 
        collection = database.getCollection("users");
        Document user = collection.find(new Document("username", usernameinput)).first();
        if (user != null ) {
            String storedHash = user.getString("hashedpassword");
            if (BCrypt.checkpw(passwordinput, storedHash)) {
               System.out.println("Login successful!");
                try {
                    App.setRoot("chat");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showError("Invalid username or password.");
            }
        } else {
            showError("Invalid username or password.");
        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void signup() {
        try {
            App.setRoot("signup");
        } catch (IOException e) {
          //  e.printStackTrace();
        }
    }
    public void forgotpassword() {
       
    }
    
}
