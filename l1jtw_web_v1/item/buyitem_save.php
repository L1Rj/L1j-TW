<?
require("../setup.php");
$code=$_POST[code];
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$eid=$_POST[eid];
$buycount=$_POST[buycount];
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

if($open_event!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$a=0;

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_item_list WHERE `id` LIKE '$eid' ORDER BY `id` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
	$point=$strac['point'];
	$itemid=$strac['itemid'];
	$itemname=$strac['itemname'];
	$itemcount=$strac['itemcount'];
	$itemlv=$strac['itemlv'];
	$charge_count=$strac['charge_count'];
	$usetime=$strac['usetime'];
	$maxbuycount=$strac['maxbuycount'];
	$tpoint=$point*$buycount;
	$starttime=$strac['starttime'];
	$stoptime=$strac['stoptime'];

	if($usetime!=0){
		$itemname=$itemname."(".$usetime."秒)";
	}

	if($starttime!=NULL && $starttime>=$time){
		$err_count++;
		$msg=$msg."本物品尚未開始販售<br>";
	}

	if($stoptime!=NULL && $stoptime<=$time){
		$err_count++;
		$msg=$msg."本物品已結束販售<br>";
	}

	if($buycount<=0 || $buycount==NULL){
		$err_count++;
		$msg=$msg."異常錯誤<br>";
	}

	if($itemid<=0 || $itemid==NULL){
		$err_count++;
		$msg=$msg."異常錯誤<br>";
	}

	$str="select count(*) from zwls_item_list where id='$eid'";
	$chk_eid=$db->get_var($str);
	if($chk_eid==0){
		$err_count++;
		$msg=$msg."異常錯誤<br>";
	}

	if($maxbuycount!=0){
		$date=date("Y-m-d",$basetime);
		$str="select count(*) from zwls_item_get_log where itemtype='道具' and itemno='$itemid' and point='- $point' and logfrom='物品兌換' and time>='$date' and account='$id'";
		$chk_mbc=$db->get_var($str);
		$chk_mbcc=$chk_mbc+$buycount;
		if($chk_mbcc>$maxbuycount){
			$err_count++;
			$msg=$msg."本物品每個帳號每日限買 $maxbuycount 組，您已購買 $chk_mbc 組<br>";
		}
	}

	$str="select count(*) from zwls_item_list where id='$eid' and count>=$buycount";
	$chk_itemc=$db->get_var($str);
	if($chk_itemc==0){
		$err_count++;
		$msg=$msg."剩餘數量不足<br>";
	}

	$str="select count(*) from user_register where name='$id' and event_point>=$tpoint";
	$chk_ep=$db->get_var($str);
	if($chk_ep==0){
		$err_count++;
		$msg=$msg."點數不足<br>";
	}

	$str="select count(*) from character_warehouse where account_name='$id'"; 
	$chk_cw=$db->get_var($str);
	$chk_cw=$chk_cw+$buycount;
	if($chk_cw>200){
		$err_count++;
		$msg=$msg."倉庫已達200物品上限<br>";
	}
}

if($err_count==0){
	$str="update user_register set event_point=event_point-$tpoint , bonus_point=bonus_point+$tpoint where name='$id'";
	$db->query($str);
	$str="update zwls_item_list set count=count-$buycount where id='$eid'";
	$db->query($str);
	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','回饋點數',NULL,NULL,'兌換 +$itemlv $itemname ($itemcount)','$buycount','+ $tpoint','物品兌換','$time','$ip')";
	$db->query($str);
	While($a<$buycount){
		$str="select count(*) from character_warehouse where id='$randid'";
		$chk_it=$db->get_var($str);
		$str2="select count(*) from character_elf_warehouse where id='$randid'";
		$chk_it2=$db->get_var($str2);
		$str3="select count(*) from character_items where id='$randid'";
		$chk_it3=$db->get_var($str3);
		$str4="select count(*) from clan_warehouse where id='$randid'";
		$chk_it4=$db->get_var($str4);
		While($chk_it!=0 || $chk_it2!=0 || $chk_it3!=0 || $chk_it4!=0){
			$randid=$randid+1;
			$str="select count(*) from character_warehouse where id='$randid'";
			$chk_it=$db->get_var($str);
			$str2="select count(*) from character_elf_warehouse where id='$randid'";
			$chk_it2=$db->get_var($str2);
			$str3="select count(*) from character_items where id='$randid'";
			$chk_it3=$db->get_var($str3);
			$str4="select count(*) from clan_warehouse where id='$randid'";
			$chk_it4=$db->get_var($str4);
		}
		$str="insert into `character_warehouse` (`id` , `account_name` , `item_id` , `item_name` , `count` , `is_equipped` , `enchantlvl` , `is_id` , `durability` , `charge_count` , `remaining_time`) values ('$randid','$id','$itemid','$itemname','$itemcount','0','$itemlv','0','0','$charge_count','$usetime')";
		$db->query($str);
		$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','道具','$randid','$itemid','+$itemlv $itemname','$itemcount','- $point','物品兌換','$time','$ip')";
		$db->query($str);
		$str="update zwls_setup set setup=$randid where type='itemno'";
		$db->query($str);
		$a++;
	}
	if($itemlv==0){
		$msg=$msg."獲得".$itemname." (".$itemcount.") ".$buycount."組<br>";
	}else{
		$msg=$msg."獲得+".$itemlv." ".$itemname." (".$itemcount.") ".$buycount."組<br>";
	}
	$msg=$msg."購買完成<br>本次花費點數 $tpoint 點<br>獲得回饋點數 $tpoint 點<br>";
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