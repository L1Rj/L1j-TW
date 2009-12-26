<?
require("../setup.php");
$code=$_POST[code];
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$toid=$_POST[toid];
$mastername=$_POST[mastername];
$s2=$_POST[s2];
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
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
<?
$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level=200";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤";
}

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤<br>";
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `char_name` LIKE '$mastername' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$objid=$strac['objid'];

	$str="insert into `zwls_onlinerepays` (`account` , `name` , `objid` , `type` , `s1` , `time` , `ip` , `userread` , `mastername` , `masteraccount` , `masterobjid` , `s2` , `mastertime` , `masterip`) values ('$toid','System_msg','0','11','這是 $mastername 給您的訊息','$time','127.0.0.1','0','$mastername','$id','$objid','$s2','$time','$ip')";
	$db->query($str);
	$msg=$msg."發送完成<br>";
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