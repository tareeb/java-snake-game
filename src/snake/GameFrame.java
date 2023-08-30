package snake;

import javax.swing.JFrame;

public class GameFrame extends JFrame{      //inheritance

    GameFrame(){
        GamePanel one = new GamePanel();        //Creating Object of class gamepanel
        this.add(one);                          //Adding gamepanel object to Jframe
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
        this.setUndecorated(true);
        this.setResizable(false);               // size of frame cant be resize
        this.pack();                            // will pack Jpanel into Jframe
        this.setVisible(true);                  //This will make jframe visible
        this.setLocationRelativeTo(null);       //Jframe will appear center of the screen otherwise topleft corner
    }
    
}
