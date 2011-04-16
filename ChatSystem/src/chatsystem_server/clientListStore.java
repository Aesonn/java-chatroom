package chatsystem_server;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class clientListStore {

    private clientList first;

    public clientListStore(){
        this.first = null;
    }

    public boolean isEmpty(){
        return (first==null);
    }

    public clientList findClientList(String name){
        clientList current = first;
        while(current.getRoomName().compareTo(name)!=0)
        {
            if(current.next == null)
                return null;
            else
                current = current.next;
        }
        return current;
    }

    public boolean isExist(String name){
        clientList current = findClientList(name);
        if(current != null)
            return true;
        else
            return false;
    }

    public void insertClientListToTheFirst(String name, String time){
        clientList newUserList = new clientList(name, time);
        newUserList.next = first;
        first = newUserList;
    }

    public void insertClientListToTheFirst(String name, String pass, String time){
        clientList newUserList = new clientList(name, pass, time);
        newUserList.next = first;
        first = newUserList;
    }

     public clientList removeClientList(String name){
        clientList current = first;
        clientList previous = first;
        while(current.getRoomName().compareTo(name)!=0)
        {
            if(current.next == null)
                return null;
            else
            {
                previous = current;
                current = current.next;
            }
        }
        if(current == first)
            first = first.next;
        else
            previous.next = current.next;
        return current;
    }

     public clientList checkClient(String name){
        clientList current = first;
        while(current.findClient(name)!=null)
        {
            if(current.next == null)
                return null;
            else
                current = current.next;
        }
        return current;
     }

     public void sendAvailableRoom(ObjectOutputStream os) throws IOException{
         clientList current = first;
         while(current != null)
         {
            os.writeObject(current.getRoomName());
            current = current.next;
         }
     }
}
