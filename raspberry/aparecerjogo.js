function aparecerjogo(val, app){

    firebase.analytics();
    var firestore = firebase.firestore(app);


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
        }
      }).catch((error) => {
        console.log("Erro:", error);
      })

      if (val['type'] == 0) {
        document.getElementById("tipojogo").innerText = "Blitz";
        document.getElementById("timer").innerText = "5 : 00";
      } else if (val['type'] == 1) {
        document.getElementById("tipojogo").innerText = "Rápido";
        document.getElementById("timer").innerText = "15 : 00";
      } else if (val['type'] == 2) {
        document.getElementById("tipojogo").innerText = "Normal";
        document.getElementById("timer").innerText = "30 : 00";
      }
      sleep(parseInt(10000))
}