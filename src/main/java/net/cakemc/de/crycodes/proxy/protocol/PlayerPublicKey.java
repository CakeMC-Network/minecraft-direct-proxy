package net.cakemc.de.crycodes.proxy.protocol;

/**
 * The type Player public key.
 */
public class PlayerPublicKey {
    private final long expiry;
    private final byte[] key;
    private final byte[] signature;

    /**
     * Instantiates a new Player public key.
     *
     * @param expiry    the expiry
     * @param key       the key
     * @param signature the signature
     */
    public PlayerPublicKey(final long expiry, final byte[] key, final byte[] signature) {
        this.expiry = expiry;
        this.key = key;
        this.signature = signature;
    }

    /**
     * Gets expiry.
     *
     * @return the expiry
     */
    public long getExpiry() {
        return this.expiry;
    }

    /**
     * Get key byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getKey() {
        return this.key;
    }

    /**
     * Get signature byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getSignature() {
        return this.signature;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerPublicKey other)) return false;
        if (!other.canEqual(this)) return false;
        if (this.getExpiry() != other.getExpiry()) return false;
        if (!java.util.Arrays.equals(this.getKey(), other.getKey())) return false;
        return java.util.Arrays.equals(this.getSignature(), other.getSignature());
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerPublicKey;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $expiry = this.getExpiry();
        result = result * PRIME + (int) ($expiry >>> 32 ^ $expiry);
        result = result * PRIME + java.util.Arrays.hashCode(this.getKey());
        result = result * PRIME + java.util.Arrays.hashCode(this.getSignature());
        return result;
    }

    @Override
    public String toString() {
        return "PlayerPublicKey(expiry=" + this.getExpiry() + ", key=" + java.util.Arrays.toString(this.getKey()) + ", signature=" + java.util.Arrays.toString(this.getSignature()) + ")";
    }
}
