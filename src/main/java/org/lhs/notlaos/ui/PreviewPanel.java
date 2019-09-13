package org.lhs.notlaos.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joml.Vector3f;
import org.lhs.notlaos.machine.Machine;
import org.lhs.notlaos.translation.MGCTranslator;
import org.lhs.notlaos.vector.IVectorElement;

/**
 * Shows what a given GCode file will look like
 * 
 * @author jediminer543
 *
 */
public class PreviewPanel extends JPanel implements INubSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6014567145408425912L;
	
	
	private List<IVectorElement> elements = null;
	private Machine m;
	
	//Test Code Please Ignore
	public static void notmain(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					PreviewPanel test = new PreviewPanel(new Machine(null, -1, 600, 400));
					frame.setContentPane(test);
					test.setVector(new MGCTranslator()
							.translate(new FileInputStream(new File("GCode/MGC/Test.MGC")))
							.getVector(new Vector3f(), Color.BLUE, Color.RED));
					frame.setSize(1200, 800);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public PreviewPanel(Machine m) {
		this.m = m;
	}
	
	public void setVector(List<IVectorElement> newVec) {
		elements = new ArrayList<IVectorElement>(newVec);
		repaint();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(200, 100);
	}
	
	private int inset = 5;
	
	private int lineStep = 10;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Shape supclip = g.getClip();
		
		Graphics2D g2 = (Graphics2D)g;
				
		g2.setFont(getFont().deriveFont(15f));
		g2.setStroke(new BasicStroke(2.5f));
		
		g2.drawRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
		
		g2.translate(inset, inset);
		
		double xScale = ((double)(getWidth()-inset*2))/m.getBedWidth();
		double yScale = ((double)(getHeight()-inset*2))/m.getBedHeight();
				
		double selScale = Math.min(xScale, yScale);
		
		g2.setClip(-2, -2, getWidth()-inset*2 + 4, getHeight()-inset*2 + 4);
		
		g2.setStroke(new BasicStroke(0.5f));
		g2.setColor(Color.LIGHT_GRAY);
		
		//Setup machine coordinates
		g2.scale(selScale, -selScale);
		g2.translate(0, -m.getBedHeight()); 
		
		//Draw Grid
		for (int i = 0; i <= m.getBedHeight()/lineStep; i++) {
			g2.drawLine(0, (int)(i*lineStep), (int)(m.getBedWidth()), (int)(i*lineStep));
		}
		for (int i = 0; i <= m.getBedWidth()/lineStep; i++) {
			g2.drawLine((int)(i*lineStep), 0, (int)(i*lineStep), (int)(m.getBedHeight()));
		}
		
		//Draw Cut
		g2.setStroke(new BasicStroke(1.5f));
		if (elements != null) {
			for (IVectorElement ive : elements) {
				ive.draw(g2);
			}
		}
		
		g2.setClip(supclip);
	}
	
	@Override
	public void handleCommand(NubCommand nc) {
		
	}

}
