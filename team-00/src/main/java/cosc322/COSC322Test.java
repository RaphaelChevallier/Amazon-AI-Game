
package cosc322;

import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;

import java.util.Map;

/**
 * An example showing how to implement a GamePlayer 
 * @author yong.gao@ubc.ca
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient;
	
    private String userName = "Raphael Chevallier";
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {
        //this is our connect server
        GameClient gameClient;
        GamePlayer player1 = new GamePlayer() {
            @Override
            public void onLogin() {
                System.out.println("Logged in");
            }
            @Override
            public String userName() {
                return "Raphael Chevallier";
            }
            @Override
            public boolean handleGameMessage(String s, Map<String, Object> map) {
                return true;
            }
        };
        if (args.length >= 2)//if input, use input
            gameClient = new GameClient(args[0], args[1], player1);
        else//default to best user
            gameClient = new GameClient("Yong", "Gao", player1);
        System.out.println(player1.userName());

    }
	
    /**
     * Any name and passwd 
     * @param userName
     * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
        this.userName = userName;
        gameClient = new GameClient(userName, passwd);
        }

        @Override
        public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
        //This method will be called by the GameClient when it receives a game-related message
        //from the server.

        //For a detailed description of the message types and format,
        //see the method GamePlayer.handleGameMessage() in the game-client-api document.
        return true;
        }

        @Override
        public void onLogin() {
        System.out.println("Logged in");
        System.out.println(this.gameClient.getRoomList());
        //this.gameClient.logout();
        }


        @Override
        public String userName() {
        return userName;
        }
}//end of class