//Abertura Aleatória
var hint = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;

//Pausar Execução de uma função assincrona durante x ms
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

//Dados da Abertura
async function alterarEcra() {
    while (true) {
        //Dados
        document.getElementById("title").innerText = hints[hint]['title'];
        document.getElementById("peqtext").innerText = hints[hint]['text'];
        document.getElementById("board").src = hints[hint]['img'];
        document.getElementById("movimentos").innerText = hints[hint]['moves'];

        //Pause x ms
        await sleep(parseInt(hints[hint]['time'] + "000"))

        //Evitar Repetições
        do {
            random = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;
        }
        while (random == hint)

        hint = random;
    }
}