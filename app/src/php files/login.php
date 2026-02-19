<?php
header("Content-Type: application/json");

include "db.php";

// Read JSON input
$data = json_decode(file_get_contents("php://input"), true);

// Validate input
if (
    empty($data['email']) ||
    empty($data['password'])
) {
    echo json_encode([
        "status" => "error",
        "message" => "Email and password are required"
    ]);
    exit;
}

$email    = trim($data['email']);
$password = $data['password'];

// Check user & verification
$stmt = $conn->prepare(
    "SELECT id, full_name, password, is_verified 
     FROM users 
     WHERE email = ?"
);
$stmt->bind_param("s", $email);
$stmt->execute();

$result = $stmt->get_result();

if ($result->num_rows === 0) {
    echo json_encode([
        "status" => "error",
        "message" => "Email not registered"
    ]);
    exit;
}

$user = $result->fetch_assoc();

// Email verification check
if ($user['is_verified'] != 1) {
    echo json_encode([
        "status" => "error",
        "message" => "Email not verified"
    ]);
    exit;
}

// Password verification
if (!password_verify($password, $user['password'])) {
    echo json_encode([
        "status" => "error",
        "message" => "Invalid password"
    ]);
    exit;
}

// Success
echo json_encode([
    "status" => "success",
    "message" => "Login successful",
    "user" => [
        "id" => $user['id'],
        "full_name" => $user['full_name'],
        "email" => $email
    ]
]);

$conn->close();
?>
