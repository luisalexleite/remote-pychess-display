#porporcao 16:9 - alterar consoante a resolução
import pyautogui
import subprocess
import time
import tkinter
import chess
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from lib.chessengine import checkMove

#requisitos do firebase
cred = credentials.Certificate('raspberry/cred/remote-pychess-f8ba9c6e343c.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app/'
})

def end_game():
    #fechar jogo
    pyautogui.hotkey('ctrl', 'q')

ref = db.reference('/teste')
print(ref.get())

def makeMove(game):
    """
    logica da função feita falta ligar à bd - firestore e realtime database
    """
    #enquanto o jogo decorrer
    while state == 1:
        #se hover algum moviento disponível
        if movimento_disponivel == 0:
            #verificar se movimento é válido
            valid, checkmate, stalemate, nomaterial, claim, repetition, board = checkMove(board, 'move')

            #verificar se há vitória, empate ou se continua o jogo
            if (valid == True):
                if (checkmate == True):
                    print("checkmate")  
                    pyautogui.write()
                    pyautogui.press('enter')
                    print()
                elif (stalemate == True):
                    print("stalemate")
                    pyautogui.write()
                    pyautogui.press('enter')
                    print()
                elif (nomaterial == True):
                    print("Insufficent Material")
                    pyautogui.write()
                    pyautogui.press('enter')
                    print()
                elif (repetition == True):
                    print("Repetition")
                    pyautogui.write()
                    pyautogui.press('enter')
                    print()
                elif (claim == True):
                    print("Claim Draw")
                    pyautogui.write()
                    pyautogui.press('enter')
                    print()
                else:
                    pyautogui.write()
                    pyautogui.press('enter')
                    print()
                    print('Movimento Válido')
            else:
                print('Movimento inválido')
        else:
            #terminar o jogo
            end_game()

def start_game(white, black, mode):

    #tempo de espera
    time.sleep(10)

    #informacoes de resolucao do ecra
    root = tkinter.Tk()
    width = root.winfo_screenwidth()
    height = root.winfo_screenheight()
    root.destroy()

    #abrir o PyChess
    subprocess.Popen(['pychess'])

    #esperar pela abertura do PyChess
    time.sleep(1)

    #PyChess em fullscreen
    pyautogui.hotkey('fn', 'f11')

    #abrir as preferencias
    pyautogui.click(int(width)/22.588235294,int(height)/98.181818182)
    pyautogui.press('down', presses=2)
    pyautogui.press('enter')

    #esperar que abram as preferencias
    time.sleep(1)

    #mudar nome do jogador com as brancas
    pyautogui.press('pgup')
    pyautogui.press('down')
    pyautogui.hotkey('ctrl', 'a')
    pyautogui.hotkey('backspace')
    pyautogui.write(white)

    #mudar nome do jogador com as pretas
    pyautogui.press('down')
    pyautogui.hotkey('ctrl', 'a')
    pyautogui.press('backspace')
    pyautogui.write(black)

    #fechar preferencias
    pyautogui.press('esc')

    #abrir configuracao do jogo
    pyautogui.hotkey('ctrl', 'n')
    if mode == 'blitz':
        #iniciar jogo em blitz
        pyautogui.click(int(width)/2.167042889,int(height)/2.03)
        pyautogui.press('enter')
    elif mode == 'rapid':
        #iniciar jogo em rapid
        pyautogui.click(int(width)/2.167042889,int(height)/1.918294849)
        pyautogui.press('enter')
    elif mode == 'normal':
        #iniciar jogo em normal
        pyautogui.click(int(width)/2.167042889,int(height)/1.839863714)
        pyautogui.press('enter')
    
    makeMove('something')