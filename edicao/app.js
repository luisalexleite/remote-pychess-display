  $(document).ready(function(){
  $(".loader").fadeOut(2000);

  // Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
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

  // Initialize Firebase
  var app = firebase.initializeApp(firebaseConfig);
  firebase.analytics();

  var database = firebase.database();
  var firestore = firebase.firestore(app);

  var ref = database.ref('games');

  ref.on('child_added', (data) => {
    setTimeout(dados, 8000);
    function dados() {
       var val = data.val();
       var state = val['state'];
       if (state == 0) {

        function alterarmenu() {
          $(".wrapper").slideUp(3000);
          setTimeout(menujogadores, 3200);
          }
        function menujogadores () {
          $(".jogadores").show().animate({opacity: '1'},2500);
        }
        $(".loader").fadeOut(2000);
        alterarmenu();


          fswhites = firestore.collection('profile').doc(val['whites']);
          fsblacks = firestore.collection('profile').doc(val['blacks']);

          fswhites.get().then((doc) => {
            if (doc.exists) {
              var array = doc.data()
              document.getElementById("brancas").innerText = array['username'];
              document.getElementById("imgbrancas").src = array['image'];
              document.getElementById("ratingbrancas").innerText = "Elo: " + array['rating'];
            } else {
              console.log("Uff!");
            }
          }).catch((error) => {
            console.log("Erro:", error);
          })

          fsblacks.get().then((doc) => {
            if (doc.exists) {
              var array = doc.data()
              document.getElementById("pretas").innerText = array['username'];
              document.getElementById("imgpretas").src = array['image'];
              document.getElementById("ratingpretas").innerText = "Elo: " + array['rating'];
            } else {
              console.log("Im gay!");
            }
          }).catch((error) => {
            console.log("Erro:", error);
          })


          if (val['type'] == 0) {
            document.getElementById("tipojogo").innerText = "Blitz";
            document.getElementById("timer").innerText = "5 : 00";
          } else if (val['type'] == 1) {
            document.getElementById("tipojogo").innerText = "Rápido";
            document.getElementById("timer").innerText = "10 : 00";
          } else if (val['type'] == 2) {
            document.getElementById("tipojogo").innerText = "Normal";
            document.getElementById("timer").innerText = "20 : 00";
          }
        }

        //firebase.database().ref('inline/' + Date.now()) .set({
          //game : key
        //});
         // window.location = 'http://localhost:8000/startgame.php?id=' + key;
       }
  });

  alterarEcra();
});
