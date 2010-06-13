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
<tr><td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td></tr>
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

if($err_count==0){
	if($mode==0){
		$point=$_POST[point];
		$pointc=$_POST[pointc];
		$tpoint=$point*$pointc;
		$money=$warehouse_money*$tpoint;
		$a=0;

		if($open_buy!="1"){
			$err_count++;
			$msg=$msg."本系統尚未開放<br>";
		}

		if($point!=10 && $point!=100 && $point!=1000 && $point!=10000){
			$err_count++;
			$msg=$msg."異常錯誤(非法操作)<br>";
		}

		if($pointc<1 || $pointc>10){
			$err_count++;
			$msg=$msg."異常錯誤(非法操作)<br>";
		}

		$str="select count(*) from character_warehouse where account_name='$id' and item_id=$warehouse_moneyid and count>=$money";
		$chk_mcount=$db->get_var($str);
		if($chk_mcount==0){
			$err_count++;
			$msg=$msg."倉庫 $warehouse_moneyname 不足<br>";
		}

		$str="select count(*) from characters where account_name='$id' and OnlineStatus=1";
		$chk_online=$db->get_var($str);
		if($chk_online!=0){
			$err_count++;
			$msg=$msg."人物尚未離線<br>";
		}
	}elseif($mode==1){
		$card=$_POST[card];
		$type=$_POST[type];

		if($type==1){
			$str="select count(*) from zwls_item_card where password='$card' and used=0";
			$chk_card1=$db->get_var($str);
			if($chk_card1==0){
				$err_count++;
				$msg=$msg."序號錯誤<br>";
			}

			mysql_select_db($sql_dbname, $login_on);
			$straa=sprintf("SELECT * FROM zwls_item_card WHERE `password` LIKE '$card' ORDER BY `password` DESC");
			$strab=mysql_query($straa, $login_on) or die(mysql_error());
			$strac=mysql_fetch_assoc($strab);
			$item=$strac['item'];
			$itemlv=$strac['itemlv'];
			$itemname=$strac['itemname'];
			$count=$strac['count'];
			$usetime=$strac['usetime'];

			if($item==$warehouse_moneyid){
				$str="select count(*) from character_warehouse where account_name='$id' and item_id=$warehouse_moneyid";
				$chk_wmcount=$db->get_var($str);
				if($chk_wmcount!=0){
					$err_count++;
					$msg=$msg."倉庫 $warehouse_moneyname 尚未使用完畢<br>";
				}
			}

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
		}elseif($type==2){
			$str="select count(*) from zwls_point_card where password='$card' and used=0";
			$chk_card2=$db->get_var($str);
			if($chk_card2==0){
				$err_count++;
				$msg=$msg."序號錯誤<br>";
			}

			mysql_select_db($sql_dbname, $login_on);
			$strba=sprintf("SELECT * FROM zwls_point_card WHERE `password` LIKE '$card' ORDER BY `password` DESC", $str);
			$strbb=mysql_query($strba, $login_on) or die(mysql_error());
			$strbc=mysql_fetch_assoc($strbb);
			$point=$strbc['point'];
		}
	}elseif($mode==2){
		$invitec=$_POST[invitec];
		$tcodepoint=$codepoint*$invitec;
		$a=0;

		if($open_buycode!="1"){
			$err_count++;
			$msg=$msg."本系統尚未開放<br>";
		}

		$str="select count(*) from user_register where name='$id' and event_point>=$tcodepoint";
		$chk_point1=$db->get_var($str);
		if($chk_point1==0){
			$err_count++;
			$msg=$msg."點數不足<br>";
		}
	}
}

if($err_count==0){
	if($mode==0){
		$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'點數','$tpoint','+ $tpoint','點數購買','$time','$ip')";
		$db->query($str);
		$str="update character_warehouse set count=count+$tpoint-$money where account_name='$id' and item_id=$warehouse_moneyid";
		$db->query($str);
		$str="update user_register set event_point=event_point+$tpoint where name='$id'";
		$db->query($str);
		$msg=$msg."點數購買完成<br>";
	}elseif($mode==1){
		if($type==1){
			$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','道具','$randid','$item','+$itemlv $itemname','$count',NULL,'儲值專區','$time','$ip')";
			$db->query($str);
			$str="insert into `character_warehouse` (`id` , `account_name` , `item_id` , `item_name` , `count` , `is_equipped` , `enchantlvl` , `is_id` , `durability` , `remaining_time`) values ('$randid','$id','$item','$itemname','$count','0','$itemlv','0','0','$usetime')";
			$db->query($str);
			$str="update zwls_item_card set used=1 , whouse='$id' , time2='$time' where password='$card'";
			$db->query($str);
		}elseif($type==2){
			$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'點數','$point','+ $point','儲值專區','$time','$ip')";
			$db->query($str);
			$str="update user_register set event_point=event_point+$point where name='$id'";
			$db->query($str);
			$str="update zwls_point_card set used=1 , whouse='$id' , time2='$time' where password='$card'";
			$db->query($str);
		}
		$msg=$msg."序號儲值完成<br>回到<a href=\"seach.php\">序號查詢</a>區";
	}elseif($mode==2){
		While($a<$invitec){
			$ran_string="";
			$ran_chars='1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
			for($i=0;$i<14;$i++){
				$ran_string.=$ran_chars[rand(0,61)];
			}
			if($a<10){
				$a2="0".$a;
			}else{
				$a2=$a;
			}

			$invitecode=$zsit.$a2.$ran_string;
			$str="select count(*) from zwls_invite_code where invitecode='$invitecode'";
			$chk_ps2=$db->get_var($str);
			While($chk_ps2!=0){
				$ran_string="";
				$ran_chars='1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
				for($i=0;$i<14;$i++){
					$ran_string.=$ran_chars[rand(0,61)];
				}
			$invitecode=$zsit.$c2.$ran_string;
			$str="select count(*) from zwls_invite_code where invitecode='$invitecode'";
			$chk_ps2=$db->get_var($str);
			}

			$str="insert into `zwls_invite_code` (`invitecode` , `used` , `name` , `whouse` , `time` , `time2`) values ('$invitecode','0','$id',NULL,'$time',NULL)";
			$db->query($str);
			$a++;
		}
		$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','註冊碼',NULL,NULL,'註冊碼','$invitec','- $tcodepoint','註冊碼購買','$time','$ip')";
		$db->query($str);		
		$str="update user_register set event_point=event_point-$tcodepoint where name='$id'";
		$db->query($str);
		$msg=$msg."註冊碼購買完成<br>";
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
