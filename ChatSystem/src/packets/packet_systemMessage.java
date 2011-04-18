package packets;

import java.io.Serializable;

public class packet_systemMessage implements Serializable {

    private byte opcode;
    private String time;

    public packet_systemMessage() {
    }

    public packet_systemMessage(byte opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the opcode
     */
    public byte getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(byte opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }
}