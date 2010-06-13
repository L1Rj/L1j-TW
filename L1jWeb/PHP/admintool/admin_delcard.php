<?
require("../setup.php");
$password=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";

if($password!=$adminpass){
	$err_count++;
	$msg=$msg."管理密碼錯誤<br>";
}
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
<?if($err_count==0){?>
<form method="post" action="admin_delcard_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="password" value="<?=$password?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>序號清除(管理介面)</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">選擇：<select size="1" name="delmode" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">清除：已使用的邀請碼</option>
<option value="2">清除：已使用的物品序號</option>
<option value="3">清除：已使用的點數序號</option>
</select>
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
<input type="submit" OnClick="history.back();" value="回上一頁" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
</center>
</body>
</html>