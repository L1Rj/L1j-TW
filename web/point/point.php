<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$mode=$_GET[mode];

if($mode==NULL || $mode<0 || $mode>2){
	$mode=0;
}

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
<?
if($err_count==0){
?>
<form method="post" action="point_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="mode" value="<?=$mode?>">
<input type="hidden" name="code" value="<?=$code?>">
<?
	if($mode==0){
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>點數購買</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">購買點數1點需要倉庫<?=$warehouse_moneyname?>(<?=$warehouse_money?>)。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">點數即時購買即時儲值。</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">購買點數1點回饋1個<?=$warehouse_moneyname?>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">回饋<?=$warehouse_moneyname?>於購買後直接存放在您的倉庫。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">點數種類：<select size="1" name="point" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="10">點數[10]</option>
<option value="100">點數[100]</option>
<option value="1000">點數[1000]</option>
<option value="10000">點數[10000]</option>
</select></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">點數數量：<select size="1" name="pointc" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">1張</option>
<option value="2">2張</option>
<option value="3">3張</option>
<option value="4">4張</option>
<option value="5">5張</option>
<option value="6">6張</option>
<option value="7">7張</option>
<option value="8">8張</option>
<option value="9">9張</option>
<option value="10">10張</option>
</select></div></td>
	</tr>
<?
	}elseif($mode==1){
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>儲值專區</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">儲值密碼：<input type="text" name="card" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$_GET[card]?>"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">儲值類型：<input name="type" type="radio" <?if($_GET[point]==NULL){?>checked <?}?>value="1">道具 <input name="type" type="radio" <?if($_GET[point]!=NULL){?>checked <?}?>value="2">點數</div></td>
	</tr>
<?
	}elseif($mode==2){
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>註冊碼購買</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">註冊碼1個需要點數<?=$codepoint?>點</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">註冊碼數量：<select size="1" name="invitec" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">1張</option>
<option value="2">2張</option>
<option value="3">3張</option>
<option value="4">4張</option>
<option value="5">5張</option>
</select></div></td>
	</tr>
<?
	}
?>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?
}else{
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?
}
?>
</table>
</center>
</body>
</html>
