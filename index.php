<?php
    $con = mysqli_connect("localhost", "ase1", "rainforest12","android");
    
    $lat = $_POST["lat"];
    $lang = $_POST["lang"];
    $did = $_POST["did"];
    
    $statement = mysqli_prepare($con, "INSERT INTO atable (lat, lang, did) VALUES (?, ?, ?)");
	mysqli_stmt_bind_param($statement, "sss", $lat, $lang, $did);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $lat, $lang, $did);
    
    $response = array();
    $response["success"] = false;
	
	$statement = mysqli_prepare($con, "SELECT * FROM atable WHERE lat = ?");
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
