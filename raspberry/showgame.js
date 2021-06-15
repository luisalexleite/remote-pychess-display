function showgame(key, app) {

    var firestore = firebase.firestore(app);

    setTimeout(mostrarjogo, 2000);
    setTimeout(startgame, 10000);


    function mostrarjogo() {
        var ref = firebase.database().ref("games/" + key);
        ref.once("value")
            .then(function(snapshot) {
                val = snapshot.val();

                function alterarmenu() {
                    $(".wrapper").slideUp(3000);
                    setTimeout(menujogadores, 3200);
                }

                function menujogadores() {
                    $(".jogadores").show().animate({ opacity: '1' }, 2500);
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

    function startgame() {
        window.location = 'http://localhost:8000/startgame.php?id=' + key
    }
}