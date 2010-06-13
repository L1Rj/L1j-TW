<?
require("../setup.php");
$code=$_POST[code];
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$no=$_POST[no];
$rename=$_POST[mastername];
$rppoint=$_POST[rppoint];
$rppoint=(int)$rppoint;
$s2=$_POST[s2];
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
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
<?
$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level=200";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤";
}

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤<br>";
}

if($rppoint<=0){$rppoint=0;}
elseif($rppoint>=1000000){$rppoint=1000000;}

if($rppoint!=0){
	$s2=$s2."
本次回報贈與 $rppoint 點。";
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM characters WHERE `char_name` LIKE '$rename' ORDER BY `objid` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
	$reobjid=$strac['objid'];

	$strba = sprintf("SELECT * FROM zwls_onlinerepays WHERE `no` LIKE '$no' ORDER BY `no` ASC");
	$strbb = mysql_query($strba, $login_on) or die(mysql_error());
	$strbc = mysql_fetch_assoc($strbb);
	$rpacc=$strbc['account'];

	$str="select count(*) from characters where account_name='$id' and objid='$reobjid'";
	$chk_char=$db->get_var($str);
	if($chk_char==0){
		$err_count++;
		$msg=$msg."異常錯誤(非法操作)<br>";
	}
}

if($err_count==0){
	$str="update zwls_onlinerepays set mastername='$rename' where no='$no'";
	$db->query($str);
	$str="update zwls_onlinerepays set userread=0 , masteraccount='$id' , masterobjid='$reobjid' , s2='$s2' , mastertime='$time' , masterip='$ip' where no='$no'";
	$db->query($str);

	if($rppoint!=0){
	$str="insert into `zwls_item_get_log` (`no` , `account` , `itemtype` , `objid` , `itemno` , `itemname` , `itemcount` , `point` , `logfrom` , `time` , `ip`) values (NULL,'$rpacc','點數',NULL,NULL,'點數','$rppoint','+ $rppoint','回報專區','$time','$ip')";
	$db->query($str);
	$str="update user_register set event_point=event_point+$rppoint where name='$rpacc'";
	$db->query($str);
	}

	$msg=$msg."回應完成<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</div></td></tr>
</table>
</center>
</body>
</html>