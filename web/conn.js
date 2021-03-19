  // Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
  var firebaseConfig = {
    apiKey: "AIzaSyBqUAS_fYtJDWXIaQdf2MR1yU_Ya808iJw",
    authDomain: "remote-pychess.firebaseapp.com",
    databaseURL: "https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app",
    projectId: "remote-pychess",
    storageBucket: "remote-pychess.appspot.com",
    messagingSenderId: "914254287981",
    appId: "1:914254287981:web:5fc544c7daa4ec110a5b64",
    measurementId: "G-TRD3REPTL0"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();

  var ref = firebase.database().ref("games/1/");
  ref.once("value")
  .then(function(snapshot) {
    var key = snapshot.key; // "ada"
    console.log(key)
    var childKey = snapshot.child("blacks").val(); // "last"
    console.log(childKey)
  });