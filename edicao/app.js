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
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();

  var database = firebase.database();
  var firestore = firebase.firestore();

  var ref = database.ref('games');
  
  ref.on('child_added', (data) => {
       var key = data.key;
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
      
        setTimeout(queryjogadores, 3000);

        var whites = val["whites"];
        var blacks = val["blacks"];

        document.getElementById("brancas").innerText = whites.value;
        
        //firebase.database().ref('inline/' + Date.now()) .set({
          //game : key
        //});
          window.location = 'http://localhost:8000/startgame.php?id=' + key;
       }
  });

  alterarEcra();
});