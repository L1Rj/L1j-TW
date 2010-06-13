<?
require("../setup.php");
$code=$_POST[code];
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
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
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

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤(非法執行)<br>";
}

if($open_reset!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$charname=$_POST[charname];
	$sta=$_POST[sta];
	$dex=$_POST[dex];
	$con=$_POST[con];
	$wis=$_POST[wis];
	$cha=$_POST[cha];
	$int=$_POST[int];
	$tatol=$sta+$dex+$con+$wis+$cha+$int;
	$b=1;
	$c=1;

	if($sta<7 || $sta>20 || $dex<7 || $dex>20 || $con<7 || $con>20 || $wis<7 || $wis>20 || $cha<7 || $cha>20 || $int<7 || $int>20 || $tatol!=75){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	$str="select count(*) from characters where char_name='$charname' and account_name='$id'";
	$chk_name=$db->get_var($str);
	if($chk_name==0){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	$str="select count(*) from user_register where name='$id' and event_point>=$charresetpoint";
	$chk_point=$db->get_var($str);
	if($chk_point==0){
		$err_count++;
		$msg=$msg."點數不足<br>";
	}

	$str="select count(*) from characters where account_name='$id' and OnlineStatus='1'";
	$chk_ol=$db->get_var($str);
	if($chk_ol!=0){
		$err_count++;
		$msg=$msg."人物尚未離線<br>";
	}

	$str="select count(*) from character_warehouse where id='$randid'";
	$chk_it=$db->get_var($str);
	$str2="select count(*) from character_items where id='$randid'";
	$chk_it2=$db->get_var($str2);
	$str3="select count(*) from clan_warehouse where id='$randid'";
	$chk_it3=$db->get_var($str3);
	While($chk_it!=0 || $chk_it2!=0 || $chk_it3!=0){
		$randid=$randid+1;
		$str="select count(*) from character_warehouse where id='$randid'";
		$chk_it=$db->get_var($str);
		$str2="select count(*) from character_items where id='$randid'";
		$chk_it2=$db->get_var($str2);
		$str3="select count(*) from clan_warehouse where id='$randid'";
		$chk_it3=$db->get_var($str3);
	}
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `char_name` LIKE '".$charname."' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$lv=$strac['level'];
	$charobjid=$strac['objid'];

	if($dex>=7&&$dex<=9){$acc=$lv/8;}
	elseif($dex>=10&&$dex<=12){$acc=$lv/7;}
	elseif($dex>=13&&$dex<=15){$acc=$lv/6;}
	elseif($dex==16&&$dex==17){$acc=$lv/5;}
	elseif($dex>=18){$acc=$lv/4;}
	$acc=(int)$acc;
	$ac=10-$acc;

	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'$charname 重置',NULL,'- $charresetpoint','重置系統','$time','$ip')";
	$db->query($str);
	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','回饋點數',NULL,NULL,'$charname 重置',NULL,'+ $charresetpoint','重置系統','$time','$ip')";
	$db->query($str);

	$str="update characters set Ac=$ac , Str=$sta , Con=$con , Dex=$dex , Cha=$cha , Intel=$int , Wis=$wis , BonusStatus=0 , ElixirStatus=0 where char_name='$charname' and account_name='$id'";
	$db->query($str);

	$str="update user_register set event_point=event_point-$charresetpoint , bonus_point=bonus_point+$charresetpoint where name='$id'";
	$db->query($str);
	$msg=$msg."重置完成<br>本次花費點數 $charresetpoint 點<br>獲得回饋點數 $charresetpoint 點<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</table>
</center>
</body>
</html>