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

	$str="select count(*) from character_warehouse where account_name='$id'";
	$chk_wm=$db->get_var($str);

	$str="select count(*) from character_warehouse where account_name='system'";
	$chk_ws=$db->get_var($str);

	$str="select count(*) from zwls_item_trade where whosell='$id' and tradestatus='0'";
	$chk_trc=$db->get_var($str);

	$str="select count(*) from zwls_item_trade where tradestatus='0'";
	$chk_trc2=$db->get_var($str);

	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM character_warehouse WHERE `account_name` LIKE '$id' ORDER BY `item_id` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);

	$strba=sprintf("SELECT * FROM zwls_item_trade WHERE `tradestatus` LIKE '0' ORDER BY `selltime` DESC");
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
		<td width="100%" bgcolor=#303030 colspan="6"><div align="center"><font size=2 color=red>二手市場</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="6"><div align="left">每筆交易成功時由系統抽取<?=$itemtradetax?>%的點數處理費。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="6"><div align="left">交易最低點數為<?=$itemtrademinsellprice?>點，最高<?=$itemtrademaxsellprice?>點。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="6"><div align="left">交易後不接受任何條件反悔，買賣前請三思。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="6"><div align="left">本系統採用點數交易。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="6"><div align="left">交易完成前，原賣家可以無視售價購回。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="6"><div align="left">目前單人販售數量限制為<?=$itemtrademaxsell?>件。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="6"><div align="left">全部販售數量限制為<?=$itemtrademaxallsell?>件。</div></td>
	</tr>
<?
	if($chk_wm!=0){
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="6"><div align="center">倉庫物品清單(<?=$chk_trc?>/<?=$itemtrademaxsell?>)</div></td>
	</tr>
	<tr>
		<td width="10%" bgcolor=#202020><div align="center">NO.</div></td>
		<td width="50%" bgcolor=#202020><div align="center">道具名稱</div></td>
		<td width="10%" bgcolor=#202020><div align="center">數量</div></td>
		<td width="10%" bgcolor=#202020><div align="center">鑑定</div></td>
		<td width="10%" bgcolor=#202020><div align="center">售價</div></td>
		<td width="10%" bgcolor=#202020><div align="center">操作</div></td>
	</tr>
<?
		do{
			if($strac['item_id']>=100000 && $strac['item_id']<200000){
				$color="<font color=yellow>+".$strac['enchantlvl']." ".$strac['item_name']."</font>";
			}elseif($strac['item_id']>=200000 && $strac['item_id']<300000){
				$color="<font color=red>+".$strac['enchantlvl']." ".$strac['item_name']."</font>";
			}else{
				$color="+".$strac['enchantlvl']." ".$strac['item_name'];
			}

			if($strac['remaining_time']!=0){
				$color=$color."(".$strac['remaining_time']."秒)";
			}
?>
<form method="post" action="item_save.php" name="form1" onsubmit="B<?=$c?>.disabled=1">
<input type="hidden" name="mode" value="0">
<input type="hidden" name="code" value="<?=$code?>">
<input type="hidden" name="objid" value="<?=$strac['id']?>">
	<tr>
		<td bgcolor=#101010><div align="center" class="style7"><?=$c?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?=$color?> <a href="#" onclick="if (confirm('刪除後就無法復原，確認要刪除？')) location.href='delcwitem.php?itemobjid=<?=$strac['id']?>'"><font size=2 color=red><b>刪除</b></font></a></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?=$strac['count']?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?
if($strac['is_id'] == "1") echo "是";
else echo "否";
?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><input name="sellpoint" value="1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
		<td bgcolor=#101010><div align="center"><input type="submit" value="販售" name="B<?=$c?>" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?
			$c++;
		}while($strac=mysql_fetch_assoc($strab));
	}
	if($chk_trc2!=0){
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="6"><div align="center">販賣物品清單(<?=$chk_trc2?>/<?=$itemtrademaxallsell?>)</div></td>
	</tr>
	<tr>
		<td width="10%" bgcolor=#202020><div align="center">NO.</div></td>
		<td width="50%" bgcolor=#202020><div align="center">道具名稱</div></td>
		<td width="10%" bgcolor=#202020><div align="center">數量</div></td>
		<td width="10%" bgcolor=#202020><div align="center">鑑定</div></td>
		<td width="10%" bgcolor=#202020><div align="center">售價</div></td>
		<td width="10%" bgcolor=#202020><div align="center">操作</div></td>
	</tr>
<?
		do{
			$strca=sprintf("SELECT * FROM character_warehouse WHERE `id` LIKE '".$strbc['id']."' ORDER BY `id` ASC");
			$strcb=mysql_query($strca, $login_on) or die(mysql_error());
			$strcc=mysql_fetch_assoc($strcb);

			$whosell=$strbc['whosell'];

			if($strcc['enchantlvl']==0){
				$itemlv="";
			}else{
				$itemlv="+<font color=orange>".$strcc['enchantlvl']."</font> ";
			}

			if($strcc['item_id']>=100000 && $strcc['item_id']<200000){
				$color2="<font color=yellow>".$itemlv.$strbc['itemname']."</font>";
			}elseif($strcc['item_id']>=200000 && $strcc['item_id']<300000){
			$color2="<font color=red>".$itemlv.$strbc['itemname']."</font>";
			}else{
				$color2=$itemlv.$strbc['itemname'];
			}

			if($strcc['remaining_time']!=0){
				$color2=$color2."(".$strcc['remaining_time']."秒)";
			}

			$strda=sprintf("SELECT * FROM characters WHERE `account_name` LIKE '".$strbc['whosell']."' ORDER BY `level` DESC");
			$strdb=mysql_query($strda, $login_on) or die(mysql_error());
			$strdc=mysql_fetch_assoc($strdb);
			$username=$strdc['char_name'];

			if($id==$whosell){
				$d_str="<b><font color=orange>".$d."</font></b>";
			}else{
				$d_str=$d;
			}
?>
<form method="post" action="item_save.php" name="form1" onsubmit="B<?=$d?>.disabled=1">
<input type="hidden" name="mode" value="1">
<input type="hidden" name="code" value="<?=$code?>">
<input type="hidden" name="objid" value="<?=$strbc['id']?>">
	<tr>
		<td bgcolor=#101010><div align="center" class="style7"><?=$d_str?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?if($id==$whosell){?><b><?=$color2?></b><?}else{?><?=$color2?><?}?><font size=2> By <?=$username?></font></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?=$strcc['count']?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?
if($strbc['is_id'] == "1") echo "是";
else echo "否";
?></div></td>
		<td bgcolor=#101010><div align="center" class="style7"><?=$strbc['point']?></div></td>
<?
if($id==$whosell){
$s_str="*收回*";
}else{
$s_str="購買";
}
?>
		<td bgcolor=#101010><div align="center"><input type="submit" value="<?=$s_str?>" name="B<?=$c?>" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
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