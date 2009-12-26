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

if($open_changename!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$oldname=$_POST[oldname];
	$newname=$_POST[newname];
	$changesex=$_POST[changesex];

	if($newname==NULL){
		$err_count++;
		$msg=$msg."更名暱稱不得為空<br>";
	}

	if($changesex==NULL||$changesex<0||$changesex>2){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	$str="select count(*) from characters where char_name='$oldname' and account_name='$id'";
	$chk_name=$db->get_var($str);
	if($chk_name==0){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	$str="select count(*) from user_register where name='$id' and event_point>=$changenamepoint";
	$chk_ep=$db->get_var($str);
	if($chk_ep==0){
		$err_count++;
		$msg=$msg."點數不足<br>";
	}

	$str="select count(*) from characters where account_name='$id' and OnlineStatus='1'";
	$chk_ol=$db->get_var($str);
	if($chk_ol!=0){
		$err_count++;
		$msg=$msg."人物尚未離線<br>";
	}

	$str="select count(*) from characters where char_name='$newname' and account_name!='$id'";
	$chk_newname=$db->get_var($str);
	if($chk_newname!=0){
		$err_count++;
		$msg=$msg."暱稱重複<br>";
	}
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `char_name` LIKE '".$oldname."' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$objid=$strac['objid'];
	$sex=$strac['Sex'];
	$type=$strac['Type'];
	$class=$strac['Class'];

	if($changesex!=0){	// 判斷變更性別
		if($changesex==1){	// 判斷變更性別為男性
			$sex=0;	// 變更性別為男性
			if($type==0){	// 判斷原始職業為王族
				$class=0;	// 變更為王族男性造型
			}elseif($type==1){	// 判斷原始職業為騎士
				$class=61;	// 變更為騎士男性造型
			}elseif($type==2){	// 判斷原始職業為妖精
				$class=138;	// 變更為妖精男性造型
			}elseif($type==3){	// 判斷原始職業為法師
				$class=734;	// 變更為法師男性造型
			}elseif($type==4){	// 判斷原始職業為黑暗妖精
				$class=2786;	// 變更為黑暗妖精男性造型
			}elseif($type==5){	// 判斷原始職業為龍騎士
				$class=6658;	// 變更為龍騎士男性造型
			}elseif($type==6){	// 判斷原始職業為幻術師
				$class=6671;	// 變更為幻術師男性造型
			}
		}elseif($changesex==2){	// 判斷變更性別為女性
			$sex=1;	// 變更性別為女性
			if($type==0){	// 判斷原始職業為王族
				$class=1;	// 變更為王族女性造型
			}elseif($type==1){	// 判斷原始職業為騎士
				$class=48;	// 變更為騎士女性造型
			}elseif($type==2){	// 判斷原始職業為妖精
				$class=37;	// 變更為妖精女性造型
			}elseif($type==3){	// 判斷原始職業為法師
				$class=1186;	// 變更為法師女性造型
			}elseif($type==4){	// 判斷原始職業為黑暗妖精
				$class=2796;	// 變更為黑暗妖精女性造型
			}elseif($type==5){	// 判斷原始職業為龍騎士
				$class=6661;	// 變更為龍騎士女性造型
			}elseif($type==6){	// 判斷原始職業為幻術師
				$class=6650;	// 變更為幻術師女性造型
			}
		}
	}

	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'$oldname 更名 $newname',NULL,'- $changenamepoint','更名系統','$time','$ip')";
	$db->query($str);
	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','回饋點數',NULL,NULL,'$oldname 更名 $newname',NULL,'+ $changenamepoint','更名系統','$time','$ip')";
	$db->query($str);
	$str="update user_register set event_point=event_point-$changenamepoint , bonus_point=bonus_point+$changenamepoint where name='$id'";
	$db->query($str);
	$str="update character_buddys set buddy_name='$newname' where buddy_id='$objid'";
	$db->query($str);
	$str="update characters set char_name='$newname' , Sex=$sex , Class=$class where objid='$objid'";
	$db->query($str);
	$str="update clan_data set leader_name='$newname' where leader_id='$objid'";
	$db->query($str);
	$str="insert into `zwls_changename` (`objid` , `account` , `oldname` , `newname` , `time`) values ('$objid','$id','$oldname','$newname','$time')";
	$db->query($str);
	$msg=$msg."更名完成<br>本次花費點數 $changenamepoint 點<br>獲得回饋點數 $changenamepoint 點<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
<tr><td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td></tr>
</table>
</center>
</body>
</html>