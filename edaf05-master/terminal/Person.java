public class Person {
	int index;
	int[] pref;
	int nbr;
	int size;
	boolean status;
	Person man;
	
	public Person(int nbr ,int size) {
		status = true;
		index = 0;
		this.size = size;
		this.nbr = nbr-1;
		pref = new int[size];
	}
	public void setPref(int person) {
		pref[index] = person-1;
		index++;
		if (index == size) {
			index = 0;
		}
	}
	public boolean getPref(int index) {
		int currIn = man.getNbr();
		for (int i = 0; i<pref.length; i++) {
			if (pref[i] == currIn) {
				return false;
			} else if (pref[i] == index) {
				return true;
			}
		}
		return false;
	}
	
	public int askW() {
		index++;
		return pref[index-1];
	}
	
	public boolean alone() {
		if (index == pref.length) {
			return true;
		}
		return false;
	}
	public boolean isFree() {
		return status;
	}
	
	public void setPar(Person man) {
		this.man = man;
		status = false;
	}
	
	public Person curMan() {
		return man;
	}
	
	public int getNbr() {
		return nbr;	
	}
}