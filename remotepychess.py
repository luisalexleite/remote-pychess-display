# Internal Libraries
import time
import math
import sys

# External Libraries
from PyQt5 import QtCore, QtGui, QtWidgets, QtSvg
import chess
import chess.svg
import chess.pgn
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from firebase_admin import auth
from lib.chessengine import checkMove
import requests

# Firebase Requirements
# Go to https://console.cloud.google.com/iam-admin/serviceaccounts?project=project-name click on the service email and create a new json key
cred = credentials.Certificate('credentials-file-location')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app/your-db-name/'
})


# Firestore
firedb = firestore.client()

# Game Id
gameid = sys.argv[1]

# Inicial Board State - FEN Code
board = chess.Board().fen()

# Variables
whites = False
moveCount = 1
state = 0
firstmovewhite = firstmoveblack = True
pointswhite = 0
pointsblack = 0
pieceswhite = ""
piecesblack = ""
movearr = []
opening = "?"
movehistory = ""
check = None
exists = True
lastmove = None


# Game has Started
def changestate(gameid):
    db.reference(f'games/{gameid}').update({'state': 1})


# Seconds to MM:SS
def convert(seconds):
    return time.strftime("%M:%S", time.gmtime(seconds))


# Game Type/Time
def getType(gameid):
    typ = db.reference(f'games/{gameid}/type').get()
    return typ


if(getType(gameid) == 0):
    secondswhite = secondsblack = 300
elif (getType(gameid) == 1):
    secondswhite = secondsblack = 900
else:
    secondswhite = secondsblack = 2400


# Get Opening During the Game
def getOpening(move):
    global movearr, exists
    exist = False
    exporter = chess.pgn.StringExporter(
        headers=False, variations=False, comments=False)
    eco = open('eco.pgn', 'r')
    movearr.append(move)
    boardreset = chess.Board()
    movecheck = chess.Board().variation_san(
        [boardreset.push_san(m) for m in movearr])
    if exists == True:
        while True:
            game = chess.pgn.read_game(eco)
            if game is None:
                openingcheck = None
                break
            else:
                san = game.accept(exporter)
                if (movecheck in str(san)):
                    exist = True
                    if (movecheck + " *" in str(san)):
                        openingcheck = game.headers
                        break
    else:
        openingcheck = None
    eco.close()
    return openingcheck, movecheck, exist


# Opening Data Change
def refreshOpenings(opening):
    try:
        openingcheck = opening['ECO'] + " " + \
            opening['Opening'] + " " + opening['Variation']
    except:
        openingcheck = opening['ECO'] + " " + opening['Opening']

    return openingcheck


# Pieces and Piece Power Points
def getPieces(fen):
    piecesblack = ""
    pieceswhite = ""
    pointswhite = 0
    pointsblack = 0

    q = fen.count('q')
    r = 2 - fen.count('r')
    n = 2 - fen.count('n')
    b = 2 - fen.count('b')
    p = 8 - fen.count('p')

    Q = fen.count('Q')
    R = 2 - fen.count('R')
    N = 2 - fen.count('N')
    B = 2 - fen.count('B')
    P = 8 - fen.count('P')

    if q == 0:
        piecesblack += '\u265b'
        pointswhite += 9

    if r > 0:
        piecesblack += r * '\u265c'
        pointswhite += r * 5

    if n > 0:
        piecesblack += n * '\u265e'
        pointswhite += n * 3

    if b > 0:
        piecesblack += b * '\u265d'
        pointswhite += b * 3

    if p > 0:
        piecesblack += p * '\u265f'
        pointswhite += p * 1

    if Q == 0:
        pieceswhite += '\u2655'
        pointsblack += 9

    if R > 0:
        pieceswhite += R * '\u2656'
        pointsblack += R * 5

    if N > 0:
        pieceswhite += N * '\u2658'
        pointsblack += N * 3

    if B > 0:
        pieceswhite += B * '\u2657'
        pointsblack += B * 3

    if P > 0:
        pieceswhite += P * '\u2659'
        pointsblack += P * 1

    return pieceswhite, piecesblack, pointswhite, pointsblack


# Player Data
def getUsers(gameid):
    white = db.reference(f'games/{gameid}/whites').get()
    black = db.reference(f'games/{gameid}/blacks').get()
    whiteprof = firedb.collection(u'profile').document(
        f'{white}').get().to_dict()
    blackprof = firedb.collection(u'profile').document(
        f'{black}').get().to_dict()

    return white, black, whiteprof, blackprof


# Elo Calculations
def getElo(white, black):
    whiteelo = int(white['rating'])
    blackelo = int(black['rating'])
    whiteelodif = math.ceil(
        10 * (1 / (1 + math.pow(10, (whiteelo - blackelo)/400))))
    blackelodif = math.ceil(
        10 * (1 / (1 + math.pow(10, (blackelo - whiteelo)/400))))

    return whiteelodif, blackelodif


# Elo Change - after game finished
def registElo(whiteelo, blackelo):
    white = firedb.collection(u'profile').document(f'{whiteid}')
    white.update({u'rating': whiteelo})
    black = firedb.collection(u'profile').document(f'{blackid}')
    black.update({u'rating': blackelo})


# Timer
def checktime(whites, firstmovewhite, firstmoveblack):
    global secondswhite, secondsblack
    if(whites == False and firstmovewhite == False):
        secondswhite -= 1
        if (secondswhite < 0):
            secondswhite = 0
    elif (whites == True and firstmoveblack == False):
        secondsblack -= 1
        if (secondsblack < 0):
            secondsblack = 0


# Check Moves
def makeMove(gameid):
    global board
    global whites
    global moveCount
    global firstmovewhite, firstmoveblack
    global secondsblack, secondswhite
    global opening, movehistory, exists
    global lastmove
    # While Game is Ocurring
    if db.reference(f'games/{gameid}/state').get() == 1 or db.reference(f'games/{gameid}/state').get() == 4:
        if secondsblack <= 0:
            db.reference(
                f'games/{gameid}').update({'state': 2, 'method': 8, 'result': 1})
        elif secondswhite <= 0:
            db.reference(
                f'games/{gameid}').update({'state': 2, 'method': 8, 'result': 3})
        # Last Move
        movement = db.reference(
            f'movements/{gameid}').order_by_key().equal_to(f'{moveCount}').get()
        # If New Move
        try:
            # Check Move
            if movement[f'{moveCount}']['state'] == 0:
                move = movement[f'{moveCount}']['move']
                valid, checkmate, stalemate, nomaterial, claim, repetition, board, lastmove = checkMove(
                    board, move, lastmove)
                # Move Change Result
                if (valid == True):
                    whites ^= True
                    if (checkmate == True):
                        if(whites == True):
                            result = 1
                        else:
                            result = 3

                        db.reference(
                            f'movements/{gameid}/{moveCount}').update({'state': 1})
                        db.reference(
                            f'games/{gameid}').update({'state': 2, 'method': 1, 'result': result})
                    elif (stalemate == True):
                        db.reference(
                            f'movements/{gameid}/{moveCount}').update({'state': 1})
                        db.reference(
                            f'games/{gameid}').update({'state': 2, 'method': 2, 'result': 2})
                    elif (nomaterial == True):
                        db.reference(
                            f'movements/{gameid}/{moveCount}').update({'state': 1})
                        db.reference(
                            f'games/{gameid}').update({'state': 2, 'method': 3, 'result': 2})
                    elif (repetition == True):
                        db.reference(
                            f'movements/{gameid}/{moveCount}').update({'state': 1})
                        db.reference(
                            f'games/{gameid}').update({'state': 2, 'method': 4, 'result': 2})
                    elif (claim == True):
                        db.reference(
                            f'movements/{gameid}/{moveCount}').update({'state': 1})
                        db.reference(f'games/{gameid}').update({'state': 4})
                    else:
                        db.reference(
                            f'movements/{gameid}/{moveCount}').update({'state': 1})
                    moveCount = moveCount + 1
                    openinginfo, movehistory, exists = getOpening(move)
                    if exists == True and openinginfo != None:
                        opening = refreshOpenings(openinginfo)
                else:
                    db.reference(
                        f'movements/{gameid}/{moveCount}').update({'state': 2})
                    moveCount = moveCount + 1

                if (whites == True and firstmovewhite == True):
                    firstmovewhite = False
                elif (whites == False and firstmoveblack == True):
                    firstmoveblack = False

                side = chess.BLACK if whites else chess.WHITE
                check = chess.Board(board).king(
                    side) if chess.Board(board).is_check() else None

                return True, chess.svg.board(board=chess.Board(board), check=check, lastmove=lastmove)

            side = chess.BLACK if whites else chess.WHITE
            check = chess.Board(board).king(
                side) if chess.Board(board).is_check() else None

            return True, chess.svg.board(board=chess.Board(board), check=check, lastmove=lastmove)
        except:
            side = chess.BLACK if whites else chess.WHITE
            check = chess.Board(board).king(
                side) if chess.Board(board).is_check() else None

            return True, chess.svg.board(board=chess.Board(board), check=check, lastmove=lastmove)
    else:
        # Finish Game
        result = db.reference(f'games/{gameid}').get()
        return False, result


class Ui_Janela(object):

    # UI initial Config
    def __init__(self):
        self.timer = QtCore.QTimer()
        self.timer.setSingleShot(False)
        self.timer.setInterval(1000)
        self.timer.timeout.connect(self.setupUi)
        self.timer.start()

    # Game State
    def jogo(self):
        check = makeMove(gameid)

        if (check[0] == False):
            result = check[1]
            self.endgame = QtWidgets.QMessageBox()
            self.endgame.setIcon(QtWidgets.QMessageBox.Information)
            self.endgame.setStandardButtons(QtWidgets.QMessageBox.NoButton)

            if result['result'] == 1:
                inistr = f"{white['username']} ganhou por "
                if(whiteelodif >= int(black['rating'])):
                    blackelo = 0
                    blackdif = 0
                    blackelostr = f"{blackelo} -{blackdif}"
                else:
                    blackelo = int(black['rating']) - whiteelodif
                    blackdif = whiteelodif
                    blackelostr = f"{blackelo} -{blackdif}"

                whiteelo = int(white['rating']) + whiteelodif
                whitedif = whiteelodif
                whiteelostr = f"{whiteelo} +{whitedif}"

            elif result['result'] == 3:
                inistr = f"{black['username']} ganhou por "
                if(blackelodif >= int(white['rating'])):
                    whiteelo = 0
                    whitedif = 0
                    whiteelostr = f"{whiteelo} -{whitedif}"
                else:
                    whiteelo = int(white['rating']) - blackelodif
                    whitedif = blackelodif
                    whiteelostr = f"{whiteelo} -{whitedif}"

                blackelo = int(black['rating']) + blackelodif
                blackdif = blackelodif
                blackelostr = f"{blackelo} +{blackdif}"
            else:
                inistr = "Empate por "
                blackelo = int(black['rating']) + 1
                blackdif = 1
                blackelostr = f"{blackelo} +{blackdif}"
                whiteelo = int(white['rating'])
                whitedif = 0
                whiteelostr = f"{whiteelo} -{whitedif}"

            registElo(whiteelo, blackelo)

            if result['method'] == 1:
                title = finistr = "Checkmate"
            elif result['method'] == 2:
                title = finistr = "Afogamento"
            elif result['method'] == 3:
                title = finistr = "Material Suficiente"
            elif result['method'] == 4 or result['method'] == 5:
                title = finistr = "Repetição"
            elif result['method'] == 6:
                title = finistr = "Acordo"
            elif result['method'] == 7:
                title = finistr = "Desistência"
            elif result['method'] == 8:
                finistr = "ter acabado o tempo"
                title = "Acabou o Tempo"

            self.endgame.setText(
                inistr + finistr + ".\n" + white['username'] + f": {whiteelostr} " + black['username'] + f": {blackelostr}")
            self.endgame.setWindowTitle(title)
            self.timer = QtCore.QTimer()
            self.timer.setSingleShot(True)
            self.timer.setInterval(10000)
            self.timer.timeout.connect(self.close)
            self.timer.start()
            self.endgame.show()
        else:
            gamesvg = check[1]
            self.game = QtSvg.QSvgWidget(self.centralwidget)
            self.game.setGeometry(QtCore.QRect(500, 300, 861, 731))
            self.game.load(gamesvg.encode("UTF-8"))
            self.game.setObjectName("game")

    # Exit App
    def close(self):
        sys.exit()

    # UI
    def setupUi(self):
        global pieceswhite, piecesblack, pointswhite, pointsblack
        pieceswhite, piecesblack, pointswhite, pointsblack = getPieces(
            board)
        Janela.setObjectName("Janela")
        Janela.setWindowModality(QtCore.Qt.NonModal)
        Janela.setEnabled(True)
        Janela.resize(1920, 1080)
        font = QtGui.QFont()
        font.setKerning(False)
        font.setStyleStrategy(QtGui.QFont.NoAntialias)
        Janela.setFont(font)
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap("img/favicon.png"),
                       QtGui.QIcon.Normal, QtGui.QIcon.Off)
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
        self.whites.setAlignment(
            QtCore.Qt.AlignRight | QtCore.Qt.AlignTrailing | QtCore.Qt.AlignVCenter)
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
        self.wprofpic_resized = self.wprofpicmap.scaled(
            191, 191, QtCore.Qt.IgnoreAspectRatio)
        self.whitespic.setPixmap(self.wprofpic_resized)
        self.whitespic.setObjectName("whitespic")
        self.blackspic = QtWidgets.QLabel(self.centralwidget)
        self.blackspic.setGeometry(QtCore.QRect(1650, 30, 171, 151))
        self.bprofpic = QtGui.QImage()
        self.bprofpic.loadFromData(requests.get(f"{black['image']}").content)
        self.bprofpicmap = QtGui.QPixmap(self.bprofpic)
        self.bprofpic_resized = self.bprofpicmap.scaled(
            191, 191, QtCore.Qt.IgnoreAspectRatio)
        self.blackspic.setPixmap(self.bprofpic_resized)
        self.blackspic.setObjectName("blackspic")
        self.jogo()
        self.blackstime = QtWidgets.QLabel(self.centralwidget)
        self.blackstime.setGeometry(QtCore.QRect(1290, 200, 121, 61))
        font = QtGui.QFont()
        font.setPointSize(30)
        self.blackstime.setFont(font)
        self.blackstime.setStyleSheet("color: white;")
        self.blackstime.setObjectName("blackstime")
        self.whitestime = QtWidgets.QLabel(self.centralwidget)
        self.whitestime.setGeometry(QtCore.QRect(470, 200, 121, 61))
        font = QtGui.QFont()
        font.setPointSize(30)
        self.whitestime.setFont(font)
        self.whitestime.setStyleSheet("color: white;")
        self.whitestime.setObjectName("whitestime")
        self.whitepoints = QtWidgets.QLabel(self.centralwidget)
        self.whitepoints.setGeometry(QtCore.QRect(50, 300, 400, 100))
        font = QtGui.QFont()
        font.setPointSize(12)
        self.whitepoints.setFont(font)
        self.whitepoints.setAlignment(QtCore.Qt.AlignTop)
        self.whitepoints.setWordWrap(True)
        self.whitepoints.setStyleSheet("color: black;")
        self.whitepoints.setObjectName("whitepoints")
        self.blackpoints = QtWidgets.QLabel(self.centralwidget)
        self.blackpoints.setGeometry(QtCore.QRect(1450, 300, 400, 100))
        font = QtGui.QFont()
        font.setPointSize(12)
        self.blackpoints.setFont(font)
        self.blackpoints.setAlignment(QtCore.Qt.AlignTop)
        self.blackpoints.setWordWrap(True)
        self.blackpoints.setStyleSheet("color: white;")
        self.blackpoints.setObjectName("blackpoints")
        self.opening = QtWidgets.QLabel(self.centralwidget)
        self.opening.setGeometry(QtCore.QRect(1450, 400, 400, 100))
        font = QtGui.QFont()
        font.setPointSize(15)
        self.opening.setFont(font)
        self.opening.setAlignment(QtCore.Qt.AlignTop)
        self.opening.setWordWrap(True)
        self.opening.setStyleSheet("color: white;")
        self.opening.setObjectName("opening")
        self.movehistory = QtWidgets.QLabel(self.centralwidget)
        self.movehistory.setGeometry(QtCore.QRect(1450, 500, 400, 100))
        font = QtGui.QFont()
        font.setPointSize(12)
        self.movehistory.setFont(font)
        self.movehistory.setAlignment(QtCore.Qt.AlignTop)
        self.movehistory.setWordWrap(True)
        self.movehistory.setStyleSheet("color: white;")
        self.movehistory.setObjectName("movehistory")
        Janela.setCentralWidget(self.centralwidget)
        self.retranslateUi()
        QtCore.QMetaObject.connectSlotsByName(Janela)
        checktime(whites, firstmovewhite, firstmoveblack)

    # HTML Conversion
    def retranslateUi(self):
        _translate = QtCore.QCoreApplication.translate
        Janela.setWindowTitle(_translate("Janela", "Remote PyChess"))
        self.whites.setText(_translate(
            "Janela", f"<html><head/><body><p><span style=\" font-size:25pt;\">{white['username']}</span></p></body></html>"))
        self.whiteselo.setText(_translate(
            "Janela", f"<html><head/><body><p><span style=\" font-size:36pt;\">ELO: {white['rating']}</span></p></body></html>"))
        self.blackselo.setText(_translate(
            "Janela", f"<html><head/><body><p><span style=\" font-size:36pt;\">ELO: {black['rating']}</span></p></body></html>"))
        self.blacks.setText(_translate(
            "Janela", f"<html><head/><body><p><span style=\" font-size:25pt;\">{black['username']}</span></p></body></html>"))
        self.blackstime.setText(_translate(
            "Janela", f"<html><head/><body><p>{convert(secondsblack)}</p></body></html>"))
        self.whitestime.setText(_translate(
            "Janela", f"<html><head/><body><p>{convert(secondswhite)}</p></body></html>"))
        self.whitepoints.setText(_translate(
            "Janela", f"<html><head/><body><p><span style='text-shadow: 2px 2px #ffffff'>{piecesblack}</span><br><span style='color:white;'>{pointswhite} ponto(s)</span></p></body></html>"))
        self.blackpoints.setText(_translate(
            "Janela", f"<html><head/><body><p>{pieceswhite}<br><span style='color:white;'>{pointsblack} ponto(s)</span></p></body></html>"))
        self.opening.setText(_translate(
            "Janela", f"<html><head/><body><p>Abertura - {opening}<br></body></html>"))
        self.movehistory.setText(_translate(
            "Janela", f"<html><head/><body><p>{movehistory}<br></body></html>"))


# App Execution
if __name__ == "__main__":
    import sys
    whiteid, blackid, white, black = getUsers(gameid)
    whiteelodif, blackelodif = getElo(white, black)
    app = QtWidgets.QApplication(sys.argv)
    Janela = QtWidgets.QMainWindow()
    ui = Ui_Janela()
    changestate(gameid)
    ui.setupUi()
    Janela.showFullScreen()
    sys.exit(app.exec())
