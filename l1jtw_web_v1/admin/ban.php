<?php
include_once("setup.php");

if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$acc_name = $_GET['account'];
	$rs1=CT("SELECT * FROM `accounts` WHERE `login` ='".$acc_name."'"); // 抓出login符合$acc_name的資料
	$rsc1=count($rs1); // 計算符合的筆數
	$c=0; // 增加accounts計數值預設為0

	if($rs1[$c][6]==0){
		$upsql = "update accounts set banned=1 where login='$acc_name'";
	}else{
		$upsql = "update accounts set banned=0 where login='$acc_name'";
	}
	FT($upsql);
	header("refresh: 0; url=search.php?account=$acc_name");
}else{
	header("refresh: 0; url=index.php");
}
?>