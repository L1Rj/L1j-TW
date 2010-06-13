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
mysql_select_db($sql_dbname, $login_on);
$straa = sprintf("SELECT * FROM zwls_event_item_card WHERE `no` LIKE '$eventno' ORDER BY `no` ASC");
$strab = mysql_query($straa, $login_on) or die(mysql_error());
$strac = mysql_fetch_assoc($strab);
$eventname=$strac['eventname'];
$itemid=$strac['itemid'];
$itemlv=$strac['itemlv'];
$itemname=$strac['itemname'];
$itemcount=$strac['itemcount'];
$itemusetime=$strac['itemusetime'];
$itemaccount=$strac['itemaccount'];

$ran_string="";
$ran_chars = '1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
for($i = 0; $i < 14; $i++){
$ran_string .= $ran_chars[rand(0, 61)];
}

$cardpassword="ZSEI99".$ran_string;
$str="select count(*) from zwls_item_card where password='$cardpassword'";
$chk_card=$db->get_var($str);
While($chk_card!=0){
$ran_string="";
$ran_chars = '1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
for($i = 0; $i < 14; $i++){
$ran_string .= $ran_chars[rand(0, 61)];
}
$cardpassword="ZSEI99".$ran_string;
$str="select count(*) from zwls_item_card where password='$cardpassword'";
$chk_card=$db->get_var($str);
}

$str="insert into `zwls_item_card` (`password` , `item` , `itemlv` , `itemname` , `count` , `usetime` , `used` , `name`, `time`) values ('$cardpassword','$itemid','$itemlv','[$eventname]$itemname','$itemcount','$itemusetime','0','$itemaccount','$time')";
$db->query($str);
$str="update zwls_event_item_card set ok='1',okid='$id',oktime='$time',okip='$ip' where no=$eventno";
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