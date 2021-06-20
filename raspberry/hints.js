//Dados das Aberturas
var hints = {
    "1": {
        "title": "Defesa Siciliana",
        "text": "A Defesa Siciliana é uma das principais respostas para e4. As pretas assumem o controle da quadrado d4 com um peão lateral, assim ele desequilibra a posição e evita dar às brancas um alvo central.",
        "img": "img/siciliana.gif",
        "moves": "1.e4 c5",
        "time": 9
    },
    "2": {
        "title": "Defesa Francesa",
        "text": "A Defesa Francesa encontra e4 com e6, preparando-se para contra-atacar o peão e4 com d5. O preto bloqueia em seu bispo de quadrado claro, mas ganha uma sólida cadeia de peões e possibilidades de contra-ataque. A defesa francesa tem o nome de um jogo de correspondência de 1834 entre as cidades de Londres e Paris, no qual a defesa francesa foi utilizada.",
        "img": "img/francesa.gif",
        "moves": "1.e4 e6",
        "time": 9
    },
    "3": {
        "title": "Defesa Eslava",
        "text": "Com a Defesa Eslava, o peão c6 suporta o peão atacado em d5. Desta maneira, ele é capaz de recapturar em d5 com o peão, mantendo um ponto central forte.",
        "img": "img/defesaeslava.gif",
        "moves": "1.d4 d5 2.c4 c6",
        "time": 9
    },
    "4": {
        "title": "Benko Gambit",
        "text": "A Benko Gambit é uma resposta especial a d4 onde a preta sacrifica um peão de imediato. Se a branca jogar 4.cxb5 e depois 4...a6 normalmente segue. Bem executado, permite grande pressão no lado da rainha",
        "img": "img/benkogambit.gif",
        "moves": "1.d4 Nf6 2.c4 c5 3.d5 b5",
        "time": 9
    },
    "5": {
        "title": "Defesa India do Rei",
        "text": "Uma defesa hiper moderna que involve o fianqueto (padrão de desenvolvimento de peças onde o bispo é desenvolvido na segunda linha do cavalo adjacente) do bispo do rei das pretas.",
        "img": "img/defesaindiarei.gif",
        "moves": "1.d4 Nf6 2.c4 g6",
        "time": 9
    },
    "6": {
        "title": "Defesa de Benoni",
        "text": "A Benoni é muito popular em nivel amador, além de ser casualmente utilizada em níveis mais elevados, com Fabiano Caruana e Maxime Vachier-Lagravevery a utilizar a defesa em diversos torneios durante o ano 2019.",
        "img": "img/defesabenoni.gif",
        "moves": "1.d4 Nf6 2.c4 c5",
        "time": 9
    },
    "7": {
        "title": "Abertura Ruy López",
        "text": "Tambem conhecido com Jogo Espanhol, a jogada Ruy Lopez é uma das aberturas mais populares do xadrez.",
        "img": "img/aberturaruylopez.gif",
        "moves": "1.e4 e5 2.Nf3 Nc6 3.Bb5",
        "time": 9
    },
    "9": {
        "title": "Gambito da Dama",
        "text": "Uma das variações fundamentais d 1.d4 é a Gambito da Dama. As Brancas imediatamente atacam com o peão preto central do lado.",
        "img": "img/gambitodama.gif",
        "moves": "1.d4 d5 2.c4",
        "time": 9
    },
    "9": {
        "title": "Jogo Escocês",
        "text": "Em vês de desenvolver outra peça, no jogo escocês, as brancas escolhes abrir o centro desafiando o peão e5.",
        "img": "img/escoces.gif",
        "moves": "1.e4 e5 2.Nf3 Nc6 3.d4",
        "time": 9
    },
    "10": {
        "title": "Jogo Italiano",
        "text": "No jogo Italiano, as Brancas desenvolvem uma presença forte com um bispo diagonal, apontado ao centro em f7.",
        "img": "img/italiano.gif",
        "moves": "1.e4 e5 2.Nf3 Nc6 3.Bc4",
        "time": 9
    },
    "11": {
        "title": "Jogo de Vienna",
        "text": "No jogo de Vienna, as brancas concentram-se na defesa do seu próprio peão.",
        "img": "img/vienna.gif",
        "moves": "1.e4 e5 2.Nc3",
        "time": 9
    },
    "12": {
        "title": "Defesa de Grunfeld",
        "text": "A defesa de Grunfeld combina o fianqueto de bispo do rei com d5. Neste caso, o peão é normalmente trocado por outra peça.",
        "img": "img/grunfeld.gif",
        "moves": "1.d4 Nf6 2.c4 g6 3.Nc3 d5",
        "time": 9
    },
    "13": {
        "title": "Ataque Trompowsky",
        "text": "Uma das aberturas menos comuns , o Trompowsky desenvolve o bispo para um local atigo com itenção de capturar em f6.",
        "img": "img/trompowsky.gif",
        "moves": "1.d4 Nf6 2.Bg5",
        "time": 9
    }
};