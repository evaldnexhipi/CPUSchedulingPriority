import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ProcessesFrame extends JFrame implements ActionListener{
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	
	private final int FRAME_WIDTH=1000;
	private final int FRAME_HEIGHT=600;
	
	private JLabel arrivalTimeLabel;
	private JLabel burstTimeLabel;
	private JLabel priorityLabel;
	private JButton runButton;
	
	private JTextField arrivalTimeField;
	private JTextField burstTimeField;
	private JTextField priorityField;
	private JButton addButton;

	private JTable processTable;
	private ArrayList<Process> processesList;
	
	public ProcessesFrame () {
		setTitle("Skedulimi me Prioritet Ndalues");
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		processesList= new ArrayList <Process>();
		
		createNorthPanel();
		createCenterPanel();
		createSouthPanel();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void createNorthPanel() {
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(2,4));
			
		arrivalTimeLabel = new JLabel ("Process Arival Time");
		burstTimeLabel = new JLabel ("Process Burst Time");
		priorityLabel = new JLabel("Process Priority");
		runButton = new JButton ("Run Algorithm");
		runButton.addActionListener(this);
		
		arrivalTimeField = new JTextField ("");
		burstTimeField = new JTextField ("");
		priorityField = new JTextField("");
		addButton = new JButton("Add Process");
		addButton.addActionListener(this);
		
		northPanel.add(arrivalTimeLabel);
		northPanel.add(burstTimeLabel);
		northPanel.add(priorityLabel);
		northPanel.add(runButton);
		
		northPanel.add(arrivalTimeField);
		northPanel.add(burstTimeField);
		northPanel.add(priorityField);
		northPanel.add(addButton);
		add(northPanel,BorderLayout.NORTH);
	}
	
	private void createCenterPanel() {
		centerPanel = new JPanel ();
		Vector columnNames = new Vector<String>();
		columnNames.add("Process Number");
		columnNames.add("Arrival Time");
		columnNames.add("Burst Time");
		columnNames.add("Priority");
		
		Vector data = new Vector<Object>();
		Vector data1 = new Vector <String>();
		processTable = new JTable(data,columnNames);
		
		JScrollPane scrollPane = new JScrollPane(processTable);
		processTable.setFillsViewportHeight(true);
		
		centerPanel.add(scrollPane);
		add(centerPanel,BorderLayout.CENTER);
	}
	
	private void createSouthPanel() {
		southPanel = new JPanel();
		southPanel.add(new JLabel("Evald Nexhipi"));
		add(southPanel,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource()==addButton) {
			DefaultTableModel model = (DefaultTableModel) processTable.getModel();
			Vector newRow = new Vector <String>();
			
			Process p = new Process (Integer.parseInt(arrivalTimeField.getText()),Integer.parseInt(burstTimeField.getText()),Integer.parseInt(priorityField.getText()));
			processesList.add(p);
			
			newRow.add(p.getNumber());
			newRow.add(p.getArrivalTime());
			newRow.add(p.getBurstTime());
			newRow.add(p.getPriority());
			model.addRow(newRow);
			
		}
		/* ------------ ALGORITMI ------------- */
		else if (event.getSource()==runButton) {
			ArrayList <Process> inUseList = new ArrayList <Process>();
			int time=0;
			int done=0;
			System.out.println("---Intervali Kohor , Procesi---");
			
			for (int t=0; t<=time;t++) {
				for (Process p : processesList) {
					if (p.getArrivalTime()==t) {
						inUseList.add(p);
					}
				}

				Process currentProcess = inUseList.get(0);
				time+=currentProcess.getBurstTime();
				 
				int hPIndex = highestPriorityIndex(inUseList);
				if (hPIndex!=0) {
					Collections.swap(inUseList, 0, hPIndex);
					Process removedP = inUseList.remove(hPIndex);
					inUseList.add(removedP);
					currentProcess=inUseList.get(0);
				}
				
				currentProcess.inUse(t);
				if (currentProcess.getBurstTime()==0) {
					done++;
					inUseList.remove(0);
				}
				System.out.println("Time: "+t+"-"+(t+1)+" , Process: "+currentProcess.getTitle());
				
				/* ---------- STATISTIKA ---------- */
				if (done==processesList.size()) {
					
					System.out.println("\n---STATISTIKA----");
					double sumPritja = 0; double sumQendrimi = 0;
					
					for (Process pr : processesList) {
						sumPritja+=pr.getKohaPritjes();
						sumQendrimi+=pr.getKohaQendrimit();
						System.out.println(pr.getTitle()+" Koha e pritjes: "+pr.getKohaPritjes()+" | "+"Koha e qendrimit: "+pr.getKohaQendrimit());
					}
					
					double mesPritja = sumPritja / processesList.size();
					double mesQendrimi = sumQendrimi / processesList.size();
					System.out.println("Koha mesatare e pritjes: "+mesPritja);
					System.out.println("Koha mesatare e qendrimit: "+mesQendrimi);
					break;
				}
				/* ---------- STATISTIKA ---------- */
			}		
		}
	}
	/* ------------ ALGORITMI ------------- */
	
	public int highestPriorityIndex (ArrayList <Process> list) {
		int index = 0;
		int iter = 0;
		int min = list.get(0).getPriority();
		
		for (Process p : list) {
			if (p.getPriority()<min) {
				min=p.getPriority();
				index=iter;
			}
			iter++;
		}
		return index;
	}
}
