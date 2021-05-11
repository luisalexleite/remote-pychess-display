var hint = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

var hint = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;

async function alterarEcra () {
    while (true) {
        document.getElementById("title").innerText = hints[hint]['title'];
        document.getElementById("peqtext").innerText = hints[hint]['text'];
        document.getElementById("board").src = hints[hint]['img'];
        hint = Math.floor(Math.random() * parseInt(Object.keys(hints).length)) + 1;
        await sleep(parseInt(hints[hint]['time'] + "000"))
        }
}