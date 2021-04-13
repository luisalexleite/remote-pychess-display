#porporcao 16:9 - alterar consoante a resolução
import pyautogui
import subprocess
import time
import tkinter
import chess
import firebase_admin
import math
import sys
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from firebase_admin import auth
from lib.chessengine import checkMove

#requisitos do firebase
cred = credentials.Certificate('./cred/remote-pychess-f8ba9c6e343c.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app/remote-pychess-default-rtdb/'
})

def end_game():
    #fechar jogo
    pyautogui.hotkey('ctrl', 'q')

def makeMove(gameid):
    moveCount = 1
    board = chess.Board().fen()
    whites=False
    #enquanto o jogo decorrer
    while db.reference(f'games/{gameid}/state').get() == 1 or db.reference(f'games/{gameid}/state').get() == 4:
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
                        if(whites == True):
                            result = 1
                        else:
                            result = 3
                        pyautogui.write(movement [f'{moveCount}']['move'])
                        pyautogui.press('enter')
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 1, 'result': result})
                    elif (stalemate == True):
                        pyautogui.write(movement [f'{moveCount}']['move'])
                        pyautogui.press('enter')
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 2, 'result': 2})
                    elif (nomaterial == True):
                        pyautogui.write(movement [f'{moveCount}']['move'])
                        pyautogui.press('enter')
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 3, 'result': 2})
                    elif (repetition == True):
                        pyautogui.write(movement [f'{moveCount}']['move'])
                        pyautogui.press('enter')
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 4, 'result': 2})
                    elif (claim == True):
                        pyautogui.write(movement [f'{moveCount}']['move'])
                        pyautogui.press('enter')
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 4})
                    else:
                        pyautogui.write(movement [f'{moveCount}']['move'])
                        pyautogui.press('enter')
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                else:
                    db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 2})
            moveCount +=1
        except:
            continue
    else:
        #terminar o jogo
        end_game()

def start_game(gameid):
    #alterar delay
    delay = 15
    delaymin = math.ceil(delay/5)
    delaymed = math.ceil(delay/3)

    white = db.reference(f'games/{gameid}/whites').get()
    black = db.reference(f'games/{gameid}/blacks').get()
    mode = db.reference(f'games/{gameid}/type').get()


    #tempo de espera
    time.sleep(delaymed)

    #informacoes de resolucao do ecra
    root = tkinter.Tk()
    width = root.winfo_screenwidth()
    height = root.winfo_screenheight()
    root.destroy()

    #abrir o PyChess
    subprocess.Popen(['pychess'])

    #esperar pela abertura do PyChess
    time.sleep(delay)

    #PyChess em fullscreen
    pyautogui.hotkey('fn', 'f11')

    time.sleep(delaymin)
    
    #abrir as preferencias
    pyautogui.click(int(width)/22.588235294,int(height)/98.181818182)
    pyautogui.press('down')
    time.sleep(0.5)
    pyautogui.press('down')
    pyautogui.press('enter')

    #esperar que abram as preferencias
    time.sleep(delaymed)

    #mudar nome do jogador com as brancas
    pyautogui.press('pgup')
    time.sleep(delaymin)
    pyautogui.press('down')
    time.sleep(delaymin)
    pyautogui.hotkey('ctrl', 'a')
    time.sleep(delaymin)
    pyautogui.hotkey('backspace')
    time.sleep(delaymin)
    pyautogui.write(white)

    #mudar nome do jogador com as pretas
    time.sleep(delaymin)
    pyautogui.press('down')
    time.sleep(delaymin)
    pyautogui.hotkey('ctrl', 'a')
    time.sleep(delaymin)
    pyautogui.press('backspace')
    time.sleep(delaymin)
    pyautogui.write(black)

    time.sleep(delaymin)
    #fechar preferencias
    pyautogui.press('esc')

    #abrir configuracao do jogo
    time.sleep(delaymed)

    pyautogui.hotkey('ctrl', 'n')
    if mode == 0:
        #iniciar jogo em blitz
        time.sleep(0.5)
        pyautogui.click(int(width)/2.167042889,int(height)/2.03)
        time.sleep(0.5)
        pyautogui.press('enter')
    elif mode == 1:
        #iniciar jogo em rapid
        time.sleep(0.5)
        pyautogui.click(int(width)/2.167042889,int(height)/1.918294849)
        time.sleep(0.5)
        pyautogui.press('enter')
    elif mode == 2:
        #iniciar jogo em normal
        time.sleep(0.5)
        pyautogui.click(int(width)/2.167042889,int(height)/1.839863714)
        time.sleep(0.5)
        pyautogui.press('enter')
        
    time.sleep(delaymin)
    db.reference(f'games/{gameid}').update({'state' : 1})

    time.sleep(delaymed)
    makeMove(gameid)

start_game(sys.argv[1])