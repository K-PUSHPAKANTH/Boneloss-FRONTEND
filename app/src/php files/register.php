<?php
header("Content-Type: application/json");
include "db.php";

// Get raw JSON data
$data = json_decode(file_get_contents("php://input"), true);

// Validate input
if (
    empty($data['full_name']) ||
    empty($data['email']) ||
    empty($data['phone']) ||
    empty($data['password'])
) {
    echo json_encode([
        "status" => "error",
        "message" => "All fields are required"
    ]);
    exit;
}

$full_name = $data['full_name'];
$email     = $data['email'];
$phone     = $data['phone'];
$password  = password_hash($data['password'], PASSWORD_DEFAULT);

// Check if email already exists
$check = $conn->prepare("SELECT id FROM users WHERE email = ?");
$check->bind_param("s", $email);
$check->execute();
$check->store_result();

if ($check->num_rows > 0) {
    echo json_encode([
        "status" => "error",
        "message" => "Email already registered"
    ]);
    exit;
}

// Insert user
$stmt = $conn->prepare(
    "INSERT INTO users (full_name, email, phone, password) VALUES (?, ?, ?, ?)"
);
$stmt->bind_param("ssss", $full_name, $email, $phone, $password);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "success",
        "message" => "Registration successful"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Registration failed"
    ]);
}

$conn->close();
?>
