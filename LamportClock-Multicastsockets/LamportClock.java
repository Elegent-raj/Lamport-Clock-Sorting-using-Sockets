import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.lang.Thread;

public class LamportClock extends Thread {

    private MulticastSocket sock;
    private InetAddress group;
    private int port;

    // local time of a process
    private int time;

    // order of the process (viewed from the master's perspective)
    private int order;

    public LamportClock(InetAddress group, int port) throws Exception {
        this.group = group;
        this.port = port;

        // if we don't assign an order to a process
        this.order = -1;

        // set local time to random, trying to do it with random clock values
        // Random rand = new Random();
        // this.time = rand.nextInt(10);
        this.time = 0;

        sock = new MulticastSocket(port);
        sock.setTimeToLive(2);
        sock.joinGroup(group);
    }

    public LamportClock(InetAddress group, int port, int order) throws Exception {
        this(group, port);
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }

    public int getTime() {
        return this.time;
    }

    public int localEvent() {
        ++this.time;
        System.out.println(this.getId() + " performing local event. local time is " + this.time);
        return this.time;
    }

    public int receivedEvent(long senderId, int receivedTime) {
        System.out.println(this.getId() + " received message from "
           + senderId + ". local time is " + this.time);

        return this.time;
    }

    public int sendEvent(String msg) throws Exception {
        byte[] data = msg.getBytes();

        DatagramPacket d = new DatagramPacket(data, data.length, group, port);
        sock.send(d);

        return this.time;
    }

    public void updateTime(Event e) throws Exception {
        int type = e.type;

        switch (type) {
            // LOCAL EVENT
            case 0:
                this.localEvent();
                break;

            // SEND EVENT
            case 1: // extract information from the event
                long senderId = e.senderId;
                long receiverId = e.receiverId;
                // increase the time first before sending the message
                e.localTime = ++this.time;
                String content = e.content;

                 /** send a message of the following format
                 * SENDER_ID|RECEIVER_ID|LOCAL_TIME
                 */
                String msg = Long.toString(senderId) + "-" + Long.toString(receiverId)
                    + "-" + e.localTime + "-" + content;
                sendEvent(msg);
                break;

            // RECEIVE EVENT
            case 2:
                // update its logical clock
                this.time = Math.max(e.localTime, this.time) + 1;
                break;

            /*
            // LEAVE EVENT
            case 3:
                // leave group
                sock.leaveGroup(group);
                System.out.println("Process Id " + this.getId() + "left");
                break;
            */

        }
        

        printTime(e);
    }

    public void printTime(Event e) {
        String logging = "********************************************\n";
        logging += "Process " + this.getId() + "\n";
        logging += "Process' local time " + this.getTime() + "\n";
        logging += "\tEvent type: ";

        switch(e.type) {
            case 0:
                logging += "LOCAL EVENT\n";
                break;
            case 1:
                logging += "SEND EVENT\n";
                break;
            case 2:
                logging += "RECEIVE EVENT\n";
                break;
            default:
                break;
        }

        logging += "\tEvent sender's ID: " + e.senderId + "\n";
        logging += "\tEvent receiver's ID: " + e.receiverId + "\n";
        logging += "\tEvent local time: " + e.localTime + "\n";
        logging += "\tEvent content: " + e.content + "\n";
        logging += "********************************************\n";

        System.out.print(logging);
    }

    public void run() {
        String greeting = "";
        greeting = "Unique ID " + this.getId() +
            " is initialized with local clock " + this.time;
        if (this.order != -1)
            greeting = "Process " + this.order + " " + greeting;

        System.out.println(greeting);
        try {
            while (true) {
                DatagramPacket d = new DatagramPacket(new byte[256], 256);
                sock.receive(d);
                String s = new String(d.getData());
                //System.out.println(this.getId() + " received " + s);

                String[] meta = s.trim().split("-");
                long senderId = Long.parseLong(meta[0]);
                long receiverId = Long.parseLong(meta[1]);
                int localTime = Integer.parseInt(meta[2]);
                    String content = "";
                    // if there is a message
                    if (meta.length >= 4)
                        content = meta[3];
    
                    if (this.getId() == receiverId) 
                    {
                        Event e = new Event(2, senderId, receiverId, localTime, content);
                        updateTime(e);
                    }
                }
        } catch (Exception e) {
            System.err.println("LC Failed: " + e);
            return;
        }
    }

}