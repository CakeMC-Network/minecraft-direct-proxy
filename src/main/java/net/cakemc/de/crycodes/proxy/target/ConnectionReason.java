package net.cakemc.de.crycodes.proxy.target;

public enum ConnectionReason {
        /**
         * Lobby fallback reason.
         */
        LOBBY_FALLBACK,
        /**
         * TargetServer down redirect reason.
         */
        SERVER_DOWN_REDIRECT,
        /**
         * Kick redirect reason.
         */
        KICK_REDIRECT,
        /**
         * Join proxy reason.
         */
        JOIN_PROXY,
        /**
         * Unknown reason.
         */
        UNKNOWN
    }