import java.util.ArrayList;

public class Process {
	private int number;
	private String title;
	private int arrivalTime;
	private int burstTime; //permban kohen e ekzekutimit te procesit e cila mund te ndryshoje gjate ekzekutimit te algoritmit
	private int burstTime2;//permban kohen e ekzekutimit te procesit e cila nuk do te ndryshohet por do te mbahet fikse per qellime statistikore
	private int priority;
	private static int numberSerial = 0; //variabel statitike qe do te na ndihmoje ne gjenerimin e nje numri unik identifikues per proceset e krijuar
 	private ArrayList <Integer>timeSections;//kjo strukture te dhenash do te ruaje seksionet kohore kur procesi ka qene ne perdorim (per qellime statistikore)
	
	public Process () {
		numberSerial++;
		number=numberSerial;
		title="P"+number;
		burstTime=0;
		priority = 10000;
		timeSections = new ArrayList<Integer>();
	}
	
	public Process (int arrivalTime, int burstTime, int priority) {
		numberSerial++; //gjenerimi i nje numri unik per cdo proces te krijuar
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
	
	public ArrayList <Integer> getTimeSections(){
		return this.timeSections;
	}
	
	public String toString() {
		return "Process[title="+this.title+", arrivalTime="+this.arrivalTime+", burstTime="+this.burstTime+", priority="+this.priority+"]";
	}
	
	public void inUse (int time) {
		/* Nqs nje proces ka marre kontrollin e CPU */
		burstTime--; //Reduktojme kohen e ekzekutimit te procesit
		timeSections.add(time); //vendosim intervalin kohor kur procesi eshte ne ekzekutim ne timeSections
	}
	
	public int getKohaPritjes () {
		//Koha e pritjes fillimisht per nje proces eshte diferenca e momentit te pare qe ai merr kontrollin e CPU me kohen e mberritjes se tij
		int time=timeSections.get(0)-this.arrivalTime;
		//Per secilin prej seksioneve kohore
		for (int i=0; i<timeSections.size()-1;i++) {
			/*Nqs diferenca e seksioneve kohore (te njepasnjeshem) eshte me e madhe se 1 (pra ai nuk eshte ne ekzekutim por ne pritje)
			  kjo diference i shtohet kohes se pritjes.
			*/
			int dif = timeSections.get(i+1)-timeSections.get(i);
			if (dif>1)
				time+=dif-1;
		}
		return time;
	}
	
	public int getKohaQendrimit() {
		//Koha e Qendrimit te procesit do te llogaritet si me poshte:
		return getKohaPritjes()+this.burstTime2;
	}
}
