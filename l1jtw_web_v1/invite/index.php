<?
require("setup.php");
$no=$_GET[n];
$code=$_GET[c];
$err_count=0;
$a=1;

$str="select count(*) from zwls_invite_zone where no='$no' and code='$code'";
$chk_no=$db->get_var($str);
if($chk_no==0){
	$err_count++;
	$msg=$msg."編號錯誤<br>";
}

if($open_invitezone!=1){
	$err_count++;
	$msg=$msg."本系統尚未開放<br>";
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM zwls_invite_zone WHERE `no` LIKE '$no' ORDER BY no ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$account=$strac['account'];
	$name=$strac['name'];

	$str="select count(*) from zwls_invite_code where name='$account' and used=0";
	$chk_cd=$db->get_var($str);
	if($chk_cd==0){
		$err_count++;
		$msg=$msg."目前尚無註冊碼<br>";
	}

	$strba=sprintf("SELECT * FROM zwls_invite_code WHERE `name` LIKE '$account' AND `used` LIKE '0' ORDER BY time ASC , invitecode ASC");
	$strbb=mysql_query($strba, $login_on) or die(mysql_error());
	$strbc=mysql_fetch_assoc($strbb);
}
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$name?>的註冊碼分享區</title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red><?=$name?>的註冊碼分享區</font></div></td>
	</tr>
<?
if($err_count==0){
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">您好，歡迎加入本站會員。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">本站目前使用推薦制，請點選以下任一個註冊碼後，進入網站申請會員。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">申請會員成功後，往後進入本站請使用最下面的網址進入即可。</div></td>
	</tr>
<?
	do{
?>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">註冊碼<?=$a?>：<a href="javascript://" onClick="window.open('index2.php?itc=<?=$strbc['invitecode']?>','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=1024,height=768')"><?=$strbc['invitecode']?></a></div></td>
	</tr>
<?
		$a++;
	}while($strbc = mysql_fetch_assoc($strbb));
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>服務狀況</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器名稱：<?=$lineage_server_name?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>伺服器版本：<?=$lineage_server_ver?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器狀態：<?
	   $fso = @fsockopen($lineage_server_ip, $lineage_server_port, &$errno, &$errstr, 3);
	   if ($fso) echo "<b><font color=00ff00>伺服器執行中</font></b>";
	   else echo "<b><font color=ff0000>伺服器維修中</font></b>";
	  ?></font></td></tr>
	<tr>
		<td width="100%" bgcolor=#101010>伺服器經驗倍率：<?=$lineage_server_exp?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器金幣倍率：<?=$lineage_server_money?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>伺服器掉寶倍率：<?=$lineage_server_item?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器善惡倍率：<?=$lineage_server_law?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器友好倍率：<?=$lineage_server_kar?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>伺服器武器強化成功率：<?=$lineage_server_w?>%</td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020>伺服器防具強化成功率：<?=$lineage_server_a?>%</td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010>伺服器負重倍率：<?=$lineage_server_wei?></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="right">本站網址：<a href="./"><?=$web_url?>/</a></div></td>
	</tr>
<?
}else{
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</center>
</body>
</html>