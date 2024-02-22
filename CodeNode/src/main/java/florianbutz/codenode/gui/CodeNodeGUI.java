package florianbutz.codenode.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import florianbutz.codenode.main.Main;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.multi.MultiScrollPaneUI;
import javax.swing.JLabel;

public class CodeNodeGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea taClassTree;

	public static void BuildWindow() {
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
		setTitle("Code Node");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 655, 399);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Datei");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Öffnen");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OpenFile();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 390, 284);
		scrollPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);
		
		taClassTree = new JTextArea();
		taClassTree.setFont(new Font("Noto Sans", Font.PLAIN, 13));
		taClassTree.setEditable(false);
		taClassTree.setText("Methoden:\r\n\r\n+ main : void\r\n\t~ list : int[]\r\n\r\n+ findMin : int\r\n\t~ indexOfMin : int\r\n\t~ i : int\r\n\r\n+ badResize : void\r\n\t~ temp : int[]\r\n\t~ limit : int\r\n\t~ i : int\r\n\r\n+ goodResize : int[]\r\n\t~ result : int[]\r\n\t~ limit : int\r\n\t~ i : int\r\n\r\n+ findAndPrintPairs : void\r\n\t~ i : int\r\n\t~ j : int\r\n\r\n+ bubblesort : void\r\n\t~ temp : int\r\n\t~ changed : boolean\r\n\t~ i : int\r\n\t~ j : int\r\n\r\n+ showList : void\r\n\t~ i : int\r\n\r\n+ isAscending : boolean\r\n\t~ ascending : boolean\r\n\t~ index : int\r\n\r\n\r\nVariablen:\r\n\r\n+ helloWorldString = \"Hello, world\" : String");
		scrollPane.setViewportView(taClassTree);
		taClassTree.setOpaque(false);
		taClassTree.setBackground(new Color(255, 255, 255, 150));
		
		JLabel lblNewLabel = new JLabel("Ausgabe:");
		lblNewLabel.setFont(new Font("Noto Sans", Font.BOLD, 15));
		lblNewLabel.setBounds(10, 11, 276, 20);
		contentPane.add(lblNewLabel);

	}
	
	public void OpenFile() {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            fileChooser.getSelectedFile().getAbsolutePath());
	       
	       		String parsed = Main.ParseCodes(fileChooser.getSelectedFile().getAbsolutePath());
	       		System.out.println(parsed);
	       		taClassTree.setText(parsed);
	    }
	}
}
