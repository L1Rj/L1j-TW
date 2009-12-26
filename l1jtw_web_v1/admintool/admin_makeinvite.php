<?
require("../setup.php");
$password=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";

if($password!=$adminpass){
	$err_count++;
	$msg=$msg."管理密碼錯誤<br>";
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM user_register ORDER BY `name` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
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
<form method="post" action="admin_makeinvite_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="password" value="<?=$password?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>註冊碼建立(管理介面)</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">輸入玩家帳號與註冊碼資訊後，本程式將自動給予玩家註冊碼序號。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">玩家帳號：<input type="text" name="id" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="13">
<select size="1" name="id2" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="手動輸入">手動輸入</option>
<?do{?>
<option value="<?=$strac['name']?>"><?=$strac['name']?></option>
<?}while($strac=mysql_fetch_assoc($strab));?>
</select> (若非"手動輸入"將以選擇的帳號為準)
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">序號數量：<select size="1" name="cardc" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
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