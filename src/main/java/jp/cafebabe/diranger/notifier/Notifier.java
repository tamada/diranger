package jp.cafebabe.diranger.notifier;

/**
 * This class represents the notification center.
 */
public interface Notifier {
    void push(String message);

    default void pushf(String formatter, Object... params) {
        push(String.format(formatter, params));
    }

    default void push(Exception e) {

    }
}
