<?php

//Executar o remote-pychess.py com o código obtido
exec("python3 remotepychess.py " . $_GET['id'] . " 2>&1");

//Voltar para o Ecrã Inicial após o término do programa
echo "<script>window.location = 'http://localhost:8000';</script>";
