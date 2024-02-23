package florianbutz.codenode.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import florianbutz.codenode.main.ErrorCode;
import florianbutz.codenode.main.Main;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JTextArea;

public class CodeNodeGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTextMap;
	private TreePanel treePanel;
	private JSlider sNodemapTransparency;

	public static void BuildWindow() {
		FlatDarkLaf.setup();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeNodeGUI frame = new CodeNodeGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public CodeNodeGUI() {
		setBackground(new Color(34, 40, 49));
		setTitle("Code Node");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1078, 617);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(57, 62, 70));
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Datei");
		mnNewMenu.setForeground(new Color(221, 221, 221));
		mnNewMenu.setBackground(new Color(57, 62, 70));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Öffnen");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenFile();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Beenden");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExitApp(0);
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(60, 63, 65));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(57, 62, 70));
		scrollPane.setBounds(10, 42, 310, 503);
		scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		lbTextMap = new JLabel("<html>\r\nTest<br>\r\n&nbsp;&nbsp;&nbsp;&nbsp;test</html>");
		lbTextMap.setVerticalAlignment(SwingConstants.TOP);
		lbTextMap.setForeground(new Color(221, 221, 221));
		lbTextMap.setBorder(new EmptyBorder(10, 10, 10, 10));
		lbTextMap.setFont(new Font("Noto Sans", Font.PLAIN, 14));
		lbTextMap.setOpaque(true);
		lbTextMap.setBackground(new Color(72, 77, 87));
		scrollPane.setViewportView(lbTextMap);
		
		JLabel lblNewLabel = new JLabel("Text Struktur:");
		lblNewLabel.setForeground(new Color(221, 221, 221));
		lblNewLabel.setFont(new Font("Noto Sans", Font.BOLD, 15));
		lblNewLabel.setBounds(10, 11, 115, 20);
		contentPane.add(lblNewLabel);
		
		treePanel = new TreePanel();
		treePanel.nodeBackgroundColor = new Color(72, 77, 87);
		treePanel.connectionColor = new Color(72, 77, 87);
		treePanel.backgroundColor = new Color(48, 51, 58);
		
		CodeNode welcomeNode = new CodeNode("Wilkommen", 350, 240, 200, 75, null, "Klicke auf 'Datei öffnen' um zu starten.");
		treePanel.AddNode(welcomeNode);
		
		treePanel.setBounds(330, 42, 702, 465);
		contentPane.add(treePanel);
		
		JLabel lblNodemap = new JLabel("Nodemap:");
		lblNodemap.setForeground(new Color(221, 221, 221));
		lblNodemap.setFont(new Font("Noto Sans", Font.BOLD, 15));
		lblNodemap.setBounds(330, 11, 78, 20);
		contentPane.add(lblNodemap);
		
		JButton btnResetViewPosition = new JButton("");
		btnResetViewPosition.setToolTipText("Kameraposition in der Nodemap zurrücksetzen.");
		btnResetViewPosition.setIcon(GetIcon("/florianbutz/codenode/img/reset_btnicon.png", 20, 20));
		btnResetViewPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetViewport();
			}
		});
		btnResetViewPosition.setBackground(new Color(72, 77, 87));
		btnResetViewPosition.setBounds(1005, 518, 27, 27);
		contentPane.add(btnResetViewPosition);
		
		JButton btnDatei = new JButton("Datei öffnen");
		btnDatei.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenFile();
			}
		});
		btnDatei.setBackground(new Color(72, 77, 87));
		btnDatei.setBounds(330, 518, 115, 27);
		contentPane.add(btnDatei);
		
		JButton btnBGPattern1 = new JButton("");
		btnBGPattern1.setToolTipText("Hintergrundmuster der Nodemap ändern.");
		btnBGPattern1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.ChangePattern(0);
			}
		});
		btnBGPattern1.setIcon(GetIcon("/florianbutz/codenode/img/pattern1_btnicon.png", 15, 15));
		btnBGPattern1.setBackground(new Color(72, 77, 87));
		btnBGPattern1.setBounds(1012, 11, 20, 20);
		contentPane.add(btnBGPattern1);
		
		JButton btnBGPattern2 = new JButton("");
		btnBGPattern2.setToolTipText("Hintergrundmuster der Nodemap ändern.");
		btnBGPattern2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.ChangePattern(1);
			}
		});
		btnBGPattern2.setIcon(GetIcon("/florianbutz/codenode/img/pattern2_btnicon.png", 15, 15));
		btnBGPattern2.setBackground(new Color(72, 77, 87));
		btnBGPattern2.setBounds(982, 11, 20, 20);
		contentPane.add(btnBGPattern2);
		
		JButton btnBGPattern3 = new JButton("");
		btnBGPattern3.setToolTipText("Hintergrundmuster der Nodemap ändern.");
		btnBGPattern3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.ChangePattern(2);
			}
		});
		 
		btnBGPattern3.setIcon(GetIcon("/florianbutz/codenode/img/pattern3_btnicon.png", 15, 15));
		btnBGPattern3.setBackground(new Color(72, 77, 87));
		btnBGPattern3.setBounds(952, 11, 20, 20);
		contentPane.add(btnBGPattern3);
		
		sNodemapTransparency = new JSlider();
		sNodemapTransparency.setMaximum(50);
		sNodemapTransparency.setValue(15);
		sNodemapTransparency.setToolTipText("Transparenz des Hintergrundmusters der Nodemap ändern.");
		sNodemapTransparency.setBounds(864, 11, 78, 20);
		sNodemapTransparency.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	float transparency = sNodemapTransparency.getValue() / 100f;
		        treePanel.backgroundTransparency = transparency;
		      }
		    });
		contentPane.add(sNodemapTransparency);
		
		JButton btnDeleteNodemap = new JButton("");
		btnDeleteNodemap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetNodemap();
			}
		});
		btnDeleteNodemap.setToolTipText("Nodemap löschen.");
		btnDeleteNodemap.setBackground(new Color(72, 77, 87));
		btnDeleteNodemap.setBounds(968, 518, 27, 27);
		btnDeleteNodemap.setIcon(GetIcon("/florianbutz/codenode/img/delete_btnicon.png", 20, 20));
		contentPane.add(btnDeleteNodemap);
		
		/*
		JSlider sNodemapViewportScale = new JSlider();
		sNodemapViewportScale.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	float scale = sNodemapViewportScale.getValue() / 100f;
		        treePanel.viewportScale = scale;
		      }
		    });
		sNodemapViewportScale.setMinimum(25);
		sNodemapViewportScale.setValue(100);
		sNodemapViewportScale.setToolTipText("Transparenz des Hintergrundmusters der Nodemap ändern.");
		sNodemapViewportScale.setMaximum(250);
		sNodemapViewportScale.setBounds(864, 518, 94, 27);
		contentPane.add(sNodemapViewportScale);
		*/
	}
	
	public static void DisplayError(String message, Object stacktrace, ErrorCode errorCode) {
        Object[] options = {"Ignorieren", "Programm Beenden"};

        JTextArea textArea = new JTextArea(stacktrace.toString());
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 75));
        
        int result = JOptionPane.showOptionDialog(
                null,
                new Object[]{message + "\n-> " + errorCode + "\n ", scrollPane},
                "Ein Fehler ist aufgetreten. Code: " + errorCode.getValue(),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]);
        
		if (result == JOptionPane.OK_OPTION) {
			return;
		} else {
		    ExitApp(-1);
		}
	}
	
	public ImageIcon GetIcon(String imgPath, int sizeX, int sizeY) {
			Image i1 = new ImageIcon(CodeNodeGUI.class.getResource(imgPath)).getImage();
			Image newimg = i1.getScaledInstance( sizeX, sizeY,  java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newimg);
			return icon;
	}
	
	public void ResetViewport() {
		treePanel.ResetViewport();
	}
	
	public void ResetNodemap() {
		treePanel.ResetGraph();
	}
	
	public static void ExitApp(int exitCode) {
		System.exit(exitCode);
	}
	
	public void OpenFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		int returnVal = fileChooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            fileChooser.getSelectedFile().getAbsolutePath());
	       
	       		String filePath= fileChooser.getSelectedFile().getAbsolutePath(); 
	       		
	       		String parsed = Main.ParseCodes(treePanel, filePath);
	       		lbTextMap.setText(parsed);
	    }
	}
}
