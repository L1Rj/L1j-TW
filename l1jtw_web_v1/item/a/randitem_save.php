<?
require("../../setup.php");
require("itemlist.php");
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
include("../../html/head.php");
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

if($open_rand!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$randitemc=$_POST[randitemc];
	$tranditempoint=$randitempoint*$randitemc;
	$d=0;

	if($randitemc<=0 || $randitemc>10){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	$str="select count(*) from user_register where name='$id' and event_point<$tranditempoint";
	$chk_point=$db->get_var($str);
	if($chk_point!=0){
		$err_count++;
		$msg=$msg."點數不足<br>";
	}

	$str="select count(*) from character_warehouse where account_name='$id'"; 
	$chk_cw=$db->get_var($str);
	if($chk_cw>200){
		$err_count++;
		$msg=$msg."倉庫已達200物品上限<br>";
	}
}

if($err_count==0){
	$randitemnum=0;
	$a=0;
	$b=1;
	While($a<$b){
		$c=${"randitem".$b};
		if($c==''){$b=$b-1;}
		else{$b++;}
		$a++;
	}

	$c=${"randitem".$b};

	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','回饋點數',NULL,NULL,'轉蛋 物品轉蛋A','$randitemc','+ $tranditempoint','物品轉蛋','$time','$ip')";
	$db->query($str);
	$str="update user_register set event_point=event_point-$tranditempoint , bonus_point=bonus_point+$tranditempoint where name='$id'";
	$db->query($str);

	While($d<$randitemc){
		$randitemnum=0;
		$rand=rand(1,$maxrandnum);

		$e=1;
		While($e<=$b){
			$f=$e+1;
			if(0 < $rand && $rand <= $randitem1[0]){$randitemnum=1;}
			if(${"randitem".$e}[0] < $rand && $rand <= ${"randitem".$f}[0]){$randitemnum=$f;}
			$e++;
		}

		$randitemno=${"randitem".$randitemnum}[1];
		$randitemlv=${"randitem".$randitemnum}[2];
		$randitemname=${"randitem".$randitemnum}[3];
		$randitemcount=${"randitem".$randitemnum}[4];
		$usetime=${"randitem".$randitemnum}[5];

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

		if($randitemnum==0){
			$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','轉蛋',NULL,NULL,'銘謝惠顧',NULL,'- $randitempoint','物品轉蛋','$time','$ip')";
			$db->query($str);
		}else{
			$str="insert into `character_warehouse` (`id` , `account_name` , `item_id` , `item_name` , `count` , `is_equipped` , `enchantlvl` , `is_id` , `durability` , `remaining_time`) values ('$randid','$id','$randitemno','$randitemname','$randitemcount','0','$randitemlv','0','0','$usetime')";
			$db->query($str);
			if($usetime!=0){$randitemname=$randitemname."(".$usetime."秒)";}
			$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','轉蛋','$randid','$randitemno','$rand +$randitemlv $randitemname','$randitemcount','- $randitempoint','物品轉蛋','$time','$ip')";
			$db->query($str);
		}
		$str="update zwls_setup set setup=$randid where type='itemno'";
		$db->query($str);
		if($randitemnum==0){
			$msg=$msg."很抱歉！轉蛋機這次吃了您的點數！<br>";
		}else{
			if($randitemlv==0){
			$msg=$msg."獲得".$randitemname." (".$randitemcount.")<br>";
		}else{
			$msg=$msg."獲得+".$randitemlv." ".$randitemname." (".$randitemcount.")<br>";
		}
	}
	$d++;
}
$msg=$msg."購買完成<br>本次花費點數 $tranditempoint 點<br>獲得回饋點數 $tranditempoint 點<br>";
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