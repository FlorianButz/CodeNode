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
import javax.swing.event.TreeModelListener;

import florianbutz.codenode.main.ErrorCode;
import florianbutz.codenode.main.Main;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.validator.language_level_validations.chunks.NoUnderscoresInIntegerLiteralsValidator;

import java.awt.Font;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;

public class CodeNodeGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lbTextMap;
	private TreePanel treePanel;
	private JSlider sNodemapTransparency;

	public static CodeNodeGUI instance;
	
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
		instance = this;

		setBackground(new Color(34, 40, 49));
		setTitle("Code Node - v" + Main.softwareVersion);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1063, 617);
		
		setMinimumSize(new Dimension(1078, 617));
		
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
				AddFile();
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
		
		JMenu mnNewMenu_1 = new JMenu("Info");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Version");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        JOptionPane.showMessageDialog(
		                null,
		                "Dieses Programm wurde als Schulprojekt von Florian Butz erstellt. \nSoftware Version: " + Main.softwareVersion,
		                "Version",
		                JOptionPane.INFORMATION_MESSAGE
		                );
			}
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);
		
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		contentPane.setBackground(new Color(60, 63, 65));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(new Color(57, 62, 70));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(35);
		
		lbTextMap = new JLabel("");
		lbTextMap.setVerticalAlignment(SwingConstants.TOP);
		lbTextMap.setForeground(new Color(221, 221, 221));
		lbTextMap.setBorder(new EmptyBorder(10, 10, 10, 10));
		lbTextMap.setFont(new Font("Noto Sans", Font.PLAIN, 14));
		lbTextMap.setBackground(new Color(72, 77, 87));
		scrollPane.setViewportView(lbTextMap);
		
		JLabel lblNewLabel = new JLabel("Text Struktur");
		lblNewLabel.setForeground(new Color(221, 221, 221));
		lblNewLabel.setFont(new Font("Noto Sans", Font.BOLD, 15));
		
		treePanel = new TreePanel();
		treePanel.nodeBackgroundColor = new Color(72, 77, 87);
		treePanel.connectionColor = new Color(72, 77, 87);
		treePanel.backgroundColor = new Color(48, 51, 58);
		
		CodeNode welcomeNode = new CodeNode("Wilkommen", 350, 240, 225, 75, null, "@nKlicke auf 'Datei öffnen' um zu starten.");
		treePanel.AddNode(welcomeNode);
		
		lblNodemap = new JLabel("Nodemap");
		lblNodemap.setForeground(new Color(221, 221, 221));
		lblNodemap.setFont(new Font("Noto Sans", Font.BOLD, 15));
		

		JCheckBoxMenuItem chckbxmntmNewCheckItem = new JCheckBoxMenuItem("Light Mode");
		chckbxmntmNewCheckItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxmntmNewCheckItem.isSelected()) {
					FlatLightLaf.setup();
					treePanel.backgroundColor = new Color(225, 225, 225);
					instance.setVisible(false);
					SwingUtilities.updateComponentTreeUI(instance);
					instance.setVisible(true);

				}else {
					FlatDarkLaf.setup();
					treePanel.backgroundColor = new Color(72, 76, 87);
					instance.setVisible(false);
					SwingUtilities.updateComponentTreeUI(instance);
					instance.setVisible(true);
				}
			}
		});
		mnNewMenu_1.add(chckbxmntmNewCheckItem);
		
		JButton btnResetViewPosition = new JButton("");
		btnResetViewPosition.setToolTipText("Kameraposition in der Nodemap zurrücksetzen.");
		btnResetViewPosition.setIcon(GetIcon("/florianbutz/codenode/img/reset_btnicon.png", 20, 20));
		btnResetViewPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetViewport();
			}
		});
		btnResetViewPosition.setBackground(new Color(72, 77, 87));
		
		JButton btnBGPattern1 = new JButton("");
		btnBGPattern1.setToolTipText("Hintergrundmuster der Nodemap ändern.");
		btnBGPattern1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.ChangePattern(0);
			}
		});
		btnBGPattern1.setIcon(GetIcon("/florianbutz/codenode/img/pattern1_btnicon.png", 15, 15));
		btnBGPattern1.setBackground(new Color(72, 77, 87));
		
		JButton btnBGPattern2 = new JButton("");
		btnBGPattern2.setToolTipText("Hintergrundmuster der Nodemap ändern.");
		btnBGPattern2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.ChangePattern(1);
			}
		});
		btnBGPattern2.setIcon(GetIcon("/florianbutz/codenode/img/pattern2_btnicon.png", 15, 15));
		btnBGPattern2.setBackground(new Color(72, 77, 87));
		
		JButton btnBGPattern3 = new JButton("");
		btnBGPattern3.setToolTipText("Hintergrundmuster der Nodemap ändern.");
		btnBGPattern3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.ChangePattern(2);
			}
		});
		 
		btnBGPattern3.setIcon(GetIcon("/florianbutz/codenode/img/pattern3_btnicon.png", 15, 15));
		btnBGPattern3.setBackground(new Color(72, 77, 87));
		
		sNodemapTransparency = new JSlider();
		sNodemapTransparency.setMaximum(50);
		sNodemapTransparency.setValue(15);
		sNodemapTransparency.setToolTipText("Transparenz des Hintergrundmusters der Nodemap ändern.");
		sNodemapTransparency.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	float transparency = sNodemapTransparency.getValue() / 100f;
		        treePanel.backgroundTransparency = transparency;
		      }
		    });
		
		JButton btnDeleteNodemap = new JButton("");
		btnDeleteNodemap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetNodemap();
			}
		});
		btnDeleteNodemap.setToolTipText("Nodemap löschen.");
		btnDeleteNodemap.setBackground(new Color(72, 77, 87));
		btnDeleteNodemap.setIcon(GetIcon("/florianbutz/codenode/img/delete_btnicon.png", 20, 20));
		
		btnRefreshFile = new JButton("");
		btnRefreshFile.setEnabled(false);
		btnRefreshFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RefreshOpenFile();
			}
		});
		btnRefreshFile.setToolTipText("Datei erneut laden (Dies kann Änderungen verwerfen!)");
		btnRefreshFile.setIcon(GetIcon("/florianbutz/codenode/img/reset_btnicon.png", 20, 20));
		btnRefreshFile.setBackground(new Color(72, 77, 87));
		
		JSeparator separator = new JSeparator();
		
		btnCopyTextStructure = new JButton("");
		btnCopyTextStructure.setEnabled(false);
		btnCopyTextStructure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.CopyStructTextToClipboard(lbTextMap.getText());
				JOptionPane.showMessageDialog(
		                null,
		                "Textstruktur wurde in die Zwischenablage kopiert.",
		                "Information",
		                JOptionPane.INFORMATION_MESSAGE
		                );
			}
		});
		btnCopyTextStructure.setOpaque(false);
		btnCopyTextStructure.setIcon(GetIcon("/florianbutz/codenode/img/copy_btnicon.png", 15, 15));
		btnCopyTextStructure.setToolTipText("Textstruktur kopieren.");
		btnCopyTextStructure.setBackground(new Color(72, 77, 87));
		
		JPanel textStructPanel = new JPanel();
		textStructPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		textStructPanel.setPreferredSize(new Dimension(10, 35));
		textStructPanel.setBounds(10, 178, 316, 367);
		textStructPanel.setOpaque(false);
		
		JPanel nodesPanel = new JPanel();
		nodesPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
		nodesPanel.setBounds(338, 11, 710, 534);
		nodesPanel.setOpaque(false);
		GroupLayout gl_textStructPanel = new GroupLayout(textStructPanel);
		gl_textStructPanel.setHorizontalGroup(
			gl_textStructPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_textStructPanel.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
					.addComponent(btnCopyTextStructure, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
		);
		gl_textStructPanel.setVerticalGroup(
			gl_textStructPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_textStructPanel.createSequentialGroup()
					.addGap(11)
					.addGroup(gl_textStructPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnCopyTextStructure, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
		);
		textStructPanel.setLayout(gl_textStructPanel);
		
		JPanel fileTreePanel = new JPanel();
		fileTreePanel.setBorder(new EmptyBorder(0, 0, 15, 0));
		fileTreePanel.setPreferredSize(new Dimension(10, 45));
		fileTreePanel.setBounds(10, 11, 316, 149);
		fileTreePanel.setOpaque(false);
		GroupLayout gl_nodesPanel = new GroupLayout(nodesPanel);
		gl_nodesPanel.setHorizontalGroup(
			gl_nodesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nodesPanel.createSequentialGroup()
					.addGroup(gl_nodesPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_nodesPanel.createSequentialGroup()
							.addComponent(lblNodemap, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
							.addGap(462)
							.addComponent(sNodemapTransparency, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(btnBGPattern3, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(btnBGPattern2, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnBGPattern1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
						.addComponent(treePanel, GroupLayout.DEFAULT_SIZE, 836, Short.MAX_VALUE)
						.addGroup(gl_nodesPanel.createSequentialGroup()
							.addGap(0)
							.addComponent(btnRefreshFile, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 735, Short.MAX_VALUE)
							.addComponent(btnDeleteNodemap)
							.addGap(4)
							.addComponent(btnResetViewPosition, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
					.addGap(1))
		);
		gl_nodesPanel.setVerticalGroup(
			gl_nodesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_nodesPanel.createSequentialGroup()
					.addGroup(gl_nodesPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNodemap, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(sNodemapTransparency, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBGPattern3, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBGPattern2, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBGPattern1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addComponent(treePanel, GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
					.addGap(22)
					.addGroup(gl_nodesPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnRefreshFile, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDeleteNodemap, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnResetViewPosition, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
		);
		nodesPanel.setLayout(gl_nodesPanel);
		
		JLabel lblGeoeffnete = new JLabel("Geöffnete Dateien");
		lblGeoeffnete.setForeground(new Color(221, 221, 221));
		lblGeoeffnete.setFont(new Font("Noto Sans", Font.BOLD, 15));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JButton btnAddFile = new JButton("");
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddFile();
			}
		});
		btnAddFile.setToolTipText("Datei hinzufügen.");
		btnAddFile.setIcon(GetIcon("/florianbutz/codenode/img/add_btnicon.png", 10, 10));
		btnAddFile.setBackground(new Color(72, 77, 87));
		GroupLayout gl_fileTreePanel = new GroupLayout(fileTreePanel);
		gl_fileTreePanel.setHorizontalGroup(
			gl_fileTreePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fileTreePanel.createSequentialGroup()
					.addComponent(lblGeoeffnete, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddFile, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
		);
		gl_fileTreePanel.setVerticalGroup(
			gl_fileTreePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_fileTreePanel.createSequentialGroup()
					.addGroup(gl_fileTreePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGeoeffnete, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddFile, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
		);
		
		tFileTree = new JTree();
		tFileTree.setShowsRootHandles(true);
		tFileTree.setTransferHandler(new FileDropHandler(this));
        
		tFileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {

				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tFileTree.getLastSelectedPathComponent();
                int selectedIndex = getSelectedIndex(tFileTree) - 1;
				
                if (selectedNode != null && selectedNode.isLeaf()) {
                	OpenFile(fileTree.get(selectedIndex).getAbsolutePath());
                }
                
			}
			
			private int getSelectedIndex(JTree tree) {
		        int[] selectedRows = tree.getSelectionRows();
		        if (selectedRows != null && selectedRows.length > 0) {
		            // Assuming single selection, return the first selected row
		            return selectedRows[0];
		        }
		        return -1; // No selection
		    }
		});
		tFileTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Dateien") {
				{
				}
			}
		));
		scrollPane_1.setViewportView(tFileTree);
		fileTreePanel.setLayout(gl_fileTreePanel);
		
		JSplitPane splitPaneLeft = new JSplitPane();
		splitPaneLeft.setBorder(new EmptyBorder(0, 0, 0, 15));
		splitPaneLeft.setOneTouchExpandable(true);
		splitPaneLeft.setTopComponent(fileTreePanel);
		splitPaneLeft.setBottomComponent(textStructPanel);
		splitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneLeft.setResizeWeight(0.2);
		splitPaneLeft.setBounds(10, 11, 316, 534);
		
		JSplitPane splitPaneFullScreen = new JSplitPane();
		splitPaneFullScreen.setOneTouchExpandable(true);
		splitPaneFullScreen.setTopComponent(splitPaneLeft);
		splitPaneFullScreen.setBottomComponent(nodesPanel);
		splitPaneFullScreen.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneFullScreen.setResizeWeight(0.5);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(327)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addComponent(splitPaneFullScreen, GroupLayout.DEFAULT_SIZE, 1038, Short.MAX_VALUE)
					.addGap(9))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(11)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 524, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(6)
					.addComponent(splitPaneFullScreen, GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
					.addGap(6))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public static void DisplayError(String message, Object stacktrace, ErrorCode errorCode) {
        Object[] options = {"Programm Beenden", "Ignorieren"};

        int result = 0;
        
        if(stacktrace != null) {
        	JTextArea textArea = new JTextArea(stacktrace.toString());
        	textArea.setEditable(false);

        	JScrollPane scrollPane = new JScrollPane(textArea);
        	scrollPane.setPreferredSize(new Dimension(450, 75));
        	
        	result = JOptionPane.showOptionDialog(
                    null,
                    new Object[]{message + "\n-> " + errorCode + "\n ", scrollPane},
                    "Ein Fehler ist aufgetreten. Code: " + errorCode.getValue(),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]);
        }else {
        	result = JOptionPane.showOptionDialog(
                    null,
                    new Object[]{message + "\n-> " + errorCode + "\n "},
                    "Ein Fehler ist aufgetreten. Code: " + errorCode.getValue(),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]);
		}
        
		if (result == JOptionPane.OK_OPTION) {
			ExitApp(-1);
		} else {
			return;
		}
	}
	
	public static boolean DisplayQuestion(String title, String message, int messageType, boolean isNoDefault) {
		Object[] options = {};
		if(!isNoDefault)
        	options = new Object[] {"Ja", "Nein"};
		else
        	options = new Object[] {"Nein", "Ja"};

        int result = JOptionPane.showOptionDialog(
                null,
                message,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                messageType,
                null,
                options,
                options[0]);
        
		if (result == JOptionPane.OK_OPTION) {
			if(isNoDefault) return false;
			return true;
		} else {
			if(isNoDefault) return true;
			return false;
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
		if(DisplayQuestion("Warnung", "Wollen sie wirklich alles löschen?\nNicht gespeicherte daten gehen dadurch verloren!", JOptionPane.WARNING_MESSAGE, false))
		{
			treePanel.ResetGraph();
			lbTextMap.setText("");

       		btnRefreshFile.setEnabled(false);
       		btnCopyTextStructure.setEnabled(false);
       		lblNodemap.setText("Nodemap");
		}
	}
	
	public static void ExitApp(int exitCode) {
		System.exit(exitCode);
	}
	
	private String lastFilePath = "";
	private JButton btnRefreshFile;
	private JButton btnCopyTextStructure;
	
	private String currentOpenFile = ""; 
	
	public void OpenFile(String filePath) {	
   		if(currentOpenFile.equals(filePath)) return;
		String parsed = Main.ParseCodes(treePanel, filePath);
   		lbTextMap.setText(parsed);
   		
   		lastFilePath = filePath;
   		btnRefreshFile.setEnabled(true);
   		btnCopyTextStructure.setEnabled(true);
   		lblNodemap.setText("Nodemap: " + Path.of(filePath).getFileName());
   		currentOpenFile = filePath;
	}
	
	public void RefreshOpenFile() {
		if(lastFilePath == "") {
			DisplayError("Datei konnte nicht erneut geladen werden.", "", ErrorCode.PathNotSet);
       		btnRefreshFile.setEnabled(false);
			return;
		}

		if(DisplayQuestion("Warnung", "Wollen sie die Datei wirklich erneut laden?\nNicht gespeicherte daten gehen dadurch verloren!", JOptionPane.WARNING_MESSAGE, true))
		{
			String filePath= lastFilePath; 
			String parsed = Main.ParseCodes(treePanel, filePath);
			lbTextMap.setText(parsed);
		}
	}
	
	private List<File> fileTree = new ArrayList<File>();
	private JTree tFileTree;
	private JLabel lblNodemap;
	
	public void AddFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		
		int returnVal = fileChooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();

			ParseResult<CompilationUnit> result;
			JavaParser parser = new JavaParser();
			for (File file : files) {
				if(fileTree.contains(file)) continue;
				
				try {
		            String javaCode = Files.readString(Path.of(file.getAbsolutePath()));
		            result = parser.parse(javaCode);
					
					if(result.isSuccessful())
						fileTree.add(file);
					else
			        	DisplayError("Datei beinhaltet keinen oder fehlerhaften Java code.", null, ErrorCode.ParsingError);
		            
		        } catch (IOException e) {
		        	DisplayError("Datei konnte nicht gelesen werden.", e.getLocalizedMessage(), ErrorCode.FileLoadFaliure);
		        }
			}

			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Dateien");
			DefaultTreeModel treeModel = new DefaultTreeModel(root);

			for (File file : fileTree) {
				DefaultMutableTreeNode fileChild = new DefaultMutableTreeNode(file.getName());
				root.add(fileChild);
			}

			tFileTree.setModel(treeModel);
		}
	}
	
	public void AddFile(String path) {
			ParseResult<CompilationUnit> result;
			JavaParser parser = new JavaParser();

			File file = new File(path);
			if(fileTree.contains(file)) return;
			
			try {
				String javaCode = Files.readString(Path.of(file.getAbsolutePath()));
				result = parser.parse(javaCode);

				if(result.isSuccessful())
					fileTree.add(file);
				else
					DisplayError("Datei beinhaltet keinen oder fehlerhaften Java code.", null, ErrorCode.ParsingError);

			} catch (IOException e) {
				DisplayError("Datei konnte nicht gelesen werden.", e.getLocalizedMessage(), ErrorCode.FileLoadFaliure);
			}


			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Dateien");
			DefaultTreeModel treeModel = new DefaultTreeModel(root);

			for (File file1 : fileTree) {
				DefaultMutableTreeNode fileChild = new DefaultMutableTreeNode(file1.getName());
				root.add(fileChild);
			}

			tFileTree.setModel(treeModel);
	
	}
}

class FileDropHandler extends TransferHandler {

	CodeNodeGUI gui;
	public FileDropHandler(CodeNodeGUI gui) {
		this.gui = gui;
	}
	
    @Override
    public boolean canImport(TransferSupport support) {
        // Check if the transfer involves dropping a file
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        Transferable transferable = support.getTransferable();

        try {
            // Get the list of dropped files
            List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

            // Process the dropped files
            for (File file : fileList) {
            	gui.AddFile(file.getAbsolutePath());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}