package posidon.uranium.client;

import posidon.uranium.main.Main;
import posidon.potassium.packets.PlayerJoinPacket;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Runnable {

    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static Socket connection;

    public static boolean start(String ip, int port) {
        try {
            connection = new Socket(ip, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            send(new PlayerJoinPacket(0, "leoxshn"));
            new Thread(new Client(), "unraniumClient").start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Main.running = false;
            System.err.println("Can't connect to potassium server");
            return false;
        }
    }

    public static void send(Object o) {
        try {
            output.writeObject(o);
            output.flush();
        }
        catch (SocketException e) { /*TODO: when server server stops*/ kill(); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void kill() {
        try {
            output.close();
            input.close();
            connection.close();
        } catch (Exception ignore) {}
    }

    @Override
    public void run() {
        while (Main.running) {
            try { EventListener.onEvent(input.readObject()); }
            catch (EOFException | SocketException | StreamCorruptedException e) { kill(); }
            catch (Exception e) { e.printStackTrace(); }
        } kill();
    }
}