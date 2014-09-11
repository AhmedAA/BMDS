
public class Interference
{

	static int c = 0;
	static int N = 10000;
	
	public static void main(String[] args) throws Exception 
	{
		System.out.println ("Counting to 2*N = " + 2*N);
				
		Runnable r = () -> {			
			for (int j = 0; j < N; ++j) {
				++c;		
			}		
		};
				
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);			
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println ("Done, got: " + c);				
	}

}
