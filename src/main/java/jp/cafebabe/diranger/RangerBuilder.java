package jp.cafebabe.diranger;

import jp.cafebabe.diranger.impl.Queue;
import jp.cafebabe.diranger.impl.RangerDefault;

/**
 * A builder class for {@link Ranger <code>Ranger</code>}.
 * @author Haruaki TAMADA
 */
public class RangerBuilder {
    public static enum Type {
        /**
         * This type first returns an iterator object.
         * It then traverses the directory in parallel and adds entries to the iterator object.
         * The iterator object blocks if the entry is empty and the traversal has not been completed,
         * until entry added.
         */
        Queue,
        /**
         * This type stores all entries to the list.
         * And then, returns <code>Stream</code> object from the list.
         */
        Default,
    }

    /**
     * <code>build(Type.Default)</code>
     * @return
     */
    public static Ranger build() {
        return build(Type.Default);
    }

    /**
     * returns an suitable object of {@link Ranger <code>Ranger</code>}.
     * @param type
     * @return
     */
    public static Ranger build(Type type) {
        return switch(type) {
            case Queue -> new Queue();
            case Default -> new RangerDefault();
        };
    }
}
