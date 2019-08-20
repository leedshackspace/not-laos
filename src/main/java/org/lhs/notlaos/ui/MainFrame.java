package org.lhs.notlaos.ui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame implements INubSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4022634459164767828L;
	private JPanel contentPane;

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
		setBounds(100, 100, 670, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		filePanel = new FilesPanel(new File("C:\\Users\\benob\\Documents\\not-laos\\test\\"));
		GridBagConstraints gbc_filePanel = new GridBagConstraints();
		gbc_filePanel.fill = GridBagConstraints.VERTICAL;
		gbc_filePanel.anchor = GridBagConstraints.WEST;
		gbc_filePanel.insets = new Insets(0, 0, 0, 5);
		gbc_filePanel.gridx = 0;
		gbc_filePanel.gridy = 0;
		contentPane.add(filePanel, gbc_filePanel);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					filePanel.up();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					filePanel.down();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
	}
	
	boolean selected = false;
	int highlighted = 0;
	private FilesPanel filePanel;
	
	@Override
	public void HandleCommand(NubCommand nc) {
		switch (highlighted) {
		case 0: //Files
			if (selected) {
				filePanel.HandleCommand(nc);
				if (nc == NubCommand.Click) {
					selected = false;
				}
			}
			switch (nc) {
			case Click:
				selected = true;
				break;
			}
		}
		
	}

}
