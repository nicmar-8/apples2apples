package D7032E_Software_Engineering;
public class Main {

	public static void main(String argv[]) {
		Apples2Apples game;
		if(argv.length == 0) {
			try {
				game = new Apples2Apples(0);				
			} catch (Exception e) {e.printStackTrace(System.out);}
		} else {
			try {
				//If just a number is submitted then this is the Server and there are online clients
				int numberOfOnlineClients = Integer.parseInt(argv[0]);
				game = new Apples2Apples(numberOfOnlineClients);
			} catch(NumberFormatException e) {
				//If it is not a number then we assume it's an URL and then this is one of the online clients
				try {
					game = new Apples2Apples(argv[0]);					
				} catch (Exception err){System.out.println(err.getMessage());}
			} catch(Exception e) {
				e.printStackTrace(System.out);
				System.out.println("Something went wrong");
			}
		}
	}
}
