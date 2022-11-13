module diranger {
    requires java.logging;
    requires java.desktop;
    requires io.vavr;
    requires org.slf4j;

    exports jp.cafebabe.diranger;
    exports jp.cafebabe.diranger.ignorefiles;
    exports jp.cafebabe.diranger.impl;
}