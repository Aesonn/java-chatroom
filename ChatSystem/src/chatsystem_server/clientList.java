package chatsystem_server;

import packets.packet_clientMessage;
import packets.packet_systemMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class clientList {

    private String roomName;
    private String password;
    private String time;
    private int size;
    private client first;
    public  clientList next;

    public clientList(String name, String time){
        this.roomName = name;
        this.password = null;
        this.time = time;
        this.first = null;
    }

    public clientList(String name, String pass, String time){
        this.roomName = name;
        this.password = pass;
        this.time = time;
        this.first = null;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getPassword() {
        return password;
    }

    public String getTime() {
        return time;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty(){
        return (first==null);
    }

    public void insertClientToTheFirst(String name, String time, ObjectOutputStream output){
        client newClient = new client(name, time, output);
        newClient.next = first;
        first = newClient;
    }

    public void insertClientToTheFirst(String name){
        client newClient = new client(name);
        newClient.next = first;
        first = newClient;
    }

    public client removeClient(String name){
        client current = first;
        client previous = first;
        while(current.getClientName().compareTo(name)!=0)
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

    public void sendGroupMessage(packet_systemMessage sm, packet_clientMessage um) throws IOException{
        client current = first;
        while(current != null)
        {
            current.getClientOutput().writeObject(sm);
            current.getClientOutput().writeObject(um);
            current = current.next;
        }
    }

    public client findClient(String name){
        client current = first;
        System.out.println("current1");
        if(first == null){
            return null;
        }
        else
        {
        while(current.getClientName().compareTo(name)!=0)
        {
            System.out.println("current2");
            if(current.next == null)
                return null;
            else
                current = current.next;
        }
        }
        return current;
    }

    public boolean checkClient(String name){
        return findClient(name)==null;
    }

   public boolean isExist(String name){
        client current = findClient(name);
        if(current != null)
            return true;
        else
            return false;
    }

    public String sendSameRoomClient(){
         client current = first;
         String List = "Online user:\n";
         int i = 1;
         while(current != null)
         {
            List += i+". "+current.getClientName()+"\n";
            current = current.next;
            i++;
         }
         return List;
     }

    public void sendRoomList(clientListStore cls) throws IOException{
        client current = first;
         while(current != null)
         {
             cls.sendAvailableRoom(current.getClientOutput());
             current = current.next;
         }
    }
}
