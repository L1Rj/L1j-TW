<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$code="";
	$codestr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i = 0; $i < 10; $i++){
		$code .= $codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	$eid=$_GET[eid];

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_item_list WHERE id=$eid ORDER BY `id` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);

	$str="select count(*) from zwls_item_list where id=$eid and itemid!=0";
	$chk_count=$db->get_var($str);
	if($chk_count==0){
		$err_count++;
		$msg=$msg."無道具資料<br>";
	}
}
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>

<script Language="Javascript">
function tot(tot,t)
{
totalpoint=document.getElementById(t).value*<?=$strac['point']?>;
document.getElementById(tot).value = totalpoint;
}
</script>

</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<?if($err_count==0){?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="center"><font size=2 color=red>物品兌換</font></div></td>
	</tr>
<?
	$charge_count=$strac['charge_count'];
	$itemname=$strac['itemname'];
	if($charge_count!=0){
		$itemname=$itemname." (".$charge_count.")";
	}

	$itemstr="";
	$itemlv=$strac['itemlv'];
	if($strac['itemid']>=100000 && $strac['itemid']<200000){
		if($itemlv!=0){
			$color="<font color=yellow>+".$itemlv." ".$itemname."</font>";
		}else{
		$color="<font color=yellow>".$itemname."</font>";
		}
	}elseif($strac['itemid']>=200000 && $strac['itemid']<300000){
		if($itemlv!=0){
			$color="<font color=red>+".$itemlv." ".$itemname."</font>";
		}else{
		$color="<font color=red>".$itemname."</font>";
		}
	}else{
		if($itemlv!=0){
			$color="+".$itemlv." ".$itemname;
		}else{
		$color=$itemname;
		}
	}

	if($strac['starttime']==NULL){$strac['starttime']="無限制";}
	if($strac['stoptime']==NULL){$strac['stoptime']="無限制";}
	$s1="";
	$s2="";
	if($strac['count']==0){
		$s1="<s>";
		$s2="</s>";
	}

	if($strac['itemid']>0 && $strac['itemid']<20000 || $strac['itemid']>100000 && $strac['itemid']<120000 || $strac['itemid']>200000 && $strac['itemid']<220000){
		mysql_select_db($sql_dbname, $login_on);
		$strba = sprintf("SELECT * FROM weapon WHERE `item_id` LIKE '".$strac['itemid']."' ORDER BY `item_id` ASC");
		$strbb = mysql_query($strba, $login_on) or die(mysql_error());
		$strbc = mysql_fetch_assoc($strbb);

		$dmg="傷害：".$strbc['dmg_small']."/".$strbc['dmg_large']."<br>";
		if($strbc['safenchant']!=-1){$safe="安定值：".$strbc['safenchant']."<br>";}else{$safe="安定值：N/A<br>";}
		$weight=$strbc['weight']/1000;
		$weight="重量：".$weight."<br>";
		if($strbc['type']=="tohandsword" || $strbc['type']=="bow" || $strbc['type']=="tohandstaff"){$type="雙手武器<br>";}else{$type="";}
		if($strbc['use_royal']==1){$user="[王族]";}else{$user="";}
		if($strbc['use_knight']==1){$usek="[騎士]";}else{$usek="";}
		if($strbc['use_mage']==1){$usem="[法師]";}else{$usem="";}
		if($strbc['use_elf']==1){$usee="[妖精]";}else{$usee="";}
		if($strbc['use_darkelf']==1){$used="[黑暗精靈]";}else{$used="";}
		if($strbc['use_dragonknight']==1){$usedk="[龍騎士]";}else{$usedk="";}
		if($strbc['use_illusionist']==1){$usei="[幻術師]";}else{$usei="";}
		$use=$user.$usek.$usee.$usem.$used.$usedk.$usei;
		if($use=="[王族][騎士][妖精][法師][黑暗精靈][龍騎士][幻術師]"){$use="可使用職業：全職";}else{$use="可使用職業：".$user.$usek.$usee.$usem.$used.$usedk.$usei;}
		if($strbc['hitmodifier']!=0){$hitmod="額外攻擊成功：".$strbc['hitmodifier']."<br>";}else{$hitmod="";}
		if($strbc['dmgmodifier']!=0){$dmgmod="額外攻擊點數：".$strbc['dmgmodifier']."<br>";}else{$dmgmod="";}
		if($strbc['add_str']!=0){$addstr="力量：".$strbc['add_str']."<br>";}else{$addstr="";}
		if($strbc['add_con']!=0){$addcon="體質：".$strbc['add_con']."<br>";}else{$addcon="";}
		if($strbc['add_dex']!=0){$adddex="敏捷：".$strbc['add_dex']."<br>";}else{$adddex="";}
		if($strbc['add_int']!=0){$addint="智力：".$strbc['add_int']."<br>";}else{$addint="";}
		if($strbc['add_wis']!=0){$addwis="精神：".$strbc['add_wis']."<br>";}else{$addwis="";}
		if($strbc['add_cha']!=0){$addcha="魅力：".$strbc['add_cha']."<br>";}else{$addcha="";}
		if($strbc['add_hp']!=0){$addhp="體力：".$strbc['add_hp']."<br>";}else{$addhp="";}
		if($strbc['add_mp']!=0){$addmp="魔力：".$strbc['add_mp']."<br>";}else{$addmp="";}
		if($strbc['add_hpr']!=0){$addhpr="體力回覆量：".$strbc['add_hpr']."<br>";}else{$addhpr="";}
		if($strbc['add_mpr']!=0){$addmpr="魔力回覆量：".$strbc['add_mpr']."<br>";}else{$addmpr="";}
		if($strbc['add_sp']!=0){$addsp="魔攻：".$strbc['add_sp']."<br>";}else{$addsp="";}
		if($strbc['m_def']!=0){$md="魔防：".$strbc['m_def']."<br>";}else{$md="";}
		if($strbc['haste_item']==1){$haste="永久加速<br>";}else{$haste="";}
		if($strbc['magicdmgmodifier']!=0){$magicdmg="額外魔法傷害：".$strbc['magicdmgmodifier']."<br>";}else{$magicdmg="";}
		if($strbc['min_lvl']!=0){$minlv="等級限制：".$strbc['min_lvl']."以上<br>";}else{$minlv="";}
		if($strbc['max_lvl']!=0){$maxlv="等級限制：".$strbc['max_lvl']."以下<br>";}else{$maxlv="";}
		if($strbc['trade']!=0){$trade="無法轉移<br>";}else{$trade="";}
		
		$itemstr=$dmg.$safe.$type.$hitmod.$dmgmod.$addstr.$addcon.$adddex.$addint.$addwis.$addcha.$addhp.$addmp.$addhpr.$addmpr.$addsp.$md.$haste.$magicdmg.$minlv.$maxlv.$trade.$weight.$use;
	}elseif($strac['itemid']>20000 && $strac['itemid']<40000 || $strac['itemid']>120000 && $strac['itemid']<140000 || $strac['itemid']>220000 && $strac['itemid']<240000){
		mysql_select_db($sql_dbname, $login_on);
		$strba = sprintf("SELECT * FROM armor WHERE `item_id` LIKE '".$strac['itemid']."' ORDER BY `item_id` ASC");
		$strbb = mysql_query($strba, $login_on) or die(mysql_error());
		$strbc = mysql_fetch_assoc($strbb);

		$ac="防禦：".$strbc['ac']."<br>";
		if($strbc['safenchant']!=-1){$safe="安定值：".$strbc['safenchant']."<br>";}else{$safe="安定值：N/A<br>";}
		$weight=$strbc['weight']/1000;
		$weight="重量：".$weight."<br>";
		if($strbc['use_royal']==1){$user="[王族]";}else{$user="";}
		if($strbc['use_knight']==1){$usek="[騎士]";}else{$usek="";}
		if($strbc['use_mage']==1){$usem="[法師]";}else{$usem="";}
		if($strbc['use_elf']==1){$usee="[妖精]";}else{$usee="";}
		if($strbc['use_darkelf']==1){$used="[黑暗精靈]";}else{$used="";}
		if($strbc['use_dragonknight']==1){$usedk="[龍騎士]";}else{$usedk="";}
		if($strbc['use_illusionist']==1){$usei="[幻術師]";}else{$usei="";}
		$use=$user.$usek.$usee.$usem.$used.$usedk.$usei;
		if($use=="[王族][騎士][妖精][法師][黑暗精靈][龍騎士][幻術師]"){$use="可使用職業：全職";}else{$use="可使用職業：".$user.$usek.$usee.$usem.$used.$usedk.$usei;}
		if($strbc['add_str']!=0){$addstr="力量：".$strbc['add_str']."<br>";}else{$addstr="";}
		if($strbc['add_con']!=0){$addcon="體質：".$strbc['add_con']."<br>";}else{$addcon="";}
		if($strbc['add_dex']!=0){$adddex="敏捷：".$strbc['add_dex']."<br>";}else{$adddex="";}
		if($strbc['add_int']!=0){$addint="智力：".$strbc['add_int']."<br>";}else{$addint="";}
		if($strbc['add_wis']!=0){$addwis="精神：".$strbc['add_wis']."<br>";}else{$addwis="";}
		if($strbc['add_cha']!=0){$addcha="魅力：".$strbc['add_cha']."<br>";}else{$addcha="";}
		if($strbc['add_hp']!=0){$addhp="體力：".$strbc['add_hp']."<br>";}else{$addhp="";}
		if($strbc['add_mp']!=0){$addmp="魔力：".$strbc['add_mp']."<br>";}else{$addmp="";}
		if($strbc['add_hpr']!=0){$addhpr="體力回覆量：".$strbc['add_hpr']."<br>";}else{$addhpr="";}
		if($strbc['add_mpr']!=0){$addmpr="魔力回覆量：".$strbc['add_mpr']."<br>";}else{$addmpr="";}
		if($strbc['add_sp']!=0){$addsp="魔攻：".$strbc['add_sp']."<br>";}else{$addsp="";}
		if($strbc['m_def']!=0){$md="魔防：".$strbc['m_def']."<br>";}else{$md="";}
		if($strbc['haste_item']==1){$haste="永久加速<br>";}else{$haste="";}
		if($strbc['min_lvl']!=0){$minlv="等級限制：".$strbc['min_lvl']."以上<br>";}else{$minlv="";}
		if($strbc['max_lvl']!=0){$maxlv="等級限制：".$strbc['max_lvl']."以下<br>";}else{$maxlv="";}
		if($strbc['trade']!=0){$trade="無法轉移<br>";}else{$trade="";}

		if($strbc['damage_reduction']!=0){$damagereduction="額外傷害減免：".$strbc['damage_reduction']."<br>";}else{$damagereduction="";}
		if($strbc['weight_reduction']!=0){$weightreduction="載重上限增加<br>";}else{$weightreduction="";}
		if($strbc['hit_modifier']!=0){$hitmodifier="攻擊成功：".$strbc['hit_modifier']."<br>";}else{$hitmodifier="";}
		if($strbc['dmg_modifier']!=0){$dmg_modifier="額外攻擊點數：".$strbc['dmg_modifier']."<br>";}else{$dmg_modifier="";}
		if($strbc['bow_hit_modifier']!=0){$bowhitmodifier="弓的命中率：".$strbc['bow_hit_modifier']."<br>";}else{$bowhitmodifier="";}
		if($strbc['bow_dmg_modifier']!=0){$bowdmgmodifier="弓的打擊值：".$strbc['bow_dmg_modifier']."<br>";}else{$bowdmgmodifier="";}

		if($strbc['defense_water']!=0){$defensewater="水屬性魔防：".$strbc['defense_water']."<br>";}else{$defensewater="";}
		if($strbc['defense_wind']!=0){$defensewind="風屬性魔防：".$strbc['defense_wind']."<br>";}else{$defensewind="";}
		if($strbc['defense_fire']!=0){$defensefire="火屬性魔防：".$strbc['defense_fire']."<br>";}else{$defensefire="";}
		if($strbc['defense_earth']!=0){$defenseearth="地屬性魔防：".$strbc['defense_earth']."<br>";}else{$defenseearth="";}

		if($strbc['regist_stun']!=0){$registstun="昏迷抗性：".$strbc['regist_stun']."<br>";}else{$registstun="";}
		if($strbc['regist_stone']!=0){$registstone="石化抗性：".$strbc['regist_stone']."<br>";}else{$registstone="";}
		if($strbc['regist_sleep']!=0){$registsleep="睡眠抗性：".$strbc['regist_sleep']."<br>";}else{$registsleep="";}
		if($strbc['regist_freeze']!=0){$registfreeze="寒冰抗性：".$strbc['regist_freeze']."<br>";}else{$registfreeze="";}
		if($strbc['regist_sustain']!=0){$registsustain="支撐抗性：".$strbc['regist_sustain']."<br>";}else{$registsustain="";}
		if($strbc['regist_blind']!=0){$registblind="暗黑抗性：".$strbc['regist_blind']."<br>";}else{$registblind="";}

		$itemstr=$ac.$safe.$addstr.$addcon.$adddex.$addint.$addwis.$addcha.$addhp.$addmp.$addhpr.$addmpr.$addsp.$damagereduction.$weightreduction.$hitmodifier.$dmg_modifier.$bowhitmodifier.$bowdmgmodifier.$md.$defensewater.$defensewind.$defensefire.$defenseearth.$registstun.$registstone.$registsleep.$registfreeze.$registsustain.$registblind.$haste.$minlv.$maxlv.$trade.$weight.$use;
	}
?>
<form method="POST" action="buyitem_save.php" name="form<?=$b?>" onsubmit="B<?=$b?>.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
<input type="hidden" name="eid" value="<?=$eid?>">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="center"><?=$color?><?if($strac['usetime']!=0){?> (<?=$strac['usetime']?>秒)<?}?> <?=$strac['itemcount']?>個</div></td>
	</tr>
<?if($strac['itemhelp']!=NULL){?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="left"><?=$strac['itemhelp']?></div></td>
	</tr>
<?}
if($itemstr!=NULL){?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="left"><?=$itemstr?></div></td>
	</tr>
<?}?>
	<tr>
		<td width="50%" bgcolor=#303030><div align="center">剩餘<?=$strac['count']?>組</div></td>
		<td width="50%" bgcolor=#303030><div align="center">一組<?=$strac['point']?>點</div></td>
	</tr>
<?if($strac['maxbuycount']!=0){?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="center">本物品每個帳號每日限買<?=$strac['maxbuycount']?>組</div></td>
	</tr>
<?}?>
	<tr>
		<td bgcolor=#303030><div align="right">起始兌換時間：</div></td>
		<td bgcolor=#303030><div align="left"><?=$strac['starttime']?></div></td>
	</tr>
	<tr>
		<td bgcolor=#303030><div align="right">結束兌換時間：</div></td>
		<td bgcolor=#303030><div align="left"><?=$strac['stoptime']?></div></td>
	</tr>
	<tr>
		<td bgcolor=#303030><div align="center">兌換<select size="1" name="buycount" OnChange="tot('totalpoint','buycount')" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">1</option>
<?
$ibc=2;
$maxc=100;
if($strac['maxbuycount']!=0){$maxc=$strac['maxbuycount'];}
While($ibc<=$maxc && $ibc<=$strac['count']){
?>
<option value="<?=$ibc?>"><?=$ibc?></option>
<?
$ibc++;
}
?>
</select>組 
共<input id="totalpoint" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" readonly=readonly value="<?=$strac['point']?>">點</div></td>
<td bgcolor=#303030><div align="center"><input type="submit" value="兌換" name="B<?=$b?>" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</body>

</html>