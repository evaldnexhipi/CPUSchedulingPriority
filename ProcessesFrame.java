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
		
		//Metodat per ndertimin e Nderfaqjes Grafike
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
		//Emertimi i kolonave te JTable
		Vector columnNames = new Vector<String>();
		columnNames.add("Process Number");
		columnNames.add("Arrival Time");
		columnNames.add("Burst Time");
		columnNames.add("Priority");
		
		Vector data = new Vector<Object>();
		Vector data1 = new Vector <String>();
		//Inicializimi i JTable
		processTable = new JTable(data,columnNames);
		
		//Vendosim tabelen brenda nje JScrollPane
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
			/*Instruksionet e meposhtme ekzekutohen pas klikimit te buttonit addButton*/ 
			DefaultTableModel model = (DefaultTableModel) processTable.getModel();
			Vector newRow = new Vector <String>();
			
			//Sa here qe klikohet buttoni addButton krijojme nje proces te ri, i cili inicializohet me vlerat e inputeve perkatese
			Process p = new Process (Integer.parseInt(arrivalTimeField.getText()),Integer.parseInt(burstTimeField.getText()),Integer.parseInt(priorityField.getText()));
			//Procesi i krijuar shtohet ne ArrayList <Process> processesList
			processesList.add(p);
			
			//Bejme paraqitjen e procesit te shtuar ne JTable-in e pozicionuar ne qender
			newRow.add(p.getNumber());
			newRow.add(p.getArrivalTime());
			newRow.add(p.getBurstTime());
			newRow.add(p.getPriority());
			model.addRow(newRow);
			
		}
		/* ------------ ALGORITMI ------------- */
		else if (event.getSource()==runButton) {
			/*Instruksionet e meposhtme ekzekutohen pas klikimit te buttonit runButton */
			//inUseList do te permbaje te gjithe proceset qe kane mberritur ne kohen t ose me vone,dhe kerkojne te marrin kontrollin e CPU.
			ArrayList <Process> inUseList = new ArrayList <Process>();
			int time=0; 
			//done permban numrin e proceseve qe kane perfunduar ekzekutimin
			int done=0;
			System.out.println("---Intervali Kohor , Procesi---");
			
			/*Cikli i meposhtem do te trajtoje ne intervale prej 1 njesie
			 * situaten e proceseve ne inUseList*/ 
			for (int t=0; t<=time;t++) {
				//Cikli i meposhtem vendos ne inUseList te gjithe proceset qe kane mberritur ne kohen t 
				for (Process p : processesList) {
					if (p.getArrivalTime()==t) {
						inUseList.add(p);
					}
				}
				//Procesi korrent qe do te trajtohet do te jete i pari ne rradhen inUseList
				Process currentProcess = inUseList.get(0);
				//time (si kufi i siperm) rritet me kohen e ekzekutimit te procesit korrent
				time+=currentProcess.getBurstTime();
				//ne rradhen inUseList kontrollojme nqs gjejme nje proces me prioritet me te madh se ai korrent  
				int hPIndex = highestPriorityIndex(inUseList);
				if (hPIndex!=0) { //Nqs gjendet nje proces me prioritet me te madh se ai korrent atehere
					//Zevendosjme kete proces me indeksin hPIndex me procesin e pare ne rradhe me indeks 0
					Collections.swap(inUseList, 0, hPIndex);
					//Heqim procesin e meparshem nga rradha
					Process removedP = inUseList.remove(hPIndex);
					//Dhe e vendosim ne fund te saj
					inUseList.add(removedP);
					//Ndryshojme vleren e procesit korrent 
					currentProcess=inUseList.get(0);
				}
				//Tani mund t'i japim te drejten procesit korrent te marre kontrollin e CPU
				currentProcess.inUse(t);
				if (currentProcess.getBurstTime()==0) { //Nese procesi korrent ka perfunduar se ekzekutuari atehere
					//inkrementojme me 1 vleren e variablit done (qe mban numrin e proceseve te perfunduar)
					done++;
					//ky proces mund te largohet nga rradha e proceseve qe kerkojne te marrin kontrollin e CPU
					inUseList.remove(0);
				}
				//Pasqyrojme situaten ne kete interval
				System.out.println("Time: "+t+"-"+(t+1)+" , Process: "+currentProcess.getTitle());
				
				/* ---------- STATISTIKA ---------- */
				if (done==processesList.size()) { //Nqs ka perfunduar ekzekutimi i te gjithe proceseve atehere mund te llogarisim disa statistika
					System.out.println("\n---STATISTIKA----");
					double sumPritja = 0; //sumPritja do te permbaje shumen e koheve te pritjeve te proceseve
					double sumQendrimi = 0;//sumQendrimi do te permbaje shumen e koheve te qendrimit te proceseve 
					//shtojme vlerat e variablave te mesiperme si me poshte:
					for (Process pr : processesList) {
						sumPritja+=pr.getKohaPritjes();
						sumQendrimi+=pr.getKohaQendrimit();
						System.out.println(pr.getTitle()+" Koha e pritjes: "+pr.getKohaPritjes()+" | "+"Koha e qendrimit: "+pr.getKohaQendrimit());
					}
					//Llogarisim mesataren per secilen prej 2 variablave
					double mesPritja = sumPritja / processesList.size();
					double mesQendrimi = sumQendrimi / processesList.size();
					System.out.println("Koha mesatare e pritjes: "+mesPritja);
					System.out.println("Koha mesatare e qendrimit: "+mesQendrimi);
					break; //Mund te perfundojme ciklin pasi kemi perfunduar me trajtimin e proceseve
				}
				/* ---------- STATISTIKA ---------- */
			}		
		}
	}
	/* ------------ ALGORITMI ------------- */
	
	public int highestPriorityIndex (ArrayList <Process> list) {
		/* Algoritmi i meposhtem kthen indeksin e procesit me prioritet me te larte */
		int index = 0;
		int iter = 0;
		int min = list.get(0).getPriority();
		//Kontrollojme listen me procese
		for (Process p : list) {
			//Nese gjendet ndonje proces me prioritet me te larte atehere:
			if (p.getPriority()<min) {
				min=p.getPriority(); //vlera e min perditesohet me vleren e ketij prioriteti
				index=iter; //ruhet indeksi i ketij procesi ne variablin index
			}
			iter++;
		}
		return index;
	}
}
