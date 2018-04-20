import random
import sys
import math
import time

EMPTY = 0
WHITE = 1
BLACK = -1

#Draws the board
def drawBoard(board):
	hline = "  +---+---+---+---+---+---+---+---+"
	print("    a   b   c   d   e   f   g   h")
	print(hline)
	for i in range(1,9):
		print(str(i) +  " |", end="")
		for j in range(1,9):
			temp = board[i][j]
			if temp == 0:
				print("   |", end="")
			elif temp == 1:
				print(" O |", end="")
			elif temp == 2:
				print(" S |", end="")
			else:
				print(" X |", end="")

		print("")

	print(hline)

#Returns a list of all available moves
def showMoves(board, player):
	moves = []
	for i in range(1,9):
		for j in range(1,9):
			if board[i][j] == 0:
				if checkMove(board, player, i, j) == True:
					moves.append((i - 1)*8 + j)
	return moves

#Checks wether a move is okay for that player
def checkMove(board, player, x, y):
	for i in range(x-1, x+2):
		for j in range(y-1, y+2):
			if i == x and j == y or not(validTile(i,j)):
				continue

			if board[i][j] == player:
				continue

			if board[i][j] == -player:
				vec = checkMoveDirection(board, player, x, y, i-x, j-y)
				if vec[0] == True:
					return True
	return False

#Checks a move and put player on that tile and flips everything that should be flipped
#returns number of tiles that got swaped
def checkMoveAndFlip(board, player, x, y):
	if board[x][y] != EMPTY:
		return 0
	check = 0
	for i in range(x-1, x+2):
		for j in range(y-1, y+2):
			if i == x and j == y or not(validTile(i,j)):
				continue

			if board[i][j] == player:
				continue

			if board[i][j] == -player:
				vec = checkMoveDirection(board, player, x, y, i-x, j-y)
				if vec[0] == True:
					check = True
					for k in range(0,int(vec[1])):
						dx = int((i-x)*k)
						dy = int((j-y)*k)
						board[x +  dx][y + dy] = player
						check += 1
	return check

#returns a vector containing of wether a certain direction
#should be be flipped and how many tiles we should flip
def checkMoveDirection(board, player, x, y, dx, dy):
	vec = [False, 1]
	x += dx
	y += dy
	while validTile(x, y):
		if board[x][y] == EMPTY:
			return vec
		elif board[x][y] == player:
			vec[0] = True
			return vec
		else:
			vec[1] += 1
			x += dx
			y += dy
	return vec


#Checks wether x and y is on the board
def validTile(x, y):
	return x >= 1 and x <= 8 and y >= 1 and y <= 8


#Duplicates the board
def dupBoard(board):
	dup = [x[:] for x in board]
	return dup


#MinMax algorithm with alphabeta, right now there is a depths instead of a timelimit
def minMax(alpha, beta, board, ms, maxDepth, depth, player):

	moves = showMoves(board, player)
	if ms <= time.time()*1000.0 or depth > maxDepth:
		print("knas")
	elif len(moves) == 0:
		return 0
	maxValue = float("inf")
	bestMove = 0
	for i in moves:
		dup = dupBoard(board)
		checkMoveAndFlip(dup, player, (i-1)%8+1, math.ceil(i/8))
		value = playMin(alpha, beta, dup, ms,maxDepth, depth+1, -player)
		if value < maxValue:
			bestMove = i
			maxValue = value
	return bestMove

def playMax(alpha, beta, board, ms, maxDepth, depth, player):

	moves = showMoves(board, player)
	if  ms <= time.time()*1000.0 or depth > maxDepth:
		return alpha
	elif  len(moves) == 0:  #might not be correct
		return alpha

	for i in moves:
		dup = dupBoard(board)
		checkMoveAndFlip(dup, player, (i-1)%8+1, math.ceil(i/8))
		value = playMin(alpha, beta, dup, ms, maxDepth, depth +1, -player)
		if value >= beta:
			return beta
		if value > alpha:
			alpha = value

	return alpha

def playMin(alpha, beta, board, ms, maxDepth, depth, player):
	
	moves = showMoves(board, player)
	if  ms <= time.time()*1000.0 or depth > maxDepth:
		return beta
	elif len(moves) == 0:    #might not be correct
		return beta
	

	for i in moves:
		dup = dupBoard(board)
		checkMoveAndFlip(dup, player, (i-1)%8+1, math.ceil(i/8))
		value = playMax(alpha, beta, dup, ms, maxDepth, depth+1, -player)
		if value <= beta:
			return alpha
		if value < beta:
			beta = value
	return beta

#montecarlo simulation
def monteCarlo(boardState, player, ms):
	moves = showMoves(boardState, player)
	times = []
	wins = []
	if len(moves) == 0:
		return 0

	for i in range(0, len(moves)):
		times.append(0)
		wins.append(0)

	while (ms > time.time()*1000.0 ):
		for i in range(0, len(moves)):
			times[i] += 1
			dup = dupBoard(boardState)
			if simulation(dup, player) == 1:
				wins[i] += 1

		#print(time)
	maxValue = float("-inf")
	index = 0;
	for i in range(0, len(wins)):
		if maxValue < wins[i]:
			maxValue = wins[i]
			index = i


	return moves[index]

#Random simulation for montecarlo
def simulation(board, player):
	while True:
		moves1 = showMoves(board, player)
		if len(moves1) == 0:
			moves2 = showMoves(board, -player)
			if len(moves2) == 0:
				return won(board, player)
			else:
				move = random.randint(0, len(moves2)-1)
				checkMoveAndFlip(board, -player, math.ceil(moves2[move]/8), (moves2[move]-1)%8 + 1)
		else:
			move = random.randint(0, len(moves1)-1)
			checkMoveAndFlip(board, player, math.ceil(moves1[move]/8), (moves1[move]-1)%8 + 1)
			moves2 = showMoves(board, -player)
			if len(moves2) != 0:
				move = random.randint(0, len(moves2)-1)
				checkMoveAndFlip(board, -player, math.ceil(moves2[move]/8), (moves2[move]-1)%8 + 1)

	return 0


#checks which player won
def won(board, player):
	player1 = 0
	player2 = 0
	for i in range(1,9):
		for j in range(1, 9):
			if board[i][j] == player:
				player1 += 1
			elif board[i][j] == -player:
				player2 += 1

	if player1 > player2:
		return player
	elif player2 > player1:
		return -player
	else: 
		return 0

def translateInput(string):
	if len(string) == 2:
		try:
			return [ord(string[0]) - 96, int(string[1])]
		except ValueError:
			print("Format error, shuting down")
			sys.exit(1)



def aiFight(player, board, temp, maxDepth):
	while len(showMoves(board, player)) > 0 or len(showMoves(board, -player)) > 0:
		drawBoard(board)
		time.sleep(1)
		print("AlphaBeta is thinking")
		ms = time.time()*1000.0 + 1000*temp
		compMove = minMax(float("-inf"), float("inf") ,board, ms, maxDepth, 0, player)
		if compMove == 0:
			print("AlphaBeta can not do anything")

		else:
			checkMoveAndFlip(board, player, math.ceil(compMove/8), (compMove-1)%8+1)
			drawBoard(board)
		print("MonteCarlo is thinking")
		ms = time.time()*1000.0 + 1000*temp
		compMove = monteCarlo(board, -player, ms)
		if compMove == 0:
			print("MonteCarlo can not do anything")
		else:
			checkMoveAndFlip(board, -player, math.ceil(compMove/8), (compMove-1)%8+1)

def regularFight(player, board, temp, maxDepth):
	while len(showMoves(board, player)) > 0 or len(showMoves(board, -player)) > 0:
		drawBoard(board)
		time.sleep(1)
		print("Make a move that's available (e.g a3, only this format works)")
		userMove = input()
		move = translateInput(userMove)
		while not(checkMove(board, player, move[1], move[0])):
			move = translateInput(input("Invailid move, type a valid move. (eg a3)"))

		checkMoveAndFlip(board, player, move[1], move[0])
		drawBoard(board)
		print("AlphaBeta is thinking")
		ms = time.time()*1000.0 + 1000*temp
		compMove = minMax(float("-inf"), float("inf") ,board, ms, maxDepth, 0, -player)
		if compMove == 0:
			print("AlphaBeta can not do anything")
		else:
			checkMoveAndFlip(board, -player, math.ceil(compMove/8), (compMove-1)%8+1)




#The game matrix
board = [[]]
for i in range(0,9):
	board.append([])
	for j in range(0,9):
		board[i].append(0)


#Starting pieces
piece = input("white or black? (w or b)")
if piece == "w" or piece == "W":
	player = WHITE
else:
	player = BLACK



board[4][4] = player
board[4][5] = -player
board[5][5] = player
board[5][4] = -player


#checks wether anyone can make a move right now
temp = int(input("How much time will be computer be allowed to think? (in seconds)"))
maxDepth = int(input("maxdepth to search for?"))

gameMode = input("type 'ai' for montecarlo vs minmax or anything else to just fight minmax algorithm")

if gameMode == "ai":
	aiFight(player, board, temp, maxDepth)
else:
	regularFight(player, board, temp, maxDepth)

drawBoard(board)
result = won(board, player)
if result == 1:
	print("AlphaBeta won")
elif result == -1:
	print("MonteCarlo won")
else: 
	print("they both lost")

sum1 = 0
sum2 = 0
for i in range(1,9):
	for j in range(1,9):
		if board[i][j] == player:
			sum1 += 1
		elif board[i][j] == -player:
			sum2 += 1
print(str(sum1) + "-" + str(sum2))




# board = [[0,0,0,0,0,0,0,0,0],[0,1,1,1,1,1,1,1,1],[0,1,1,1,-1,-1,-1,1,1],[0,1,1,-1,-1,-1,1,-1,1],[0,1,-1,-1,1,-1,-1,1,1],[0,1,-1,1,1,1,-1,1,1],[0,1,1,1,-1,-1,-1,-1,-1],[0,1,1,1,1,1,1,1,-1],[0,1,1,1,1,1,1,1,0]]
# rBoard = [[]]
# for i in range(0,9):
# 	rBoard.append([])
# 	for j in range(0,9):
# 		rBoard[i].append(0)
# # iterate through rows
# for i in range(0, 9):
#    # iterate through columns
#    for j in range(0, 9):
#        rBoard[j][i] = board[i][j]
# player = WHITE
# drawBoard(rBoard)
# moves = showMoves(board, player)
# print(moves)
# compMove = minMax(float("-inf"), float("inf") ,board, 8, 0, player)
# print(str(compMove))