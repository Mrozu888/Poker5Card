package org.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Message {

    private void sendToMultiplePlayer(int[] playerIds, String message) {
        for (int playerId : playerIds ) {
            sendTo(playerId, message);
        }
    }

    private void sendTo(int playerId, String message) {
        SocketChannel receiver = GameAction.getSocketChannelByPlayerId(playerId);
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            receiver.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
