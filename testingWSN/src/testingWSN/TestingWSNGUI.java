package testingWSN;

import java.awt.EventQueue;

import jssc.SerialPort;
import jssc.SerialPortException;

import javax.swing.JFrame;
import javax.swing.JComboBox;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JPopupMenu;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

import javax.swing.JLabel;

import com.jgoodies.forms.factories.DefaultComponentFactory;

import javax.swing.UIManager;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.Color;

import net.miginfocom.swing.MigLayout;

import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.SystemColor;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollBar;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;

import javax.swing.DefaultListModel;

import jssc.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.util.Vector;

public class TestingWSNGUI {

	private TestingWSN test;
	
	private String NumOfPackages;
	private String channelStr;
	private String selectSerial;
	private String mode; 
	private String output;
	private String testMode;
	
	private Thread threadForTest;
	
	private boolean labelRXWork;
	
	private JFrame frame;
	private JPanel panel_2;

	private JRadioButton rdbtnRXButton;
	private JRadioButton rdbtnTXButton;
	private JPanel radioPanel;
	
	private JComboBox comboBox;
	private JTextField textField;
	
	private JRadioButton rdbtnShortOutputButton;
	private JRadioButton rdbtnFullOutputButton;
	private JPanel outputPanel;
	
	private JButton btnStopButton;
	private JButton btnNewButton;
	
	private JTextArea textArea;
	
	private JLabel lblNewLabel_1;
	private JLabel lblTestLabel;
	private JRadioButton rdbtnAllTestsButton;
	private JRadioButton rdbtnRSSITestButton;
	private JRadioButton rdbtnPERTestButton;
	private JRadioButton rdbtnSeqTestButton;
	private JPanel testPanel;
	private JTextField textField_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestingWSNGUI window = new TestingWSNGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestingWSNGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		labelRXWork = false;
		channelStr = "";
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Тестирование БСС");
		frame.setSize(780, 600);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(UIManager.getColor("controlHighlight"));
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel textHeader = new JLabel("Тестирование беспроводных сетей связи IEEE 802.15.4");
		textHeader.setFont(new Font("Liberation Serif", Font.BOLD, 15));
		panel.add(textHeader);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(UIManager.getColor("text"));
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JLabel botLabel = new JLabel("Санкт-Петербургский университет телекоммуникаций им.М.А.Бонч-Бруевича 2015");
		botLabel.setFont(new Font("Liberation Serif", Font.BOLD, 14));
		panel_1.add(botLabel);
		
		panel_2 = new JPanel();
		panel_2.setBackground(UIManager.getColor("windowBorder"));
		frame.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(null);
		
		
		JLabel lblSerial = new JLabel("Serial port:");
		lblSerial.setFont(new Font("Liberation Serif", Font.BOLD, 12));
		lblSerial.setBounds(12, 14, 70, 15);
		panel_2.add(lblSerial);
		
		Vector comboBoxItems=new Vector();
		for(int ix = 0; ix < jssc.SerialPortList.getPortNames().length; ++ix){
			 comboBoxItems.add(jssc.SerialPortList.getPortNames()[ix].toString());
		} 
		final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems);
		comboBox = new JComboBox(model);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(model.getSize() > 0){
					selectSerial = comboBox.getSelectedItem().toString();
				}
			}
		});
		comboBox.setBounds(83, 10, 117, 20);
		panel_2.add(comboBox);
		
		JButton btnRefresh = new JButton("Load Serial Port List");
		btnRefresh.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
				model.removeAllElements();
				for(int ix = 0; ix < jssc.SerialPortList.getPortNames().length; ++ix){
					 model.addElement(jssc.SerialPortList.getPortNames()[ix].toString());
				} 
				if(model.getSize() > 0){
					selectSerial = comboBox.getSelectedItem().toString();		
				}
			}
		});
		btnRefresh.setBounds(212, 10, 180, 20);
		panel_2.add(btnRefresh);
		
		if(model.getSize() > 0){
			selectSerial = comboBox.getSelectedItem().toString();
		}
		
		ButtonGroup bgMode = new ButtonGroup();

		rdbtnRXButton = new JRadioButton("RX mode");
		rdbtnRXButton.setMnemonic(KeyEvent.VK_R);
		rdbtnRXButton.setActionCommand("RX");
		
		rdbtnTXButton = new JRadioButton("TX mode");
		rdbtnTXButton.setMnemonic(KeyEvent.VK_T);
		rdbtnTXButton.setActionCommand("TX");
		
		bgMode.add(rdbtnRXButton);
		bgMode.add(rdbtnTXButton);
		
		rdbtnRXButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mode = rdbtnRXButton.getActionCommand();
				textField.setEditable(false);
				outputPanel.setVisible(true);
				lblNewLabel_1.setVisible(true);
			}
		});
		
		rdbtnTXButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mode = rdbtnTXButton.getActionCommand();
				textField.setEditable(true);
				outputPanel.setVisible(false);
				lblNewLabel_1.setVisible(false);
				testPanel.setVisible(false);
				lblTestLabel.setVisible(false);
			}
		});
		
		radioPanel = new JPanel(new GridLayout(0, 1));
		radioPanel.add(rdbtnRXButton);
        radioPanel.add(rdbtnTXButton);
		radioPanel.setBounds(600, 11, 90, 40);
        panel_2.add(radioPanel);
        
        JLabel lblNewLabel = new JLabel("Number of sending packages:");
        lblNewLabel.setBounds(12, 50, 240, 20);
        panel_2.add(lblNewLabel);
        
        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		NumOfPackages = textField.getText();
        		System.out.println(NumOfPackages);
        	}
        });
        textField.setBounds(245, 50, 50, 20);
        textField.setEditable(false);
        panel_2.add(textField);
        textField.setColumns(10);
        
        ButtonGroup bgOutput = new ButtonGroup();
        
        rdbtnFullOutputButton = new JRadioButton("Full output");
        rdbtnFullOutputButton.setMnemonic(KeyEvent.VK_F);
        rdbtnFullOutputButton.setActionCommand("FULL");
        
        rdbtnShortOutputButton = new JRadioButton("Short output");
        rdbtnShortOutputButton.setMnemonic(KeyEvent.VK_S);
        rdbtnShortOutputButton.setActionCommand("SHORT");
        bgOutput.add(rdbtnFullOutputButton);
        bgOutput.add(rdbtnShortOutputButton);
        
        rdbtnFullOutputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				output = rdbtnFullOutputButton.getActionCommand();
				testPanel.setVisible(false);
				lblTestLabel.setVisible(false);
			}
		});
		
        rdbtnShortOutputButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				output = rdbtnShortOutputButton.getActionCommand();
				testPanel.setVisible(true);
				lblTestLabel.setVisible(true);
			}
		});
		
		outputPanel = new JPanel(new GridLayout(0, 1));
		outputPanel.add(rdbtnFullOutputButton);
		outputPanel.add(rdbtnShortOutputButton);
		outputPanel.setBounds(125, 100, 120, 40);
        panel_2.add(outputPanel);
        
        lblNewLabel_1 = new JLabel("Output format:");
        lblNewLabel_1.setBounds(12, 100, 110, 17);
        panel_2.add(lblNewLabel_1);
        lblNewLabel_1.setVisible(false);
        outputPanel.setVisible(false);
        
        btnNewButton = new JButton("Start");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println(NumOfPackages);
        		if(selectSerial != null){
        			if(channelStr != "") {
        				ListenSerialThread.sendCommand(selectSerial, "setChannel 0x" + channelStr);
        				try{
        					Thread.sleep(200);
        				} catch(InterruptedException e) {};
        			}
        			
        			if(mode == "RX") {
        				if(output == "FULL") {	
        					test = new TestingWSN(selectSerial, mode, NumOfPackages, output, testMode);
        						
        					threadForTest = new Thread(test);
        					threadForTest.start();
        					
        					labelRXWork = true;
        					textArea.setText("Если вы хотите остановить режим чтения и прочесть результаты, нажмите\nклавишу Stop");
        					btnStopButton.setVisible(true);
        					btnNewButton.setVisible(false);
        				} else if (output == "SHORT") {
        					if(testMode == "ALL" || testMode == "RSSI" || testMode == "PER" || testMode == "SEQ")
        					{
        						test = new TestingWSN(selectSerial, mode, NumOfPackages, output, testMode);
        						
            					threadForTest = new Thread(test);
            					threadForTest.start();
            					
            					labelRXWork = true;
            					textArea.setText("Если вы хотите остановить режим чтения и прочесть результаты, нажмите клавишу Stop");
            					btnStopButton.setVisible(true);
            					btnNewButton.setVisible(false);
        					} else {
        						textArea.setText("Выберите режим тестирования");
        					}
        				} else {
        					textArea.setText("Выберите формат вывода");
        				}
        			} else if(mode == "TX") {
        				if(NumOfPackages != null) {
        					textArea.setText("Начало отправки данных");
        					btnNewButton.setVisible(false);
        					
        					test = new TestingWSN(selectSerial, mode, NumOfPackages, output, testMode);
    						
        					threadForTest = new Thread(test);
        					threadForTest.start();
        					while(true){
                				try{
                					Thread.sleep(500);
                				} catch(InterruptedException e) {};
                				if(test.getCheckEndLabel()) break;
                			}
        					textArea.setText(test.getMsg());
        					
        					btnNewButton.setVisible(true);
        					test = null;
        	        		threadForTest = null;
        				} else {
        					textArea.setText("Введите количество пакетов, которые следует отправить");
        				}
        			}else {
        				textArea.setText("Выберите режим работы модуля");
        			}
        		} else { 
        			textArea.setText("Выберите UART-порт, к которому подключен модуль");
        		}
        	}
        });
        btnNewButton.setBounds(600, 90, 90, 20);
        panel_2.add(btnNewButton);
        
        btnStopButton = new JButton("Stop");
        btnStopButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		if(labelRXWork) {
        			labelRXWork = false;
        			test.stopWork();
        			while(true){
        				try{
        					Thread.sleep(500);
        				} catch(InterruptedException e) {};
        				if(test.getCheckEndLabel()) break;
        			}
					textArea.setText(test.getMsg());
        		}
        		btnStopButton.setVisible(false);
        		btnNewButton.setVisible(true);
        		test = null;
        		threadForTest = null;
        	}
        });
        btnStopButton.setBounds(600, 120, 90, 20);
        panel_2.add(btnStopButton);
        btnStopButton.setVisible(false);
        
        textArea = new JTextArea(20, 70);
        textArea.setEditable(false);
        
        JScrollPane scroll = new JScrollPane (textArea);
        scroll.setBounds(12, 150, 750, 360);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel_2.add(scroll);
        
        lblTestLabel = new JLabel("Test:");
        lblTestLabel.setBounds(330, 50, 61, 17);
        panel_2.add(lblTestLabel);
        lblTestLabel.setVisible(false);
        
        ButtonGroup bgTest = new ButtonGroup();
        
        rdbtnAllTestsButton = new JRadioButton("All Tests");
        rdbtnAllTestsButton.setMnemonic(KeyEvent.VK_A);
        rdbtnAllTestsButton.setActionCommand("ALL");
        
        rdbtnRSSITestButton = new JRadioButton("RSSI Test");
        rdbtnRSSITestButton.setMnemonic(KeyEvent.VK_R);
        rdbtnRSSITestButton.setActionCommand("RSSI");
        
        rdbtnPERTestButton = new JRadioButton("PER Test");
        rdbtnPERTestButton.setMnemonic(KeyEvent.VK_P);
        rdbtnPERTestButton.setActionCommand("PER");
        
        rdbtnSeqTestButton = new JRadioButton("Sequence Test");
        rdbtnSeqTestButton.setMnemonic(KeyEvent.VK_SPACE);
        rdbtnSeqTestButton.setActionCommand("SEQ");
		
        rdbtnAllTestsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				testMode = rdbtnAllTestsButton.getActionCommand();
			}
		});
		
        rdbtnRSSITestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				testMode = rdbtnRSSITestButton.getActionCommand();
			}
		});
        
        rdbtnPERTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				testMode = rdbtnPERTestButton.getActionCommand();
			}
		});
        
        rdbtnSeqTestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				testMode = rdbtnSeqTestButton.getActionCommand();
			}
		});
        
        bgTest.add(rdbtnAllTestsButton);
        bgTest.add(rdbtnRSSITestButton);
        bgTest.add(rdbtnPERTestButton);
        bgTest.add(rdbtnSeqTestButton);
        
        testPanel = new JPanel(new GridLayout(0, 1));
        testPanel.add(rdbtnAllTestsButton);
        testPanel.add(rdbtnRSSITestButton);
        testPanel.add(rdbtnPERTestButton);
        testPanel.add(rdbtnSeqTestButton);
        testPanel.setBounds(375, 50, 150, 80);
        panel_2.add(testPanel);
        
        JLabel lblNewLabel_2 = new JLabel("Channel:");
        lblNewLabel_2.setBounds(420, 12, 70, 17);
        panel_2.add(lblNewLabel_2);
        
        textField_1 = new JTextField();
        textField_1.setBounds(510, 12, 50, 20);
        textField_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		int channel = Integer.valueOf(textField_1.getText());
        		if(channel < 11 || channel > 26) {
        			textArea.setText("Выберите номер канала c 11 до 26");
        		} else channelStr = Integer.toHexString(channel);
        		System.out.println(channel);
        	}
        });
        panel_2.add(textField_1);
        textField_1.setColumns(10);
        testPanel.setVisible(false);
        /*
        textArea.setText("dgysagfdsvchvdshcvhsdvcyuvsdyfcgysdatfisdatfitsdafisdhcyufas6dftegfhwefyg68cfsdaycvasutdfcs86a\ngsaydfsauydfasuFDUVASGUxvctasvctasFCUDVCvsuctasdfcysdfcdtsdafc6sdfacyvsdctsdiafcydvcds67icsdvctusdfc6sdfcysdvcusdaifcsdcsdatcifsdacvyusd\nsayuvdyuasvDTASVDfasydasyuDVYUAISVDASVDyasVDAS\nSvsyiavdasvdgasvdgcascdvasYDVASIDVUISAVDU"+
        "csdugidsgvgdsuvgdvivdviugdviobdfsuivbudfbv\ndysavduasvdyvsadcvastvdsacyvdsucds\ndydfgsyvfsdafuigsdfuigsdfgdafpuosad\ngysvdyusagdygasdfgsadgasuIDUIASOGDUIOASGD\nDSGADYGSAYDGSYGSDYhusdhvoi\nghsdgfuidsfgud\nfghfdsgdfsguidfhsgudfsghidfs\ngfidsgfsdagfgdaufguiasdgfisdagfosdagfgsdafouids\ndgasyiudgasfyuisagfsdyuisdaugsd\nfgdsyiagfsdgfgsdafyiusda"+
        "\nfsdagfidsafgsdgfsdbfjlhsdfkhsdakfjasdkf\nsadgiasgdhasvdhjvashdvgasVIDYIUAS\nfvasduygfuisdgfuisduf\ndgfsdafgsdufgsduafgsdafds\ndgfidsagfsdagfuisdafguisdaf\ndfgsdiyfgsdyfgsdagfsdagfuisdagfisd\ndsafgiysdagfysdagfsdagfuiosdagfyisdagf\ndfasduifgsdafgasdfgasdfgasdf\nggdsfigasdfsdfsduiguidfgsdfgsdaufigdsi\ndfgsdaygfsdagfisdafg\nuisdasdsadfggdf");
		*/
	}

	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
