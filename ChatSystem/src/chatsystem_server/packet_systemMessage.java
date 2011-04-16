package chatsystem_server;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class packet_systemMessage {

    private byte opcode;
    private String time;

    public packet_systemMessage(byte opcode) {
        String DATE_FORMAT = " hh:mm,dd/MM/yyyy;";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar c1 = Calendar.getInstance();
        time = sdf.format(c1.getTime());
        this.opcode = opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(byte opcode) {
        this.opcode = opcode;
    }
}
