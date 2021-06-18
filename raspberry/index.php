<!-- MaterializeCSS -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

<!-- Icons -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<!-- Firebase -->
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-app.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-analytics.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-auth.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-database.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-firestore.js"></script>

<!-- JQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>

<!-- Estilos -->
<style>
     /* Fonte Aleo - Google Fonts */
     @import url('https://fonts.googleapis.com/css2?family=Aleo&display=swap');

     /*Ecrã inicial*/
     .loader {
          background-color: #2d2f30;
          position: fixed;
          width: 100%;
          height: 100%;
          z-index: 1;
          display: flex;
          align-items: center;
          justify-content: center;
     }

     #board {
          height: 300px;
          width: 300px;
     }

     body {
          background-color: #2d2f30;
          font-family: "Aleo";
          overflow-y: hidden;
     }

     .wrapper {
          background-color: #2d2f30;
          text-align: center;
     }

     #logo {
          margin-top: -60px;
     }

     #mens {
          color: #FFFFFF;
          font-size: 30px;
     }

     #board {
          height: 300px;
          width: 300px;
     }

     .card-panel {
          background-color: #de6464;
     }

     #peqtext {
          font-size: 20px;
     }

     /*Animação - Três Pontos (Ecrã Inicial)*/
     @keyframes blink {

          0% {
               opacity: .2;
          }

          20% {
               opacity: 1;
          }

          100% {
               opacity: .2;
          }
     }

     #mens span {
          animation-name: blink;
          animation-duration: 1.4s;
          animation-iteration-count: infinite;
          animation-fill-mode: both;
     }

     #mens span:nth-child(2) {
          animation-delay: .2s;
     }

     #mens span:nth-child(3) {
          animation-delay: .4s;
     }

     #mens span {
          font-size: 50px;
     }

     /*Início do Jogo*/
     .img {
          height: 250px;
          width: 250px;
     }

     .jogadores {
          text-align: center;
          display: none;
          opacity: 0;
     }
</style>

<!-- Loader Inicial -->
<div class="loader">
     <div class="preloader-wrapper big active">
          <div class="spinner-layer spinner-red-only">
               <div class="circle-clipper left">
                    <div class="circle"></div>
               </div>
               <div class="gap-patch">
                    <div class="circle"></div>
               </div>
               <div class="circle-clipper right">
                    <div class="circle"></div>
               </div>
          </div>
     </div>
</div>

<!-- Ecrã Inicial -->
<div class="wrapper">
     <img id='logo' src='img/logo.png'>
     <br>
     <div id="mens">À espera de um jogo<span>.</span><span>.</span><span>.</span></div>
     <br>
     <img id="board" src="img/siciliana.gif">
     <br>
     <br>
     <br>
     <div class="container">
          <div class="card-panel">
               <span class="white-text">
                    <h4 style="text-align:left"><span style="font-size:30px" class="material-icons">
                              info
                         </span> <span id="title">Defesa Siciliana</span></h4>
                    <p id="peqtext" style="text-align:left">A defesa siciliana é uma das principais respostas para e4. As pretas assumem o controle da quadrado d4 com um peão lateral, assim ele desequilibra a posição e evita dar às brancas um alvo central.
                    <p id="movimentos" style="text-align:left">1.e4 c5
               </span>
               </p>
          </div>
     </div>
</div>

<!-- Ecrã Inicio do Jogo -->
<div class="jogadores">
     <img id='logo' src='img/logo.png'>
     <br>
     <br>
     <br>
     <div class="container">
          <div class="row">
               <div class="col s4">
                    <h5 style="color:white; text-align:left;" id="brancas">Nome</h5>
               </div>
               <div class="col s4"></div>
               <div class="col s4">
                    <h5 style="color:white; text-align:left;" id="pretas">Nome</h5>
               </div>
          </div>
          <div class="row">
               <div class="col s4 whites "> <img class="img" id="imgbrancas"></img></div>
               <div class="col s4" style="margin-top:75px">
                    <h1 style="color:white">VS.</h1>
               </div>
               <div class="col s4 blacks "> <img class="img" id="imgpretas"></img></div>
          </div>
          <div class="row">
               <div class="col s4">
                    <h5 style="color:white; text-align:left;" id="ratingbrancas">Elo: Ind.</h5>
               </div>
               <div class="col s4"></div>
               <div class="col s4">
                    <h5 style="color:white; text-align:left;" id="ratingpretas">Elo: Ind.</h5>
               </div>
          </div>
          <div class="row">
               <div class="col s4"><img src="img/Whites.png" style="height: 100px; width:250px;"></img></div>
               <div class="col s4"></div>
               <div class="col s4"><img src="img/Blacks.png" style="height: 100px; width:250px;"></img></div>
          </div>
          <div class="row">
               <div class="col s12">
                    <h3 style="color:white" id="tipojogo"> Tipo de Jogo </h3>
               </div>
               <div class="col s12">
                    <h5 style="color:white" id="timer"> Timer </h5>
               </div>
          </div>
     </div>
</div>

<!-- Aberturas -->
<script src="hints.js"></script>
<script src="hintsexec.js"></script>

<!-- Iniciar o Jogo -->
<script src="showgame.js"></script>

<!-- JS Principal -->
<script src="main.js"></script>