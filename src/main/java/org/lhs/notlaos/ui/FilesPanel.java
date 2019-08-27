package org.lhs.notlaos.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

/**
 * File list panel
 *
 * Lists all files in a directory and allows them to be selected
 * 
 * Is written entirely using the Graphics API, so is slightly hard to read (sorry)
 * 
 * @author jediminer543
 *
 */
public class FilesPanel extends JPanel implements INubSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4801617030031934389L;

	File rootDir;
	
	/**
	 * Create the panel.
	 */
	public FilesPanel(File rootDir) {
		this.rootDir = rootDir;
		init();
	}
	
	/**
	 * Initialise local variables
	 */
	private void init() {
		backFiles();
	}
	
	public void backFiles() {
		List<File> backFiles = new ArrayList<File>();
		if (rootDir.listFiles() != null) backFiles.addAll(Arrays.asList(rootDir.listFiles()));
		backFiles.sort(Comparator.comparing(f -> ((File)f).lastModified()).reversed());
		files = backFiles; //Buffer swap
		repaint();
	}
	
	List<File> files = new ArrayList<File>();
	
	int tgtIndex = 0;
	boolean selected = false;
	int highlight = 0;
	
	/**
	 * Move selection cursor up
	 */
	public void up() {
		tgtIndex = Math.min(tgtIndex+1, files.size()-1);
		repaint();
	}
	
	/**
	 * Move selection cursor down
	 */
	public void down() {
		tgtIndex = Math.max(tgtIndex-1, 0);
		repaint();
	}
	
	public void select() {
		selected = !selected; 
		repaint();
	}
	
	public void highlight(int highlight) {
		this.highlight = highlight;
		repaint();
	}
	
	private int itemWidth = 250;
	private int itemHeight = 80;
	private int itemSpacing = 10;
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(itemWidth+20, itemHeight+20);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(itemWidth+20, 3*(itemHeight+20));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Shape supclip = g.getClip();
		
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setFont(getFont().deriveFont(15f));
		g2.setStroke(new BasicStroke(2.5f));
		
		int viewable = this.getHeight()/itemHeight;
				
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
		
		g2.clip(new RoundRectangle2D.Float(5, 5, this.getWidth()-10, this.getHeight()-10, 5, 5));
		
		int index = 0;
		int vidx = 0;
		for (File f : files) {
			//Skip to middle
			if (tgtIndex-index > viewable/2) {
				index++;
				continue;
			}
			//Draw boundaries
			g.setColor(index == tgtIndex ? (selected ? UIGlobals.SelFG : UIGlobals.TrimFG) : UIGlobals.NormFG);
			//Set clip
			Shape clip = g.getClip();
			//Draw internals
			Area newClip = new Area(new Rectangle2D.Float(0, vidx*(itemSpacing+itemHeight), itemWidth+1+itemSpacing, itemHeight+itemSpacing));
			Area oldClip = new Area(clip);
			newClip.intersect(oldClip);
			g.setClip(newClip);
			g.translate(itemSpacing, itemSpacing+vidx*(itemSpacing+itemHeight));
			paintFile(g2, f);
			g.translate(-itemSpacing, -itemSpacing-vidx*(itemSpacing+itemHeight));
			//End draw
			g.setClip(clip);
			index++;
			vidx++;
			//Postemptive skip for cycle saving
			if (vidx > viewable) {
				break;
			}
		}
		
		g.setClip(supclip);
	}
	
	/**
	 * Paint a given file
	 * 
	 * @param g graphics to draw with
	 * @param f file do display
	 */
	private void paintFile(Graphics2D g, File f) {
		g.drawRoundRect(0, 0, itemWidth, itemHeight-1, 10, 10);
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.drawString(f.getName(), 10, 10+g.getFontMetrics().getHeight());
		g.setFont(g.getFont().deriveFont(0));
		g.drawString(new Date(f.lastModified()).toInstant().toString(), 10, 15+2*g.getFontMetrics().getHeight());
	}

	@Override
	public void HandleCommand(NubCommand nc) {
		switch (nc) {
		case Up:
			up();
			break;
		case Down:
			down();
			break;
		case Click:
			select();
			break;
		default:
			break;
		}
	}

}
