package chatsystem_server;

import packets.packet_clientMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import packets.packet_newRoom;

public class room {

    private String roomName;
    private String description;
    private String question;
    private String answer;
    private String time;
    private int size;
    
    private client first;
    public  room next;

    String DATE_FORMAT = "kk:mm:ss, dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public room(String name, String des){
        Calendar c = Calendar.getInstance();
        String nowtime = sdf.format(c.getTime());
        
        this.roomName = name;
        this.description = des;
        this.question = null;
        this.answer = null;
        this.time = nowtime;
        this.first = null;
    }

    public room(packet_newRoom newRoom){
        Calendar c1 = Calendar.getInstance();
        String nowtime = sdf.format(c1.getTime());
        
        this.roomName = newRoom.getName();
        this.description = newRoom.getDescription();
        this.question = newRoom.getQuestion();
        this.answer = newRoom.getAnswer();
        this.time = nowtime;
        this.first = null;
    }
    
    public room(String name, String des, String ques, String answ){
        Calendar c1 = Calendar.getInstance();
        String nowtime = sdf.format(c1.getTime());
        
        this.roomName = name;
        this.description = des;
        this.question = ques;
        this.answer = answ;
        this.time = nowtime;
        this.first = null;
    }

    public String getRoomName() {
        return roomName;
    }
    
    public String getDescription() {
        return description;
    }

    public String getQuestion() {
        return question;
    }
    
    public String getAnswer() {
        return answer;
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

    public void insertClientToTheFirst(String name, ObjectOutputStream output){
        Calendar c1 = Calendar.getInstance();
        String nowtime = sdf.format(c1.getTime());
        
        client newClient = new client(name, nowtime, output);
        size++;
        
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
        
        size--;
        return current;
    }

    public void sendGroupMessage(Byte sm, packet_clientMessage um) throws IOException{
        client current = first;
        while(current != null)
        {
            current.getClientOutput().writeByte(sm);
            current.getClientOutput().flush();
            current.getClientOutput().writeObject(um);
            current.getClientOutput().flush();
            current = current.next;
        }
    }

    public client findClient(String name){
        client current = first;
        if(first == null){
            return null;
        }
        else
        {
        while(current.getClientName().compareTo(name)!=0)
        {
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

    public String getSameRoomClient(){
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

    public void sendRoomList(roomList cls, Thread t) throws IOException, InterruptedException{
        client current = first;
         while(current != null)
         {
             cls.sendAvailableRoom(current.getClientOutput(), t);
             current = current.next;
         }
    }
}