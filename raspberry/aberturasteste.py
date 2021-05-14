import chess
import chess.pgn
from lib.chessengine import checkMove


board = chess.Board().fen()
game = chess.pgn.Game()
movearr = []
boardreset = chess.Board()
exporter = chess.pgn.StringExporter(
    headers=False, variations=False, comments=False)

eco = open("eco.pgn")
first_game = chess.pgn.read_game(eco)
sec_game = chess.pgn.read_game(eco)
print(first_game.headers)


while True:
    inp = input()
    teste = checkMove(board, inp)
    state = teste[0]
    if (state == True):
        movearr.append(inp)

    print(movearr)
    boardreset = chess.Board()
    movecheck = chess.Board().variation_san(
        [boardreset.push_san(m) for m in movearr])

    print(movecheck)

    if (movecheck + " *" == sec_game.accept(exporter)):
        print(sec_game.headers['Opening'])
    board = teste[6]

    print(teste[6])
