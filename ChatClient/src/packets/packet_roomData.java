package packets;

import java.io.Serializable;

public class packet_roomData implements Serializable {

    private String name;
    private String password;
    private String time;

    public packet_roomData(String name, String pass){
        this.name = name;
        this.password = pass;
    }

    public boolean isEmpty(){
        return (name==null);
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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