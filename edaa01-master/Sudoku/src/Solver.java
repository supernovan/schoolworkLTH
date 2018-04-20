public class Solver {
	private Board sb;

	public Solver(Board sb) {
		this.sb = sb;
	}

	/*
	 * Undersöker om ett givet tal kan vara på en specifik plats i matrisen.
	 * Retunerar true om den kan vara det, annars retuneras false.
	 */

	private boolean checkCell(int row, int col, int num) {
		int rowPos = (row / sb.BOXSIZE) * sb.BOXSIZE;
		int colPos = (col / sb.BOXSIZE) * sb.BOXSIZE;
		for (int i = 0; i < sb.SIZE; i++) {
			if (sb.get(row, i) == num
				|| sb.get(i, col) == num
				|| sb.get(rowPos + (i%sb.BOXSIZE), colPos + (i / sb.BOXSIZE)) == num) {
				return false;
			}
		}
		return true;
	}

	public boolean backTrackSolve(int row, int col) {
		int nextCol = (col + 1) % sb.SIZE;
		int nextRow;
		if(nextCol == 0) {
			nextRow = row + 1;
		}
		else {
			nextRow = row;
		}
		
		if(row > 8) {
			return true;
		}
		if(sb.get(row, col) != 0) {
			return backTrackSolve(nextRow, nextCol);
		}
		for (int i = 1; i < 10; i++) {
			if (checkCell(row, col, i)) {
				sb.set(row, col, i);
				if (backTrackSolve(nextRow, nextCol)) {
					return true;
				}
			}

		}
		sb.set(row, col, 0);
		return false;
	}
	public boolean checkMatrix(int[][] matrix) {
		for(int i = 0; i<9; i++) {
			for (int j = 0; j<9; j++) {
				if(compareCell(i, j, matrix[i][j])) {
					return false;
				}
//				if(compareTarget(i, j, matrix[i][j])) {
//					return false;
//				}
			}
		}
		return true;
	}
	/*
	 * Enbart till för att kolla om en specifik cell 
	 * 
	 */
	private boolean compareCell(int row,int col, int num) { 
		if(num == sb.EMPTY) {
			return false;
		}
		else if(num < sb.EMPTY  || num > 9) {
			return true;
		}
		for(int i = 0; i<9; i++) {
			if(i == row) {
				continue;
			}
			if(sb.get(i, col) == num) {
				return true;
			}
		}
		for(int i = 0; i<9; i++) {
			if(i == col) {
				continue;
			}
			if(sb.get(row, i) == num) {
				return true;
			}
		}
		for(int i = 0; i<9; i++) {
			int rowPos = (row / sb.BOXSIZE) * sb.BOXSIZE;
			int colPos = (col / sb.BOXSIZE) * sb.BOXSIZE;
			if(rowPos +  (i%sb.BOXSIZE) == row && colPos + (i / sb.BOXSIZE) == col) {
				continue;
			}
			if(sb.get(rowPos + (i%sb.BOXSIZE), colPos + (i / sb.BOXSIZE)) == num) {
				return true;
			}
		}
		return false;
	}
	public boolean compareTarget(int row, int col, int num) {
		if(num == sb.EMPTY) {
			return false;
		}
		else if(num < sb.EMPTY  || num > 9) {
			return true;
		}
		int colPos = (col / sb.BOXSIZE);
		int rowPos = (row / sb.BOXSIZE);
		for(int i = 0; i<9; i++) {
			if(i/sb.BOXSIZE == colPos) {
				continue;
			}
			if(!(checkCell(row, i, num))) {
				return true;
			}
		}
		for(int i = 0; i<9; i++) {
			if(i/sb.BOXSIZE == rowPos) {
				continue;
			}
			if(!(checkCell(i, col, num))) {
				return true;
			}
		}
		return false;
	}

}
