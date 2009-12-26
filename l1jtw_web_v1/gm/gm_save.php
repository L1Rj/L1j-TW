<?
require("../setup.php");
$code=$_POST[code];
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
$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level='200'";
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

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤(非法執行)<br>";
}

if($open_gm!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$objid=$_POST[objid];

	$str="select count(*) from characters where objid='$objid' and OnlineStatus='1'";
	$chk_ol=$db->get_var($str);
	if($chk_ol!=0){
		$err_count++;
		$msg=$msg."人物尚未離線<br>";
	}
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `objid` LIKE '$objid' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);

	if($strac['AccessLevel']==200){
		$str="update characters set AccessLevel=0 where objid='$objid'";
		$db->query($str);
	}else{
		$str="update characters set AccessLevel=200 where objid='$objid'";
		$db->query($str);
	}
	$msg=$msg."修改完成<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</table>
</center>
</body>
</html>