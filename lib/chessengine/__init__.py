# python-chess
import chess


# Move State
def checkMove(board, move, lastmove):
    board = chess.Board(board)
    try:
        board.push_san(move)
    except:
        return False, False, False, False, False, False, board.fen(), lastmove
    checkmate = board.is_checkmate()
    stalemate = board.is_stalemate()
    nomaterial = board.is_insufficient_material()
    claim = board.can_claim_draw()
    repetition = board.is_fivefold_repetition()
    lastmove = board.peek()

    return True, checkmate, stalemate, nomaterial, claim, repetition, board.fen(), lastmove


# Game Example (Terminal)
def playExample():
    checkmate = False
    stalemate = False
    nomaterial = False
    claim = False
    repetition = False
    board = chess.Board().fen()

    cor = "Jogador das Brancas"
    while checkmate == False or stalemate == False or nomaterial == False or repetition == False:
        if(checkmate == True or stalemate == True or nomaterial == True or repetition == True):
            if checkmate == True:
                if cor == "Jogador das Brancas":
                    cor = "Jogador das Pretas"
                else:
                    cor = "Jogador das Brancas"
                print("\n" + cor + " venceu por Checkmate")
            elif stalemate == True:
                print("\nEmpate por Afogamento")
            elif nomaterial == True:
                print("\nEmpate por Material Insuficiente")
            elif repetition == True:
                print("\nEmpate por Repetição")
            break
        elif claim == True:
            opcao = input("Quer empate? [S/N]")
            if opcao == "s" or opcao == "S":
                print("\nEmpate por Repetição")
                break
            elif opcao != "s" and opcao != "S" and opcao != "n" or opcao != "N":
                while opcao != "s" and opcao != "S" and opcao != "n" or opcao != "N":
                    opcao = input("Quer empate? [S/N]")
                    if opcao == "s" or opcao == "S":
                        print("\nEmpate por Repetição")
                        break
        valid = False
        while valid == False:
            move = input("\n" + cor + ' mexa uma peça:')
            print("\n")
            valid, checkmate, stalemate, nomaterial, claim, repetition, board = checkMove(
                board, move)
            if valid == False:
                print("\nMovimento inválido\n")
            else:
                if cor == "Jogador das Brancas":
                    cor = "Jogador das Pretas"
                else:
                    cor = "Jogador das Brancas"
                print(chess.Board(board))
                break
