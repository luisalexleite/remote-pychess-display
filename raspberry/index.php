<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<!-- The core Firebase JS SDK is always required and must be listed first -->
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-app.js"></script>

<!-- TODO: Add SDKs for Firebase products that you want to use
     https://firebase.google.com/docs/web/setup#available-libraries -->
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-analytics.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-auth.js"></script>
<script src="https://www.gstatic.com/firebasejs/8.2.10/firebase-database.js"></script>

<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Aleo&display=swap');
body {
     background-color: #2d2f30;
     font-family: "Aleo";
}
.wrapper{
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
</style>
<div class="wrapper">
<img id='logo' src='img/logo.png'>
<br>
<div id="mens">À espera de um jogo...</div>
<br>
<img id="board" src="img/siciliana.gif">
<br>
<br>
<br>
<div class="container">
<div class="card-panel">
     <span class="white-text"><h4 style="text-align:left"><span style="font-size:30px" class="material-icons">
info
</span> <span id="title">Defesa Siciliana</span></h4>
<p id="peqtext" style="text-align:left">A defesa siciliana é uma das principais respostas para e4. As pretas assumem o controle da quadrado d4 com um peão lateral, assim ele desequilibra a posição e evita dar às brancas um alvo central.</span>
</p>
      </div>
</div>
</div>
<script src="hints.js"></script>
<script src="hintsexec.js"></script>
<script src="app.js"></script>
