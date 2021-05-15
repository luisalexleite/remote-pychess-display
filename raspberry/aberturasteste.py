import chess
import chess.pgn
from lib.chessengine import checkMove

board = chess.Board().fen()
movearr = []
exporter = chess.pgn.StringExporter(
    headers=False, variations=False, comments=False)
ECOcode = ""
opening = ""
variation = ""
lasteco = 0

while True:
    inp = input()
    teste = checkMove(board, inp)
    state = teste[0]
    board = teste[6]
    finish = 0
    eco = open('eco.pgn', 'r+')

    if (state == True):
        movearr.append(inp)
        boardreset = chess.Board()
        movecheck = chess.Board().variation_san(
            [boardreset.push_san(m) for m in movearr]) + " *"

        while True:
            game = chess.pgn.read_game(eco)
            if game is None:
                openingcheck = None
                break
            else:
                san = game.accept(exporter)
                if (movecheck in str(san)):
                    openingcheck = game
                    break

        if openingcheck is not None:
            ECOcode = openingcheck.headers['ECO']
            opening = openingcheck.headers['Opening']

            try:
                variation = openingcheck.headers['Variation']
            except:
                variation = ""

    print(ECOcode, opening, variation)
