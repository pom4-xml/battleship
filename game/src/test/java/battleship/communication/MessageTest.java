package battleship.communication;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import battleship.communication.Message;
import battleship.communication.Message.MessageType;

public class MessageTest {

    @Test
    void serializeAndDeserialize_simple() {
        Message m = new Message(MessageType.ATTACK, "A5");
        String s = m.serialize();
        assertTrue(s.startsWith("ATTACK|"));
        Message parsed = new Message(s);
        assertNotNull(parsed);
        assertEquals(MessageType.ATTACK, parsed.getType());
        assertEquals("A5", parsed.getPayload());
    }

    @Test
    void serializeEmptyPayload() {
        Message m = new Message(MessageType.ATTACK, "");
        String s = m.serialize();
        Message parsed = new Message(s);
        assertEquals(MessageType.ATTACK, parsed.getType());
        assertEquals("", parsed.getPayload());
    }
}