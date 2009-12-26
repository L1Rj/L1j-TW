<?
require("../setup.php");
$code=$_POST[code];
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$name=$_POST[name];
$title=$_POST[title];
$notemsg=$_POST[msg];
$err_count=0;
$msg="";
?>
<html>

<head>
<meta http-equiv="Content-Language" content="utf8">
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
$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤(非法執行)<br>";
}

if($title==NULL || $notemsg==NULL){
	$err_count++;
	$msg=$msg."標題、內容請勿為空<br>";
}

$str="select count(*) from accounts where login='$id' and banned='1'"; 
$chk_lock=$db->get_var($str);
if($chk_lock==1){
	$err_count++;
	$msg=$msg."帳號鎖定中<br>";
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM characters WHERE `char_name` LIKE '$name' ORDER BY `objid` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
	$objid=$strac['objid'];

	$str="select count(*) from characters where account_name='$id' and objid='$objid'";
	$chk_gm=$db->get_var($str);
	if($chk_gm==0){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}
}

if($err_count==0){
	$str="insert into `zwls_notebook` (`account` , `name` , `title` , `msg` , `datetime` , `ip`) values ('$id','$name','$title','$notemsg','$time','$ip')";
	$db->query($str);
	$msg=$msg."記事完成<br>";
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