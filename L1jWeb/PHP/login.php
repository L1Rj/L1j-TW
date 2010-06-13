<?
require("setup.php");
$id=$_POST[id];
$d_pass=$_POST[pass];
$e_id=hash('md5', $id);
$e_pass=hash('sha256', "$d_PasswordSalt$d_pass$e_id");
// $e_pass=base64_encode(mhash(MHASH_SHA1,$d_pass)); //舊的密碼寫法
$cookietime=$_POST[cookietime];

if($cookietime==1){
	$cookieday=1;
}elseif($cookietime==2){
	$cookieday=7;
}elseif($cookietime==3){
	$cookieday=30;
}else{
	$cookieday=365;
}

mysql_select_db($sql_dbname, $login_on);
$straa=sprintf("SELECT * FROM accounts WHERE `login` LIKE '$id' ORDER BY `login` ASC");
$strab=mysql_query($straa, $login_on) or die(mysql_error());
$strac=mysql_fetch_assoc($strab);
$id=$strac['login'];
$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);

Session_start();
if($_SESSION["Checknum"] == $_POST['checknum']) {
	if($chk_id!=0){
		setcookie("linsfuserid", "$id", strtotime("+$cookieday days"));
		setcookie("linsfuserpass", "$e_pass", strtotime("+$cookieday days"));
		$str="update user_register set logintime='$time' , loginip='$ip' where name='$id'";
		$db->query($str);
		header("Location:html/menu.php");
	}else{
		header("Location:html/menu.php?error_msg=1");
	}
}else{
	header("Location:html/menu.php?error_msg=2");
}
?>