package org.lhs.notlaos.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ButtonPanel extends JPanel implements INubSelect
{
	
	int inset = 5;
	
	List<IButton> btns;
	
	int preferedWidth = 200;
	
	public ButtonPanel() {
		this.btns = new ArrayList<ButtonPanel.IButton>();
		this.btns.add(new Button(1, "Test", () -> System.out.println("HELLO")));
		this.btns.add(new Button(2, "SuperLongTest90", () -> System.out.println("HELLO")));
		init();
	}
	
	public ButtonPanel(List<IButton> btns) {
		this.btns = new ArrayList<>(btns);
		init();
	}
	
	private void init() {
		this.setFont(getFont().deriveFont(20f));
		preferedWidth = 200;
		btns.stream() // Itterate over buttons
		.mapToInt(
				b -> (this.getFontMetrics(getFont()).stringWidth(b.getText())+inset*4)/b.getWidth() // Get String length per unit button width
		)
		.max() // Find maximum
		.ifPresent(
				i -> preferedWidth = i * btns.stream().mapToInt(j->j.getWidth()).sum()
		);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3354199170215185655L;

	int highlight = 0;
	
	public void highlight(int highlight) {
		this.highlight = highlight;
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(preferedWidth, getFontMetrics(getFont()).getHeight() + inset*4);
	}
	
	private List<Rectangle> buttonCollisions;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Shape supclip = g.getClip();
		
		Graphics2D g2 = (Graphics2D)g;
		
		//Setup
		g2.setFont(getFont());
		g2.setStroke(new BasicStroke(2.5f));
		
		//Border
		g2.setColor(UIGlobals.L2BG);
		g2.fillRoundRect(inset, inset, this.getWidth()-inset*2, this.getHeight()-inset*2, 5, 5);
		if (highlight == 1) {
			g2.setColor(UIGlobals.TrimFG);
		} else if (highlight == 2) {
			g2.setColor(UIGlobals.SelFG);
		} else {
			g2.setColor(UIGlobals.NormFG);
		}
		g2.drawRoundRect(5, 5, this.getWidth()-10, this.getHeight()-10, 5, 5);
		g2.setColor(UIGlobals.NormFG);
		//Internal Clip
		g2.clip(new RoundRectangle2D.Float(inset, inset, this.getWidth()-inset*2, this.getHeight()-inset*2, 5, 5));
		
		//Draw Buttons
		g.translate(inset, inset);
		drawButtons(g2);
		g.translate(-1*inset, -1*inset);
		
		g.setClip(supclip);
	}
	
	public void drawButtons(Graphics2D g2) {
		int step = (getWidth()-inset*3)/(btns.stream().mapToInt(i->i.getWidth()).sum());
		
		int curstep = 0;
		
		int btnHeight = getHeight()-4*inset;
		int idx = 0;
		
		buttonCollisions = new ArrayList<>();
		
		for (IButton btn : btns) {
			int btnOrigin = inset + step*curstep;
			int btnWidth = step*btn.getWidth() - inset;
			
			
			
			if (selected == idx) {
				if (press) {
					g2.setColor(UIGlobals.SelFG);
					
				} else {
					g2.setColor(UIGlobals.TrimFG);
				}
			} else {
				g2.setColor(UIGlobals.NormFG);
			}
			g2.drawRoundRect(btnOrigin, inset, btnWidth, btnHeight, 5, 5);
			
			buttonCollisions.add(new Rectangle(btnOrigin+inset, inset+inset, btnWidth, btnHeight));
			
			g2.drawString(btn.getText(), btnOrigin + btnWidth/2 - g2.getFontMetrics().stringWidth(btn.getText())/2, inset + g2.getFontMetrics().getAscent());
			curstep += btn.getWidth();
			
			idx++;
		}
	}
	
	{
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < buttonCollisions.size(); i++) {
					if (buttonCollisions.get(i).contains(e.getPoint())) {
						selected = i;
						handleCommand(NubCommand.Click);
					}
				}
			}
		});
	}
	
	public static interface IButton {
		
		public int getWidth();
		
		public String getText();
		
		public void click();
		
	}
	
	public static class Button implements IButton {
		int width;
		String text;
		Runnable action;
		
		public Button(int width, String text, Runnable action) {
			super();
			this.width = width;
			this.text = text;
			this.action = action;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public String getText() {
			return text;
		}

		@Override
		public void click() {
			if (action != null) action.run();
		}
		
	}
	
	int selected = 0;
	boolean press = false;
	
	private void redrawButton(int tidx) {
		paintComponent(getGraphics());
	}
	
	@Override
	public void handleCommand(NubCommand nc) {
		switch (nc) {
		case Click:
			System.out.println("Starting Click");
			press = true;
			redrawButton(selected);
			btns.get(selected).click();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			press = false;
			redrawButton(selected);
			Thread.yield();
			System.out.println("Finishing Click");
			break;
		case Left:
			selected = Math.max(selected-1, 0);
			repaint();
			break;
		case Right:
			selected = Math.min(selected+1, btns.size()-1);
			repaint();
			break;
		default:
			break;
		}
	}
}
