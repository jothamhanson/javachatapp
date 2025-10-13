package javachatappp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class textbubble {

    private final String messageText;
    private final String timestamp;
    private final String sender;

    public textbubble(String messageText, String timestamp, String sender) {
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public HBox render(ScrollPane scrollPane, String currentUser) {
        Label msgLabel = new Label(messageText);
        msgLabel.setWrapText(true);
        msgLabel.setFont(Font.font("Segoe UI", 14));
        msgLabel.setPadding(new Insets(8, 12, 8, 12));
        msgLabel.setMaxWidth(300);
        msgLabel.setStyle(
            "-fx-background-color: " +
            (sender.equals(currentUser) ? "#140918ff" : "#5417c7") + ";" +
            "-fx-background-radius: 12px;" +
            "-fx-text-fill: white;"
        );

        Label timeLabel = new Label(formatTimestamp(timestamp));
        timeLabel.setFont(Font.font("Segoe UI", 10));
        timeLabel.setTextFill(Color.web("#a9a9a9"));

        VBox bubbleBox = new VBox(msgLabel, timeLabel);
        bubbleBox.setSpacing(2);

        HBox container = new HBox(bubbleBox);
        container.setPadding(new Insets(5));
        container.setAlignment(sender.equals(currentUser) ? Pos.CENTER_RIGHT : Pos.CENTER_RIGHT);

        scrollPane.layout();
        scrollPane.setVvalue(1.0);

        return container;
    }

    private String formatTimestamp(String ts) {
        try {
            return ts.substring(11, 16);
        } catch (Exception e) {
            return "";
        }
    }
}
