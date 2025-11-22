/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sensor.dispatcher;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Dispatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Mduduzi Sibisi
 */
public class SensorDispatcher {

    public static void main(String[] args) {
        String natsURL = "nats://127.0.0.1:4222";

        try (Connection nc = Nats.connect(natsURL)) {

            Dispatcher dispatcher = nc.createDispatcher((msg) -> {
                System.out.printf("%s on subject %s\n",
                        new String(msg.getData(), StandardCharsets.UTF_8),
                        msg.getSubject());
            });

            dispatcher.subscribe("inventory");

            Thread.sleep(20000);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}
