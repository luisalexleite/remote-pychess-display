function showgame(key, app) {

    //Firestore
    var firestore = firebase.firestore(app);

    //Mostrar o proximo jogo após 2 segundos
    setTimeout(mostrarjogo, 2000);

    //Começar o proximo jogo após 10 segundos
    setTimeout(startgame, 10000);

    //Mostrar o proximo jogo
    function mostrarjogo() {

        //localização da chave recebida pela função
        var ref = firebase.database().ref("games/" + key);

        //Quando obter dados
        ref.once("value")
            .then(function(snapshot) {
                val = snapshot.val();

                //Retirar Ecrã Inicial
                function alterarmenu() {
                    $(".wrapper").slideUp(3000);
                    setTimeout(menujogadores, 3200);
                }

                //Aparecer Próximo Jogo
                function menujogadores() {
                    $(".jogadores").show().animate({ opacity: '1' }, 2500);
                }

                //Retirar Ecrã Inicial
                alterarmenu();


                //Dados dos Jogadores
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

                //Tipo de Jogo
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

    //Começar o Jogo
    function startgame() {
        window.location = 'http://localhost:8000/startgame.php?id=' + key
    }
}