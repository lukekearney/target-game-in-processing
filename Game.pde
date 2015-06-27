class Game{
  int score,lives,screen; //screen variable uses 0 for start screen, 1 for game screen and 2 for game over screen
  PShape heart=loadShape("heart.svg");
  Ball[] shooterlist = new Ball[2];
  Target target;
  Obstacle obstacle;
  Life life;
  
  Game(){ //sets up game
    this.score=0;
    this.lives=3;
    this.screen=0;
    this.shooterlist[0]=null; //sets balls
    this.shooterlist[1]=new Ball(this.createNewBall());
    this.target=new Target(random(0,width),140,1);
  }
  
  void gameOver(){ //trigger game over
    this.screen=2; //sets screen to game over
    noStroke();
    fill(138,28,28);
    rect(0,height/8,width,height/8*6);
    stroke(255);
    this.shooterlist[0]=null; //removes active ball
    String[] scores=loadStrings("scores.txt"); //loads high score from text file
    if(int(scores[0])<this.score){ //checks if current score is greater than high score
      scores[0]=""+this.score+""; //makes string
      saveStrings("data/scores.txt",scores); //saves to the scores.txt in data folder
    }
    fill(255);
    int size=round((30.0/320)*width);
    println(size);
    textSize(size);
    textAlign(CENTER,TOP);
    text("Game Over",width/2,height/8);
    size=round((20.0/320)*width);
    textSize(size);
    text("Your Score\n"+this.score,size*2.7,height/8*2);
    text("High Score\n"+scores[0],width-(size*2.7),height/8*2);
    fill(189,34,34);
    button=new Button((width/4)*3,(height/8),width/8,height/8*5,"Play Again");
    button.display();
  }
  
  void startScreen(){ //starting screen
    background(0,0,0);
    shape(loadShape("Instructions.svg"),0,0,width,height);
    fill(0);
    stroke(255);
    button=new Button(width/2,height/8,width/4,height/8*6,"Start Game!");
    button.display();
  }
  
  void gotoGame(){ //resets game screen to screen 1
    this.screen=1;
  }
  
  void gameScreen(){ //gameplay screen
    background(0,0,0);
    shape(loadShape("BG.svg"),0,0,width,height);
    shape(loadShape("shooter.svg"),width/2-(0.1*height),height-(0.2*height),0.2*height,0.2*height);
    runGame(); //triggers game
    shape(this.heart,width-75,0,30,30);
    textSize(18);
    fill(255);
    text("x "+this.lives,width-30,20);
    textAlign(LEFT,CENTER);
    text("Score: "+this.score,10,20);
  }
  
  private void runGame(){ 
    if(this.score>=10){
      if(this.obstacle==null){
        if(random(0,1)>0.91){ //decides whether to spawn a new obstacle
          this.obstacle=new Obstacle(width,210,1);
        }
      }
      if(this.life==null){ //decides whether to spawn new life
        if(random(0,10)>9.99){
          this.life=new Life();
        }
        
      }
    }
    this.target.create(); //draws target
    if(this.obstacle!=null){
      this.obstacle.create(); //draws obstacle
    }
    if(this.life!=null){
      this.life.create();//draws life target
    }
    this.drawShooter(); //draws line for the shooter
    this.shooterlist[1].display();//draws the next ball
    if(this.shooterlist[0]!=null){
      this.shooterlist[0].move(); //moves active ball and draws
    }
    
    if(this.shooterlist[0]!=null){
      if(this.shooterlist[0].yPos<0||this.shooterlist[0].yPos>height){//checks if ball is off screen
        if(this.shooterlist[0].yPos<0){ //if goes off top of screen
          this.loseLife(); //triggers loss of life
        }
        this.shooterlist[0]=null; //removes from array to avoid constantly firing this if statement
        canfire=true; //allows user to fire the next ball
      }
      this.target.checkHit();//checks if active ball has hit a this.target
      if(this.obstacle!=null){
        this.obstacle.checkHit();
      }
      if(this.life!=null){
        this.life.checkHit();
      }
    }
  }
  
  void reset(){ //resets game
    this.score=0;
    this.lives=3;
    this.shooterlist[1]=new Ball(this.createNewBall());
    gotoGame();//starts game
  }
  
  void loseLife(){ 
    if(this.lives==0){//checks if life count is already 0
      this.gameOver();
    }else{
      this.lives--;//reduces lives by 1
    }
  }
  
  private void drawShooter(){
    stroke(255);
    double x2=width/2-(0.25*width)*(Math.cos(radians((float)angledegrees)));//calculates point to draw line from
    double y2=(0.25*width)*(Math.sin(radians((float)angledegrees))); //calculates y co-ord to draw line from using trig fucntions
    line(width/2,height-(0.05*height),(float)x2,height-(0.05*height)-(float)y2);//draws line
    if(angledegrees>=170||angledegrees<=10){ //stop ball being shot horizontally. HAHA WE DIDN'T FORGET!!!
      increase=increase*-1;//sets shooter to rotate in opposite direction
    }
    if(canfire){ //checks if can fire
      angledegrees+=(increase*(30/frameRate)); //increases only when true. This stops the animation whil ball is moving
    }
    
  }
  
  int createNewBall(){
    if(this.score>=5){
      float num=random(0,1);
      if(this.score>=10){
        if(num>0.55){
          if(num>0.75){
            if(num>0.95){
              return 3; //returns invinciball 5% of time
            }
            return 2; //returns fast ball 20% of time
          }
          return 1; //returns slow ball 20% of time
        }
        return 0;
      }else{ //runs if score is not over 10;
        if(num>0.7){
          if(num>0.85){
            return 2; //returns type of slow ball
          }
          return 1; //returns type of fast ball
        }
        return 0; //returns normal ball 70% of the time
      }
    }
    return 0;//returns normal ball 100% of time;
  }
}
