import chess
import chess.pgn
from lib.chessengine import checkMove
import mmap
import re

board = chess.Board().fen()
movearr = []
ECOcode = ""
opening = ""
variation = ""
lasteco = 0
exporter = chess.pgn.StringExporter(
    headers=False, variations=False, comments=False)
eco = open("eco.pgn", "r+")

while True:
    inp = input()
    teste = checkMove(board, inp)
    state = teste[0]
    board = teste[6]
    finish = 0

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
                    openingcheck = game.headers
                    break

        if openingcheck is not None:
            print(openingcheck)
