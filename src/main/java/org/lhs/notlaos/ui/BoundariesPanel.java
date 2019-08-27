package org.lhs.notlaos.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class BoundariesPanel extends JPanel {
	public BoundariesPanel() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3354199170215185655L;

	int highlight = 0;
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 40);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return super.getPreferredSize();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Shape supclip = g.getClip();
		
		Graphics2D g2 = (Graphics2D)g;
		
		//Setup
		g2.setFont(getFont().deriveFont(15f));
		g2.setStroke(new BasicStroke(2.5f));
		
		//Border
		g2.setColor(UIGlobals.L2BG);
		g2.fillRoundRect(5, 5, this.getWidth()-10, this.getHeight()-10, 5, 5);
		if (highlight == 1) {
			g2.setColor(UIGlobals.TrimFG);
		} else if (highlight == 2) {
			g2.setColor(UIGlobals.SelFG);
		} else {
			g2.setColor(UIGlobals.NormFG);
		}
		g2.drawRoundRect(5, 5, this.getWidth()-10, this.getHeight()-10, 5, 5);
		//Internal Clip
		g2.clip(new RoundRectangle2D.Float(5, 5, this.getWidth()-10, this.getHeight()-10, 5, 5));
		
		int step = (getWidth()-10)/5;
		
		//MoveButton
		g2.drawRoundRect(10+step*0, 10, step, getHeight()-20, 5, 5);
		g2.drawString("Test", (int)g2.getFontMetrics().getStringBounds("Test", g2).getWidth()/2 + 15+step*0, 25);
		
		
		g.setClip(supclip);
	}
	
}
