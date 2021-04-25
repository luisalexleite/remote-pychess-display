<?php

exec("python3 remotepychess.py " . $_GET['id'] . " 2>&1");

echo "window.location = http://localhost:8000;";