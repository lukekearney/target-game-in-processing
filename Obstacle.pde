class Obstacle extends Target{
  Obstacle(float x,float y, float s){
    super(x,y,s);
    this.display=loadShape("asteroid.svg");
  }
  
  void create(){
    this.move();
    noStroke();
    fill(255,0,0);
    ellipse(this.xPos,this.yPos,this.radius*2,this.radius*2);
    shape(this.display,xPos-this.radius*1.5,yPos-this.radius*1.5,this.radius*2+0.05*height,this.radius*2+0.05*height);
  }
  
  void hit(){
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
