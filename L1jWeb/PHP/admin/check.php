<?
include_once("setup.php");
$id=$_COOKIE["linsfuserid"];
$d_pass=$_COOKIE["linsfuserpass"];

if($id==$adminid && $d_pass==$adminpass){
	setcookie("linlogin","ok",time()+3600);
	header("Location:index.php");
}else{
	header("Location:index.php");
}
?>
