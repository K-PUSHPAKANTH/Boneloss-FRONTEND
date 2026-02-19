<?php
$servername = "localhost";
$username   = "root";
$password   = "";
$database   = "boneloss";

// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Database Connection Failed: " . $conn->connect_error);
}

// Optional: success message (remove in production)
// echo "Database Connected Successfully";
?>
