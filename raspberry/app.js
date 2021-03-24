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

  function wait(ms)
{
    var d = new Date();
    var d2 = null;
    do { d2 = new Date(); }
    while(d2-d < ms);
}

function check(key) {
  database.ref("games/" + key + "/state" ).get().then(function(snapshot) {
    console.log(snapshot.val());
     if (snapshot.val() == 0) {
       //firebase.database().ref('inline/' + Date.now()) .set({
         //game : key
       //});
      
     }
});
}
  var ref = database.ref('games');
  ref.on('child_added', (data) => {
       var key = data.key;
       var val = data.val();
       console.log(key);
       console.log(val);
       var state = val['state'];
       console.log(state);
       if (state == 0) {
          window.location.href = 'http://localhost:8000/startgame.php?id=' + key;
       }
  });
