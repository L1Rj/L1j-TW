<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$mode=$_GET[mode];
if($mode==NULL){$mode=0;}
$err_count=0;
$msg="";

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
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
	$straa =sprintf("SELECT * FROM user_register WHERE `name` LIKE '$id' ORDER BY `name` DESC", $str);
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$username=$strac['username'];

	$str="select count(*) from zwls_invite_zone where account='$id'";
	$chk_ac=$db->get_var($str);
	if($chk_ac==1){
		mysql_select_db($sql_dbname, $login_on);
		$straa=sprintf("SELECT * FROM zwls_invite_zone WHERE `account` LIKE '$id' ORDER BY `no` ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
		$no=$strac['no'];
		$bcode=$strac['code'];
	}
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
<form method="post" action="apply_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="mode" value="<?=$mode?>">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>註冊碼分享專區</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">按出送出後即可獲得您的專屬註冊碼分享網址。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">註冊碼分享專區將會秀出所有目前您尚未使用的註冊碼。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">您可以將分享網址分享給其他網友，方便推薦他人註冊。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">分享專區標題：<input name="name" style="width: 100px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="12" value="<?=$username?>">的註冊碼分享區
<?
	if($chk_ac==1){
?>
<br>
您的註冊碼分享專區為：<br><a target="_blank" href="<?=$web_url?>/i.php?n=<?=$no?>&c=<?=$bcode?>"><?=$web_url?>/i.php?n=<?=$no?>&c=<?=$bcode?></a><br>
<?
	}
?>
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
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
</center>
</body>
</html>