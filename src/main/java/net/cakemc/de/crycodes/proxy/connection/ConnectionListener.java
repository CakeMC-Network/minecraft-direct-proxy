package net.cakemc.de.crycodes.proxy.connection;

/**
 * The interface Connection listener.
 *
 * @param <V> the type parameter
 */
public interface ConnectionListener<V> {

    /**
     * Done.
     *
     * @param result the result
     * @param error  the error
     */
    void done(V result, Throwable error);
}
