<?
require("../setup.php");
mysql_select_db($sql_dbname, $login_on);
$straa=sprintf("SELECT * FROM zwls_invite_code WHERE `name` LIKE 'free' AND `used` LIKE '0' ORDER BY time ASC , invitecode ASC");
$strab=mysql_query($straa, $login_on) or die(mysql_error());
$strac=mysql_fetch_assoc($strab);
?>
<html>

<head>
<meta http-equiv="Content-Language" content="text/html">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<center>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>免費註冊碼</font></div></td>
	</tr>
<?do{?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$strac['invitecode']?></div></td>
	</tr>
<?}while($strac = mysql_fetch_assoc($strab)); ?>
</table>
</center>
</body>
</html>
