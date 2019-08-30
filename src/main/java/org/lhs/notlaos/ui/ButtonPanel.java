package org.lhs.notlaos.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel {
	
	public ButtonPanel() {
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
		
		//MoveButton
		drawButtons(g2);
		
		g.setClip(supclip);
	}
	
	public void drawButtons(Graphics2D g2) {
		int step = (getWidth()-10)/5;
		
		g2.drawRoundRect(10+step*0, 10, step-5, getHeight()-20, 5, 5);
		g2.drawString("Move", 10 + step*0 + (step-5)/2 - g2.getFontMetrics().stringWidth("Move")/2, 10 + g2.getFontMetrics().getAscent());
		
		g2.drawRoundRect(10+step*1, 10, step-5, getHeight()-20, 5, 5);
		g2.drawString("Set Origin", 10 + step*1 + (step-5)/2 - g2.getFontMetrics().stringWidth("Set Origin")/2, 10 + g2.getFontMetrics().getAscent());
		
		g2.drawRoundRect(10+step*2, 10, step*2-5, getHeight()-20, 5, 5);
		g2.drawString("Origin: Top Left", 10 + step*2 + (step*2-5)/2 - g2.getFontMetrics().stringWidth("Origin: Top Left")/2, 10 + g2.getFontMetrics().getAscent());
		
		g2.drawRoundRect(10+step*4, 10, step-5, getHeight()-20, 5, 5);
		g2.drawString("Save Origin", 10 + step*4 + (step-5)/2 - g2.getFontMetrics().stringWidth("Save Origin")/2, 10 + g2.getFontMetrics().getAscent());
	}
	
	public class Button {
		int width;
		String text;
		
	}
}
