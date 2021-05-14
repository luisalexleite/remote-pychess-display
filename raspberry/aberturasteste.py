import chess
import chess.pgn
from lib.chessengine import checkMove


board = chess.Board().fen()
game = chess.pgn.Game()

while True:
    inp = input()
    teste = checkMove(board, inp)
    board = teste[6]
    print(teste[6])
