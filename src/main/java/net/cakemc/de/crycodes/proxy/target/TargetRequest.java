package net.cakemc.de.crycodes.proxy.target;

import net.cakemc.de.crycodes.proxy.connection.ConnectionListener;
import net.cakemc.de.crycodes.proxy.events.server.ProxyServerConnectEvent;


/**
 * The type Target request.
 */
public class TargetRequest {
    private final AbstractTarget target;
    private final ConnectionReason reason;
    private final ConnectionListener<Result> connectionListener;
    private int connectTimeout;
    private boolean retry;

    /**
     * Instantiates a new Target request.
     *
     * @param target             the target
     * @param reason             the reason
     * @param connectionListener the connection listener
     */
    public TargetRequest(AbstractTarget target, ConnectionReason reason, ConnectionListener<Result> connectionListener) {
        this.target = target;
        this.reason = reason;
        this.connectionListener = connectionListener;
    }

    /**
     * Gets target.
     *
     * @return the target
     */
    public AbstractTarget getTarget() {
        return this.target;
    }

    /**
     * Gets reason.
     *
     * @return the reason
     */
    public ConnectionReason getReason() {
        return this.reason;
    }

    /**
     * Gets callback.
     *
     * @return the callback
     */
    public ConnectionListener<Result> getCallback() {
        return this.connectionListener;
    }

    /**
     * Gets connect timeout.
     *
     * @return the connect timeout
     */
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     * Sets connect timeout.
     *
     * @param connectTimeout the connect timeout
     */
    public void setConnectTimeout(final int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Is retry boolean.
     *
     * @return the boolean
     */
    public boolean isRetry() {
        return this.retry;
    }

    /**
     * Sets retry.
     *
     * @param retry the retry
     */
    public void setRetry(final boolean retry) {
        this.retry = retry;
    }

    /**
     * The enum Result.
     */
    public enum Result {
        /**
         * Event cancel result.
         */
        EVENT_CANCEL,
        /**
         * Already connected result.
         */
        ALREADY_CONNECTED,
        /**
         * Already connecting result.
         */
        ALREADY_CONNECTING,
        /**
         * Success result.
         */
        SUCCESS,
        /**
         * Fail result.
         */
        FAIL
    }
}
