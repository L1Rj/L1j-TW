<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$no=$_GET[no];
$err_count=0;
$msg="";
$c=1;

$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level=200";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤";
}

$str="select count(*) from zwls_onlinerepays where no='$no'";
$chk_rps=$db->get_var($str);
if($chk_rps==0){
	$err_count++;
	$msg=$msg."無回報資料";
}

if($err_count==0){
	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	$str="update zwls_onlinerepays set masterread=1 where no=$no";
	$db->query($str);
	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_onlinerepays WHERE `no` LIKE '$no' ORDER BY `no` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
	$type=$strac['type'];

	$strba = sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' AND `AccessLevel` LIKE '200' ORDER BY `objid` ASC");
	$strbb = mysql_query($strba, $login_on) or die(mysql_error());
	$strbc = mysql_fetch_assoc($strbb);

	if($type==1){$type="網站問題";}
	elseif($type==2){$type="遊戲問題";}
	elseif($type==3){$type="角色問題";}
	elseif($type==4){$type="道具問題";}
	elseif($type==5){$type="倉庫問題";}
	elseif($type==6){$type="技能問題";}
	elseif($type==7){$type="血盟問題";}
	elseif($type==8){$type="帳號問題";}
	elseif($type==9){$type="檢舉違規";}
	elseif($type==11){$type="系統訊息";}
	else{$type="其他";}
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
<form method="POST" action="rerepays_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
<input type="hidden" name="no" value="<?=$no?>">
	<tr>
		<td bgcolor=#303030 width="100%"><div align="left">
回報編號：<?=$strac['no']?><br>
回報帳號：<?=$strac['account']?><br>
回報暱稱：<?=$strac['name']?>(<?=$strac['ip']?>)<br>
回報類型：<?=$type?><br>
回報內容：<br>
<textarea readonly=readonly name="s1" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?=$strac['s1']?></textarea><br>
回報時間：<?=$strac['time']?>
</div></td>
	</tr>
	<tr>
		<td bgcolor=#202020><div align="left">
原始回應暱稱：<?=$strac['mastername']?><br>
新的回應暱稱：<select size="1" name="mastername" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?do{?>
<option value="<?=$strbc['char_name']?>"><?=$strbc['char_name']?></option>
<?}while($strbc=mysql_fetch_assoc($strbb));?>
</select><br>
回應內容：<br>
<textarea name="s2" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?if($strac['s2']==NULL){?>您好，
您的建議與回報，
已經紀錄並交由相關人員處理。
如有任何建議，也歡迎您批評指教，
再次感謝您的支持。<?}else{?><?=$strac['s2']?><?}?></textarea><br>
回報獎勵：<input name="rppoint" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="7" value=0>點<br>
回應時間：<?=$strac['mastertime']?><br>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</form>
</table>
</center>
</body></html>