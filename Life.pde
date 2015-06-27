class Life extends Target{
  Life(){
    super(0,70,1);
    this.display=loadShape("1up.svg");
  }
  
  void create(){
    this.move();
    stroke(0);
    shape(this.display,xPos-this.radius*1.02,yPos-this.radius*1.02,(this.radius*2+0.01*height)*0.9,(this.radius*2+0.01*height)*0.9);
  }
  
  void move(){
    this.xPos=this.xPos+this.xSpeed;
    if(this.xPos>width||this.xPos<0){
      game.life=null;
    }
  }
  
  void hit(){
    canfire=true;
    game.shooterlist[0]=null;
    game.lives++;
    game.life=null;
  }
}
