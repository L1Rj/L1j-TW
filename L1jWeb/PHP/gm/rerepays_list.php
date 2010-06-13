<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";
$c=1;

$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level=200";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$str="select count(*) from zwls_onlinerepays";
	$chk_rps=$db->get_var($str);
	if($chk_rps==0){
		$msg=$msg."尚無回報紀錄<br>";
	}

	$sartmsg=$chk_rps-20;
	if($sartmsg<=0){$sartmsg=0;}

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_onlinerepays ORDER BY `no` ASC LIMIT $sartmsg,20");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);
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
<?
if($err_count==0){
if($chk_rps!=0){
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="6"><div align="center"><a href="javascript:location.reload()"><font size=2 color=red>處理中心</font></a></div></td>
	</tr>
	<tr>
		<td bgcolor=#202020 width="10%"><div align="center" class="style7">NO.</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回報類型</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回報暱稱</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回報時間</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回應暱稱</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回應時間</div></td>
	</tr>
<?
do{
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
elseif($type==11){$type="系統訊息";}
else{$type="其他";}

$readmsg="<b>未讀</b>";
if($strac['userread']==1){$readmsg="";}
if($strac['masterread']==0){$type="<b>".$type."</b>";}
?>
	<tr>
		<td bgcolor=#101010><div align="center" class="style7"><?=$strac['no']?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><a href="javascript://" onClick="window.open('rerepays.php?no=<?=$strac['no']?>','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=450')"><?=$type?></a></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><font title="<?=$strac['account']?>"><?=$strac['name']?></font> <?=$readmsg?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?=$strac['time']?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><font title="<?=$strac['masteraccount']?>"><?=$strac['mastername']?></font></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?=$strac['mastertime']?></div></td>
	</tr>
<?
}while($strac = mysql_fetch_assoc($strab));
}else{
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td></tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td></tr>
</table>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td bgcolor=#202020 width="10%"><div align="center" class="style7">NO.</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回報類型</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回報暱稱</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回報時間</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回應暱稱</div></td>
		<td bgcolor=#202020 width="18%"><div align="center" class="style7">回應時間</div></td>
	</tr>
<?}?>
	<tr>
		<td bgcolor=#202020><div align="center" class="style7">--</div></td>
		<td bgcolor=#202020><div align="center" class="style7"><a href="javascript://" onClick="window.open('gmmsg.php','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=450')">系統訊息</a></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><font title="systemmsg">System_msg</font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$time?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><font title="systemmsg">System_msg</font></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$time?></div></td>
	</tr>
<?}else{?>
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