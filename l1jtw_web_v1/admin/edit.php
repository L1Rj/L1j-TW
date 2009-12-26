<?php
include_once("setup.php");

if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$objid=$_GET['objid'];
	$rs1=CT("SELECT * FROM characters WHERE objid = '".$objid."' ORDER BY objid ASC");
	$rsc1=count($rs1);
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
	$c=0;
	if($rsc1>0){
?>
<form method='POST' action='reedit.php'>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="5%" bgcolor=#303030><div align="center">ObjID</div></td>
		<td width="15%" bgcolor=#303030><div align="center">角色名稱</div></td>
		<td width="15%" bgcolor=#303030><div align="center">封號名稱</td>
		<td width="5%" bgcolor=#303030><div align="center">等級</div></td>
		<td width="5%" bgcolor=#303030><div align="center">血量</div></td>
		<td width="5%" bgcolor=#303030><div align="center">魔力</div></td>
		<td width="5%" bgcolor=#303030><div align="center">防禦</div></td>
		<td width="5%" bgcolor=#303030><div align="center">力量</div></td>
		<td width="5%" bgcolor=#303030><div align="center">敏捷</div></td>
		<td width="5%" bgcolor=#303030><div align="center">體質</div></td>
		<td width="5%" bgcolor=#303030><div align="center">精神</div></td>
		<td width="5%" bgcolor=#303030><div align="center">魅力</div></td>
		<td width="5%" bgcolor=#303030><div align="center">智力</div></td>
		<td width="5%" bgcolor=#303030><div align="center">GM</div></td>
	</tr>
<?
		while($c<$rsc1){
?>
	<tr>
		<td bgcolor=#101010><div align="center">
<?=$rs1[$c][1]?>
<input type='hidden' name='char_acc' value='<?=$rs1[$c][0]?>'>
<input type='hidden' name='objid' value='<?=$rs1[$c][1]?>'>
</td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_name' style="width: 100px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][2]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_title' style="width: 100px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][27]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_level' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][3]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_maxhp' style="width: 30px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][6]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_maxmp' style="width: 30px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][8]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_ac' style="width: 30px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][10]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_str' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][11]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_dex' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][13]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_con' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][12]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_wis' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][16]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_cha' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][14]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_intel' style="width: 20px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][15]?>'></td>
		<td bgcolor=#202020><div align="center"><input type='text' name='char_isgm' style="width: 30px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" value='<?=$rs1[$c][37]?>'></td>
	</tr>
<?		
			$c++;
		}
?>
	<tr>
		<td bgcolor=#101010 colspan="14"><div align="center">
<input type='submit' value='確定修改' style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<input type='button' value='返回上頁' onclick='VBScript:History.Back' style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
</td>
	</tr>
</table>
<hr width="80%">
<?
		//讀取角色道具資料
	
		$rs2=CT("SELECT * FROM character_items WHERE char_id = '".$objid."' ORDER BY item_id ASC");
		$rsc2=count($rs2);
		$c2=0;
		if($rsc2>0){
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="20%" bgcolor=#303030><div align="center">流水編號</div></td>
		<td width="20%" bgcolor=#303030><div align="center">道具編號</div></td>
		<td width="30%" bgcolor=#303030><div align="center">道具名稱</div></td>
		<td width="10%" bgcolor=#303030><div align="center">數量</div></td>
		<td width="10%" bgcolor=#303030><div align="center">精鍊程度</div></td>
		<td width="10%" bgcolor=#303030><div align="center">是否裝備</div></td>
	</tr>
<?
			while($c2<$rsc2){
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][0]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][1]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][3]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][4]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][6]?></td>
		<td bgcolor=#202020><div align="center"><?if($rs2[$c2][5]==1){echo "★";}?></td>
	</tr>
<?
				$c2++;
			}
		}
		echo "</form>";
	}else{
	echo "查無此帳號與角色資料!<p>";
	echo "<input type='button' value='返回上頁' onclick='VBScript:History.Back'>";
	}
}else{
	header("refresh: 0; url=index.php");
}	
?>