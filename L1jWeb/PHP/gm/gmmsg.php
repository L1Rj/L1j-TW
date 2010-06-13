<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";
$c=1;

$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level=200";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤";
}

if($err_count==0){
	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' AND `AccessLevel` LIKE '200' ORDER BY `objid` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
}
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<center>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
<?if($err_count==0){?>
<form method="POST" action="gmmsg_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td bgcolor=#303030 width="100%"><div align="left" class="style7">接收帳號：<input type="text" name="toid" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></td>
	</tr>
	<tr>
		<td bgcolor=#202020><div align="left" class="style7">傳送暱稱：<select size="1" name="mastername" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?do{?>
<option value="<?=$strac['char_name']?>"><?=$strac['char_name']?></option>
<?} while ($strac = mysql_fetch_assoc($strab));?>
</select><br>
傳送內容：<br>
<textarea name="s2" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></textarea></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</form>
</table>
</center>
</body></html>