package restaurant;

public class Table{
	int size;
	boolean open;
	Party party;
	Servers server;
	public Table(int i){
		size=i;
		open=true;
		server=null;
	}
}