<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!-- The core Firebase JS SDK is always required and must be listed first -->
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-app.js"></script>

<!-- TODO: Add SDKs for Firebase products that you want to use
     https://firebase.google.com/docs/web/setup#available-libraries -->
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-analytics.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-auth.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-database.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-firestore.js"></script>

<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<style>
     @import url('https://fonts.googleapis.com/css2?family=Aleo&display=swap');

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

     .img {
          height: 250px;
          width: 250px;
     }

     .jogadores {
          text-align: center;
          display: none;
          opacity: 0;
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

     @keyframes blink {

          /**
     * At the start of the animation the dot
     * has an opacity of .2
     */
          0% {
               opacity: .2;
          }

          /**
     * At 20% the dot is fully visible and
     * then fades out slowly
     */
          20% {
               opacity: 1;
          }

          /**
     * Until it reaches an opacity of .2 and
     * the animation can start again
     */
          100% {
               opacity: .2;
          }
     }

     #mens span {
          /**
     * Use the blink animation, which is defined above
     */
          animation-name: blink;
          /**
     * The animation should take 1.4 seconds
     */
          animation-duration: 1.4s;
          /**
     * It will repeat itself forever
     */
          animation-iteration-count: infinite;
          /**
     * This makes sure that the starting style (opacity: .2)
     * of the animation is applied before the animation starts.
     * Otherwise we would see a short flash or would have
     * to set the default styling of the dots to the same
     * as the animation. Same applies for the ending styles.
     */
          animation-fill-mode: both;
     }

     #mens span:nth-child(2) {
          /**
     * Starts the animation of the third dot
     * with a delay of .2s, otherwise all dots
     * would animate at the same time
     */
          animation-delay: .2s;
     }

     #mens span:nth-child(3) {
          /**
     * Starts the animation of the third dot
     * with a delay of .4s, otherwise all dots
     * would animate at the same time
     */
          animation-delay: .4s;
     }

     #mens span {
          font-size: 50px;
     }
</style>
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
               </span>
               </p>
          </div>
     </div>
</div>
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
<script src="hints.js"></script>
<script src="hintsexec.js"></script>
<script src="showgame.js"></script>
<script src="app.js"></script>