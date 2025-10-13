package javachatappp;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class usertab {

    private String username;
    private Image userimage;
    private HBox userview; 

    public usertab(String username, Image image) {
        this.username = username;
        this.userimage = image;

        createProfileContent();
    }

    private void createProfileContent() {
        userview = new HBox(10);
        userview.setAlignment( javafx.geometry.Pos.CENTER_LEFT);

        if (userimage != null) {
            ImageView avatar = new ImageView(userimage);
            avatar.setFitWidth(30);
            avatar.setFitHeight(30);

            Circle clip = new Circle(15, 15, 15);
            avatar.setClip(clip);

            userview.getChildren().add(avatar);
        } else {
            StackPane avatarStack = new StackPane();
            Circle avatarCircle = new Circle(15, 15, 15);
            avatarCircle.setFill(Color.GRAY);

            Label initials = new Label(username.isEmpty() ? "" : username.substring(0, 2).toUpperCase());
            initials.setTextFill(Color.WHITE);
            initials.setFont(new Font("Arial", 16));

            avatarStack.getChildren().addAll(avatarCircle, initials);
            avatarStack.setPrefSize(30, 30);

            userview.getChildren().add(avatarStack);
        }

        Label labelusername = new Label(username);
        labelusername.setTextFill(Color.BLACK);
        labelusername.setFont(new Font("Arial", 20));
        labelusername.setTextAlignment(TextAlignment.LEFT);

        userview.getChildren().add(labelusername);
    }

    public Parent getView() {
        return userview;
    }
}
