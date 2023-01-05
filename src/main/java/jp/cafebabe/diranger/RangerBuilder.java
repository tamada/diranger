package jp.cafebabe.diranger;

import jp.cafebabe.diranger.impl.ParallelModel;
import jp.cafebabe.diranger.impl.SimpleModel;

/**
 * A builder class for <code>{@link Ranger Ranger}</code>.
 * @author Haruaki TAMADA
 */
public class RangerBuilder {
    public enum Type {
        /**
         * This type first returns an iterator object.
         * It then traverses the directory in parallel and adds entries to the iterator object.
         * The iterator object blocks if the entry is empty and the traversal has not been completed,
         * until entry added.
         */
        Parallel,
        /**
         * This type stores all entries to the list.
         * And then, returns <code>Stream</code> object from the list.
         */
        Simple,
    }

    /**
     * <code>build(Type.Queue)</code>
     * @return An instance of {@link Ranger Ranger}
     */
    public static Ranger build() {
        return build(Type.Parallel);
    }

    /**
     * returns a suitable object of {@link Ranger <code>Ranger</code>}.
     * @param type Simple or Parallel
     * @return An instance of {@link Ranger Ranger}
     */
    public static Ranger build(Type type) {
        return switch(type) {
            case Simple -> new SimpleModel();
            case Parallel -> new ParallelModel();
        };
    }
}
