<?
require("../setup.php");
$id=$_GET[id];
$password=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";

$str="select count(*) from user_register where name='$id'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號不存在<br>";
}

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
<form method="post" action="admin_savepoint_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="id" value="<?=$id?>">
<input type="hidden" name="password" value="<?=$password?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>點數加值(管理介面)</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">輸入點數資訊後，本程式將自動加值玩家點數。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">玩家帳號：<?=$id?></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">點數種類：<select size="1" name="point" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">點數[1]</option>
<option value="10">點數[10]</option>
<option value="100">點數[100]</option>
<option value="1000">點數[1000]</option>
<option value="10000">點數[10000]</option>
<option value="100000">點數[100000]</option>
<option value="-1">點數[-1]</option>
<option value="-10">點數[-10]</option>
<option value="-100">點數[-100]</option>
<option value="-1000">點數[-1000]</option>
<option value="-10000">點數[-10000]</option>
</select>
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">點數數量：<select size="1" name="pointc" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?for($a=1;$a<=10;$a++){?>
<option value="<?=$a?>"><?=$a?>張</option>
<?}?>
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