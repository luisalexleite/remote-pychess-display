<?php
require __DIR__ . "/vendor/autoload.php";
use Kreait\Firebase\Factory;

$factory = (new Factory)
    ->withServiceAccount('remote-pychess-f8ba9c6e343c.json')
    ->withDatabaseUri('https://remote-pychess-default-rtdb.europe-west1.firebasedatabase.app/');

$database = $factory->createDatabase();

$reference = $database->getReference('games/1/blacks');

$value = $reference->getValue();

echo $value;
