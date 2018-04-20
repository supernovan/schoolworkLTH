public class SudokuTester {
	public static void main(String [] args) {
		int[][] matrix = new int[9][9];
		matrix[6][6] = 1;
		matrix[6][7] = 2;
		matrix[6][8] = 3;
		matrix[7][6] = 4;
		matrix[7][7] = 5;
		matrix[7][8] = 6;
		matrix[8][5] = 7;
		Board sb = new Board(matrix);
		Solver solv = new Solver(sb);
		
		if (solv.checkMatrix(matrix)) {
			solv.backTrackSolve(0,0);	
		}
		for (int i = 0; i < 9; i++) {
		    for (int j = 0; j < 9; j++) {
		        System.out.print(matrix[i][j] + " ");
		    }
		    System.out.print("\n");
		}
	}
}
