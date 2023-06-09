package dev.mira.event;

import java.util.function.Consumer;

/**
 * EventHandler
 */
@FunctionalInterface
public interface EventHandler extends Consumer<Object[]> {
}
