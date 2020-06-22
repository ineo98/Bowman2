/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;
/**
 *
 * @author Vern Sin
 */
public class Bowman implements ActionListener, MouseListener, KeyListener, MouseMotionListener{
    public static Bowman bowman;

    public final int WIDTH = 1500, HEIGHT = 800;

    public Renderer renderer;

    public ArrayList<Player> arylist = new ArrayList<Player>();
    public Player ball1;
    public Player ball2;
    
    public boolean P1 = true, P2 = false;
    
    //no change after flying will back to original place
    public int oriX1 = 150, oriY1 = 575;
    public int oriX2 = 1300, oriY2 = 575;
    public int dist = oriX2 - oriX1;
    //coordinate will change due to flying
    public int coorX1 = 150, coorY1 = 575;
    public int coorX2 = 1300, coorY2 = 575;
    //hold drag coordinate
    public Point temp;
    
    public int [] allX = new int [6];

//	public ArrayList<Rectangle> columns;
//	public int ticks, yMotion, score;

    public boolean gameOver, started = false, shooting = false;
    public Random rand;
        
    public int seconds=-1;
  
    public Timer timer = new Timer(100, this);

    //Player 1
    public int P1X = 55;
    public int P1Y = 500;
    
    //Player 2
    public int P2X = 1350;
    public int P2Y = 500;
    public Bowman(){
        allX[0]=P1X;
        allX[1]=P2X;
        allX[2]=0; //previous arrow
        allX[3]=0; //previous arrow
        allX[4]=coorX1;
        allX[5]=coorX2;
        
        ball1 = new Player(oriX1, oriY1, P1X, P1Y);
        ball2 = new Player(oriX2, oriY2, P2X, P2Y);
        arylist.add(ball1);
        arylist.add(ball2);
        
        
        JFrame jframe = new JFrame();

	renderer = new Renderer();
	rand = new Random();

	jframe.add(renderer);
	jframe.setTitle("Bowman");
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setSize(WIDTH, HEIGHT);
	jframe.addMouseListener(this);
	jframe.addKeyListener(this);
        jframe.addMouseMotionListener(this);
        jframe.setResizable(false);
	jframe.setVisible(true);
    }

    public int ux, uy;
    public int sumXRIGHT = 0;    //p1 shoot move to right
    public int sumXLEFT = 0;   //p2 shoot move to left
    @Override
    public void actionPerformed(ActionEvent e){
        seconds++;
        if (started){
            if(P1 == true){
                if(ball1.mag>=110)
                    ball1.mag = 110;
                ux = (int)(ball1.mag*Math.cos(ball1.angle));
                uy = (int)(-4.9*seconds*seconds+ball1.mag*Math.sin(ball1.angle)*seconds+75);
                if(coorY1 >= 0 && coorY1 <= 670 && coorX1 <= 1500){
                    coorX1 += ux;
                    coorY1 = (650-uy);
                    for(int i = 0 ;i <6;i++){
                        if(i == 4){
                            continue;
                        }
                        allX[i]-=10;
                    }
                    
                    if(ball2.shoot(coorX1, coorY1, allX[1], oriY2) == true){
                        int hp = ball2.getHealth();
                        System.out.println("P1 shoot! P2 = "+ball2.getHealth());
                        if(hp <= 0){
                            started = false;
                            gameOver = true;
                        }
                    }
                }
            }else if(P2 == true){
                ux = (int)(ball2.mag*Math.cos(ball2.angle));
                uy = (int)(-4.9*seconds*seconds+ball2.mag*Math.sin(ball2.angle)*seconds+75);
                if(coorY2 >= 0 && coorY2 <= 670 && coorX2 <= 1500){
                    coorX2 -= ux;
                    coorY2 = (650-uy);
                    for(int i = 0 ;i <6;i++){
                        if(i == 5){
                            continue;
                        }
                        allX[i]+=10;
                    }
                    
                    if(ball1.shoot(coorX2, coorY2, allX[0], oriY1) == true){
                        int hp = ball1.getHealth();
                        System.out.println("P2 shoot! P1 = "+ball1.getHealth());
                        if(hp <= 0){
                            started = false;
                            gameOver = true;
                        }
                    }
                }
            }
            
        }
        if(coorY1 >= 670 || coorY2 >= 670 || coorX1 >= 1500 || coorX2 >= 1500 || coorY1 <= 0 || coorY2 <= 0){
            shooting = false;
            timer.stop();
            if(coorY1 >= 670 || coorY1 <= 0 || coorX1 >= 1500){
                coorX1 = oriX1;
                coorY1 = oriY1;
                P1 = false;
                P2 = true;
                sumXRIGHT = allX[0]-P1X;
                for(int i=0;i<6;i++){
                    allX[i]-=sumXRIGHT;
                }
                sumXRIGHT=0;
                sumXLEFT=0;
            }
            if(coorY2 >= 670 || coorY2 <= 0 || coorX2 >= 1500){
                coorX2 = oriX2;
                coorY2 = oriY2;
                P2 = false;
                P1 = true;
                sumXLEFT = P1X - allX[0];
                for(int i=0;i<6;i++){
                    allX[i]+=sumXLEFT;
                }
                sumXRIGHT=0;
                sumXLEFT=0;
            }
        }
	renderer.repaint();
    }

    public void repaint(Graphics g){
        //sky
        g.setColor(new Color(240, 255, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        //ground
        g.setColor(new Color(152,251,152));
        g.fillRect(0, HEIGHT-120, WIDTH, 150);
                
//        //rect1
//        g.setColor(Color.LIGHT_GRAY);
//        g.fillRect(coorX1, coorY1, ball1.width, ball1.height);
//        
//        //rect2
//        g.setColor(Color.DARK_GRAY);
//        g.fillRect(coorX2, coorY2, ball2.width, ball2.height);
        
        //arrow 1
        g.setColor(Color.BLACK);
        g.drawLine(coorX1-50, coorY1, coorX1, coorY1);
        g.drawLine(coorX1-5,coorY1-10,coorX1, coorY1);
        g.drawLine(coorX1-5,coorY1+10,coorX1, coorY1);
        
        //arrow 2
        g.drawLine(coorX2, coorY2, coorX2+50, coorY2);
        g.drawLine(coorX2,coorY2,(int)((coorX2+5)*Math.cos(angle)), coorY2+10);
        g.drawLine(coorX2,coorY2,(int)((coorX2+5)*Math.cos(angle)), coorY2-10);
        
        //Player 1
        g.fillOval(allX[0], P1Y, 50, 50);
        g.fillRect(allX[0]+25, P1Y+50, 2, 70);          //body
        g.fillRect(allX[0]+10, P1Y+120, 30, 2);         //connect
        g.drawArc(allX[0]+30, P1Y+50, 30, 50, 0, 90);   //bow
        g.drawArc(allX[0]+30, P1Y+50, 30, 50, 0, -90);  //bow
        g.fillRect(allX[0]+25, oriY1, oriX1-allX[0]-70, 2); //hand
        g.fillRect(allX[0]+10, P1Y+120, 2, 60);         //right leg
        g.fillRect(allX[0]+40, P1Y+120, 2, 60);         //left leg
        
        //Player 2
        g.fillOval(allX[1], P2Y, 50, 50);
        g.fillRect(allX[1]+25, P2Y+50, 2, 70);          //body
        g.fillRect(allX[1]+10, P2Y+120, 30, 2);         //connect
        g.drawArc(allX[1]-10, P2Y+50, 30, 50, 90, 180); //bow
        g.fillRect(allX[1]+25, oriY2, (oriX2-allX[1])/2, 2);//hand
        g.fillRect(allX[1]+10, P2Y+120, 2, 60);         //right leg
        g.fillRect(allX[1]+40, P2Y+120, 2, 60);         //left leg
        
        g.setColor(Color.GRAY);
	g.setFont(new Font("Arial", 1, 100));

	if (!started && !gameOver){
            g.drawString("Click to start!", 375, HEIGHT / 2 - 50);
	}

	if (gameOver){
            g.drawString("Game Over!", 425, HEIGHT / 2 - 50);
	}

        if (!gameOver && started){
            g.setFont(new Font("Arial",Font.BOLD, 16));
            //player name
            g.drawString("Player 1", 50, 30);
            g.drawString("Player 2", 950, 30);
            
            //hp bar
            g.drawRect(50, 50, 300, 10);
            g.drawRect(950, 50, 300, 10);
            
            //hp level
            g.setColor(Color.RED);
            g.fillRect(50, 50, ball1.health*3, 10);
            g.fillRect(950, 50, ball2.health*3, 10);
            
            //magnitude and angle
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial",Font.PLAIN, 14));
            g.drawString("Magnitude: "+ball1.getMag(), 50, 80);
            g.drawString("Angle: "+Math.toDegrees(ball1.getAngle()), 50, 100);
            g.drawString("Magnitude: "+ball2.getMag(), 950, 80);
            g.drawString("Angle: "+Math.toDegrees(ball2.getAngle()), 950, 100);
	}
    }

    public static void main(String[] args){
	bowman = new Bowman();
    }
    
    //Magnitude and angle
    
    public double angle;
    public int mag;
    public int shootNumber1 = 1;
    public int shootNumber2 = 1;
    public void getAngle(Point e){
        if(P1==true){
            if(coorY1 >= e.y && coorX1 <= e.x){
                double inside = ((coorY1+10 - e.y)*1.0000)/(e.x-coorX1+10);
                double tempRad = Math.tanh(inside)/2*3;
                if(tempRad < 0)
                    ball1.setAngle(0);
                else if (tempRad >= Math.PI/2 || tempRad == 88)
                    ball1.setAngle(Math.PI/2);
                else
                    ball1.setAngle(tempRad);
            }else if(coorX1 > e.x){
                ball1.setAngle(Math.PI/2);
            }else if(coorX1 <= e.x && coorY1 <= e.y){
                ball1.setAngle(0);
            }
        }else if(P2 == true){
            if(coorY2 >= e.y && coorX2 >= e.x){
                double inside = ((coorY2+10 - e.y)*1.0000)/(coorX2-e.x+10);
                double tempRad = Math.tanh(inside)/2*3;
                if(tempRad < 0)
                    ball2.setAngle(0);
                else if (tempRad >= Math.PI/2 || tempRad == 88)
                    ball2.setAngle(Math.PI/2);
                else
                    ball2.angle = tempRad;
            }else if(coorX2 < e.x){
                ball2.setAngle(Math.PI/2);
            }else if(coorX2 >= e.x && coorY2 >= e.y){
                ball2.setAngle(0);
            }
        }
    }
    
    public void getMag(Point e){
        double tempMag = (Math.sqrt(Math.pow((coorX1 - e.x), 2)+Math.pow((coorY1 - e.y), 2)))*0.5;
        if(tempMag <= 0)
            mag = 0;
        else if (tempMag >= 110)
            mag = 110;
        else
            mag = (int) tempMag;
        if(P1 == true)
            ball1.setMag(mag);
        else
            ball2.setMag(mag);
    }

    //Mouse Event--------------------------------------------------------------------------------
    @Override
    public void mouseClicked(MouseEvent e){}
        
    public boolean toggle = false;
    public boolean firstRound = false;
    @Override
    public void mouseReleased(MouseEvent e){
        if(started == false){
            started = true;
            firstRound = true;
        }
        temp = e.getPoint();
        if(temp.x>=0 && temp.x<=WIDTH && temp.y>=0 && temp.y <= HEIGHT){
            if(P1 == true && toggle == false){
//                if(firstRound == false){
//                    for(int i=0;i<6;i++){
//                        allX[i]-=(sumXRIGHT);
//                    }
//                }else{
//                    firstRound = false;
//                }
                ux=0;uy=0;
                getAngle(temp); 
                getMag(temp);
                toggle = true;
                shootNumber1++;
            }else if(P2 == true && toggle == false){
//                for(int i=0;i<6;i++){
//                    allX[i]+=(sumXLEFT);
//                }
                ux=0;uy=0;
                getAngle(temp); 
                getMag(temp);
                shootNumber2++;
            }
            if(shooting == false){
                shooting = true;
                started = true;
                if(seconds >= 0){
                    seconds = -1;
                    timer.restart();
                }else
                    timer.start();
            }
        }
        renderer.repaint();
        toggle = false;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        temp = me.getPoint();
        if(temp.x>=0 && temp.x<=WIDTH && temp.y>=0 && temp.y <= HEIGHT){
            if(P1 == true){
                getAngle(temp); getMag(temp);
                System.out.println(ball1.angle+" "+ball1.mag);
            }else if(P2 == true){
                getAngle(temp); getMag(temp);
                System.out.println(ball2.angle+" "+ball2.mag);
            }
        }
        renderer.repaint();
    }
    
    //No use--------------------------------------------------------------------------------------

    @Override
    public void mouseMoved(MouseEvent me) {}
    
    @Override
    public void mouseEntered(MouseEvent e){}
        
    @Override
    public void mouseExited(MouseEvent e){}

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e){}
    
    
    @Override
    public void keyReleased(KeyEvent e){}
	
    @Override
    public void mousePressed(MouseEvent e){}
    
}
