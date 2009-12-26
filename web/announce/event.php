<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$page=$_GET[page];
$err_count=0;
$msg="";

$onepageannouncecount=10;
$chcmin=0;

if($page==NULL){
	$page=1;
}

$str="select count(*) from zwls_event_announce where ok=1";
$chk_rps=$db->get_var($str);
if($chk_rps==0){
	$msg=$msg."尚無活動公告<br>";
}

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$chcmin=$onepageannouncecount*($page-1);

	$pagemax=$chk_rps/$onepageannouncecount;
	$pagemax=(int)$pagemax;

	$ckpagemax=$pagemax*$onepageannouncecount;
	if($ckpagemax<$chk_rps){
		$pagemax=$pagemax+1;
	}

	$pagea=1;
	$pageb=0;
	While($pagea<=$pagemax){
		if($pageb==5){
			$pageb=0;
			$pagetb=$pagetb."<br>";
		}

		$pagetb=$pagetb."<a href=\"?page=$pagea\">第 $pagea 頁</a> ";
		$pagea++;
		$pageb++;
	}

	if($chk_rps!=0){
		mysql_select_db($sql_dbname, $login_on);
		$straa = sprintf("SELECT * FROM zwls_event_announce WHERE `ok` LIKE 1 ORDER BY `no` DESC LIMIT $chcmin,$onepageannouncecount");
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
<form method="post" action="eventitemcard_save.php" name="form1" onsubmit="B1.disabled=1">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center"><font size=2 color=red>活動公告</font></div></td>
	</tr>
<?if($chk_rps!=0){?>
	<tr>
		<td width="10%" bgcolor=#303030><div align="center">NO.</div></td>
		<td width="40%" bgcolor=#303030><div align="center">活動名稱</div></td>
		<td width="20%" bgcolor=#303030><div align="center">起始日期</div></td>
		<td width="20%" bgcolor=#303030><div align="center">結束日期</div></td>
		<td width="10%" bgcolor=#303030><div align="center">活動狀態</div></td>
	</tr>
<?
do{
	$eventstarttime=$strac['eventstarttime'];
	$eventstoptime=$strac['eventstoptime'];

	if($eventstarttime=="0000-00-00 00:00:00"){
		$eventstarttime="無限制";
	}

	if($eventstoptime=="0000-00-00 00:00:00"){
		$eventstoptime="無限制";
	}

	if($time>=$eventstarttime||$eventstarttime=="無限制"){
		$eventstatus="<a href=\"event_announce.php?no=".$strac['no']."\">進行中</a>";
	}elseif($time<$eventstarttime){
		$eventstatus="<a href=\"event_announce.php?no=".$strac['no']."\">未開始</a>";
	}

	if($time>=$eventstoptime){
		$eventstatus="已結束";
	}
?>
	<tr>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strac['no']?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strac['eventname']?></font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$eventstarttime?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$eventstoptime?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$eventstatus?></div></td>
	</tr>
<?
}while($strac = mysql_fetch_assoc($strab));
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center"><?=$pagetb?></div></td>
	</tr>
<?
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
