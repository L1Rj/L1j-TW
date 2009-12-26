<?
require("../setup.php");
require("poly_list.php");
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

if($open_poly!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$name = $_POST[name];
	$polylistid = $_POST[polyid];

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM characters WHERE `char_name` LIKE '".$name."' ORDER BY `objid` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
	$objid=$strac['objid'];

	$polyselect=${"poly".$polylistid};
	$polyid=$polyselect[0];
	$polypoint=$polyselect[2];
	$polysec=$polyselect[3];

	if($polyselect==NULL){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	if($polysec==0){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}

	$str="select count(*) from user_register where name='$id' and event_point>=$polypoint";
	$chk_ep=$db->get_var($str);
	if($chk_ep==0){
		$err_count++;
		$msg=$msg."點數不足<br>";
	}

	$str="select count(*) from characters where objid='$objid' and OnlineStatus='1'";
	$chk_ol=$db->get_var($str);
	if($chk_ol!=0){
		$err_count++;
		$msg=$msg."人物尚未離線<br>";
	}
}

if($err_count==0){
	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','點數',NULL,NULL,'$name 變身',NULL,'- $polypoint','變身專區','$time','$ip')";
	$db->query($str);
	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$id','回饋點數',NULL,NULL,'$name 變身',NULL,'+ $polypoint','變身專區','$time','$ip')";
	$db->query($str);
	$str="update user_register set event_point=event_point-$polypoint , bonus_point=bonus_point+$polypoint where name='$id'";
	$db->query($str);
	$str="Delete from character_buff where char_obj_id='$objid'";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','26','$polysec','0')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','42','$polysec','0')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','67','$polysec','$polyid')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','115','$polysec','0')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','117','$polysec','0')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','168','$polysec','0')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','1000','$polysec','0')";
	$db->query($str);
	$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','1001','$polysec','0')";
	$db->query($str);
	$msg=$msg."變身完成<br>本次花費點數 $polypoint 點<br>獲得回饋點數 $polypoint 點<br>";
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
