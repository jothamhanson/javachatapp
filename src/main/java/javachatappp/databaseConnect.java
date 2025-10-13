package javachatappp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class databaseConnect {

    private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");

    public static MongoDatabase getDatabase(String databaseName) {
        return client.getDatabase(databaseName);
    }
}
