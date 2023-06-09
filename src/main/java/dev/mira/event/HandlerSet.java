package dev.mira.event;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HandlerSet
 */
final class HandlerSet extends ConcurrentHashMap<EventHandler, Boolean> implements Iterable<EventHandler> {

    public boolean add(final EventHandler handler) {
        if (this.get(handler) != null)
            return false;
        return this.put(handler, true) == null;
    }

    @Override
    public Boolean remove(final Object key) {
        return super.remove(key) != null;
    }

    @Override
    public Iterator<EventHandler> iterator() {
        return this.keys().asIterator();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.get(key) != null;
    }

    @Override
    public boolean contains(final Object key) {
        return this.containsKey(key);
    }
}
