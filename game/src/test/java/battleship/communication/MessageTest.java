package battleship.communication;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    private Message message;

    @BeforeEach
    void setUp() {
        message = new Message(Message.MessageType.ATTACK, "5,3");
    }

    @Test
    void testMessageCreation() {
        assertNotNull(message);
        assertEquals(Message.MessageType.ATTACK, message.getType());
        assertEquals("5,3", message.getPayload());
    }

    @Test
    void testSerialize() {
        String serialized = message.serialize();
        assertEquals("ATTACK|5,3", serialized);
    }

    @Test
    void testSerializeWithEmptyPayload() {
        Message msg = new Message(Message.MessageType.JOIN, "");
        String serialized = msg.serialize();
        assertEquals("JOIN|", serialized);
    }

    @Test
    void testDeserialize() {
        Message msg = new Message("RESULT|hit");
        assertEquals(Message.MessageType.RESULT, msg.getType());
        assertEquals("hit", msg.getPayload());
    }

    @Test
    void testDeserializeMultiplePipes() {
        // Si el payload contiene pipes, debería desserializarse correctamente
        Message msg = new Message("ATTACK|5,3,extra");
        assertEquals(Message.MessageType.ATTACK, msg.getType());
        assertEquals("5,3,extra", msg.getPayload());
    }

    @Test
    void testSettersGetters() {
        message.setType(Message.MessageType.JOIN);
        message.setPayload("player1");
        
        assertEquals(Message.MessageType.JOIN, message.getType());
        assertEquals("player1", message.getPayload());
    }

    @Test
    void testRoundTripSerializeDeserialize() {
        String original = message.serialize();
        Message deserialized = new Message(original);
        
        assertEquals(message.getType(), deserialized.getType());
        assertEquals(message.getPayload(), deserialized.getPayload());
    }

    @Test
    void testAllMessageTypes() {
        for (Message.MessageType type : Message.MessageType.values()) {
            Message msg = new Message(type, "test");
            assertTrue(msg.serialize().contains(type.toString()));
        }
    }
}