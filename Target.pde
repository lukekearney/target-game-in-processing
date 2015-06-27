class Target{
  float xPos,yPos,xSpeed,radius;
  PShape display;
  
  Target(float x,float y, float s){
    this.xPos=x;
    this.yPos=y/480*height;
    this.xSpeed=(s/320)*width;
    this.radius=0.05*height;
    this.display=loadShape("ufo.svg");
  }
  
  void create(){ //draws on screen
    this.move(); //moves the position of the target
    stroke(0);
    ellipse(this.xPos,this.yPos,this.radius*2,this.radius*2);
    shape(this.display,xPos-this.radius*1.05,yPos-this.radius*1.05,this.radius*2+0.01*height,this.radius*2+0.01*height);
  }
  
  void move(){ //moves 
    if(game.score<15){
      this.xPos=this.xPos+(this.xSpeed*(width/320)*(30/frameRate));
    }else{
      this.xPos=this.xPos+(this.xSpeed*(width/320)*(30/frameRate))*(game.score/15);
    }
    if(this.xPos>width||this.xPos<0){
      if(this.xSpeed<0&&this.xPos<0){
        this.xSpeed=this.xSpeed*-1;
      }
      if(this.xSpeed>0&&this.xPos>width){
        this.xSpeed=this.xSpeed*-1;
      }
      
    }
  }
  
  void checkHit(){//checks if hit
    if(game.shooterlist[0]!=null){//checks to make sure ball is moving
      if(this.yPos<=game.shooterlist[0].yPos-game.shooterlist[0].yspeed && this.yPos>=game.shooterlist[0].yPos+game.shooterlist[0].yspeed){//checks if overlapping on y-axis
        if(dist(this.xPos, this.yPos, game.shooterlist[0].xPos, game.shooterlist[0].yPos)<this.radius+(game.shooterlist[0].radius*0.5)){//checks if hit
          hit();//runs hit function(varies slightly for extended classes)
        }
      }
    }
  }
  
  void hit(){ 
    game.shooterlist[0]=null;//sets active ball to null
    game.score++;
    canfire=true;//allows firing
    this.xPos=random(0,width);//sets to random position
    this.xSpeed=xSpeed*-1;
    if(game.score%5==1&&game.score>10&&game.life==null){//decides whether to spawn new life target(based on score)
      game.life=new Life();
    }
  }
}
