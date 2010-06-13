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
	$str="select count(*) from zwls_event_announce";
	$chk_rps=$db->get_var($str);
	if($chk_rps==0){
		$msg=$msg."尚無申請紀錄<br>";
	}

	mysql_select_db($sql_dbname, $login_on);
	if($chk_rps!=0){
		$straa = sprintf("SELECT * FROM zwls_event_announce ORDER BY `no` DESC LIMIT 0,50");
		$strab = mysql_query($straa, $login_on) or die(mysql_error());
		$strac = mysql_fetch_assoc($strab);
	}
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
<form method="post" action="newevent_save.php" name="form1" onsubmit="B1.disabled=1">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center"><a href="javascript:location.reload()"><font size=2 color=red>活動申請區</font></a></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">本系統僅顯示前10次申請資料。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">活動名稱：<input name="eventname" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value=""></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">起始日期：<input name="eventstarttime" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$time?>">YYYY-mm-dd HH:ii:ss</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">結束日期：<input name="eventstoptime" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value="<?=$time?>">YYYY-mm-dd HH:ii:ss</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">活動說明：<textarea name="eventhelp" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></textarea></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">物品說明：<textarea name="eventitemhelp" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">無</textarea></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td></tr>
</form>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center">活動申請紀錄</div></td>
	</tr>
<?if($chk_rps!=0){?>
	<tr>
		<td width="10%" bgcolor=#303030><div align="center">NO.</div></td>
		<td width="40%" bgcolor=#303030><div align="center">活動名稱</div></td>
		<td width="20%" bgcolor=#303030><div align="center">起始日期</div></td>
		<td width="20%" bgcolor=#303030><div align="center">結束日期</div></td>
		<td width="10%" bgcolor=#303030><div align="center">審核狀態</div></td>
	</tr>
<?
do{
$eventstarttime=$strac['eventstarttime'];
$eventstoptime=$strac['eventstoptime'];
if($eventstarttime=="0000-00-00 00:00:00"){$eventstarttime="無限制";}
if($eventstoptime=="0000-00-00 00:00:00"){$eventstoptime="無限制";}

if($strac['ok']==0){$eventokstatus="<a href=\"newevent_ok.php?no=".$strac['no']."&ok=1\"><font color=green>通過</font></a> <a href=\"newevent_ok.php?no=".$strac['no']."&ok=2\"><font color=red>拒絕</font></a>";}
elseif($strac['ok']==1){$eventokstatus="<font color=blue title=\"通過 by ".$strac['okaccount']."\">處理完畢</font>";}
else{$eventokstatus="<font color=blue title=\"拒絕 by ".$strac['okaccount']."\">處理完畢</font>";}
?>
	<tr>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strac['no']?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><font title="申請:<?=$strac['gmaccount']?>"><a href="event.php?no=<?=$strac['no']?>"><?=$strac['eventname']?></a></font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$eventstarttime?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$eventstoptime?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$eventokstatus?></div></td>
	</tr>
<?
}while($strac = mysql_fetch_assoc($strab));
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
