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
    this.Width=W*1.0;
    this.Height=H*1.0;
    this.posX=X*1.0;
    this.posY=Y*1.0;
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
  
  void display(){
    rect(this.posX,this.posY,this.Width,this.Height);
    rectMode(CORNER);
    fill(255,255,255);
    textAlign(CENTER,CENTER);
    textSize(this.textsize);
    text(this.text,this.posX,this.posY,this.Width,this.Height);
  }
  
  boolean pressed(){
    if(mouseX>this.posX && mouseY>this.posY && mouseX<this.posX+this.Width && mouseY<this.posY+this.Height){ //checks if clicked
        return true;
    }
    return false;
  }
}
