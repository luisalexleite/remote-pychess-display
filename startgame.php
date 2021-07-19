<?php

//Execute Python
exec("python3 remotepychess.py " . $_GET['id'] . " 2>&1");

//Loading Screen - after program finishes execution
echo "<script>window.location = 'http://localhost:8000';</script>";
