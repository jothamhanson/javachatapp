package javachatappp;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 900, 550);
        stage.setScene(scene);
        stage.show();
        
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static  void initHandlers(Scene scene, Parent escapeTo) {
       scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    escapeTo.setVisible(false);
                    break;
                case ENTER:
                   // System.out.println("Enter key pressed");
                    break;
                default:
                    break;
            }
        });
      
    }

    public static void main(String[] args) {
        databaseConnect databaseConnect = new databaseConnect();  
        launch();
    }

}