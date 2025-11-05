package Lab6;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatRoom {
    String name;
    Set<String> users;

    public ChatRoom(String name) {
        this.name = name;
        users = new HashSet<>();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        if(users.contains(username)) {
            users.remove(username);
        }
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        for(String u : users) {
            sb.append(u).append("\n");
        }
        if(users.isEmpty()) {
            sb.append("EMPTY").append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(name, chatRoom.name) && Objects.equals(users, chatRoom.users);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(users);
        return result;
    }

    public String getName() {
        return name;
    }
}

class ChatSystem {
    Map<String,ChatRoom> rooms;
    Set<String> users;

    public ChatSystem() {
        rooms = new TreeMap<>();
        users = new HashSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        return rooms.get(roomName);
    }

    void register(String userName) {
        users.add(userName);
        rooms.entrySet().stream().min(Map.Entry.comparingByValue(Comparator.comparing(ChatRoom::numUsers).thenComparing(ChatRoom::getName).reversed()))
                .get().getValue().addUser(userName);
    }

    void registerAndJoin(String userName, String roomName) {
        users.add(userName);
        rooms.put(roomName, new ChatRoom(roomName));
    }
    //го  додава  корисникот  во  собата  со соодветно  име  доколку  таа  постои,  во  спротивно  фрла  исклучок  од  типот
    void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        if(!users.contains(userName)) {
            throw new NoSuchUserException(userName);
        }
        rooms.get(roomName).addUser(userName);
    }

    void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        if(!users.contains(username)) {
            throw new NoSuchUserException(username);
        }
        rooms.get(roomName).removeUser(username);
    }

    void followFriend(String username, String friend_username) throws NoSuchUserException, NoSuchRoomException {
        if(!users.contains(friend_username)) {
            throw new NoSuchRoomException(friend_username);
        }
        if(!users.contains(username)) {
            throw new NoSuchUserException(username);
        }
        rooms.values().stream()
                .filter(c -> c.hasUser(friend_username))
                .forEach(c -> c.addUser(username));
    }

    public void broadcast(String roomName, String message) throws NoSuchRoomException {
        if (!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        boolean flag = rooms.containsKey(roomName);
        if(flag) {
            System.out.println(message);
        }
    }
}

public class ChatSystemTest {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println();
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method[] mts = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    try {
                        System.out.println(cs.getRoom(jin.next())+"\n");
                    } catch (NoSuchRoomException ignored) {

                    }
                    continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        Object[] params = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        try {
                            m.invoke(cs,params);
                        } catch (IllegalAccessException | InvocationTargetException ignored) {

                        }
                    }
                }
            }
        }
    }
}
