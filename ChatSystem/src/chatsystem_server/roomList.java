package chatsystem_server;

import packets.Opcode;
import java.io.IOException;
import java.io.ObjectOutputStream;
import packets.packet_newRoom;
import packets.packet_roomData;

public class roomList implements Opcode{

    private room first;

    public roomList(){
        this.first = null;
    }

    public boolean isEmpty(){
        return (first==null);
    }

    public room findClientList(String name){
        room current = first;
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
        room current = findClientList(name);
        if(current != null)
            return true;
        else
            return false;
    }

    public void insertClientListToTheFirst(String name, String des){
        room newClientList = new room(name, des);
        newClientList.next = first;
        first = newClientList;
    }
    
    public void insertClientListToTheFirst(packet_newRoom newRoom){
        room newClientList = new room(newRoom);
        newClientList.next = first;
        first = newClientList;
    }

    public void insertClientListToTheFirst(String name, String des, String ques, String answ){
        room newUserList = new room(name, des, ques, answ);
        newUserList.next = first;
        first = newUserList;
    }

     public room removeClientList(String name){
        room current = first;
        room previous = first;
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

     public room checkClient(String name){
        room current = first;
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

     public void sendAvailableRoom(ObjectOutputStream os, Thread t) throws IOException, InterruptedException{
         room current = first;
         os.writeByte(SMSG_SEND_ROOMLIST);
         os.flush();
         while(current != null)
         {
             os.writeObject(new packet_roomData(current.getRoomName(), current.getDescription(), (current.getQuestion()!=null)));
             os.flush();
             
             t.sleep(10);
             
             current = current.next;
         }
         
         os.writeObject(new packet_roomData());
         os.flush();
     }
}