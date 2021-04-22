from PyQt5 import QtCore, QtGui, QtWidgets, QtSvg
import pyautogui
import subprocess
import time
import tkinter
import chess
import chess.svg
import firebase_admin
import math
import sys
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from firebase_admin import auth
from lib.chessengine import checkMove
import requests
import asyncio
import tracemalloc

tracemalloc.start()
#requisitos do firebase
cred = credentials.Certificate('./cred/remote-pychess-f8ba9c6e343c.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app/remote-pychess-default-rtdb/'
})
firedb = firestore.client()
gameid = sys.argv[1]
board = chess.Board().fen()
whites=False
moveCount = 1
state = 0

def getUsers(gameid):
    white = db.reference(f'games/{gameid}/whites').get()
    black = db.reference(f'games/{gameid}/blacks').get()
    whiteprof = firedb.collection(u'profile').document(f'{white}').get().to_dict()
    blackprof = firedb.collection(u'profile').document(f'{black}').get().to_dict()

    return whiteprof, blackprof

def makeMove(gameid):
    global board
    global whites
    global moveCount
    #enquanto o jogo decorrer
    if db.reference(f'games/{gameid}/state').get() == 1 or db.reference(f'games/{gameid}/state').get() == 4:
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

                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 1, 'result': result})
                    elif (stalemate == True):
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 2, 'result': 2})
                    elif (nomaterial == True):
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 3, 'result': 2})
                    elif (repetition == True):
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 2, 'method': 4, 'result': 2})
                    elif (claim == True):
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                        db.reference(f'games/{gameid}').update({'state' : 4})
                    else:
                        db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 1})
                else:
                    db.reference(f'movements/{gameid}/{moveCount}').update({'state' : 2})
                moveCount = moveCount + 1
                return chess.svg.board(board=chess.Board(board))
            return chess.svg.board(board=chess.Board(board))   
        except:
            return chess.svg.board(board=chess.Board(board))
    else:
        #terminar o jogo
        return False

class Ui_Janela(object):
    def __init__(self):
        self.timer = QtCore.QTimer()
        self.timer.setSingleShot(False)
        self.timer.setInterval(100)
        self.timer.timeout.connect(self.setupUi)
        self.timer.start()
        

    def jogo(self):
        gamesvg = makeMove(gameid)

        if (gamesvg == False):
            global state

            if (state == 0):
                self.endgame = QtWidgets.QMessageBox()
                self.endgame.setIcon(QtWidgets.QMessageBox.Information)
                self.endgame.setStandardButtons(QtWidgets.QMessageBox.NoButton)
                self.endgame.setText("Alguém ganhou por Checkmate ou outra coisa qualquer")
                self.endgame.setWindowTitle("Checkmate")
                state = 1
            else:
                self.timer = QtCore.QTimer()
                self.endgame.show()
                #kill(5 segundos)
        else:
            self.game = QtSvg.QSvgWidget(self.centralwidget)
            self.game.setGeometry(QtCore.QRect(500, 300, 861, 731))
            self.game.load(gamesvg.encode("UTF-8"))
            self.game.setObjectName("game")

    def setupUi(self):
        Janela.setObjectName("Janela")
        Janela.setWindowModality(QtCore.Qt.NonModal)
        Janela.setEnabled(True)
        Janela.resize(1920, 1080)
        font = QtGui.QFont()
        font.setKerning(False)
        font.setStyleStrategy(QtGui.QFont.NoAntialias)
        Janela.setFont(font)
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap("img/favicon.png"), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        Janela.setWindowIcon(icon)
        Janela.setAutoFillBackground(False)
        Janela.setStyleSheet("background-color: rgb(45, 47, 48);")
        Janela.setAnimated(False)
        self.centralwidget = QtWidgets.QWidget(Janela)
        self.centralwidget.setObjectName("centralwidget")
        self.logo = QtWidgets.QLabel(self.centralwidget)
        self.logo.setEnabled(True)
        self.logo.setGeometry(QtCore.QRect(550, -40, 741, 221))
        self.logo.setText("")
        self.logo.setPixmap(QtGui.QPixmap("img/logo.png"))
        self.logo.setScaledContents(False)
        self.logo.setWordWrap(False)
        self.logo.setObjectName("logo")
        self.whites = QtWidgets.QLabel(self.centralwidget)
        self.whites.setGeometry(QtCore.QRect(300, 40, 271, 61))
        self.whites.setStyleSheet("color: white;")
        self.whites.setAlignment(QtCore.Qt.AlignRight|QtCore.Qt.AlignTrailing|QtCore.Qt.AlignVCenter)
        self.whites.setObjectName("whites")
        self.whiteselo = QtWidgets.QLabel(self.centralwidget)
        self.whiteselo.setGeometry(QtCore.QRect(380, 120, 271, 61))
        self.whiteselo.setStyleSheet("color: white;")
        self.whiteselo.setObjectName("whiteselo")
        self.blackselo = QtWidgets.QLabel(self.centralwidget)
        self.blackselo.setGeometry(QtCore.QRect(1290, 120, 271, 61))
        self.blackselo.setStyleSheet("color: white;")
        self.blackselo.setObjectName("blackselo")
        self.blacks = QtWidgets.QLabel(self.centralwidget)
        self.blacks.setGeometry(QtCore.QRect(1290, 40, 271, 61))
        self.blacks.setStyleSheet("color: white;")
        self.blacks.setObjectName("blacks")
        self.whitehorse = QtWidgets.QWidget(self.centralwidget)
        self.whitehorse.setGeometry(QtCore.QRect(250, 20, 61, 81))
        self.whitehorse.setStyleSheet("image: url(img/whitehorse.png);")
        self.whitehorse.setObjectName("whitehorse")
        self.blackhorse = QtWidgets.QWidget(self.centralwidget)
        self.blackhorse.setGeometry(QtCore.QRect(1570, 20, 61, 81))
        self.blackhorse.setStyleSheet("image: url(img/favicon.png);")
        self.blackhorse.setObjectName("blackhorse")
        self.whitespic = QtWidgets.QLabel(self.centralwidget)
        self.whitespic.setGeometry(QtCore.QRect(50, 30, 171, 151))
        self.wprofpic = QtGui.QImage()
        self.wprofpic.loadFromData(requests.get(f"{white['image']}").content)
        self.wprofpicmap = QtGui.QPixmap(self.wprofpic)
        self.wprofpic_resized = self.wprofpicmap.scaled(191, 191, QtCore.Qt.IgnoreAspectRatio)
        self.whitespic.setPixmap(self.wprofpic_resized)
        self.whitespic.setObjectName("whitespic")
        self.blackspic = QtWidgets.QLabel(self.centralwidget)
        self.blackspic.setGeometry(QtCore.QRect(1650, 30, 171, 151))
        self.bprofpic = QtGui.QImage()
        self.bprofpic.loadFromData(requests.get(f"{black['image']}").content)
        self.bprofpicmap = QtGui.QPixmap(self.bprofpic)
        self.bprofpic_resized = self.bprofpicmap.scaled(191, 191, QtCore.Qt.IgnoreAspectRatio)
        self.blackspic.setPixmap(self.bprofpic_resized)
        self.blackspic.setObjectName("blackspic")
        self.jogo()
        self.blackstime = QtWidgets.QLabel(self.centralwidget)
        self.blackstime.setGeometry(QtCore.QRect(1290, 200, 121, 61))
        font = QtGui.QFont()
        font.setPointSize(36)
        self.blackstime.setFont(font)
        self.blackstime.setStyleSheet("color: white;")
        self.blackstime.setObjectName("blackstime")
        self.whitestime = QtWidgets.QLabel(self.centralwidget)
        self.whitestime.setGeometry(QtCore.QRect(450, 200, 121, 61))
        font = QtGui.QFont()
        font.setPointSize(36)
        self.whitestime.setFont(font)
        self.whitestime.setStyleSheet("color: white;")
        self.whitestime.setObjectName("whitestime")
        Janela.setCentralWidget(self.centralwidget)
        self.retranslateUi()
        QtCore.QMetaObject.connectSlotsByName(Janela)

    def retranslateUi(self):
        _translate = QtCore.QCoreApplication.translate
        Janela.setWindowTitle(_translate("Janela", "Remote PyChess"))
        self.whites.setText(_translate("Janela", f"<html><head/><body><p><span style=\" font-size:25pt;\">{white['username']}</span></p></body></html>"))
        self.whiteselo.setText(_translate("Janela", f"<html><head/><body><p><span style=\" font-size:36pt;\">ELO: {white['rating']}</span></p></body></html>"))
        self.blackselo.setText(_translate("Janela", f"<html><head/><body><p><span style=\" font-size:36pt;\">ELO: {black['rating']}</span></p></body></html>"))
        self.blacks.setText(_translate("Janela", f"<html><head/><body><p><span style=\" font-size:25pt;\">{black['username']}</span></p></body></html>"))
        self.blackstime.setText(_translate("Janela", "<html><head/><body><p>00:00</p></body></html>"))
        self.whitestime.setText(_translate("Janela", "<html><head/><body><p>00:00</p></body></html>"))

if __name__ == "__main__":
    import sys
    white, black = getUsers(gameid)
    app = QtWidgets.QApplication(sys.argv)
    Janela = QtWidgets.QMainWindow()
    ui = Ui_Janela()
    ui.setupUi()
    timer = QtCore.QTimer()
    timer.setSingleShot(False)
    timer.setInterval(1000) # in milliseconds, so 5000 = 5 seconds
    #timer.timeout.connect(ui.setupUi())
    Janela.showFullScreen()
    sys.exit(app.exec())
    