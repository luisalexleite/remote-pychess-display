import chess
import chess.pgn
from lib.chessengine import checkMove


board = chess.Board().fen()
movearr = []
boardreset = chess.Board()
exporter = chess.pgn.StringExporter(
    headers=False, variations=False, comments=False)
eco = open("eco.pgn")
ECO = ""
opening = ""
variation = ""
err = 0
while True:
    inp = input()
    teste = checkMove(board, inp)
    state = teste[0]
    board = teste[6]

    if (state == True):
        movearr.append(inp)
        boardreset = chess.Board()
        movecheck = chess.Board().variation_san(
            [boardreset.push_san(m) for m in movearr]) + " *"

        print(movecheck)
        while True:
            game = chess.pgn.read_game(eco)
            if game is None:
                break
            else:
                san = game.accept(exporter)
                if (movecheck in san):
                    ECO = game.headers['ECO']
                    opening = game.headers['Opening']
                    try:
                        variation = game.headers['Variation']
                    except:
                        variation = ""

                    break

        print(ECO, opening, variation)
