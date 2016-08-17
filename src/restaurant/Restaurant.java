package restaurant;
import java.util.*;
public class Restaurant{
	List<Table> tables;
	List<Servers> servers;
	int turn=1;
	int totalServers=0, activeTables=0;
	double cashRegister = 0;
	public Restaurant(){
		tables= new ArrayList<Table>();
		servers = new ArrayList<Servers>();
	}
}