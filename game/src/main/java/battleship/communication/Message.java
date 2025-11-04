package battleship.communication;

public class Message {
    public enum MessageType {
        JOIN, ATTACK ,RESULT, CLIENT_TURN, SERVER_TURN  
    }

    MessageType type;
    String payload;

    public Message(MessageType type, String payload) {
        this.type = type;
        this.payload = payload;
    }
    public Message(String msg) {
        deserialize(msg);
    }

    public String serialize() {
        return this.type + "|" + this.payload;
    }

    public void deserialize(String msg) {
        if (msg == null) {
            throw new IllegalArgumentException("msg is null");
        }
        int idx = msg.indexOf('|');
        String typePart = (idx >= 0) ? msg.substring(0, idx).trim() : msg.trim();
        String payloadPart = (idx >= 0) ? msg.substring(idx + 1) : "";
        this.type = MessageType.valueOf(typePart);
        this.payload = payloadPart;
    }
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }
    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
}
