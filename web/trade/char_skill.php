<?
require("../setup.php");
$mode=$_GET[mode];
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
$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($open_chtr!="1"){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	$objid=$_GET[objid];
	$c=1;

	$str="select count(*) from character_skills where char_obj_id='$objid'";
	$chk_count=$db->get_var($str);
	if($chk_count==0){
		$err_count++;
		$msg=$msg."無技能資料";
	}

	if($mode==1){
		$str="select count(*) from zwls_accounts_trade where objid='$objid' and tradestatus='0'";
		$chk_er=$db->get_var($str);
		if($chk_er==0){
			$err_count++;
			$msg=$msg."異常錯誤(非法操作)";
		}
	}else{
		$str="select count(*) from accounts where login='$id' and password='$e_pass'";
		$chk_id=$db->get_var($str);
		if($chk_id==0){
			$err_count++;
			$msg=$msg."異常錯誤(非法操作)";
		}

		$str="select count(*) from characters where account_name='$id' and objid='$objid'";
		$chk_er2=$db->get_var($str);
		if($chk_er2==0){
			$err_count++;
			$msg=$msg."異常錯誤(非法操作)";
		}
	}
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM character_skills WHERE `char_obj_id` LIKE '$objid' ORDER BY `skill_id` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
	<tr>
		<td width="40%" bgcolor=#303030><div align="center">NO.</div></td>
		<td width="60%" bgcolor=#303030><div align="center">技能名稱</div></td>
	</tr>
<?
	do{
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$c?></div></td>
		<td bgcolor=#202020><div align="center"><?=$strac['skill_name']?></div></td>
<?
	$c++;
	}while($strac=mysql_fetch_assoc($strab));
}else{?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</center>
</body>
</html>