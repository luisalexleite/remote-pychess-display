import chess
import chess.svg
from lib.chessengine import checkMove

board = chess.Board().fen()
while True:
    inp = input()
    teste = checkMove(board, inp)
    state = teste[0]
    board = teste[6]

    arrayblack = []
    arraywhite = []

    if (state == True):
        k = board.count('k')
        r = 2 - board.count('r')
        n = 2 - board.count('n')
        b = 2 - board.count('b')
        p = 8 - board.count('p')

        K = board.count('K')
        R = 2 - board.count('R')
        N = 2 - board.count('N')
        B = 2 - board.count('B')
        P = 8 - board.count('P')

        if k == 0:
            arrayblack.append('k')

        if r > 0:
            arrayblack += r * ['r']

        if n > 0:
            arrayblack += n * ['n']

        if b > 0:
            arrayblack += b * ['b']

        if p > 0:
            arrayblack += p * ['p']

        if K == 0:
            arrayblack.append('K')

        if R > 0:
            arrayblack += R * ['R']

        if N > 0:
            arrayblack += N * ['N']

        if B > 0:
            arrayblack += B * ['B']

        if P > 0:
            arrayblack += P * ['P']

        print(arrayblack, arraywhite)
        for x in arrayblack:
            chess.svg.piece(chess.Piece.from_symbol(x))
        arrayblack = []
        arraywhite = []