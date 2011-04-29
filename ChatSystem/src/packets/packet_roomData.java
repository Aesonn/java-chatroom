package packets;

import java.io.Serializable;

public class packet_roomData implements Serializable {

    private String name;
    private String description;
    private boolean isPrivate;

    public packet_roomData(String name, String des, boolean isPrivate) {
        this.name = name;
        this.description = des;
        this.isPrivate = isPrivate;
    }
    
    public packet_roomData() {
        this.name = null;
        this.description = null;
        this.isPrivate = false;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the isPrivate
     */
    public boolean isIsPrivate() {
        return isPrivate;
    }
}