package dev.mira.event;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Event
 */
final class EventTest {

    @Test
    void eventNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> Event.valueOf(null), "Event name cannot be null");
    }

    @Test
    void eventNameCannotBeEmpty() {
        // Invalid names
        List.of("", " ", "    ", "     ", "\n", "\r", "\t", "\n\n\r\t")
                .forEach(str -> assertThrows(IllegalArgumentException.class, () -> Event.valueOf(str),
                        "Event name cannot be blank!"));

        // Valid names
        List.of("aa,", "b", "Test", "Hello", "ahdioahdahdhaoihdioahoia")
                .forEach(str -> assertDoesNotThrow(() -> Event.valueOf(str),
                        "Event name wasn't blank but still failed!"));
    }
}
