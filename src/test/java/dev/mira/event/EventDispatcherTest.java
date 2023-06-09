package dev.mira.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * EventDispatcherTest
 */
final class EventDispatcherTest {
    @BeforeEach
    void setup() {
        try {
            final Field instance = EventDispatcher.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (final Exception e) {
            e.printStackTrace(); // Should not happen
        }
    }

    @Test
    void connectTest() {
        final var d = EventDispatcher.getInstance();

        final var ev = Event.valueOf("Test");
        final EventHandler h = (_x) -> {
        };

        assertTrue(d.connect(ev, h));
        assertTrue(d.isConnected(ev, h));
    }

    @Test
    void connectNullTest() {
        final var d = EventDispatcher.getInstance();
        final EventHandler h = (_x) -> {
        };

        assertThrows(IllegalArgumentException.class, () -> d.connect(null, h));
        assertThrows(IllegalArgumentException.class, () -> d.connect(Event.valueOf("aaa"), null));
        assertThrows(IllegalArgumentException.class, () -> d.connect(null, null));
    }

    @Test
    void disconnectTest() {
        final var d = EventDispatcher.getInstance();
        final var ev = Event.valueOf("Test");
        final EventHandler h = (_x) -> {
        };
        final EventHandler h2 = (_x) -> {
        };

        // event does not yet exist
        assertFalse(d.disconnect(ev, h));

        // event exists but h is not yet connected
        d.connect(ev, h2);
        assertFalse(d.disconnect(ev, h));

        // event exists and h is connected
        d.connect(ev, h);
        assertTrue(d.disconnect(ev, h));
    }

    @Test
    void emitTest() {
        final var d = EventDispatcher.getInstance();
        final var ev = Event.valueOf("Test");

        final var i = new AtomicInteger(0);
        final int cnt = 3;

        IntStream
                .range(0, cnt)
                .forEach((_x) -> d.connect(ev, (_y) -> i.incrementAndGet()));

        d.emit(ev);
        assertEquals(cnt, i.get());
    }

    @Test
    void ensureNoArgs() {
        final var d = EventDispatcher.getInstance();
        final var ev = Event.valueOf("Test");

        d.connect(ev, (a) -> assertEquals(0, a.length));
    }

    @Test
    void testVarArg() {
        final var disp = EventDispatcher.getInstance();
        final var ev = Event.valueOf("Test");

        final var args = new ArrayList<Object>();
        args.add(1);
        args.add(2);
        args.add("AAA");
        args.add(Math.PI);
        args.add(1.2f);
        args.add(new ArrayList<>());
        args.add(new HashMap<>());
        args.add(new StringBuilder());

        final var classes = args.stream()
                .map(Object::getClass)
                .toArray(Class<?>[]::new);

        disp.connect(ev, (argv) -> {
            for (int i = 0; i < argv.length; i++)
                assertInstanceOf(classes[i], argv[i]);
        });

        disp.emit(ev, args.toArray());
    }
}
