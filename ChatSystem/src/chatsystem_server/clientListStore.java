package chatsystem_server;

import packets.Opcode;
import packets.packet_systemMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import packets.packet_roomData;

public class clientListStore implements Opcode{

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
        while(current != null)
        {
            if(current.findClient(name)!=null)
            {
                break;
            }
            else
            {
            if(current.next == null)
                return null;
            else
                current = current.next;
            }
        }
        return current;
     }

     public void sendAvailableRoom(ObjectOutputStream os) throws IOException{
         clientList current = first;
         os.writeObject(new packet_systemMessage(SMSG_SEND_ROOMLIST));
         while(current != null)
         {
            os.writeObject(new packet_roomData(current.getRoomName(), current.getPassword()));
            current = current.next;
         }
         os.writeObject(new packet_roomData(null,null));
     }
}
