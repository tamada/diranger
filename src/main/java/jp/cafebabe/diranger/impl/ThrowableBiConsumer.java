package jp.cafebabe.diranger.impl;

@FunctionalInterface
interface ThrowableBiConsumer<T, U, E extends Throwable> {
    void accept(T t, U u) throws E;
}
