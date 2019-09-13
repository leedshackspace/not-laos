package org.lhs.notlaos.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.joml.Vector3f;
import org.lhs.notlaos.gcode.GCode;
import org.lhs.notlaos.machine.Machine;
import org.lhs.notlaos.translation.MGCTranslator;
import org.lhs.notlaos.ui.ButtonPanel.Button;
import org.lhs.notlaos.ui.ButtonPanel.IButton;

public class MainFrame extends JFrame implements INubSelect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4022634459164767828L;
	private JPanel contentPane;

	public GCode selectedGcode = null;
	private Machine machine;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame(new Machine("", -1, 600, 400));
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
	public MainFrame(Machine m) {
		this.machine = m;
		
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
		gbc_filePanel.gridheight = 3;
		gbc_filePanel.fill = GridBagConstraints.VERTICAL;
		gbc_filePanel.anchor = GridBagConstraints.WEST;
		gbc_filePanel.insets = new Insets(0, 0, 0, 5);
		gbc_filePanel.gridx = 0;
		gbc_filePanel.gridy = 0;
		contentPane.add(filePanel, gbc_filePanel);
		
		
		List<IButton> buttons = new ArrayList<IButton>();
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
		
		buttons = new ArrayList<IButton>();
		buttons.add(new Button(2, "Cut", () -> System.out.println("Cut")));
		cutPanel = new ButtonPanel(buttons);
		GridBagConstraints gbc_cutPanel = new GridBagConstraints();
		gbc_cutPanel.fill = GridBagConstraints.BOTH;
		gbc_cutPanel.gridx = 1;
		gbc_cutPanel.gridy = 2;
		contentPane.add(cutPanel, gbc_cutPanel);
		
		previewPanel = new PreviewPanel(machine);
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
	private ButtonPanel cutPanel;
	private PreviewPanel previewPanel;
	
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
					if (!selected) {
						filePanel.highlight(1);
						
					}
				}
				if (filePanel.selected) {
					try {
						this.selectedGcode = new MGCTranslator().translate(new FileInputStream(filePanel.getSelectedFile()));
						previewPanel.setVector(this.selectedGcode.getVector(new Vector3f(), Color.BLUE, Color.RED));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					this.selectedGcode = null;
					previewPanel.setVector(new ArrayList<>());
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
			if (nc == NubCommand.Down) {
				selected = false;
			}
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
				case Down:
					highlighted = 2;
					cutPanel.highlight(1);
					boundPanel.highlight(0);
					break;
				default:
					break;
				}
			}
			break;
		case 2: //Cut
			if (nc == NubCommand.Up) {
				selected = false;
			}
			if (selected) {
				if (nc == NubCommand.Deselect) {
					selected = false;
					cutPanel.highlight(1);
					break;
				}
				cutPanel.handleCommand(nc);
			} else {
				switch (nc) {
				case Click:
					selected = true;
					cutPanel.highlight(2);
					break;
				case Left:
				case Right:
					highlighted = 0;
					filePanel.highlight(1);
					cutPanel.highlight(0);
					break;
				case Up:
					highlighted = 1;
					boundPanel.highlight(1);
					cutPanel.highlight(0);
					break;
				default:
					break;
				}
			}
			break;
		}
		
	}

}
