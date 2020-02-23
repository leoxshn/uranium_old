package posidon.potassium.packets;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1;
    public String message;

    public ChatMessage(String message) { this.message = message; }
}
