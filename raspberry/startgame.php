<?php

exec("python3 remotepychess.py " . $_GET['id'] . " 2>&1");

echo "<script> window.close('','_parent','') </script>";