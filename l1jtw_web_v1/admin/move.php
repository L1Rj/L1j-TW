<?php
include_once("setup.php");

if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$char_objid=$_GET['objid'];
	$upsql="Update `characters` set `locX`='33079',`locY`='33402',`MapID`='4' where `objid`='".$char_objid."'";
	$ch=FT($upsql);
	header("refresh: 0; url=index.php");
}else{
	header("refresh: 0; url=index.php");
}
?>