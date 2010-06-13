<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$err_count=0;
	$err_count2=0;
	$a=1;
	$b=1;
	$c=1;
	$d=1;
	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}
	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	$str="select count(*) from characters where account_name='$id'";
	$chk_cm=$db->get_var($str);

	$str="select count(*) from characters where account_name='system'";
	$chk_cs=$db->get_var($str);

	$str="select count(*) from zwls_accounts_trade where tradestatus='0'";
	$chk_trc=$db->get_var($str);

	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);

	$strba=sprintf("SELECT * FROM characters WHERE `account_name` LIKE 'system' ORDER BY `objid` ASC");
	$strbb=mysql_query($strba, $login_on) or die(mysql_error());
	$strbc=mysql_fetch_assoc($strbb);
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
	<tr>
		<td width="100%" bgcolor=#303030 colspan="16"><div align="center"><font size=2 color=red>角色交易</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="16"><div align="left">每筆交易成功時由系統抽取<?=$chartradetax?>%的點數處理費。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="16"><div align="left">交易最低點數為<?=$chartrademinsellprice?>點，最高<?=$chartrademaxsellprice?>點。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="16"><div align="left">交易後不接受任何條件反悔，買賣前請三思。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="16"><div align="left">本系統採用點數交易。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="16"><div align="left">交易完成前，原賣家可以無視售價購回。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="16"><div align="left">交易包含角色身上的道具、魔法、伴侶等等。</div></td>
	</tr>
<?
	if($chk_cm!=0){
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="16"><div align="center">個人角色清單</div></td>
	</tr>
	<tr>
		<td width="5%" bgcolor=#202020><div align="center">NO.</div></td>
		<td width="20%" bgcolor=#202020><div align="center">暱稱</div></td>
		<td width="5%" bgcolor=#202020><div align="center">等級</div></td>
		<td width="5%" bgcolor=#202020><div align="center">職業</div></td>
		<td width="5%" bgcolor=#202020><div align="center">性別</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Hp</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Mp</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Str</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Dex</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Con</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Wis</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Cha</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Int</div></td>
		<td width="10%" bgcolor=#202020><div align="center">善惡</div></td>
		<td width="5%" bgcolor=#202020><div align="center">售價</div></td>
		<td width="5%" bgcolor=#202020><div align="center">操作</div></td>
	</tr>
<?
		do{
?>
<form method="POST" action="char_save.php" name="form<?=$c?>" onsubmit="B<?=$c?>.disabled=1">
<input type="hidden" name="mode" value="0">
<input type="hidden" name="code" value="<?=$code?>">
<input type="hidden" name="objid" value="<?=$strac['objid']?>">
	<tr>
		<td bgcolor=#101010><div align="center"><?=$c?></div></td>
		<td bgcolor=#101010><div align="center"><a href="javascript://" onClick="window.open('char_item.php?objid=<?=$strac['objid']?>','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=400')"><?=$strac['char_name']?></a></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['level']?></div></td>
		<td bgcolor=#101010><div align="center"><a href="javascript://" onClick="window.open('char_skill.php?objid=<?=$strac['objid']?>','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=400')"><?
if($strac['Type'] == "0" || $strac['Type'] == "32") echo "王族";
if($strac['Type'] == "1" || $strac['Type'] == "33") echo "騎士";
if($strac['Type'] == "2" || $strac['Type'] == "34") echo "妖精";
if($strac['Type'] == "3" || $strac['Type'] == "35") echo "法師";
if($strac['Type'] == "4" || $strac['Type'] == "36") echo "黑妖";
if($strac['Type'] == "5" || $strac['Type'] == "37") echo "龍騎";
if($strac['Type'] == "6" || $strac['Type'] == "38") echo "幻術";
?></a></div></td>
		<td bgcolor=#101010><div align="center"><?
if($strac['Sex'] == "0") echo "男";
else echo "女";
?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['MaxHp']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['MaxMp']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Str']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Dex']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Con']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Wis']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Cha']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Intel']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strac['Lawful']?></div></td>
		<td bgcolor=#101010><div align="center"><input name="sellpoint" value="1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
		<td bgcolor=#101010><div align="center"><input type="submit" value="販售" name="B<?=$c?>" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?
			$c++;
		}while($strac=mysql_fetch_assoc($strab));
	}
	if($chk_cs!=0){
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="16"><div align="center">販賣角色清單 (<?=$chk_trc?>/<?=$chartrademaxallsell?>)</div></td>
	</tr>
	<tr>
		<td width="5%" bgcolor=#202020><div align="center">NO.</div></td>
		<td width="20%" bgcolor=#202020><div align="center">暱稱</div></td>
		<td width="5%" bgcolor=#202020><div align="center">等級</div></td>
		<td width="5%" bgcolor=#202020><div align="center">職業</div></td>
		<td width="5%" bgcolor=#202020><div align="center">性別</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Hp</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Mp</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Str</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Dex</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Con</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Wis</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Cha</div></td>
		<td width="5%" bgcolor=#202020><div align="center">Int</div></td>
		<td width="10%" bgcolor=#202020><div align="center">善惡</div></td>
		<td width="5%" bgcolor=#202020><div align="center">售價</div></td>
		<td width="5%" bgcolor=#202020><div align="center">操作</div></td>
	</tr>
<?
		do{
		$strca = sprintf("SELECT * FROM zwls_accounts_trade WHERE `objid` LIKE '".$strbc['objid']."' AND `tradestatus` LIKE '0' ORDER BY `objid` ASC");
		$strcb = mysql_query($strca, $login_on) or die(mysql_error());
		$strcc = mysql_fetch_assoc($strcb);

		$strda=sprintf("SELECT * FROM characters WHERE `account_name` LIKE '".$strcc['whosell']."' ORDER BY `level` DESC");
		$strdb=mysql_query($strda, $login_on) or die(mysql_error());
		$strdc=mysql_fetch_assoc($strdb);
		$username=$strdc['char_name'];
?>
<form method="post" action="char_save.php" name="form<?=$d?>" onsubmit="B<?=$d?>.disabled=1">
<input type="hidden" name="mode" value="1">
<input type="hidden" name="code" value="<?=$code?>">
<input type="hidden" name="objid" value="<?=$strbc['objid']?>">
	<tr>
		<td bgcolor=#101010><div align="center"><?=$d?></div></td>
		<td bgcolor=#101010><div align="center"><a href="javascript://" onClick="window.open('char_item.php?objid=<?=$strbc['objid']?>&mode=1','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=400')"><?=$strbc['char_name']?></a></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['level']?></div></td>
		<td bgcolor=#101010><div align="center"><a href="javascript://" onClick="window.open('char_skill.php?objid=<?=$strbc['objid']?>&mode=1','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=400')"><?
if($strbc['Type'] == "0") echo "王族";
if($strbc['Type'] == "1") echo "騎士";
if($strbc['Type'] == "2") echo "妖精";
if($strbc['Type'] == "3") echo "法師";
if($strbc['Type'] == "4") echo "黑妖";
if($strbc['Type'] == "5") echo "龍騎";
if($strbc['Type'] == "6") echo "幻術";
?></a></div></td>
		<td bgcolor=#101010><div align="center"><?
if($strbc['Sex'] == "0") echo "男";
else echo "女";
?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['MaxHp']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['MaxMp']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['Str']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['Dex']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['Con']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['Wis']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['Cha']?></div></td>
		<td bgcolor=#101010><div align="center"><?=$strbc['Intel']?></div></td>
		<td bgcolor=#101010><div align="center"><font title="By <?=$username?> <?=$strcc['selltime']?>"><?=$strbc['Lawful']?></font></div></td>
		<td bgcolor=#101010><div align="center"><?=$strcc['point']?></div></td>
		<td bgcolor=#101010><div align="center"><input type="submit" value="購買" name="B<?=$c?>" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?
			$d++;
		}while($strbc=mysql_fetch_assoc($strbb));
	}
}else{?>
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