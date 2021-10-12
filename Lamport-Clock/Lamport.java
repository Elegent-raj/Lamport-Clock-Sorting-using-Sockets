// Simple Lamport Implementation
// Rohit Raj
import java.io.*;
import java.net.*;

// using runnable interface as demonstrated in MultiThreading demo in class
class Clock implements Runnable
{	
	// initialize thread and clk
	Thread t;
	int clk;
	int inc;

	// defining clock class
	public Clock(int c,int i)
	{
		clk=c; inc=i;
		t=new Thread(this,"Clock");  
		t.start();

	}
	public void run()
	{
		for(int i=0;i<10000;i++)
		{
			try
			{
			clk=clk+inc;
			Thread.sleep(1000);

			}
			catch(Exception e){}
		}

	}
	int getClk()
	{
		return clk;
	}
	void setClk(int c)
	{
		clk=c;
	}

}
// server class
class LServer implements Runnable
{
	Thread t;
	Clock c;

	public LServer()
	{
		c=new Clock(0,1);
		t=new Thread(this,"Server");
		t.start();
	}
	public void run()
	{
		ServerSocket svr;
		DataInputStream din;
		DataOutputStream dout;
		int clk=999;
		try
		{
			svr=new ServerSocket(100);
			System.out.println("Listening on port 100");
			Socket cl=svr.accept();

			System.out.println("\nConnected to >> " + cl.getInetAddress().toString());
			din=new DataInputStream(cl.getInputStream());
			dout=new DataOutputStream(cl.getOutputStream());
			String msg=null;
			while(true)
			{
					try
					{
					msg=din.readUTF();
					clk=Integer.parseInt(msg);
					System.out.println("Client Clock >> " + clk);
					System.out.println("Server Clock >> " + c.getClk());
					if(c.getClk()<clk)  // 
					{
						System.out.println("Lamports in picture...");
						c.setClk(clk+1);
						System.out.println("New Server Clock >> " + c.getClk());
					}}
					catch(Exception e)
					{
							if(msg.equalsIgnoreCase("sc time")) {System.out.println("Server Clock >> " + c.getClk()); }
					}
			}
		}
		catch(Exception e)
		{}

	}
}

// Client class
class LClient implements Runnable
{
	Thread t;
	Clock c;

	public LClient()
	{
		c=new Clock(0,2);
		t=new Thread(this,"Client");
		t.start();
	}
	public void run()
	{
		int port;
		String rip;
		Socket s;
		DataInputStream din;
		DataOutputStream dout;
		try
		{
		BufferedReader bin=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Remote IP >> ");
		rip=bin.readLine();
		System.out.println("Enter Remote Port >> ");
		port=Integer.parseInt(bin.readLine());
		s=new Socket(rip,port);
		System.out.println("Client -> Connection Made ");
		din=new DataInputStream(s.getInputStream());
		dout=new DataOutputStream(s.getOutputStream());
		String ch;
		while(true)
		{
			System.out.println("Enter Your Choice");
			ch=bin.readLine();
			if(ch.equalsIgnoreCase("send clock"))
				dout.writeUTF(Integer.toString(c.getClk()));
			else if(ch.equalsIgnoreCase("quit"))
				System.exit(-1);
			else if(ch.equalsIgnoreCase("sc time"))
			dout.writeUTF(ch);
		}

		}
		catch(Exception e){}
	}

}

public class Lamport
{

	public static void main(String a[])throws Exception
	{
		LClient cl=new LClient();
		LServer sv=new LServer();
		try
		{
			sv.t.join();
			cl.t.join();
		}
		catch(Exception e)
		{}
	}
}
