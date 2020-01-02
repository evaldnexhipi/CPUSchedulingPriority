import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
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
	private JLabel processNumberLabel;
	private JLabel arrivalTimeLabel;
	private JLabel burstTimeLabel;
	private JLabel priorityLabel;
	private JButton runButton;
	
	private JTextField processNumberField;
	private JTextField arrivalTimeField;
	private JTextField burstTimeField;
	private JTextField priorityField;
	private JButton addButton;

	private JTable processTable;
	private static int count = 0;
	private ArrayList<Process> processesList;
	private ArrayList <Process> statisticsArray;
	
	
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
		northPanel.setLayout(new GridLayout(2,5));
		
		processNumberLabel = new JLabel("Process Number");
		arrivalTimeLabel = new JLabel ("Process Arival Time");
		burstTimeLabel = new JLabel ("Process Burst Time");
		priorityLabel = new JLabel("Process Priority");
		runButton = new JButton ("Run Algorithm");
		runButton.addActionListener(this);
		
		processNumberField = new JTextField("");
		arrivalTimeField = new JTextField ("");
		burstTimeField = new JTextField ("");
		priorityField = new JTextField("");
		addButton = new JButton("Add Process");
		addButton.addActionListener(this);
		
		northPanel.add(processNumberLabel);
		northPanel.add(arrivalTimeLabel);
		northPanel.add(burstTimeLabel);
		northPanel.add(priorityLabel);
		northPanel.add(runButton);
		
		northPanel.add(processNumberField);
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
		//String [] columnNames = {"Process Number", "Arrival Time", "Burst Time","Priority"};
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
		
		add(southPanel,BorderLayout.SOUTH);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource()==addButton) {
			DefaultTableModel model = (DefaultTableModel) processTable.getModel();
			Vector newRow = new Vector <String>();
			count++;
			Process p = new Process (count,Integer.parseInt(arrivalTimeField.getText()),Integer.parseInt(burstTimeField.getText()),Integer.parseInt(priorityField.getText()));
			processesList.add(p);
			newRow.add(p.getNumber());
			newRow.add(p.getArrivalTime());
			newRow.add(p.getBurstTime());
			newRow.add(p.getPriority());
			model.addRow(newRow);
			//model.removeRow(0);
		}
		else if (event.getSource()==runButton) {
			statisticsArray = new ArrayList<Process>();
			
			int time = processesList.get(0).getBurstTime();
			for (int t=0; t<=time;t++) {
				Process currentProcess = processesList.get(0);
				time+=currentProcess.getBurstTime();
				int hPIndex = highestPriorityIndex(processesList,t);
				if (hPIndex!=0) {
					Collections.swap(processesList, 0, hPIndex);
					Process removedP = processesList.remove(hPIndex);
					processesList.add(removedP);
					currentProcess=processesList.get(0);
				}
				
				currentProcess.inUse(t);
				if (currentProcess.getBurstTime()==0) {
					Process process = processesList.remove(0);
					statisticsArray.add(process);
				}
				
				System.out.println("Time: "+t+"-"+(t+1)+" , Process: "+currentProcess.getTitle());
				if (processesList.size()==0) {
					System.out.println("\n---STATISTICS----");
					double sumPritja = 0; double sumQendrimi = 0;
					sortArray(statisticsArray);
					for (Process pr : statisticsArray) {
						sumPritja+=pr.getKohaPritjes();
						sumQendrimi+=pr.getKohaQendrimit();
						System.out.println(pr.getTitle()+" Koha e pritjes: "+pr.getKohaPritjes()+" | "+"Koha e qendrimit: "+pr.getKohaQendrimit());
					}
					double mesPritja = sumPritja / statisticsArray.size();
					double mesQendrimi = sumQendrimi / statisticsArray.size();
					System.out.println("Koha mesatare e pritjes: "+mesPritja);
					System.out.println("Koha mesatare e qendrimit: "+mesQendrimi);
					break;
				}
			}		
		}
	}
	
	public int highestPriorityIndex (ArrayList <Process> list,int time) {
		int index = 0;
		int iter = -1;
		int min = list.get(0).getPriority();
		
		for (Process p : list) {
			iter++;
			if (p.getPriority()<min && p.getArrivalTime()<=time) {
				min=p.getPriority();
				index=iter;
			}
		}
		return index;
	}
	
	public void sortArray (ArrayList <Process> array) {
		for (int i=0; i<array.size();i++) {
			for (int j=i+1; j<array.size(); j++) {
				if (array.get(j).getNumber()<array.get(i).getNumber()) {
					Collections.swap(array, i, j);
				}
			}
		}
	}
}

