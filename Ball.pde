class Ball{
  float xspeed,yspeed,xPos,yPos,radius;
  int type; //0 for normal, 1 for fast, 2 for slow, 3 for invincible
  
  public Ball(int type){ //takes type of ball as numerical value(see above)
    this.xPos=width/2;
    this.type=type;
    this.radius=0.06*height;
    this.yPos=height-(0.05*height); //includes calculation for perentage to accomodate different screen sizes
  }
  
  public Ball(){
    this.xPos=width/2;
    this.yPos=height-(0.05*height);
    this.type=0;
  }
  
  void move(){
    this.xPos+=this.xspeed; //moves ball
    this.yPos+=this.yspeed;
    this.display(); //displays on screen
    if(this.xPos>=width||this.xPos<=0){ //checks if hit edge
      xspeed=xspeed*-1;
    }
  }
  
  void display(){
    if(this.type==0){ //checks type of ball
      ellipse(this.xPos, this.yPos, this.radius, this.radius); //draws ball
      shape(loadShape("regular.svg"),this.xPos-(this.radius/2),this.yPos-(this.radius/2),this.radius,this.radius);
    }
    if(this.type==1){
      shape(loadShape("fast.svg"),this.xPos-(this.radius/2),this.yPos-(this.radius/2),this.radius,this.radius);
    }
    if(this.type==2){
      shape(loadShape("slow.svg"),this.xPos-(this.radius/2),this.yPos-(this.radius/2),this.radius,this.radius);
    }
    if(this.type==3){
      shape(loadShape("invinciball.svg"),this.xPos-(this.radius/2),this.yPos-(this.radius/2),this.radius,this.radius);
    }
  }
  
  void setVelocity(float x,float y){ //sets speed/velocity of ball
    this.xspeed=x*-0.025*width*(30/frameRate); //sets speed as percentage. Calculates multiplier based on current framerate
    this.yspeed=y*-0.025*width*(30/frameRate);
    if(this.type==1){ //adjusts speed for fast and slow ball
      this.xspeed=this.xspeed*2;
      this.yspeed=this.yspeed*2;
    }else if(this.type==2){
      this.xspeed=this.xspeed*0.5;
      this.yspeed=this.yspeed*0.5;
    }
  }
}
