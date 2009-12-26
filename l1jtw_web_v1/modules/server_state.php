<?php
require("../setup.php");
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>服務狀況</font></div>
	</td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>名稱：<?=$lineage_server_name?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>版本：<?=$lineage_server_ver?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器狀態：<?
	   $fso = @fsockopen($lineage_server_ip, $lineage_server_port, &$errno, &$errstr, 3);
	   if ($fso) echo "<b><font color=00ff00>正常</font></b>";
	   else echo "<b><font color=ff0000>維修中</font></b>";
	  ?></font></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>經驗倍率：<?=$lineage_server_exp?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>金幣倍率：<?=$lineage_server_money?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>道具掉落率：<?=$lineage_server_item?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>正義值倍率：<?=$lineage_server_law?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>好感度倍率：<?=$lineage_server_kar?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>武器強化率：<?=$lineage_server_w?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>防具強化率：<?=$lineage_server_a?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>負重倍率：<?=$lineage_server_wei?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>寵物經驗倍率：<?=$lineage_server_petexp?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>寵物負重倍率：<?=$lineage_server_petwei?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>商店販賣倍率：<?=$lineage_server_shopsell?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>商店收購倍率：<?=$lineage_server_shopbuy?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>重新啟動時間：<?=$lineage_server_restart?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>固定關機時間：<?=$lineage_server_closetime?></td>
	</tr>
</table>
</center>
</body>
</html>