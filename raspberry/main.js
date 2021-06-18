  //Quando a página estiver totalmente renderizada
  $(document).ready(function() {

      //Mover para cima
      $(window).scrollTop(0);

      //Retirar o loader
      $(".loader").fadeOut(2000);

      //Configuração da Firebase
      var firebaseConfig = {
          apiKey: "AIzaSyBqUAS_fYtJDWXIaQdf2MR1yU_Ya808iJw",
          authDomain: "remote-pychess.firebaseapp.com",
          databaseURL: "https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app",
          projectId: "remote-pychess",
          storageBucket: "remote-pychess.appspot.com",
          messagingSenderId: "914254287981",
          appId: "1:914254287981:web:091c8214af93fd480a5b64",
          measurementId: "G-YX8YYWSDH0"
      };

      //Iniciar todas as funções da Firebase
      var app = firebase.initializeApp(firebaseConfig);

      //Dados Anliticos de Utilização
      firebase.analytics();

      //Realtime Database
      var database = firebase.database();

      //Tabela onde estão os jogos a iniciar
      var ref = database.ref('games');

      //Quando um "filho" é adicionado à tabela
      ref.on('child_added', (data) => {

          //dados e chave do que foi inserido
          var val = data.val();
          var key = data.key;

          //estado do jogo
          var state = val['state'];

          //se o jogo não tiver iniciado
          if (state == 0) {

              //função para iniciar o jogo
              showgame(key, app);
          }
      });

      //mostrar diferentes aberturas no ecrã inicial
      alterarEcra();
  });