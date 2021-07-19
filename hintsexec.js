//Random Opening
var hint = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;

//Stop Execution during x ms
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

//Opening Data
async function changeOpening() {
    while (true) {
        //Data
        document.getElementById("title").innerText = hints[hint]['title'];
        document.getElementById("peqtext").innerText = hints[hint]['text'];
        document.getElementById("board").src = hints[hint]['img'];
        document.getElementById("movimentos").innerText = hints[hint]['moves'];

        //Pause x ms
        await sleep(parseInt(hints[hint]['time'] + "000"))

        //Avoid Same Opening
        do {
            random = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;
        }
        while (random == hint)

        hint = random;
    }
}