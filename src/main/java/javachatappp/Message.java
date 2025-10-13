package javachatappp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class Message {
    private String content;
    private String sender;
    private String timestamp;
     String chatroomname;
    MongoCollection collection;
    MongoDatabase database = databaseConnect.getDatabase("messages");

     Message(String content, String sender, String timestamp) {
        this.content = content;
       this.sender = sender;
        this.timestamp = timestamp;
    }
   

Map<String, Object> toMap() {
        return Map.of(
            "content", content,
            "sender", sender,
            "timestamp", timestamp
        );
    }

    public void todb() {
    database.getCollection(chatroomname).insertOne(new Document(toMap()));  
    }

    public String getSender() {
        return sender;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getText() {
        return content;
    }

    public String createchatroom(  String username , String reciever){
       List<String> chatroom = Arrays.asList(username, reciever);
       chatroom.sort(String::compareTo); 
       database.createCollection(chatroomname = String.join("and", chatroom));
       return chatroomname;
    }
}
