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
  var gamelist = [];
  var wait = [];
  var side = ['whites', 'blacks']

  function length (arr) {
    if (arr == null) {
      return 0;
    } else if (Object.keys(arr).length == 0) {
      console.log('teste');
      return 0;
    } else if (Object.keys(arr[Object.keys(arr).length -1]).length == 0 || Object.keys(arr[Object.keys(arr).length -1]).length == 3) {
      return 0;
    }
    else{
      return 1;
    }
  }

  function random(side) {
    return side[Math.floor(Math.random() * side.length)]
  }

  function checkLine() {
    //ver se alguum dos que estão na fila corresponde aos critérios de escolha
  }

  var search = database.ref('search');
  search.on('child_added', (data) => {
    var val = data.val();
    var key = data.key;
    console.log(val['user']);
    if(val['state'] == 0) {
    if (length(gamelist) == 0) {
      if(wait.length == 0) {
      var game = [];
      if (val['side'] == 'random') {
      game[random(side)] = val['user'];
      game['type'] = val['type'];
      gamelist.push(game);
    } else {
        game[val['side']] = val['user'];
        game['type'] = val['type'];
        gamelist.push(game);
    }
  } else {
    if (wait[0]['side'] == 'random') {
      game[random(side)] = wait['user'];
      game['type'] = wait['type'];
      gamelist.push(game);
      wait.shift();
      var push = [];
      push['user'] = val['user'];
      push['type'] = val['type'];
      push['side'] = val['side'];
      wait[Object.keys(wait).length] = push;
    } else {
        game[val['side']] = wait['user'];
        game['type'] = wait['type'];
        gamelist.push(game);
        wait.shift();
        var push = [];
        push['user'] = val['user'];
        push['type'] = val['type'];
        push['side'] = val['side'];
        wait[Object.keys(wait).length] = push;
    }
  }
    } else {
      //falta CheckLine();
      if(val['type'] == gamelist[Object.keys(gamelist).length -1]['type']) {
        var game = gamelist[Object.keys(gamelist).length -1];
        if (val['side'] == 'random') {
          if(game ['whites'] == null) {
            game['whites'] = val['user'];
          } else {
            game['blacks'] = val['user'];
          }
          game['type'] = val['type'];
        } else {
          if(game[val['side']] == null) {
            game[val['side']] = val['user'];
            game['type'] = val['type'];
          } else {
            var push = [];
            push['user'] = val['user'];
            push['type'] = val['type'];
            push['side'] = val['side'];
            wait[Object.keys(wait).length] = push;
          }
        }
      } else {
            var push = [];
            push['user'] = val['user'];
            push['type'] = val['type'];
            push['side'] = val['side'];
            wait[Object.keys(wait).length] = push;
      }
    }
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
       }
  });

  alterarEcra();