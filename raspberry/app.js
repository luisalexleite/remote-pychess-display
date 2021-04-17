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
  var game = [];
  var wait = [];
  var line = [];
  var side = ['whites', 'blacks'];

  function changeState(key) {
    firebase.database().ref('search/' + key).update({
      state: 1
    });
  }

  function newMatch(key, val, game, side) {
    if (val['state'] == 0) {
      if(val['side'] == 'random') {
        game[side[Math.floor(Math.random()*side.length)]] = val['user'];
        game['type'] = val['type'];
        changeState(key);
      } else {
        game[val['side']] = val['user'];
        game['type'] = val['type'];
        changeState(key);
      }
    }
  }

  function match(key, val, game) {
    if (val['state'] == 0) {
      if (val['type'] == game['type']) {
      if (val['side'] == 'random') {
        if (typeof game['whites'] == 'undefined') {
          game['whites'] = val['user'];
          line.push(game);
          changeState(key);
          game = [];
        } else {
          game['blacks'] = val['user'];
          line.push(game);
          changeState(key);
          game = [];
        }
      } else {
      if (game[val['side']] === 'undefined') {
        game[val['side']] = val['user'];
        line.push(game);
        changeState(key);
        game = [];
      } else {
        wait.push([val['side'], val['user'], val['type']]);
        changeState(key);
      }
    }
  }
  } else {
    wait.push([val['side'], val['user'], val['type']]);
    changeState(key);
  }
  }

  var search = database.ref('search');
  search.on('child_added', (data) => {
    var val = data.val();
    var key = data.key;
    
    if (Object.keys(game).length == 0) {
      if (Object.keys(wait).length == 0) {
      newMatch(key,val,game,side);
      } else {
        //verificar se existem elementos à espera
      }
    } else {
      match(key, val, game);
} 
});

  var ref = database.ref('games');
  ref.on('child_added', (data) => {
       var key = data.key;
       var val = data.val();
       var state = val['state'];
       if (state == 0) {
        //firebase.database().ref('inline/' + Date.now()) .set({
          //game : key
        //});
          window.open('http://localhost:8000/startgame.php?id=' + key, '_blank', 'scrollbars=no,resizable=no,status=no,location=no,toolbar=no,menubar=no,width=0,height=0,left=-1000,top=-1000');
          return false;
       }
  });

  alterarEcra();