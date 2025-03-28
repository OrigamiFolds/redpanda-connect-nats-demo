/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package draft.dev.sensor.publisher;

/**
 *
 * @author Mduduzi Sibisi
 */
import io.nats.client.Connection;
import io.nats.client.Nats;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
        
public class SensorPublisher {
    private final static int ITEM_REMOVED = 0;
    private final static int ITEM_ADDED = 1;
    private final static int INVENTORY_LOW = 2;
            
    
    public static void main(String[] args) {
        String natsURL = "nats://127.0.0.1:4222";
        
        /*
         * Add code to check that args[0] isn't null and is indeed an integer
         * before parsing.
         */
        
        int option = Integer.parseInt(args[0]);
        
        // Create timestamp and format it to match RedPanda Connect configuration and QuestDB
        LocalDateTime currentTime = LocalDateTime.now();        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        String formattedDateTime = currentTime.format(formatter);
        
        try (Connection nc = Nats.connect(natsURL)) {
            String message = "";
            // Switch expressions to check what type of message to send
            switch (option) {
                case ITEM_REMOVED -> message = 
                        "{\"id\":\"0\",\"message\":\"Inventory item removed\",\"timestamp\":\"" + formattedDateTime + "\"}";
                case ITEM_ADDED -> message = ""
                        + "{\"id\":\"1\",\"message\":\"Inventory item added\",\"timestamp\":\"" + formattedDateTime + "\"}";
                case INVENTORY_LOW -> message = 
                        "{\"id\":\"2\",\"message\":\"Stock is low:\",\"timestamp\":\"" + formattedDateTime + "\"}";
            }
            
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            nc.publish("inventory", messageBytes);
            System.out.println("Messsage published to Nats: " + message);
            Thread.sleep(200);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}
