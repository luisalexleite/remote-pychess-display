#porporcao 16:10 - alterar consoante a resolução
import pyautogui
import subprocess
import time
import tkinter

def start_game(white, black, mode):
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
    pyautogui.click(int(width)/16.941176470588,int(height)/82)
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
    pyautogui.hotkey('backspace')
    pyautogui.write(black)

    #fechar preferencias
    pyautogui.press('esc')

    #abrir configuracao do jogo
    pyautogui.click(int(width)/2.47422680412,int(height)/3.37078651685)
    if mode == 'blitz':
        #iniciar jogo em blitz
        pyautogui.click(int(width)/2.13333333333,int(height)/2.08333333333)
        pyautogui.press('enter')
    elif mode == 'rapid':
        #iniciar jogo em rapid
        pyautogui.click(int(width)/2.13333333333,int(height)/1.93965517241)
        pyautogui.press('enter')
    elif mode == 'normal':
        #iniciar jogo em normal
        pyautogui.click(int(width)/2.13333333333,int(height)/1.82926829268)
        pyautogui.press('enter')