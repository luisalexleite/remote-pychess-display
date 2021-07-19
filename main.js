  //When page loads...
  $(document).ready(function() {

      //Going Up
      $(window).scrollTop(0);

      //Take loader after 2 sec
      $(".loader").fadeOut(2000);

      //Firebase Config
      var firebaseConfig = {
          //Get firebase config from - https://console.firebase.google.com/project/project-name/settings/general/web
      };

      //Start Firebase
      var app = firebase.initializeApp(firebaseConfig);

      //Analytics Data
      firebase.analytics();

      //Realtime Database
      var database = firebase.database();

      //Game DB
      var ref = database.ref('games');

      //Child Added on Game DB
      ref.on('child_added', (data) => {

          //Data and Key
          var val = data.val();
          var key = data.key;

          //Game State
          var state = val['state'];

          //If game hasn't started
          if (state == 0) {

              //Start Game
              showgame(key, app);
          }
      });

      //Openings
      changeOpening();
  });
