<?
require("../setup.php");
$password=$_COOKIE["linsfuserpass"];
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
<tr><td width="100%" bgcolor=#303030 colspan="7"><div align="center"><font size=2 color=red>系統訊息</font></div></td></tr>
<?
if($password!=$adminpass){
	$err_count++;
	$msg=$msg."管理密碼錯誤<br>";
}

if($err_count==0){
	$searchmode=$_POST[searchmode];

	mysql_select_db($sql_dbname, $login_on);
	if($searchmode==1){
		$straa=sprintf("SELECT * FROM zwls_invite_code ORDER BY used ASC , time ASC , invitecode ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
	}elseif($searchmode==2){
		$straa=sprintf("SELECT * FROM zwls_item_card ORDER BY used ASC , item ASC , time ASC , password ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
	}elseif($searchmode==3){
		$straa=sprintf("SELECT * FROM zwls_point_card ORDER BY used ASC , point ASC , time ASC , password ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
	}
?>
	<tr>
		<td width="10%" bgcolor=#202020><div align="center">卡片序號</div></td>
		<td width="20%" bgcolor=#202020><div align="center">卡片名稱</div></td>
		<td width="10%" bgcolor=#202020><div align="center">使用</div></td>
		<td width="15%" bgcolor=#202020><div align="center">購買者</div></td>
		<td width="15%" bgcolor=#202020><div align="center">使用者</div></td>
		<td width="15%" bgcolor=#202020><div align="center">購買時間</div></td>
		<td width="15%" bgcolor=#202020><div align="center">使用時間</div></td>
	</tr>
<?
	if($searchmode==1){
		do{
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$strac['invitecode']?></div></td>
		<td bgcolor=#202020><div align="center">註冊碼</div></td>
		<td bgcolor=#202020><div align="center"><?if($strac['used']==1){?>已使用<?}?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['name']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['whouse']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['time']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['time2']?></div></td>
	</tr>
<?
		}while($strac=mysql_fetch_assoc($strab));
	}elseif($searchmode==2){
		do{
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$strac['password']?></div></td>
		<td bgcolor=#202020><div align="center">+<?=$strac['itemlv']?> <?=$strac['itemname']?> (<?=$strac['count']?>)</div></td>
		<td bgcolor=#202020><div align="center"><?if($strac['used']==1){?>已使用<?}?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['name']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['whouse']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['time']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['time2']?></div></td>
	</tr>
<?
		}while($strac=mysql_fetch_assoc($strab));
	}elseif($searchmode==3){
		do{
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$strac['password']?></div></td>
		<td bgcolor=#202020><div align="center">點數[<?=$strac['point']?>]</div></td>
		<td bgcolor=#202020><div align="center"><?if($strac['used']==1){?>已使用<?}?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['name']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['whouse']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['time']?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['time2']?></div></td>
	</tr>
<?
		}while($strac=mysql_fetch_assoc($strab));
	}
}
?>
</table>
<input type="submit" OnClick="history.back();" value="回上一頁" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
</center>
</body>
</html>