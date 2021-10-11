
// Rohit Raj Anirudh Veludandi
// Client side code for sorting
// TCP/IP implementation
// Run SortClient only after running SortServer
import java.io.*;
import java.net.*;

class SortClient {
	public static void main(String ar[]) throws Exception {
		// Creating socket and connecting it to server
		Socket s = new Socket("localhost", 12345);
		// Serializing the input and output streams
		PrintWriter p = new PrintWriter(s.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		BufferedReader ink = new BufferedReader(new InputStreamReader(System.in));
		// Taking the input from client
		System.out.println("How many numbers to sort? ");
		int num = Integer.parseInt(ink.readLine());
		p.println(num);
		p.flush();
		System.out.println("Enter " + num + " numbers to sort::");
		String sarr[] = new String[num];
		for (int i = 0; i < num; i++) {
			System.out.print("No. " + (i + 1) + " = ");
			sarr[i] = ink.readLine();
			p.println(sarr[i]);
			p.flush();
		}
		String res;
		System.out.println("\nSorted Numbers received from server::");
		while ((res = in.readLine()) != null) {
			System.out.println(res);
		}
		s.close();

	}
}