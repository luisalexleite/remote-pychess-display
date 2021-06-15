  $(document).ready(function() {
      $(window).scrollTop(0);
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

      var ref = database.ref('games');

      ref.on('child_added', (data) => {
          var val = data.val();
          var key = data.key;
          var state = val['state'];
          if (state == 0) {
              showgame(key, app);
          }
      });

      alterarEcra();
  });