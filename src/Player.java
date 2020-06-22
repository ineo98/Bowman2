/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Point;
import java.awt.Rectangle;
/**
 *
 * @author Vern Sin
 */
public class Player {
    public int Xcoor;
    public int Ycoor;
    public double angle;
    public int mag;
    public int width = 20;
    public int height = 20;
    public Rectangle rect;
    public int PX, PY;
    public int health = 100;
    
    public Player(int xcoor, int ycoor, int PX, int PY){
        this.Xcoor = xcoor;
        this.Ycoor = ycoor;
        this.PX = PX;
        this.PY = PY;
    }
    
    public boolean shoot(int xco, int yco, int playerX, int playerY){
        yco += 105;
        Rectangle rect = new Rectangle(playerX, playerY, 80, 170);
        Rectangle temp = new Rectangle(xco, yco, 20, 20);
        int leftX = Math.max(rect.x, temp.x);
        int rightX = (int) Math.min(rect.getMaxX(), temp.getMaxX());
        int topY = Math.max(rect.y,temp.y);
        int botY =  (int) Math.min(rect.getMaxY(), temp.getMaxY());
        if ((rightX > leftX) && (botY > topY)) {
            health -= 20;
            return true;
        }else{
            if(rect.intersects(temp)){
                health -= 20;
                return true;
            }
        }
        return false;
    }
    
    public void setAngle(double angle){
        this.angle = angle;
    }

    public int getMag() {
        return mag;
    }

    public void setMag(int mag) {
        this.mag = mag;
    }
    
    public double getAngle(){
        return angle;
    }
    
    public int getHealth(){
        return health;
    }
  
}
