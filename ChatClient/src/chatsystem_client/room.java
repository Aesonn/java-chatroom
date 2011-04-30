package chatsystem_client;

public class room {
    private String name;
    private String description;
    private boolean isPrivate;
    public room next;
    
    public room(String name, String des, boolean isPrivate){
        this.name = name;
        this.description = des;
        this.isPrivate = isPrivate;
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
    
   
    @Override
    public String toString(){
        if(isIsPrivate())
            return "<Private>| "+getName()+"  -  "+getDescription();
        else
            return "<Public> | "+getName()+"  -  "+getDescription();
    }
}

