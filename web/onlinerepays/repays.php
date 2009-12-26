<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$no=$_GET[no];
$err_count=0;
$msg="";
$c=1;

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from zwls_onlinerepays where no='$no' and account='$id'";
$chk_rpcount=$db->get_var($str);
if($chk_rpcount==0){
	$err_count++;
	$msg=$msg."無回報資料";
}

if($err_count==0){
	$str="update zwls_onlinerepays set userread=1 where no=$no";
	$db->query($str);

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_onlinerepays WHERE `no` LIKE '$no' ORDER BY `no` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
	$type=$strac['type'];

	if($type==1){$type="網站問題";}
	elseif($type==2){$type="遊戲問題";}
	elseif($type==3){$type="角色問題";}
	elseif($type==4){$type="道具問題";}
	elseif($type==5){$type="倉庫問題";}
	elseif($type==6){$type="技能問題";}
	elseif($type==7){$type="血盟問題";}
	elseif($type==8){$type="帳號問題";}
	elseif($type==9){$type="檢舉違規";}
	else{$type="其他";}

	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);
}
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<center>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
<?if($err_count==0){?>
	<tr>
		<td bgcolor=#303030 width="100%"><div align="left">
回報編號：<?=$strac['no']?><br>
回報暱稱：<?=$strac['name']?><br>
回報類型：<?=$type?><br>
回報內容：<br>
<textarea readonly=readonly name="text" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?=$strac['s1']?></textarea><br>
回報時間：<?=$strac['time']?>
</div></td>
	</tr>
<?
	if($strac['mastertime']==NULL){
?>
	<tr>
		<td bgcolor=#303030 width="100%"><div align="center"><font size=4 color=red>尚未處理</font></div></td>
	</tr>
<?
	}else{
?>
	<tr>
		<td bgcolor=#202020><div align="left">
回應暱稱：<?=$strac['mastername']?><br>
回應內容：<br>
<textarea readonly=readonly name="text" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?=$strac['s2']?></textarea><br>
回應時間：<?=$strac['mastertime']?><br>
</div></td>
	</tr>
<?
	}
}else{
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</center>
</body></html>