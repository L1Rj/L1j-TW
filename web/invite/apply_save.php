<?
require("../setup.php");
$code=$_POST[code];
$name=$_POST[name];
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
<?
if($name==NULL){
	$err_count++;
	$msg=$msg."分享專區標題不得為空<br>";
}

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from accounts where login='$id' and banned='1'"; 
$chk_lock=$db->get_var($str);
if($chk_lock==1){
	$err_count++;
	$msg=$msg."帳號鎖定中<br>";
}

if($open_invitezone!=1){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤(非法執行)<br>";
}

if($err_count==0){
	$bcode=rand(1000,9999);
	$str="select count(*) from zwls_invite_zone where account='$id'";
	$chk_ac=$db->get_var($str);
	if($chk_ac==1){
		$str="update zwls_invite_zone set name='$name' , code=$bcode where account='$id'";
		$db->query($str);
		mysql_select_db($sql_dbname, $login_on);
		$straa=sprintf("SELECT * FROM zwls_invite_zone WHERE `account` LIKE '$id' ORDER BY `no` ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
		$no=$strac['no'];
		$msg=$msg."您的註冊碼分享專區為：<br><a target=\"_blank\" href=\"$web_url/i.php?n=$no&c=$bcode\">$web_url/i.php?n=$no&c=$bcode</a><br>";
	}else{
		$str="insert into `zwls_invite_zone` (`account` , `code` , `name`) values ('$id','$bcode','$name')";
		$db->query($str);
		$msg=$msg."申請完成<br>";
		mysql_select_db($sql_dbname, $login_on);
		$straa=sprintf("SELECT * FROM zwls_invite_zone WHERE `account` LIKE '$id' ORDER BY `no` ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
		$no=$strac['no'];
		$msg=$msg."您的註冊碼分享專區為：<br><a target=\"_blank\" href=\"$web_url/i.php?n=$no&c=$bcode\">$web_url/i.php?n=$no&c=$bcode</a><br>";
	}
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</table>
</center>
</body>
</html>
