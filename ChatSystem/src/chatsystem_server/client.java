package chatsystem_server;

import java.io.ObjectOutputStream;

public class client {

    private String clientName;
    private String joinRoomTime;
    private ObjectOutputStream clientOutput;
    public  client next;

    public client(String name, String time,ObjectOutputStream output){
        this.clientName = name;
        this.joinRoomTime = time;
        this.clientOutput = output;
    }

    public client(String name){
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    public String getJoinRoomTime() {
        return joinRoomTime;
    }

    public ObjectOutputStream getClientOutput() {
        return clientOutput;
    }
}
