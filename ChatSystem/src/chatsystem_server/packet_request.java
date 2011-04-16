package chatsystem_server;

public class packet_request {

    private byte opcode;
    private String name;
    private String time;

    public packet_request() {
    }

    public byte getOpcode() {
        return opcode;
    }

    public String getName() {
        return name;
    }

   public String getTime() {
        return time;
    }
    
}
