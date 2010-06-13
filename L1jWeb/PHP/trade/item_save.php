<?
require("../setup.php");
$mode=$_POST[mode];
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

if($open_item!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$objid=$_POST[objid];

	if($mode==0){
		$sellpoint=$_POST[sellpoint];
		$c=1;

		$straa=sprintf("SELECT * FROM character_warehouse WHERE `id` LIKE '$objid' ORDER BY `id` ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
		$itemid=$strac['item_id'];
		$itemname=$strac['item_name'];

		if($sellpoint<$itemtrademinsellprice || $sellpoint>$itemtrademaxsellprice){
			$err_count++;
			$msg=$msg."販賣金額設定錯誤<br>";
		}

		$str="select count(*) from characters where account_name='$id' and OnlineStatus=1";
		$chk_online=$db->get_var($str);
		if($chk_online!=0){
			$err_count++;
			$msg=$msg."人物尚未離線<br>";
		}

		$str="select count(*) from armor where item_id=$itemid and trade=1";
		$chk_ats=$db->get_var($str);
		$str="select count(*) from etcitem where item_id=$itemid and trade=1";
		$chk_ets=$db->get_var($str);
		$str="select count(*) from weapon where item_id=$itemid and trade=1";
		$chk_wts=$db->get_var($str);
		if($chk_ats==1 || $chk_ets==1 || $chk_wts==1){
			$err_count++;
			$msg=$msg."此物品不可轉移<br>";
		}

		$str="select count(*) from zwls_item_trade where whosell='$id' and tradestatus='0'";
		$chk_ws=$db->get_var($str);
		if($chk_ws>=$itemtrademaxsell){
			$err_count++;
			$msg=$msg."個人物品販賣量已達上限<br>";
		}

		$str="select count(*) from zwls_item_trade where tradestatus='0'";
		$chk_as=$db->get_var($str);
		if($chk_as>=$itemtrademaxallsell){
			$err_count++;
			$msg=$msg."全體物品販賣量已達上限<br>";
		}
	}elseif($mode==1){
		mysql_select_db($sql_dbname, $login_on);
		$straa=sprintf("SELECT * FROM zwls_item_trade WHERE `id` LIKE '$objid' AND `tradestatus` LIKE '0' ORDER BY `id` ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);
		$whosell=$strac['whosell'];
		$point=$strac['point'];
		$point2=$point*(1-($itemtradetax/100));
		$point2=(int)$point2;

		$itemname=$strac['itemname'];
		$point3=$point-$point2;

		if($id==$whosell || $id==$adminid){
			$point=0;
			$point2=0;
			$point3=0;
		}

		$str="select count(*) from user_register where name='$id' and e_pass='$e_pass' and event_point>=$point";
		$chk_point=$db->get_var($str);
		if($chk_point==0){
			$err_count++;
			$msg=$msg."點數不足<br>";
		}

		$str="select count(*) from zwls_item_trade where id='$objid' and tradestatus='0'";
		$chk_er=$db->get_var($str);
		if($chk_er==0){
			$err_count++;
			$msg=$msg."異常錯誤(非法操作)<br>";
		}
	}
}

if($err_count==0){
	if($mode==0){
		$str="update character_warehouse set account_name='system' where id='$objid'";
		$db->query($str);
		$str="insert into `zwls_item_trade` (`id` , `itemname` , `point` , `whosell` , `whobuy` , `selltime` , `tradestatus`) values ('$objid','$itemname','$sellpoint','$id',NULL,'$time','0')";
		$db->query($str);
		$msg=$msg."販賣完成<br>";
	}elseif($mode==1){
		$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$whosell','點數',NULL,NULL,'販售道具 $itemname',NULL,'+ $point','二手市場','$time','$ip')";
		$db->query($str);
		$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$whosell','點數',NULL,NULL,'點數處理費',NULL,'- $point3','二手市場','$time','$ip')";
		$db->query($str);
		$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'購買道具 $itemname',NULL,'- $point','二手市場','$time','$ip')";
		$db->query($str);
		$str="update character_warehouse set account_name='$id' where id='$objid'";
		$db->query($str);
		$str="update zwls_item_trade set whobuy='$id' , buytime='$time' , tradestatus=1 where id='$objid' and tradestatus='0'";
		$db->query($str);
		$str="update user_register set event_point=event_point-$point where name='$id' and e_pass='$e_pass'";
		$db->query($str);
		$str="update user_register set event_point=event_point+$point2 where name='$whosell'";
		$db->query($str);
		$msg=$msg."購買完成<br>";
	}
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
