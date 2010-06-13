<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";

$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level='200'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$str="select count(*) from zwls_event_item_card";
	$chk_rps=$db->get_var($str);
	if($chk_rps==0){
		$msg=$msg."尚無申請紀錄<br>";
	}

	mysql_select_db($sql_dbname, $login_on);
	if($chk_rps!=0){
		$straa = sprintf("SELECT * FROM zwls_event_item_card ORDER BY `no` DESC LIMIT 0,50");
		$strab = mysql_query($straa, $login_on) or die(mysql_error());
		$strac = mysql_fetch_assoc($strab);
	}

	$strba = sprintf("SELECT * FROM zwls_event_item_card ORDER BY `no` DESC LIMIT 0,50");
	$strbb = mysql_query($strba, $login_on) or die(mysql_error());
	$strbc = mysql_fetch_assoc($strbb);
}
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
<?if($err_count==0){?>
<form method="post" action="eventitemcard_save.php" name="form1" onsubmit="B1.disabled=1">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center"><a href="javascript:location.reload()"><font size=2 color=red>活動物品獎勵申請區</font></a></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">本系統僅顯示前50次申請資料。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">請詳細填寫獎勵內容。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">不可堆疊之物品，一次請填寫一張。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">活動名稱：<input name="eventname" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$strac['eventname']?>">(辨識用)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">物品代號：<input name="itemid" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$strac['itemid']?>">(編號需與名稱相同)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">物品等級：<select size="1" name="itemlv" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?
if($chk_rps!=0){
?>
<option value="<?=$strac['itemlv']?>"><?=$strac['itemlv']?></option>
<?
}
$itemlv=0;
While($itemlv<=20){
?>
<option value="<?=$itemlv?>"><?=$itemlv?></option>
<?
$itemlv++;
}
?>
</select>(消耗物品無須設定)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">物品名稱：<input name="itemname" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$strac['itemname']?>">(名稱需與編號相同)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">物品數量：<input name="itemcount" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$strac['itemcount']?>">(不可堆疊的請分次申請)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">使用秒數：<select size="1" name="itemusetime" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?
if($chk_rps!=0 && $strac['itemusetime']==0){
?>
<option value="0">無限制</option>
<?
}elseif($chk_rps!=0 && $strac['itemusetime']!=0){
?>
<option value="<?=$strac['itemusetime']?>"><?=$strac['itemusetime']?>秒</option>
<?
}
?>
<option value="0">無限制</option>
<?
$itemusetime=30;
While($itemusetime<=7200){
?>
<option value="<?=$itemusetime?>"><?=$itemusetime?>秒</option>
<?
$itemusetime=$itemusetime+30;
}
?>
</select>(消耗物品無須設定)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">獲獎暱稱：<input name="itemcharname" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="">(是暱稱不是帳號)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center">活動物品獎勵申請紀錄</div></td>
	</tr>
<?if($chk_rps!=0){?>
	<tr>
		<td width="10%" bgcolor=#303030><div align="center">NO.</div></td>
		<td width="30%" bgcolor=#303030><div align="center">活動名稱</div></td>
		<td width="30%" bgcolor=#303030><div align="center">物品名稱</div></td>
		<td width="15%" bgcolor=#303030><div align="center">獲獎暱稱</div></td>
		<td width="15%" bgcolor=#303030><div align="center">處理進度</div></td>
	</tr>
<?
do{
$usetime="";
if($strbc['itemusetime']==0){$usetime="無限制";}
else{$usetime=$strbc['itemusetime']."秒";}
?>
	<tr>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strbc['no']?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><font title="申請:<?=$strbc['gmaccount']?>"><?=$strbc['eventname']?></font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><font title="編號:<?=$strbc['itemid']?> 秒數:<?=$usetime?>">+<?=$strbc['itemlv']?> <?=$strbc['itemname']?> (<?=$strbc['itemcount']?>)</font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><font title="<?=$strbc['itemaccount']?>"><?=$strbc['itemcharname']?></font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?
$repaysmsg="";
if($strbc['ok']==0){$repaysmsg="<a href=\"eventitemcard_ok.php?eventno=".$strbc['no']."\"><font color=green>通過</font></a> <a href=\"eventitemcard_no.php?eventno=".$strbc['no']."\"><font color=red>拒絕</font></a>";}
elseif($strbc['ok']==1){$repaysmsg="<font color=blue title=\"通過 by ".$strbc['okid']."\">處理完畢</font>";}
elseif($strbc['ok']==2){$repaysmsg="<font color=blue title=\"拒絕 by ".$strbc['okid']."\">處理完畢</font>";}
echo $repaysmsg;
?></div></td>
	</tr>
<?
}while($strbc = mysql_fetch_assoc($strbb));
}else{
?>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left"><?=$msg?></div></td>
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