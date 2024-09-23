package net.cakemc.de.crycodes.proxy.network.packet.impl;


import io.netty.buffer.ByteBuf;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacket;
import net.cakemc.de.crycodes.proxy.network.packet.AbstractPacketHandler;
import net.cakemc.de.crycodes.proxy.protocol.ProtocolVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The type Disconnect report details packet.
 */
public class DisconnectReportDetailsPacket extends AbstractPacket {
    private Map<String, String> details;

    /**
     * Instantiates a new Disconnect report details packet.
     */
    public DisconnectReportDetailsPacket() {
    }

    /**
     * Instantiates a new Disconnect report details packet.
     *
     * @param details the details
     */
    public DisconnectReportDetailsPacket(final Map<String, String> details) {
        this.details = details;
    }

    @Override
    public void read(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {
        int len = readVarInt(buf);

        details = new HashMap<>();
        for (int i = 0; i < len; i++) {
            details.put(readString(buf, 128), readString(buf, 4096));
        }
    }

    @Override
    public void write(ByteBuf buf, ProtocolVersion.Direction direction, int protocolVersion) {

        writeVarInt(details.size(), buf);
        for (Map.Entry<String, String> detail : details.entrySet()) {
            writeString(detail.getKey(), buf, 128);
            writeString(detail.getValue(), buf, 4096);
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception {
        handler.handle(this);
    }

    /**
     * Gets details.
     *
     * @return the details
     */
    public Map<String, String> getDetails() {
        return this.details;
    }

    /**
     * Sets details.
     *
     * @param details the details
     */
    public void setDetails(final Map<String, String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "DisconnectReportDetailsPacket(details=" + this.getDetails() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DisconnectReportDetailsPacket other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$details = this.getDetails();
        final Object other$details = other.getDetails();
        return Objects.equals(this$details, other$details);
    }

    /**
     * Can equal boolean.
     *
     * @param other the other
     * @return the boolean
     */
    protected boolean canEqual(final Object other) {
        return other instanceof DisconnectReportDetailsPacket;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $details = this.getDetails();
        result = result * PRIME + ($details == null ? 43 : $details.hashCode());
        return result;
    }
}
