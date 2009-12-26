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
<tr><td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td></tr>
<?
$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level='200'";
$chk_id=$db->get_var($str);
if($chk_id==0){
$err_count++;
$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
$eventname=$_POST[eventname];
$itemid=$_POST[itemid];
$itemlv=$_POST[itemlv];
$itemname=$_POST[itemname];
$itemcount=$_POST[itemcount];
$itemusetime=$_POST[itemusetime];
$itemcharname=$_POST[itemcharname];

if($eventname==NULL){
$err_count++;
$msg=$msg."活動名稱未輸入<br>";
}

$str="select count(*) from armor where item_id='$itemid' and name='$itemname'";
$chk_iia=$db->get_var($str);
$str="select count(*) from etcitem where item_id='$itemid' and name='$itemname'";
$chk_iie=$db->get_var($str);
$str="select count(*) from weapon where item_id='$itemid' and name='$itemname'";
$chk_iiw=$db->get_var($str);
if($chk_iia==0 && $chk_iie==0 && $chk_iiw==0){
$err_count++;
$msg=$msg."物品編號或名稱錯誤<br>";
}

if($itemlv<0 || $itemlv>20){
$err_count++;
$msg=$msg."物品等級錯誤<br>";
}

if($itemname==NULL){
$err_count++;
$msg=$msg."物品名稱未輸入<br>";
}

if($itemcount==NULL || $itemcount<=0){
$err_count++;
$msg=$msg."物品數量未輸入<br>";
}

if($itemusetime==NULL || $itemcount<0 || $itemcount>7200){
$err_count++;
$msg=$msg."使用秒數未輸入<br>";
}

$str="select count(*) from characters where char_name='$itemcharname'";
$chk_chn=$db->get_var($str);
if($chk_chn==0){
$err_count++;
$msg=$msg."獲獎暱稱不存在<br>";
}

mysql_select_db($sql_dbname, $login_on);
$straa = sprintf("SELECT * FROM characters WHERE `char_name` LIKE '$itemcharname' ORDER BY `objid` ASC");
$strab = mysql_query($straa, $login_on) or die(mysql_error());
$strac = mysql_fetch_assoc($strab);
$itemaccount=$strac['account_name'];
$itemcharname=$strac['char_name'];
}

if($err_count==0){
$str="insert into `zwls_event_item_card` (`gmaccount` , `eventname` , `itemid` , `itemlv` , `itemname` , `itemcount` , `itemusetime` , `itemcharname`, `itemaccount` , `datetime` , `gmip`) values ('$id','$eventname','$itemid','$itemlv','$itemname','$itemcount','$itemusetime','$itemcharname','$itemaccount','$time','$ip')";
$db->query($str);
$msg=$msg."申請完成<br>";
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