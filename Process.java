import java.util.ArrayList;

public class Process {
	private int number;
	private String title;
	private int arrivalTime;
	private int burstTime;
	private int burstTime2;
	private int priority;
	private static int numberSerial = 0;
	private ArrayList <Integer>timeSections;
	
	public Process () {
		numberSerial++;
		number=numberSerial;
		title="P"+number;
		burstTime=0;
		priority = 10000;
		timeSections = new ArrayList<Integer>();
	}
	
	public Process (int arrivalTime, int burstTime, int priority) {
		numberSerial++;
		this.number=numberSerial;
		this.title="P"+number;
		this.arrivalTime=arrivalTime;
		this.burstTime=burstTime;
		this.burstTime2=burstTime;
		this.priority=priority;
		timeSections = new ArrayList<Integer>();
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getBurstTime() {
		return burstTime;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setNumber (int number) {
		this.number = number;
	}
	
	public void setTitle (String title) {
		this.title=title;
	}
	
	public void setBurstTime (int burstTime) {
		this.burstTime=burstTime;
	}
	
	public int getArrivalTime() {
		return this.arrivalTime;
	}
	public void setPriority (int priority) {
		this.priority=priority;
	}
	
	public String toString() {
		return "Process[title="+this.title+", arrivalTime="+this.arrivalTime+", burstTime="+this.burstTime+", priority="+this.priority+"]";
	}
	
	public void inUse (int time) {
		timeSections.add(time);
		burstTime--;
	}
	
	public int getKohaPritjes () {
		int time=timeSections.get(0)-this.arrivalTime;
		for (int i=0; i<timeSections.size()-1;i++) {
			int dif = timeSections.get(i+1)-timeSections.get(i);
			if (dif>1)
				time+=dif-1;
		}
		return time;
	}
	
	public int getKohaQendrimit() {
		return getKohaPritjes()+this.burstTime2;
	}
}
