package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

	static final int lives_allowed = 20;
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
	char directionapple = 'P';
    boolean running = false;
	boolean gameWin = false;
    Timer timer;
    Random random;

	int xd ;
	int yd ;
	
    GamePanel(){
	random = new Random();
	this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
	this.setBackground(Color.black);
	this.setFocusable(true);
	this.addKeyListener(new MyKeyAdapter());
	startGame();
	}
	public void startGame() {
	    newApple();
	    running = true;
	    timer = new Timer(DELAY,this);
	    timer.start();
	}
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    draw(g);
	}
	public void draw(Graphics g) {
		
	    if(running) {
		for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
            	    g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
		    g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
                    }
	    g.setColor(Color.red);
	    g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
		for(int i = 0; i< bodyParts;i++) {
		    if(i == 0) {
			g.setColor(Color.green);
			g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		    else {
			g.setColor(new Color(45,180,0));
			//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
			g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Lives: "+(lives_allowed - applesEaten), (SCREEN_WIDTH - metrics.stringWidth("Lives: "+(lives_allowed - applesEaten)))/2, g.getFont().getSize());
		}
		else {
			if(gameWin){
				gameWin(g);
			}else{
				gameOver(g);
			}
		}
		
	}
	public void newApple(){
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

		//create a new apple if existing apple is under snake's body
                for (int i=bodyParts; i>0; i--){
                    if (x[i] == appleX && y[i] == appleY){
                        newApple();
                    }
                    }
	}
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}

	public void moveapple(){
		
		switch(directionapple) {
		case 'U':
			if(appleY - UNIT_SIZE <= 0) break ; 
			appleY = appleY - 2*UNIT_SIZE;
			break;
		case 'D':
			if(appleY + 2*UNIT_SIZE >= SCREEN_WIDTH) break ; 
			appleY= appleY + 2*UNIT_SIZE;
			break;
		case 'L':
			if(appleX - UNIT_SIZE <= 0) break ; 
			appleX = appleX - 2*UNIT_SIZE;
			break;
		case 'R':
			if(appleX + 2*UNIT_SIZE >= SCREEN_WIDTH) break ;
			appleX = appleX + 2*UNIT_SIZE;
			break;
		case 'P':
			appleX = appleX + 0 ;
			appleY = appleY + 0 ;
		}
		
		directionapple = 'P' ;
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			if((lives_allowed - applesEaten) <= 0 ) {
				gameWin = false; 
				running = false;	//game over
			}
			newApple();
		}
	}
	public void checkCollisions() {
		//checks if head collides with body
		
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
				gameWin = true;
			}
		}
		
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
			gameWin = true;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
			gameWin = true;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
			gameWin = true;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
			gameWin = true;
		}
		
		if(!running) {
			timer.stop();
		}
	}

	//snake AI moments
	//when axis of snake head and apple are align
	public boolean xalign () {if(appleX-x[0] == 0) {return true ; } else { return false ;} }
	public boolean yalign () {if(appleY-y[0] == 0) {return true ; } else { return false ;} }
	
	//where apple is as compare to the snake
	public boolean apple_up ()    {if(appleY-y[0] < 0) {return true ; } else { return false ;} }
	public boolean apple_down ()  {if(appleY-y[0] > 0) {return true ; } else { return false ;} }
	public boolean apple_left ()  {if(appleX-x[0] < 0) {return true ; } else { return false ;} }
	public boolean apple_right () {if(appleX-x[0] > 0) {return true ; } else { return false ;} }

	//where the head might touch the edges of the screen
	public boolean upper_edge() {if( y[0]-UNIT_SIZE < 0 ) {return true ; } else { return false ;} }
	public boolean lower_edge() {if( y[0]+UNIT_SIZE >= SCREEN_HEIGHT) {return true ; } else { return false ;} }
	public boolean right_edge() {if( x[0]+UNIT_SIZE >= SCREEN_WIDTH ) {return true ; } else { return false ;} }
	public boolean left_edge()  {if(  x[0]-UNIT_SIZE < 0) {return true ; } else { return false ;} }

	//From where the head is touching its own body 
	public boolean top_smash()	 {	for(int i = bodyParts ; i>0 ; i--) {
					            if( (y[0]-UNIT_SIZE == y[i]) && (x[0] == x[i]) ) {  return true; } } return false ;	}
	public boolean bottom_smash(){	for(int i = bodyParts ; i>0 ; i--) {
					            if( (y[0]+UNIT_SIZE == y[i]) && (x[0] == x[i]) ) { return  true; } } return false ;	}
	public boolean left_smash()  {	for(int i = bodyParts ; i>0 ; i--) {
					            if( (x[0]-UNIT_SIZE == x[i]) && (y[0] == y[i]) ) { return  true; } } return false ;	}
	public boolean right_smash(){	for(int i = bodyParts ; i>0 ; i--) {
					            if( (x[0]+UNIT_SIZE == x[i]) && (y[0] == y[i]) ) { return  true; } } return false ;	}

	public void AIsnake(){
		

		//doging its own body
		if( (direction == 'U') && (top_smash() ) && !(right_smash()) && !(x[0]+UNIT_SIZE >= SCREEN_WIDTH) )  {
			direction = 'R' ;
		}else if( (direction == 'U') && (top_smash() ) && !(left_smash() ) ) {
			direction = 'L' ;
		}

		if( (direction == 'D') && (bottom_smash() ) && !(right_smash()) && !(x[0]+UNIT_SIZE >= SCREEN_WIDTH) )  {
			direction = 'R' ;
		}else if( (direction == 'D') && (bottom_smash() ) && !(left_smash() ) ) {
			direction = 'L' ;
		}

		
		if( (direction == 'R') && (right_smash() ) && !(bottom_smash()) && !(x[0]+UNIT_SIZE>= SCREEN_HEIGHT) )  {
			direction = 'D' ;
		}else if( (direction == 'R') && (right_smash() ) && !(top_smash() ) ) {
			direction = 'U' ;
		}

		if( (direction == 'L') && (left_smash() ) && !(bottom_smash()) && !(x[0]+UNIT_SIZE>= SCREEN_HEIGHT) )  {
			direction = 'D' ;
		}else if( (direction == 'L') && (left_smash() ) && !(top_smash() ) ) {
			direction = 'U' ;
		}

		

		//chasing the Apple
		//when x axies of snake head and appleare aligned

		if( (xalign ()) && (apple_down()) &&  ( (direction == 'R') || (direction == 'L') ) && !(bottom_smash()) ){
			direction = 'D';
		} else if ((xalign ()) && (apple_up()) && ((direction == 'R') || (direction == 'L')) && !(top_smash())  ){
			direction = 'U';

		} else if ((xalign ()) && (x[0]-UNIT_SIZE<=0) && (apple_down()) && (direction == 'U') && !(right_smash()) ){
			direction = 'R';
		} else if ((xalign ()) && (x[0]-UNIT_SIZE>=SCREEN_WIDTH) && (apple_down()) &&(direction == 'U') && !(left_smash())){
				direction = 'L';
		} else if ((xalign ()) && (apple_down()) && (direction == 'U') && !(left_smash()) ){
				direction = 'L';
			
		
		} else if ((xalign ()) && (x[0]-UNIT_SIZE<=0) && (apple_up()) && (direction == 'D') && !(right_smash()) ){
			direction = 'R';	 
		} else if ((xalign ()) && (x[0]-UNIT_SIZE>=SCREEN_WIDTH) && (apple_up()) && (direction == 'D') && !(left_smash()) ){
			direction = 'L';
		} else if ((xalign ()) && (apple_up()) && (direction == 'D') && !(left_smash()) ){
			direction = 'L';	 

		}
		
		//when x axies of snake head and appleare aligned
		if( (yalign()) && (apple_left()) && ( (direction =='U') || (direction =='D')) && (!left_smash()) ){
			direction = 'L';
		} else if ((yalign()) && (apple_right()) && ( (direction =='U') || (direction =='D')) && (!right_smash())){
			direction = 'R';
		}

		if( (yalign()) && (apple_left()) && (x[0]-UNIT_SIZE<=0) && (direction =='R') && (!bottom_smash()) ){
			direction = 'D' ;
		}else if( (yalign()) && (apple_left()) && (x[0]+UNIT_SIZE>=SCREEN_HEIGHT) && (direction =='R') && (!top_smash()) ){
			direction = 'U' ;
		}else if( (yalign()) && (apple_left())  && (direction =='R') && (!bottom_smash()) ){
			direction = 'D' ;
		}

		if( (yalign()) && (apple_right()) && (x[0]-UNIT_SIZE<=0) && (direction =='L') && (!bottom_smash()) ){
			direction = 'D' ;
		}else if( (yalign()) && (apple_right()) && (x[0]+UNIT_SIZE>=SCREEN_HEIGHT) && (direction =='L') && (!top_smash()) ){
			direction = 'U' ;
		}else if( (yalign()) && (apple_right())  && (direction =='L') && (!bottom_smash()) ){
			direction = 'D' ;
		}

		//doging the right edge
		if( (right_edge()) && (y[0]-UNIT_SIZE <= 0) && (direction == 'R') ) {
			 direction = 'D'; }
		if( (right_edge()) && (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (direction == 'R') ) {
				direction = 'U'; }
		if( (right_edge()) && (direction == 'R')){
			direction = 'U'; }
		
		//doging the left edge
		if( (left_edge()) && (y[0]-UNIT_SIZE <= 0) && (direction == 'L') ) {
			direction = 'D'; }
		if( (left_edge()) && (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (direction == 'L') ) {
			   direction = 'U'; }
		if( (left_edge()) && (direction == 'L')){
		   direction = 'U'; }
		
		//doging the Down edge
		if( (lower_edge()) && (x[0]-UNIT_SIZE <= 0) && (direction == 'D') ) {
			direction = 'R'; }
		if( (lower_edge()) && (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (direction == 'D') ) {
			direction = 'L'; }
		if( (lower_edge()) && (direction == 'D') ) {
			direction = 'L'; }
		
		
		//doging the upper edge
		if( (upper_edge()) && (x[0]-UNIT_SIZE <= 0) && (direction == 'U') ) {
			direction = 'R'; }
		if( (upper_edge()) && (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (direction == 'U') ) {
			direction = 'L'; }
		if( (upper_edge()) && (direction == 'U') ) {
			direction = 'L'; }

	
		//doging the right edge
		if( (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (y[0]-UNIT_SIZE <= 0) && (direction == 'R') ) {
			 direction = 'D'; }
		if( (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (direction == 'R') ) {
				direction = 'U'; }
		if( (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (direction == 'R')){
			direction = 'U'; }
		
		//doging the left edge
		if( (x[0]-UNIT_SIZE < 0) && (y[0]-UNIT_SIZE <= 0) && (direction == 'L') ) {
			direction = 'D'; }
		if( (x[0]-UNIT_SIZE < 0) && (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (direction == 'L') ) {
			   direction = 'U'; }
		if( (x[0]-UNIT_SIZE < 0) && (direction == 'L')){
		   direction = 'U'; }
		
		//doging the Down edge
		if( (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (x[0]-UNIT_SIZE <= 0) && (direction == 'D') ) {
			direction = 'R'; }
		if( (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (direction == 'D') ) {
			direction = 'L'; }
		if( (y[0]+UNIT_SIZE >= SCREEN_HEIGHT) && (direction == 'D') ) {
			direction = 'L'; }
		
		
		//doging the upper edge
		if( (y[0]-UNIT_SIZE < 0) && (x[0]-UNIT_SIZE <= 0) && (direction == 'U') ) {
			direction = 'R'; }
		if( (y[0]-UNIT_SIZE < 0) && (x[0]+UNIT_SIZE >= SCREEN_WIDTH) && (direction == 'U') ) {
			direction = 'L'; }
		if( (y[0]-UNIT_SIZE < 0) && (direction == 'U') ) {
			direction = 'L'; }

	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Lives: "+(lives_allowed - applesEaten), (SCREEN_WIDTH - metrics1.stringWidth("Lives: "+(lives_allowed - applesEaten)))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}

	public void gameWin(Graphics g){
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+(lives_allowed - applesEaten), (SCREEN_WIDTH - metrics1.stringWidth("Score: "+(lives_allowed - applesEaten)))/2, g.getFont().getSize());
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("You Win", (SCREEN_WIDTH - metrics2.stringWidth("You Win"))/2, SCREEN_HEIGHT/2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			AIsnake();
			moveapple();
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(directionapple != 'R') {
					directionapple = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(directionapple != 'L') {
					directionapple = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(directionapple != 'D') {
					directionapple = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(directionapple != 'U') {
					directionapple = 'D';
				}
				break;
			}
		}
	}
}
