<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
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
<?
$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from accounts where login='$id' and banned='1'"; 
$chk_lock=$db->get_var($str);
if($chk_lock==1){
	$err_count++;
	$msg=$msg."帳號鎖定中<br>";
}

if($open_usermoney!=1){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

$userdatetime=mktime(date("H"),date("i"),date("s"),date("m"),date("d"),date("Y"));
$userdate=date("Y-m-d",$userdatetime);
$str="select count(*) from zwls_user_get_point where account='$id' and date='$userdate'";
$chk_userm=$db->get_var($str);
$str="select count(*) from zwls_user_get_point where ip='$ip' and date='$userdate'";
$chk_userip=$db->get_var($str);
if($chk_userm!=0 || $chk_userip!=0){
	$err_count++;
	$msg=$msg."您今日已經領取過獎勵<br>";
}

if($err_count==0){
	$usermoney=rand($usermoneymin,$usermoneymax);
	$usertime=date("H:i:s",$userdatetime);

	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'點數','$usermoney','+ $usermoney','登入獎勵','$time','$ip')";
	$db->query($str);
	$str="insert into `zwls_user_get_point` (`account` , `point` , `date` , `time` , `ip`) values ('$id','$usermoney','$userdate','$usertime','$ip')";
	$db->query($str);
	$str="update user_register set event_point=event_point+$usermoney where name='$id'";
	$db->query($str);
	$usermsg="$id 您好：<br>本次登入獲得今日 ( $userdate ) 獎勵 $usermoney 點。";
}

if($err_count==0){
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="8"><div align="center"><font size=2 color=red>登入獎勵</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="8"><?=$usermsg?></div></td>
	</tr>
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