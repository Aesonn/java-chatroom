package chatsystem_server;

public class packet_roomData {

    private String name;
    private String password;
    private String time;

    public packet_roomData(){
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getTime() {
        return time;
    }    
}