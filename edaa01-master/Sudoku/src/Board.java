public class Board {
	private int[][] grid;
	public final int BOXSIZE = 3;
	public static final int SIZE = 9;
	public final int EMPTY = 0;
	public Board(int[][] grid) {
		this.grid = grid;
		
	}
	public void set(int row, int col, int nbr) {
		grid[row][col] = nbr;
	}
	public int get(int row, int col) {
		return grid[row][col];
	}
	public boolean isValidIndex(int row,int col) { //undersöker om det är utanför matrisen.
		if(row < 0 || row > 8 || col < 0 || col >8) {
			return false;
		}
		return true;
	}
	public int[][] getGrid() {
		return grid;
	}
	public String nbrToString(int row, int col) {
		return "" + grid[row][col];
	}
}