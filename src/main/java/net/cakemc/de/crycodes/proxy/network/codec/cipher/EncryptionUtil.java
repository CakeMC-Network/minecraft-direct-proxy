package net.cakemc.de.crycodes.proxy.network.codec.cipher;

import net.cakemc.de.crycodes.proxy.network.packet.impl.login.EncryptionRequestPacket;
import net.cakemc.de.crycodes.proxy.network.packet.impl.login.EncryptionResponsePacket;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * The type Encryption util.
 */
public class EncryptionUtil {
    /**
     * The constant keys.
     */
    public static final KeyPair keys;
    private static final Random random = new Random();

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            keys = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Encrypt request encryption request packet.
     *
     * @return the encryption request packet
     */
    public static EncryptionRequestPacket encryptRequest() {
        String hash = Long.toString(random.nextLong(), 16);
        byte[] pubKey = keys.getPublic().getEncoded();
        byte[] verify = new byte[4];
        random.nextBytes(verify);
        // always auth for now
        return new EncryptionRequestPacket(hash, pubKey, verify, true);
    }

    /**
     * Gets secret.
     *
     * @param resp    the resp
     * @param request the request
     * @return the secret
     * @throws GeneralSecurityException the general security exception
     */
    public static SecretKey getSecret(EncryptionResponsePacket resp, EncryptionRequestPacket request) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keys.getPrivate());
        return new SecretKeySpec(cipher.doFinal(resp.getSharedSecret()), "AES");
    }

    /**
     * Gets cipher.
     *
     * @param forEncryption the for encryption
     * @param shared        the shared
     * @return the cipher
     * @throws GeneralSecurityException the general security exception
     */
    public static Encryption getCipher(boolean forEncryption, SecretKey shared) throws GeneralSecurityException {
        Encryption cipher = new Encryption();
        cipher.init(forEncryption, shared);
        return cipher;
    }

}
