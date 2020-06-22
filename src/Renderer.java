
import java.awt.Graphics;
import javax.swing.JPanel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vern Sin
 */
public class Renderer  extends JPanel{
    private static final long serialVersionUID = 1L;
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Bowman.bowman.repaint(g);
        
    }
}
