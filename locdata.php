<?php
    $con = mysqli_connect("localhost", "root", "dinesh","mydb");
    
    $lat = $_POST["lat"];
    $lang = $_POST["lang"];
    
    $statement = mysqli_prepare($con, "INSERT INTO table1 (lat, lang) VALUES (?, ?)");
	mysqli_stmt_bind_param($statement, "ss", $lat, $lang);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $lat, $lang);
    
    $response = array();
    $response["success"] = false;
	
	$statement = mysqli_prepare($con, "SELECT * FROM table1 WHERE lat = ?");
	mysqli_stmt_bind_param($statement, "s", $lat);
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	$count = mysqli_stmt_num_rows($statement);
	mysqli_stmt_close($statement); 
	if($count>1){
		$response["success"] = true;
	}else{
		$response["success"] = false;
	}
    
    
    echo json_encode($response);
?>