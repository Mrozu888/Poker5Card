package org.example;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class PlayerClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

            // Thread for reading incoming messages
            new Thread(new IncomingMessageListener(socketChannel, selector)).start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter your message: ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    socketChannel.close();
                    break;
                }

                sendMessage(socketChannel, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(SocketChannel socketChannel, String message) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class IncomingMessageListener implements Runnable {
        private SocketChannel socketChannel;
        private Selector selector;

        public IncomingMessageListener(SocketChannel socketChannel, Selector selector) {
            this.socketChannel = socketChannel;
            this.selector = selector;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    selector.select(); // Block until a channel is ready

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isReadable()) {
                            readMessage(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readMessage(SelectionKey key) {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(256);

            try {
                int bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) {
                    clientChannel.close();
                    System.out.println("Connection closed.");
                    return;
                }

                buffer.flip();
                String message = new String(buffer.array(), 0, buffer.limit());
                System.out.println("Message from other player: " + message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

