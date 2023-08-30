package snake;

import java.awt.*;				//java gui pacakage
import java.awt.event.*;		//java listener i.e mouse keyboard pacakage
import javax.swing.*;			//java gui pacakage
import java.util.Random;	

public class GamePanel extends JPanel implements ActionListener{	//inheritance // interface

    static final int SCREEN_WIDTH  = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE); //576
    static final int DELAY = 65;				//speed of snake	//to control the rate of screen refresh rate //in millisec
    final int x[] = new int[GAME_UNITS];		//Array of the X axis unit Body of the Snake 
    final int y[] = new int[GAME_UNITS];		//Array of the Y axis unit Body of the Snake
    int intial_body_parts = 6 ;					//Inital Body Parts
	int bodyParts ;								//Bodysize at runtime
    int applesEaten ;							//score
	int p ;
    int appleX;									
    int appleY;
    char direction ;							//Runtime direction of snake
	char tempdirection ;

	char mode = '0' ;							// 0 for none // 1 for classic // 2 for blocks // 3 for AI

	//Variables For blocks mode
	int level = 20 ;							// how many maximum blocks can appear
	int currentlevel = 0 ;
					
    int[] blockX  =  new int[level];			
    int[] blockY  =  new int[level];			// For creating and remembring blocks location more than one at a time

    boolean running = false ;					//To show status of game
	boolean pause   = false ;

    Timer timer;										
    Random random;

	ImageIcon classic_image = new ImageIcon("src/snake/Images/Classic.png");
	JButton classic = new JButton(classic_image);

	ImageIcon blocks_image = new ImageIcon("src/snake/Images/Blocks.png");
	JButton blocks = new JButton(blocks_image);

	ImageIcon ai_image = new ImageIcon("src/snake/Images/AI.png");
	JButton ai = new JButton(ai_image);

	ImageIcon home_image = new ImageIcon("src/snake/Images/Home.png");
	JButton home = new JButton(home_image);

	ImageIcon quit_image = new ImageIcon("src/snake/Images/Quit.png");
	JButton quit = new JButton(quit_image);

	
	ImageIcon tryagain_image = new ImageIcon("src/snake/Images/Tryagain.png");
	JButton tryagain = new JButton(tryagain_image);

	ImageIcon background_image = new ImageIcon("src/snake/Images/b2.jpg");		
	JLabel background = new JLabel(background_image);							//Used for background image of panel

	Image snakehead_image = Toolkit.getDefaultToolkit().getImage("src/snake/Images/snakehead2.png");  

	Image apple_image = Toolkit.getDefaultToolkit().getImage("src/snake/Images/apple.png"); 
	//Image set with Image class refreshes with refreshrate


	GamePanel(){

	random = new Random();
	this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));		//size of JPanel
	this.setBackground(Color.black);
	this.setFocusable(true);
	this.addKeyListener(new MyKeyAdapter());								//keyboardlistener
	
	background.setBounds(0, 0, SCREEN_WIDTH , SCREEN_HEIGHT);
	this.add(background);
	
	startmenu();															//calling startmenu function

	this.setVisible(true);
	
	}

	public void startmenu(){

		setLayout(null);

		classic.setBounds(225, 120, 150 , 60);
		classic.setContentAreaFilled(false); classic.setBorderPainted(false);
		
		blocks.setBounds(225, 230, 150 , 60);
		blocks.setContentAreaFilled(false); blocks.setBorderPainted(false);

		ai.setBounds(225, 340, 150 , 60);
		ai.setContentAreaFilled(false); ai.setBorderPainted(false);

		background.add(classic);									//Adding buttons on jlabel - Background image
		background.add(blocks);
		background.add(ai);

		classic.addActionListener(new ActionListener(){  			//Action listener Interface implementation
			public void actionPerformed(ActionEvent e){   mode = '1' ;  
			classic.setVisible(false);	blocks.setVisible(false) ; ai.setVisible(false); background.setVisible(false);
			startGame(); }});  
		
	
		blocks.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){   mode = '2' ;  
				classic.setVisible(false);	blocks.setVisible(false) ; ai.setVisible(false); background.setVisible(false);
				startGame(); }});  
		

		ai.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){   mode = '3' ;  
				classic.setVisible(false);	blocks.setVisible(false) ; ai.setVisible(false); background.setVisible(false);
				startGame(); }});  

	}

	public void startGame() {

		bodyParts = intial_body_parts ;

		for(int i=0 ; i<bodyParts ; i++){					//To intial position of snake
			x[i] = 200 - UNIT_SIZE*i ;						//x[0] y[0]	
			y[i] = 50  ;									
		}

		direction = 'R' ;
		applesEaten = 0 ;									//Inital Score
		currentlevel = 0 ;
		p=0;

	    newApple();

		if(mode=='2'){
			newblock();
		}

	    running = true;

	    timer = new Timer(DELAY,this);						//used to set refresh rate - delay 
	    timer.start();	
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
	
		//create a new apple if existing apple is under Block body
		if(mode=='2'){
			for(int i=0 ; i<level ; i++)
			{
			if (blockX[i] == appleX && blockY[i] == appleY){
				newApple();
				}
			}
		}
		
	}

	
	public void newblock(){

		for(int i=0 ; i<level ; i++){
		
		blockX[i] = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE  ;
		blockY[i] = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE ;
		
		}
	}	

	public void paintComponent(Graphics g) {			// A method define in jpanel - gamepanel
	    super.paintComponent(g);
	    draw(g);
	}

	public void draw(Graphics g) {
		
		
	    if(running) {
		
			if(p==0){
				consoleoutput();
				p++;
			}
			
		for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {					//drawing Grid Lines
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
		    g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
		
		g.drawImage(apple_image, appleX, appleY, UNIT_SIZE , UNIT_SIZE , Color.BLACK, this) ; // Drawing Apple
		
		if(mode=='2'){ 													//drawing blocks if mode 2 
			for(int i=0 ; i<currentlevel ; i++){
				 g.setColor(Color.yellow);
				 g.fillRect(blockX[i], blockY[i] , UNIT_SIZE , UNIT_SIZE ); 

			 }
		}
		
	 
	 
		
		for(int i = 0; i< bodyParts;i++) {		//drawing snake
		    
			if(i == 0) {
			g.drawImage(snakehead_image, x[i], y[i], UNIT_SIZE, UNIT_SIZE , Color.BLACK, this) ;

			}
			else {
				
			g.setColor(new Color(45,180,0));
			g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}

		}

			g.setColor(Color.red);				//Drawing Score
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			g.drawString("Score: "+applesEaten, 240 , 40);

		
		}
		
		else if( (mode!='0') && (running == false) ){
			
			gameovermenu();
			p=0 ;
		
		}
		
	}

	public void consoleoutput(){
		System.out.print("\033[H\033[2J") ;  
        System.out.flush();   
		System.out.println("Running.....");
	}

	public void gameovermenu(){

		setLayout(null);
		
		background.setVisible(true);

		tryagain.setBounds(225, 120, 150 , 60);
		tryagain.setContentAreaFilled(false); tryagain.setBorderPainted(false);
		tryagain.setVisible(true);
		background.add(tryagain);

		home.setBounds(225, 230, 150 , 60);
		home.setContentAreaFilled(false); home.setBorderPainted(false);
		home.setVisible(true);
		background.add(home);

		quit.setBounds(225, 340, 150 , 60);
		quit.setContentAreaFilled(false); quit.setBorderPainted(false);
		quit.setVisible(true);
		background.add(quit);



		tryagain.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){     
				tryagain.setVisible(false);	home.setVisible(false) ; quit.setVisible(false); 
				background.setVisible(false);
				startGame(); paintComponent(null); 
	}});  

		
		home.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){  
				tryagain.setVisible(false);	home.setVisible(false) ; quit.setVisible(false); 
				classic.setVisible(true);	blocks.setVisible(true) ; ai.setVisible(true); 
				}});  
		
	
		quit.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e){
				System.exit(0);  }});  
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			if(mode=='3'){ AIsnake(); }
			checkApple();
			checkCollisions();
		}
		if(!pause){
			repaint();
		}
														//repaints the screen
	}

	
	public void move(){

		if(!pause){
			for(int i = bodyParts;i>0;i--) {
				x[i] = x[i-1] ;
				y[i] = y[i-1] ;
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
				case 'P':
					x[0] = x[0] + 0 ;
					y[0] = y[0] + 0 ;
				}
		}	
	}

	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++    ;
			applesEaten++  ;
			if(currentlevel < level)
				currentlevel++ ;
			newApple()     ;
		}
	}
	
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}

		//checks if head collides with block
		if(mode=='2'){
			for(int i=0 ; i<currentlevel ; i++){
				if( (x[0] == blockX[i]) && (y[0] == blockY[i]) ){
					running = false;
				}
			}
		}
		
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}

		//check if head touches right border
		if(x[0] >= SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] >= SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}

	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
				
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			
			case KeyEvent.VK_SPACE:
			
				if(pause == true){
				pause = false ;
				direction = tempdirection ;
				}
					
				else if(pause == false){
					pause = true ;
					tempdirection = direction ; 
					direction = 'P';
				}
				break;

			}
		}
	}






	//snake AI moments
	//when axis of snake head and apple are align
	public boolean xalign () {if(appleX-x[0] == 0) {return true ; } else { return false ;} }
	public boolean yalign () {if(appleY-y[0] == 0) {return true ; } else { return false ;} }
	
	//where apple is as compare to the snake
	public boolean apple_up    ()  {if(appleY-y[0] < 0) {return true ; } else { return false ;} }
	public boolean apple_down  ()  {if(appleY-y[0] > 0) {return true ; } else { return false ;} }
	public boolean apple_left  ()  {if(appleX-x[0] < 0) {return true ; } else { return false ;} }
	public boolean apple_right ()  {if(appleX-x[0] > 0) {return true ; } else { return false ;} }

	//where the head might touch the edges of the screen
	public boolean upper_edge ()  {if( y[0]-UNIT_SIZE < 0 )             {return true ; } else { return false ;} }
	public boolean lower_edge ()  {if( y[0]+UNIT_SIZE >= SCREEN_HEIGHT) {return true ; } else { return false ;} }
	public boolean right_edge ()  {if( x[0]+UNIT_SIZE >= SCREEN_WIDTH ) {return true ; } else { return false ;} }
	public boolean left_edge  ()  {if( x[0]-UNIT_SIZE < 0)              {return true ; } else { return false ;} }

	//From where the head is touching its own body 
	public boolean top_smash() 	  {	for(int i = bodyParts ; i>0 ; i--) {
					            		if( (y[0]-UNIT_SIZE == y[i]) && (x[0] == x[i])) { return true;} } return false; }
	public boolean bottom_smash() {	for(int i = bodyParts ; i>0 ; i--) {
					            		if( (y[0]+UNIT_SIZE == y[i]) && (x[0] == x[i])) { return true;} } return false; }
	public boolean left_smash()   {	for(int i = bodyParts ; i>0 ; i--) {
					            		if( (x[0]-UNIT_SIZE == x[i]) && (y[0] == y[i])) { return true;} } return false; }
	public boolean right_smash()  {	for(int i = bodyParts ; i>0 ; i--) {
					            		if( (x[0]+UNIT_SIZE == x[i]) && (y[0] == y[i])) { return true;} } return false; }

	//to cheak in which direction there is more 
	
		
				 


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
			
		////
		} else if ((xalign ()) && (x[0]-UNIT_SIZE<=0) && (apple_up()) && (direction == 'D') && !(right_smash()) ){
			direction = 'R';	 
		} else if ((xalign ()) && (x[0]-UNIT_SIZE>=SCREEN_WIDTH) && (apple_up()) && (direction == 'D') && !(left_smash()) ){
			direction = 'L';
		} else if ((xalign ()) && (apple_up()) && (direction == 'D') && !(left_smash()) ){
			direction = 'L';	 

		}
		
		//when y axies of snake head and appleare aligned
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

		}
}