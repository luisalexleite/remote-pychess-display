#porporcao 16:9 - alterar consoante a resolução
import pyautogui
import subprocess
import time
import tkinter

def start_game(white, black, mode):

    #tempo de espera
    time.sleep(120)

    #fechar o navegador
    pyautogui.hotkey('alt', 'fn', 'f4')

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
        
    def end_game():

        #fechar jogo
        pyautogui.hotkey('ctrl', 'q')

        #abrir navegador
        subprocess.call(['chromium', '--kiosk' , 'http://localhost'])