package packets;

import java.io.Serializable;

public class packet_request implements Serializable {

    private byte opcode;
    private String name;
    private String time;

    public packet_request(byte opcode, String name) {
        this.opcode = opcode;
        this.name = name;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
