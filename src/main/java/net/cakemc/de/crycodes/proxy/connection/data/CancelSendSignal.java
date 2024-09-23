package net.cakemc.de.crycodes.proxy.connection.data;

/**
 * The type Cancel send signal.
 */
public class CancelSendSignal extends Error {
    /**
     * The constant INSTANCE.
     */
    public static final CancelSendSignal INSTANCE = new CancelSendSignal();

    private CancelSendSignal() {
    }

    @Override
    public Throwable initCause(Throwable cause) {
        return this;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
