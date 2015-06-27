package processing.test.mathsystuff;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class mathsystuff extends PApplet {

double angledegrees=90;
int increase=1; //amount to increase angle per tick
PVector velocity;
boolean canfire=true;
Button button;
Game game;

public void setup(){
 
  game=new Game(); //sets new game
  fill(0,0,0); //sets background
  game.screen=0; //sets game to starting screen
}

public void draw(){
  if(game.screen==0){ //checks if on starting screen
    game.startScreen(); //generates starting screen
  }else if(game.screen==1){ //checks if on game screen
    game.gameScreen(); //generates game screen
  }
}

public void mousePressed(){
  if(game.screen==1){//see above
    velocity=PVector.fromAngle(radians((float)angledegrees));//uses PVector to make a vector from the angle of the shooter
    if(canfire==true){//checks if user is able to fire (if ball is active)
      game.shooterlist[0]=game.shooterlist[1]; //sets active ball to the next ball
      game.shooterlist[0].setVelocity(velocity.x,velocity.y);//adjusts velocity for ball depending on type
      game.shooterlist[1]=new Ball(game.createNewBall()); //creates new ball with random type as generated by the createNewBall function
      canfire=false;//disables firing while ball is active
    }
  }
  
  if(game.screen==0){ //see above
    if(button.pressed()){//checks if button is pressed
      game.gotoGame(); //loads game
    }
  }
  
  if(game.screen==2){
    if(button.pressed()){
      game.reset();//resets game
    }
  }
}
class Ball{
  float xspeed,yspeed,xPos,yPos;
  int type; //0 for normal, 1 for fast, 2 for slow, 3 for invincible
  public Ball(int type){ //takes type of ball as numerical value(see above)
    this.xPos=width/2;
    this.type=type;
    this.yPos=height-(15*480/height); //includes calculation for perentage to accomodate different screen sizes
  }
  
  public Ball(){
    this.xPos=width/2;
    this.yPos=height-(15*480/height);
    this.type=0;
  }
  
  public void move(){
    this.xPos+=this.xspeed; //moves ball
    this.yPos+=this.yspeed;
    this.display(); //displays on screen
    if(this.xPos>=width||this.xPos<=0){ //checks if hit edge
      xspeed=xspeed*-1;
    }
  }
  
  public void display(){
    if(this.type==0){ //checks type of ball
      ellipse(this.xPos, this.yPos, 30*width/320, 30*height/480); //draws ball
      shape(loadShape("regular.svg"),this.xPos-15*width/320,this.yPos-15*height/480,30*width/320,30*height/480);
    }
    if(this.type==1){
      shape(loadShape("fast.svg"),this.xPos-15*width/320,this.yPos-15*height/480,30*width/320,30*height/480);
    }
    if(this.type==2){
      shape(loadShape("slow.svg"),this.xPos-15*width/320,this.yPos-15*height/480,30*width/320,30*height/480);
    }
    if(this.type==3){
      shape(loadShape("invinciball.svg"),this.xPos-15*width/320,this.yPos-15*height/480,30*width/320,30*height/480);
    }
  }
  
  public void setVelocity(float x,float y){ //sets speed/velocity of ball
    this.xspeed=x*-5*(width/320); //sets speed as percentage
    this.yspeed=y*-5*(height/480);
    if(this.type==1){ //adjusts speed for fast and slow ball
      this.xspeed=this.xspeed*2;
      this.yspeed=this.yspeed*2;
    }else if(this.type==2){
      this.xspeed=this.xspeed*0.5f;
      this.yspeed=this.yspeed*0.5f;
    }
  }
}
class Button{
  float Width,Height,posX,posY,textsize;
  String text;
  
  Button(float W,float H,float X,float Y,String text){
    this.Width=W;
    this.Height=H;
    this.posX=X;
    this.posY=Y;
    this.text=text;
    this.textsize=this.Height/2;
  }
  
  Button(int W,int H,int X,int Y,String text){
    this.Width=W*1.0f;
    this.Height=H*1.0f;
    this.posX=X*1.0f;
    this.posY=Y*1.0f;
    this.text=text;
    this.textsize=this.Height/2;
  }
  
  Button(float W,float H,float X,float Y,String text,int size){
    this.Width=W;
    this.Height=H;
    this.posX=X;
    this.posY=Y;
    this.text=text;
    this.textsize=size;
  }
  
  public void display(){
    rect(this.posX,this.posY,this.Width,this.Height);
    rectMode(CORNER);
    fill(255,255,255);
    textAlign(CENTER,CENTER);
    textSize(this.textsize);
    text(this.text,this.posX,this.posY,this.Width,this.Height);
  }
  
  public boolean pressed(){
    if(mouseX>this.posX && mouseY>this.posY && mouseX<this.posX+this.Width && mouseY<this.posY+this.Height){ //checks if clicked
        return true;
    }
    return false;
  }
}
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
    this.target=new Target(random(0,width),140*(height/480),1);
  }
  
  public void gameOver(){ //trigger game over
    this.screen=2; //sets screen to game over
    noStroke();
    noLoop();
    fill(255,0,0);
    rect(0,height/8,width,height/8*6);
    stroke(255);
    this.shooterlist[0]=null; //removes active ball
    String[] scores=loadStrings("scores.txt"); //loads high score from text file
    if(PApplet.parseInt(scores[0])<this.score){ //checks if current score is greater than high score
      scores[0]=""+this.score+""; //makes string
      saveStrings("data/scores.txt",scores); //saves to the scores.txt in data folder
    }
    fill(255);
    textSize(30);
    textAlign(CENTER,TOP);
    text("Game Over",width/2,height/8);
    textSize(20);
    text("Your Score\n"+this.score,60,height/8*2);
    text("High Score\n"+scores[0],width-(60*width/320),height/8*2);
    fill(255,0,0);
    button=new Button(width/2,50,width/4,height/8*6,"Play Again",18);
    button.display();
  }
  
  public void startScreen(){ //starting screen
    background(0,0,0);
    shape(loadShape("Instructions.svg"),0,0,width,height);
    fill(0);
    stroke(255);
    button=new Button(width-(30*width/320),50,15,height-(100*height/480),"Start Game!",18);
    button.display();
  }
  
  public void gotoGame(){ //resets game screen to screen 1
    this.screen=1;
  }
  
  public void gameScreen(){ //gameplay screen
    background(0,0,0);
    shape(loadShape("BG.svg"),0,0,width,height);
    shape(loadShape("shooter.svg"),width/2-(50*width/320),height-(100*height/480),100*(width/320),100*(height/480));
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
        if(random(0,1)>0.91f){ //decides whether to spawn a new obstacle
          this.obstacle=new Obstacle(width,210*height/480,1*width/320);
        }
      }
      if(this.life==null){ //decides whether to spawn new life
        if(random(0,10)>9.99f){
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
  
  public void reset(){ //resets game
    loop(); //begins loop again
    this.score=0;
    this.lives=3;
    gotoGame();//starts game
  }
  
  public void loseLife(){ 
    if(this.lives==0){//checks if life count is already 0
      this.gameOver();
    }else{
      this.lives--;//reduces lives by 1
    }
  }
  
  private void drawShooter(){
    stroke(255);
    double x2=width/2-75*(Math.cos(radians((float)angledegrees)));//calculates point to draw line from
    double y2=75*(Math.sin(radians((float)angledegrees))); //calculates y co-ord to draw line from using trig fucntions
    line(width/2,height-(15*height/480),(float)x2,height-(15*height/480)-(float)y2);//draws line
    if(angledegrees==170||angledegrees==10){ //stop ball being shot horizontally. HAHA WE DIDN'T FORGET!!!
      increase=increase*-1;//sets shooter to rotate in opposite direction
    }
    if(canfire){ //checks if can fire
      angledegrees+=increase; //increases only when true. This stops the animation whil ball is moving
    }
    
  }
  
  public int createNewBall(){
    if(this.score>=5){
      float num=random(0,1);
      if(this.score>=10){
        if(num>0.55f){
          if(num>0.75f){
            if(num>0.95f){
              return 3; //returns invinciball 5% of time
            }
            return 2; //returns fast ball 20% of time
          }
          return 1; //returns slow ball 20% of time
        }
        return 0;
      }else{ //runs if score is not over 10;
        if(num>0.7f){
          if(num>0.85f){
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
class Life extends Target{
  Life(){
    super(0,70,1);
    this.display=loadShape("1up.svg");
    this.radius=20*(width/320);
  }
  
  public void create(){
    this.move();
    stroke(0);
    ellipse(xPos, yPos, 30*(width/320), 30*(width/320));
    shape(this.display,xPos-(29*width/320),yPos-(29*height/480),this.radius*2+10,this.radius*2+10);
  }
  
  public void move(){
    this.xPos=this.xPos+this.xSpeed;
    if(this.xPos>width||this.xPos<0){
      game.life=null;
    }
  }
  
  public void hit(){
    canfire=true;
    game.shooterlist[0]=null;
    game.lives++;
    game.life=null;
  }
}
class Obstacle extends Target{
  Obstacle(float x,float y, float s){
    super(x,y,s);
    this.display=loadShape("asteroid.svg");
  }
  
  public void create(){
    this.move();
    noStroke();
    fill(255,0,0);
    ellipse(xPos, yPos, 50*(width/320), 50*(width/320));
    shape(this.display,xPos-(34*width/320),yPos-(34*height/480),this.radius*2+(15*width/320),this.radius*2+(15*width/320));
  }
  
  public void hit(){
    if(game.shooterlist[0].type==3){
      game.obstacle=null;
      game.score++;
      game.shooterlist[0]=null;
    }else{
      game.shooterlist[0].yspeed=game.shooterlist[0].yspeed*-1;
    }
    canfire=true;
  }
}
class Target{
  float xPos,yPos,xSpeed,radius;
  PShape display;
  
  Target(float x,float y, float s){
    xPos=x;
    yPos=y;
    xSpeed=s;
    radius=25*(width/320);
    this.display=loadShape("ufo.svg");
  }
  
  public void create(){ //draws on screen
    this.move(); //moves the position of the target
    stroke(0);
    
    shape(this.display,xPos-(29*width/320),yPos-(29*height/320),this.radius*2+(10*width/320),this.radius*2+(10*height/320));
  }
  
  public void move(){ //moves target
    this.xPos=this.xPos+this.xSpeed;
    if(this.xPos>width||this.xPos<0){
      this.xSpeed=this.xSpeed*-1;
    }
  }
  
  public void checkHit(){//checks if hit
    if(game.shooterlist[0]!=null){//checks to make sure ball is moving
      if(this.yPos<=game.shooterlist[0].yPos-game.shooterlist[0].yspeed && this.yPos>=game.shooterlist[0].yPos+game.shooterlist[0].yspeed){//checks if overlapping on y-axis
        if(dist(this.xPos, this.yPos, game.shooterlist[0].xPos, game.shooterlist[0].yPos)<this.radius+(15*width/320)){//checks if hit
          hit();//runs hit function(varies slightly for extended classes)
        }
      }
    }
  }
  
  public void hit(){ 
    game.shooterlist[0]=null;//sets active ball to null
    game.score++;
    canfire=true;//allows firing
    this.xPos=random(0,width);//sets to random position
    if(game.score%5==1&&game.score>10&&game.life==null){//decides whether to spawn new life target(based on score)
      game.life=new Life();
    }
  }
}

  public int sketchWidth() { return 320; }
  public int sketchHeight() { return 480; }
}
