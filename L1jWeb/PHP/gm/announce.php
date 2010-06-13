<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$no=$_GET[no];
$err_count=0;
$msg="";

$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level='200'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from zwls_announce where no=$no";
$chk_ea=$db->get_var($str);
if($chk_ea==0){
	$err_count++;
	$msg=$msg."尚無遊戲公告<br>";
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_announce WHERE `no` LIKE '$no' ORDER BY `no` DESC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);

	$announce=$strac['announce'];

	$announce2=$strac['announce'];
	$announce2=str_replace(chr(13).chr(10),"<br>",$announce2); 
}
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title></head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<?if($err_count==0){?>
<form method="post" action="newannounce_edit.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="no" value="<?=$strac['no']?>">
	<tr>
		<td bgcolor=#303030 width="100%"><div align="center"><input name="announcename" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$strac['announcename']?>"></div></td>
	</tr>
	<tr>
		<td bgcolor=#202020 width="100%"><div align="left"><?=$strac['datetime']?></td>
	</tr>
	<tr>
		<td bgcolor=#101010 width="100%"><div align="left"><textarea name="announce" rows="10" style="width: 600px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?=$announce?></textarea></td>
	</tr>
	<tr>
		<td bgcolor=#202020 width="100%"><div align="center"><input type="submit" value="修改" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
	<tr>
		<td bgcolor=#303030 width="100%"><div align="center"><font size=2 color=red><?=$strac['announcename']?></font></div></td>
	</tr>
	<tr>
		<td bgcolor=#202020 width="100%"><div align="left"><?=$strac['datetime']?></td>
	</tr>
	<tr>
		<td bgcolor=#101010 width="100%"><div align="left"><?=$announce2?></td>
	</tr>
<?
}else{
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</body></html>
