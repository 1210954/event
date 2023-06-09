package dev.mira.event;

/**
 * Event
 */
public class Event {

    private final String name;

    private Event(final String name) {
        this.name = name;
    }

    public static Event valueOf(final String evName) {
        if (evName == null || evName.isBlank())
            throw new IllegalArgumentException("Event name cannot be null/empty!");
        return new Event(evName);
    }

    @Override
    public boolean equals(final Object obj) {
        return switch (obj) {
            case Event ev -> this == ev || this.name.equals(ev.name);
            default -> false;
        };
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
