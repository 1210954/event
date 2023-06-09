package dev.mira;

import dev.mira.event.Event;
import dev.mira.event.EventDispatcher;
import dev.mira.event.EventHandler;

/**
 * Hello world!
 *
 */
public class App {

    private static final EventHandler h = (argv) -> {
        int x = (Integer) argv[0];
        int y = Integer.parseInt((String) argv[1]);

        System.out.printf("Arg 1 + Arg 2 = %d\n", x+y);
    };

    public static void main(String[] args) {
        var d = EventDispatcher.getInstance();

        d.connect(Event.valueOf("Ola"), (__) -> System.out.println("Hello"));
        d.connect(Event.valueOf("Ola"), (__) -> System.out.println("I was invoked"));
        d.connect(Event.valueOf("Ola"), (__) -> System.out.println("me too"));

        d.connect(Event.valueOf("Idk"), (argv) -> {
            int x = (Integer) argv[0];
            int y = (Integer) argv[1];

            System.out.printf("Arg 1 + Arg 2 = %d\n", x+y);
        });

        d.connect(Event.valueOf("Idk2"), h);

        d.emit(Event.valueOf("Ola"));
        d.emit(Event.valueOf("Idk"), 24, 22);
        d.emit(Event.valueOf("Idk2"), 24, "22");

        System.out.println(d.disconnect(Event.valueOf("Idk2"), h));
        d.emit(Event.valueOf("Idk2"), 24, "22");
    }
}
