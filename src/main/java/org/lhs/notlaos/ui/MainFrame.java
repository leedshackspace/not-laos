package org.lhs.notlaos.ui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.lhs.notlaos.gcode.GCode;
import org.lhs.notlaos.ui.ButtonPanel.Button;

public class MainFrame extends JFrame implements INubSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4022634459164767828L;
	private JPanel contentPane;

	public GCode selectedGcode = null;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		filePanel = new FilesPanel(new File("GCode/MGC"));
		GridBagConstraints gbc_filePanel = new GridBagConstraints();
		gbc_filePanel.gridheight = 2;
		gbc_filePanel.fill = GridBagConstraints.VERTICAL;
		gbc_filePanel.anchor = GridBagConstraints.WEST;
		gbc_filePanel.insets = new Insets(0, 0, 0, 5);
		gbc_filePanel.gridx = 0;
		gbc_filePanel.gridy = 0;
		contentPane.add(filePanel, gbc_filePanel);
		
		
		List<Button> buttons = new ArrayList<Button>();
		buttons.add(new Button(2, "Move", () -> System.out.println("Move")));
		buttons.add(new Button(3, "Set Origin", () -> System.out.println("Set Origin")));
		buttons.add(new Button(3, "Boundaries", () -> System.out.println("Bounds")));
		buttons.add(new Button(4, "Origin Mode", null));
		buttons.add(new Button(3, "Save Origin", null));
		boundPanel = new ButtonPanel(buttons);
		GridBagConstraints gbc_boundPanel = new GridBagConstraints();
		gbc_boundPanel.fill = GridBagConstraints.BOTH;
		gbc_boundPanel.gridx = 1;
		gbc_boundPanel.gridy = 1;
		contentPane.add(boundPanel, gbc_boundPanel);
		
		JPanel previewPanel = new JPanel();
		GridBagConstraints gbc_previewPanel = new GridBagConstraints();
		gbc_previewPanel.insets = new Insets(0, 0, 5, 0);
		gbc_previewPanel.fill = GridBagConstraints.BOTH;
		gbc_previewPanel.gridx = 1;
		gbc_previewPanel.gridy = 0;
		contentPane.add(previewPanel, gbc_previewPanel);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				new Thread(() -> {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					handleCommand(NubCommand.Down);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					handleCommand(NubCommand.Up);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					handleCommand(NubCommand.Left);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					handleCommand(NubCommand.Right);
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					handleCommand(NubCommand.Click);
				}
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					handleCommand(NubCommand.Deselect);
				}
				}).run();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		filePanel.highlight(1);
	}
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
	}
	
	boolean selected = false;
	int highlighted = 0;
	
	private FilesPanel filePanel;
	private ButtonPanel boundPanel;
	
	@Override
	public void handleCommand(NubCommand nc) {
		switch (highlighted) {
		case 0: //Files
			if (selected) {
				if (nc == NubCommand.Deselect) {
					selected = false;
					boundPanel.highlight(1);
					break;
				}
				filePanel.handleCommand(nc);
				if (nc == NubCommand.Click) {
					selected = !filePanel.selected;
					if (!selected) filePanel.highlight(1);
				}
			} else {
				switch (nc) {
				case Click:
					selected = true;
					filePanel.highlight(2);
					break;
				case Left:
				case Right:
					highlighted = 1;
					filePanel.highlight(0);
					boundPanel.highlight(1);
					break;
				default:
					break;
				}
			}
			break;
		case 1: //Boundaries
			if (selected) {
				if (nc == NubCommand.Deselect) {
					selected = false;
					boundPanel.highlight(1);
					break;
				}
				boundPanel.handleCommand(nc);
			} else {
				switch (nc) {
				case Click:
					selected = true;
					boundPanel.highlight(2);
					break;
				case Left:
				case Right:
					highlighted = 0;
					filePanel.highlight(1);
					boundPanel.highlight(0);
					break;
				default:
					break;
				}
			}
			break;
		}
		
	}

}
