<?
require("../setup.php");
$eventno=$_GET[eventno];
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
<tr><td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td></tr>
<?
$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level='200'";
$chk_id=$db->get_var($str);
if($chk_id==0){
$err_count++;
$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from zwls_event_item_card where no='$eventno' and ok='0'";
$chk_eno=$db->get_var($str);
if($chk_eno==0){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($id!=$adminid){
$err_count++;
$msg=$msg."您無審核資格<br>";
}

if($err_count==0){
$str="update zwls_event_item_card set ok='2',okid='$id',oktime='$time',okip='$ip' where no=$eventno";
$db->query($str);
$msg=$msg."審核完成<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
<tr><td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td></tr>
</div></td></tr>
</table>
</center>
</body>
</html>