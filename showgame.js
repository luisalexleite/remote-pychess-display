function showgame(key, app) {

    //Firestore
    var firestore = firebase.firestore(app);

    //Show Next Game Info - after 2 sec
    setTimeout(showNextGame, 2000);

    //Start Next Game - after 10 sec
    setTimeout(startgame, 10000);

    //Show Next Game Info
    function showNextGame() {

        //Key Location
        var ref = firebase.database().ref("games/" + key);

        //When Data Exists
        ref.once("value")
            .then(function(snapshot) {
                val = snapshot.val();

                //Change to Game Data
                function changeScreen() {
                    $(".wrapper").slideUp(3000);
                    setTimeout(playerInfo, 3200);
                }

                //Show Player Info
                function playerInfo() {
                    $(".jogadores").show().animate({ opacity: '1' }, 2500);
                }

                //Change to Game Data
                changeScreen();


                //Player Data
                fswhites = firestore.collection('profile').doc(val['whites']);
                fsblacks = firestore.collection('profile').doc(val['blacks']);

                fswhites.get().then((doc) => {
                    if (doc.exists) {
                        var array = doc.data()
                        document.getElementById("brancas").innerText = array['username'];
                        document.getElementById("imgbrancas").src = array['image'];
                        document.getElementById("ratingbrancas").innerText = "Elo: " + array['rating'];
                    } else {
                        console.log("Sem Informação");
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

                //Game Type
                if (val['type'] == 0) {
                    document.getElementById("tipojogo").innerText = "Blitz";
                    document.getElementById("timer").innerText = "5 : 00";
                } else if (val['type'] == 1) {
                    document.getElementById("tipojogo").innerText = "Rápido";
                    document.getElementById("timer").innerText = "15 : 00";
                } else if (val['type'] == 2) {
                    document.getElementById("tipojogo").innerText = "Normal";
                    document.getElementById("timer").innerText = "40 : 00";
                }
            });
    }

    //Start Game
    function startgame() {
        window.location = 'http://localhost:8000/startgame.php?id=' + key
    }
}