// Rohit Raj Anirudh Veludandi
// Server side code for sorting
import java.io.*;
import java.net.*;

// quicksort 
class QuickSort {
	
	int partition(int[] arr, int low, int high) {
		int p = arr[high];
		// arr[high] is selected as pivot;
		int i = low - 1;
		for(int j = low; j < high; ++j) {
			if(arr[j] <= p) {
				++i;
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		int temp = arr[i+1];
		arr[i+1] = arr[high];
		arr[high] = temp;
		
		return i+1;
	}
	
	void quick_sort(int[] arr, int low, int high) {
		if(low < high) {
			// elements less than arr[q] are left side of q
			// elements greater than arr[q] are right side of q
			int q = partition(arr, low, high); 
			quick_sort(arr, low, q-1); // sorts left partition
			quick_sort(arr, q+1, high); // sorts right partition
		}
	}

}

class SortServer
{
	public static void main(String ar[]) throws Exception
	{
		ServerSocket s1=new ServerSocket(12345); // Object of ServerSocket instantiated and port number specified
		System.out.println("Server Started");
		Socket s=s1.accept(); // accpet method of ServerSocket

		// InputStream of client coupling with OutputStream of server and vice versa
		PrintWriter p=new PrintWriter(s.getOutputStream());
		BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream()));
		String num=in.readLine();
		int n=Integer.parseInt(num);
		System.out.println("Client want to sort "+ n +" numbers");
		// Receiving  and storing integer inputs to be sorted
		String sarr[]=new String[n];
		int arr[]=new int[n];
		int c;
		// To show the numbers in realtime which client is entering
		System.out.println("Received numbers::\n");
		for(int i=0;i<n;i++)
		{
			sarr[i]=in.readLine();
			arr[i]=Integer.parseInt(sarr[i]);
			System.out.println("No. " + (i+1) + " = " + arr[i]);
		
		}
		// Doing the sorting server side
		QuickSort obj = new QuickSort();
		obj.quick_sort(arr, 0, n-1);
		
		// Showing the sorted list of Numbers
		System.out.println("\nSorted list of numbers");
		String sendarr=new String();
		for (c = 0; c < n; c++) 
		{
			sendarr+="\nnum ("+c+")="+arr[c];
		}
		System.out.println(sendarr);
		p.println(sendarr);
		p.flush();
		s.close();

	}
}  