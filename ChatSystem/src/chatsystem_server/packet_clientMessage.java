package chatsystem_server;

public class packet_clientMessage {
    private String name;
    private String message;
    private String time;

    public packet_clientMessage(){
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
