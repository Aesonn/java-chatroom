package chatsystem_client;

import javax.swing.DefaultListModel;

public class roomList {

    private room first;

    public roomList() {
        first = null;
    }

    public boolean isEmpty() {
        return (first == null);
    }

    public void insertRoomToTheFirst(String name, String des, boolean isPrivate) {
        room newRoom = new room(name, des, isPrivate);

        newRoom.next = first;
        first = newRoom;
    }

    
    public void insertRoomToTheFirst(room newRoom) {
        newRoom.next = first;
        first = newRoom;
    }

    public room removeRoom(String name) {
        room current = first;
        room previous = first;
        while (current.getName().compareTo(name) != 0) {
            if (current.next == null) {
                return null;
            } else {
                previous = current;
                current = current.next;
            }
        }
        if (current == first) {
            first = first.next;
        } else {
            previous.next = current.next;
        }

        return current;
    }

    public room findRoom(String name) {
        room current = first;
        if (first == null) {
            return null;
        } else {
            while (current.getName().compareTo(name) != 0) {
                if (current.next == null) {
                    return null;
                } else {
                    current = current.next;
                }
            }
        }
        return current;
    }

    public boolean checkRoom(String name) {
        return findRoom(name) == null;
    }

    public boolean isExist(String name) {
        room current = findRoom(name);
        if (current != null) {
            return true;
        } else {
            return false;
        }
    }

    public void clearRoomList() {
        first = null;
    }

    public void showList(DefaultListModel model) {
        room current = first;
        int i = 0;
        if (!model.isEmpty()) {
            model.clear();
        }
        while (current != null) {
            model.addElement(current);
            System.out.println(current);
            current = current.next;
        }
    }
}
