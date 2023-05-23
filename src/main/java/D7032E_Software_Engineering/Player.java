package D7032E_Software_Engineering;

import java.util.*; 
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;

class Player {
	public int playerID;
	public boolean isBot;
	public boolean online;
	public Socket connection;
	public BufferedReader inFromClient;
	public DataOutputStream outToClient;
	public ArrayList<String> hand;
	public ArrayList<String> greenApples = new ArrayList<String>();
	public Player(int playerID, ArrayList<String> hand, boolean isBot) {
		this.playerID = playerID; this.hand = hand; this.isBot = isBot; this.online = false;
	}
	public Player(int playerID, boolean isBot, Socket connection, BufferedReader inFromClient, DataOutputStream outToClient) {
		this.playerID = playerID; this.isBot = isBot; this.online = true;
		this.connection = connection; this.inFromClient = inFromClient; this.outToClient = outToClient;
	}

	public void play() {
		if(isBot) {
			/** BUG - FIX LATER
			 * For some reason I must sleep a random amount of time 
			 * or the playedApple ArrayList won't get all bot answers
 			 * (The teacher knows what the bug is, but thought this was fun to do :-)   )
			 **/
			Random rnd = ThreadLocalRandom.current();
			try{Thread.sleep(rnd.nextInt(500));}catch(Exception e){}
			// continue with non-buggy code

			Apples2Apples.playedApple.add(new PlayedApple(playerID, hand.get(0)));
			hand.remove(0);
		} else if(online){
			try {
				String aPlayedApple = inFromClient.readLine();
				Apples2Apples.playedApple.add(new PlayedApple(playerID, aPlayedApple));					
			} catch (Exception e) {}
		} else { //Server player, no separate thread needed since the server player always acts last
			System.out.println("Choose a red apple to play");
			for(int i=0; i<hand.size(); i++) {
				System.out.println("["+i+"]   " + hand.get(i));
			}
			System.out.println("");

			int choice = 0;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String input=br.readLine();
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("That is not a valid option");
				play();
			} catch (Exception e) {}
			Apples2Apples.playedApple.add(new PlayedApple(playerID, hand.get(choice)));
			hand.remove(choice);
			System.out.println("Waiting for other players\n");	
		}
	}

	public PlayedApple judge() {
		if(isBot){
			return Apples2Apples.playedApple.get(0);
		} else if(online){
			int playedAppleIndex = 0;
			try {
				playedAppleIndex = Integer.parseInt(inFromClient.readLine());	
			} catch(Exception e) {}
			return Apples2Apples.playedApple.get(playedAppleIndex);
		}  else {
			System.out.println("Choose which red apple wins\n");
			int choice = 0;
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String input=br.readLine();
				choice = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("That is not a valid option");
				judge();
			} catch (Exception e) {}
			return Apples2Apples.playedApple.get(choice);			
		}
	}

	public void addCard(String redApple) {
		if(isBot || !online) {
			hand.add(redApple);
		} else {
			try {
				outToClient.writeBytes(redApple + "\n");
			} catch (Exception e){}
		}
	}
}
