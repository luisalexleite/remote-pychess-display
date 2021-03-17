  var firebaseConfig = {
    apiKey: "AIzaSyBV9Nd5X1iF7jwiK72x9MIoUHf2Sn5NyZQ",
    authDomain: "bbchess-720c1.firebaseapp.com",
    databaseURL: "https://bbchess-720c1-default-rtdb.europe-west1.firebasedatabase.app",
    projectId: "bbchess-720c1",
    storageBucket: "bbchess-720c1.appspot.com",
    messagingSenderId: "328789090053",
    appId: "1:328789090053:web:59b78044e274b74c0ea16c",
    measurementId: "G-MRX1V5YS74"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);

  var ref = firebase.database().ref("games/1/");
  ref.once("value")
  .then(function(snapshot) {
    var key = snapshot.key; // "ada"
    console.log(key)
    var childKey = snapshot.child("blacks").val(); // "last"
    console.log(childKey)
  });