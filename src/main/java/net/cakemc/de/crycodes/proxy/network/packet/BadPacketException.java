package net.cakemc.de.crycodes.proxy.network.packet;

/**
 * The type Bad packet exception.
 */
public class BadPacketException extends RuntimeException {

    /**
     * Instantiates a new Bad packet exception.
     *
     * @param message the message
     */
    public BadPacketException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Bad packet exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public BadPacketException(String message, Throwable cause) {
        super(message, cause);
    }
}
