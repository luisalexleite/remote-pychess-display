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
from firebase_admin import auth
from lib.chessengine import checkMove

#requisitos do firebase
cred = credentials.Certificate('raspberry/cred/remote-pychess-f8ba9c6e343c.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app/remote-pychess-default-rtdb/'
})
teste = db.reference(f'movements/-MWUB1mXE9rg8b7auYiJ').order_by_key().equal_to('1').get()

def end_game():
    #fechar jogo
    pyautogui.hotkey('ctrl', 'q')

def makeMove(gameid):
    """
    logica da função feita falta ligar à bd - firestore e realtime database
    """
    moveCount = 1
    board = chess.Board().fen()
    #enquanto o jogo decorrer
    while db.reference(f'games/{gameid}/state').get() == 0:
        whites=False
        #obter dados do ultimo movimento
        movement = db.reference(f'movements/{gameid}').order_by_key().equal_to(f'{moveCount}').get()
        #se hover algum moviento disponível
        try: 
            #verificar se movimento foi executado
            if movement [f'{moveCount}']['state'] == 0:
                valid, checkmate, stalemate, nomaterial, claim, repetition, board = checkMove(board, movement [f'{moveCount}']['move'])

            #verificar se há vitória, empate ou se continua o jogo
            if (valid == True):
                whites^=True
                if (checkmate == True):
                    if(whites == True) :
                        result = 1
                    else:
                        result = 3
                    pyautogui.write(movement [f'{moveCount}']['move'])
                    pyautogui.press('enter')
                    db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 1})
                    db.reference(f'games/{gameid}').update({'state' : 1, 'method': 1, 'result': result})
                elif (stalemate == True):
                    pyautogui.write(movement [f'{moveCount}']['move'])
                    pyautogui.press('enter')
                    db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 1})
                    db.reference(f'games/{gameid}').update({'state' : 1, 'method': 2, 'result': 2})
                elif (nomaterial == True):
                    pyautogui.write(movement [f'{moveCount}']['move'])
                    pyautogui.press('enter')
                    db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 1})
                    db.reference(f'games/{gameid}').update({'state' : 1, 'method': 3, 'result': 2})
                elif (repetition == True):
                    pyautogui.write(movement [f'{moveCount}']['move'])
                    pyautogui.press('enter')
                    db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 1})
                    db.reference(f'games/{gameid}').update({'state' : 1, 'method': 4, 'result': 2})
                elif (claim == True):
                    pyautogui.write(movement [f'{moveCount}']['move'])
                    pyautogui.press('enter')
                    db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 1})
                    db.reference(f'games/{gameid}').update({'state' : 3})
                else:
                    pyautogui.write(movement [f'{moveCount}']['move'])
                    pyautogui.press('enter')
                    db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 1})
            else:
                db.reference(f'movement/{gameid}/{moveCount}').update({'state' : 2})

            movement +=1
        except:
            continue
    else:
        #terminar o jogo
        end_game()

def start_game(gameid):

    white = db.reference(f'games/{gameid}/whites').get()
    black = db.reference(f'games/{gameid}/blacks').get()
    mode = db.reference(f'games/{gameid}/type').get()


    #tempo de espera
    #time.sleep(10)

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
    if mode == 0:
        #iniciar jogo em blitz
        pyautogui.click(int(width)/2.167042889,int(height)/2.03)
        pyautogui.press('enter')
    elif mode == 1:
        #iniciar jogo em rapid
        pyautogui.click(int(width)/2.167042889,int(height)/1.918294849)
        pyautogui.press('enter')
    elif mode == 2:
        #iniciar jogo em normal
        pyautogui.click(int(width)/2.167042889,int(height)/1.839863714)
        pyautogui.press('enter')
    
    #makeMove(gameid)

#start_game('-MWUB1mXE9rg8b7auYiJ')