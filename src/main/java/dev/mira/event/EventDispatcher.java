package dev.mira.event;

import java.util.concurrent.ConcurrentHashMap;

/**
 * EventHandler
 */
public class EventDispatcher {

    private static EventDispatcher instance = null;

    private final ConcurrentHashMap<Event, HandlerSet> events;

    private EventDispatcher() {
        this.events = new ConcurrentHashMap<>();
    }

    private void nonNull(final Object... args) {
        for (int i = 0; i < args.length; i++)
            if (args[i] == null)
                throw new IllegalArgumentException(
                        String.format("Argument #%d cannot be null", i));
    }

    public boolean connect(final Event event, final EventHandler handler) {
        nonNull(event, handler);

        this.events.putIfAbsent(event, new HandlerSet());
        return this.events.get(event).add(handler);
    }

    public boolean disconnect(final Event event, final EventHandler handler) {
        nonNull(event, handler);

        final var set = this.events.get(event);
        if (set == null)
            return false;
        return set.remove(handler);
    }

    public void emit(final Event event, final Object... args) {
        nonNull(event);

        final var set = this.events.get(event);
        if (set == null)
            return;
        for (final var handler : set)
            new Thread(() -> handler.accept(args)).start();
    }

    public boolean isConnected(final Event event, final EventHandler handler) {
        nonNull(event, handler);

        final var set = this.events.get(event);
        if (set == null)
            return false;
        return set.contains(handler);
    }

    public static EventDispatcher getInstance() {
        if (instance == null) {
            synchronized (EventDispatcher.class) {
                instance = new EventDispatcher();
            }
        }
        return instance;
    }
}
