<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$err_count1=0;
$err_count2=0;
$err_count3=0;
$msg="";

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from zwls_point_card where name='$id' AND used='0'";
$chk_card1=$db->get_var($str);
$str="select count(*) from zwls_item_card where name='$id' AND used='0'";
$chk_card2=$db->get_var($str);
$str="select count(*) from zwls_invite_code where name='$id' AND used='0'";
$chk_card3=$db->get_var($str);
if($chk_card1==0){
	$err_count1++;
}
if($chk_card2==0){
	$err_count2++;
}
if($chk_card3==0){
	$err_count3++;
}

if($chk_card1==$chk_card2 && $chk_card2==$chk_card3 && $chk_card3==0){
	$err_count++;
	$msg=$msg."尚無序號<br>";
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
<?if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="3"><div align="center"><font size=2 color=red>序號查詢</font></div></td>
	</tr>
	<tr>
		<td width="50%" bgcolor=#202020><div align="center">卡片序號</div></td>
		<td width="25%" bgcolor=#202020><div align="center">卡片名稱</div></td>
		<td width="25%" bgcolor=#202020><div align="center">使用</div></td>
	</tr>
<?
	if($err_count1==0){
		$straa = sprintf("SELECT * FROM zwls_point_card WHERE `name` LIKE '$id' AND `used` LIKE '0' ORDER BY point ASC , time ASC , password ASC");
		$strab = mysql_query($straa, $login_on) or die(mysql_error());
		$strac = mysql_fetch_assoc($strab);
		do {
?>
	<tr>
		<td width="50%" bgcolor=#101010><div align="left"><?=$strac['password']?></div></td>
		<td width="25%" bgcolor=#101010><div align="left">點數[<?=$strac['point']?>]</div></td>
		<td width="25%" bgcolor=#101010><div align="center"><a href="point.php?mode=1&card=<?=$strac['password']?>&point=<?=$strac['point']?>">使用</a></div></td>
	</tr>
<?
		}while($strac=mysql_fetch_assoc($strab));
	}

	if($err_count2==0){
		$strba = sprintf("SELECT * FROM zwls_item_card WHERE `name` LIKE '$id' AND `used` LIKE '0' ORDER BY item ASC , time ASC , password ASC");
		$strbb = mysql_query($strba, $login_on) or die(mysql_error());
		$strbc = mysql_fetch_assoc($strbb);
		do {
			$itemname=$strbc['itemname'];
			if($strbc['usetime']!=0){
				$itemname=$itemname."(".$strbc['usetime']."秒)";
			}
?>
	<tr>
		<td width="50%" bgcolor=#101010><div align="left"><?=$strbc['password']?></div></td>
		<td width="25%" bgcolor=#101010><div align="left">+<?=$strbc['itemlv']?> <?=$itemname?> (<?=$strbc['count']?>)</div></td>
		<td width="25%" bgcolor=#101010><div align="center"><a href="point.php?mode=1&card=<?=$strbc['password']?>">使用</a></div></td>
	</tr>
<?
		}while($strbc=mysql_fetch_assoc($strbb));
	}

	if($err_count3==0){
		$strca = sprintf("SELECT * FROM zwls_invite_code WHERE `name` LIKE '$id' AND `used` LIKE '0' ORDER BY time ASC , invitecode ASC");
		$strcb = mysql_query($strca, $login_on) or die(mysql_error());
		$strcc = mysql_fetch_assoc($strcb);
		do {
?>
	<tr>
		<td width="50%" bgcolor=#101010><div align="left"><?=$strcc['invitecode']?></div></td>
		<td width="25%" bgcolor=#101010><div align="left">註冊碼</div></td>
		<td width="25%" bgcolor=#101010><div align="center"><a href="#" onclick="if (confirm('刪除後就無法復原，確認要刪除？')) location.href='delinvitecode.php?invitecode=<?=$strcc['invitecode']?>'"><font size=2 color=red><b>刪除</b></font></a></div></td>
	</tr>
<?
		}while($strcc=mysql_fetch_assoc($strcb));
	}
}else{
?>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left"><?=$msg?></div></td>
	</tr>
<?
}
?>
</table>
</center>
</body>
</html>