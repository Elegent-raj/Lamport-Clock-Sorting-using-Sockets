# Lamport-Clock-Sorting-using-Sockets

For this project we had to implement Lamport Clock and quick sort using sockets. For the Lamport
clock, in order to simulate a distributed environment, I used Java's MulticastSocket and
DatagramPacket to send data between different threads. Once a message has been sent, it will be
relayed to all participating threads. However, only the one with the recipient ID updates its local time
and is considered a receiver. First, we instantiate a Lamport Clock instance in LamportClock.java file.
After that a new Lamport Clock object is created and it joins the pool/group along with the rest and
begins receiving messages. An event class is defined inside which we also include a sender's and
receiver's ID, the time of that event and its content. Types of events are LOCAL, SEND. Inside the
main.java file, besides participating threads that act as Lamport Clock instances, we also have a master
thread to keep track of the messages being sent among those participants. The master's job is to call
the method updateTime whenever there is a new Event happening. The reason we need to do that is
because as a Thread is in the loop to receive messages, it pretty much can't do anything else. After
this we call updateTime method, there is a switch statement to update time based on the type of
event. Almost all the code for Lamport Clock was implemented by me, Rohit Raj.
For the Quick sort using sockets project, we have created two files SortServer and SortClient. On server
side, a ServerSocket is defined, which waits for the client in the listening mode, on a particular TCP
port. This ServerSocket holds until a Client Socket is connected successfully. As soon as the client is
connected, another Socket comes into existence that will enable data sharing between the respective
client and server. A temporary Socket is created to handle the request from that particular client and
our main server socket will be free again, to listen to other requests. Hence, we have ServerSocket(ss)
and Socket(s) in SortServer. On client side, we first establish the socket connection with server. Now
program asks about the number of integers client wants to sort. Numbers are then sent to server
which sorts the numbers and sends it to client to be shown. Most of the implementation has been
done by Anirudh Veludandi, Rohit Raj wrote the code for serializing input and output buffered stream.
