<?php
include_once("setup.php");
if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$acc_name=$_GET['account'];
	$rs1=CT("SELECT * FROM `characters` WHERE `account_name` ='".$acc_name."' ORDER BY `objid` ASC");
	$rsc1=count($rs1);
	$rs2=CT("SELECT * FROM `user_register` WHERE `name` ='".$acc_name."'"); // 抓出name符合$acc_name的資料
	$rsc2=count($rs2); // 計算符合的筆數
	$rs3=CT("SELECT * FROM `accounts` WHERE `login` ='".$acc_name."'"); // 抓出login符合$acc_name的資料
	$rsc3=count($rs3); // 計算符合的筆數
	$c2=0;
	$c3=0; // 增加accounts計數值預設為0
?>
<html>

<head>
<meta http-equiv="Content-Language" content="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
	include("../html/head.php");

	if($rs3[$c3][3]==200){$access_level="管理員";}
	else{$access_level="一般";}
	if($rs3[$c3][6]==1){
		$banned="鎖定";
		$locktext="解鎖";
	}else{
		$banned="正常";
		$locktext="鎖定";
	}
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<?
	if($rsc3!=0){
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center">
<input type='button' value='變更權限' onclick=javascript:if(confirm('是否變更此帳號權限?')){window.location.href('aclevel.php?account=<?=$acc_name?>')} style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<input type='button' value='鎖定 / 解鎖' onclick=javascript:if(confirm('是否<?=$locktext?>此帳號狀態?')){window.location.href('ban.php?account=<?=$acc_name?>')} style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<input type='button' value='刪除帳號' onclick=javascript:if(confirm('是否刪除此帳號資料?')){window.location.href('delaccount.php?account=<?=$acc_name?>')} style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<input type='button' value='加值點數' onclick=javascript:if(confirm('是否加值此帳號點數?')){window.location.href('../admintool/admin_savepoint.php?id=<?=$acc_name?>')} style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">
註冊暱稱：<?=$rs2[$c2][12]?><br>
遊戲帳號：<?=$acc_name?><br>
帳號編號：<?=$rs2[$c2][0]?><br>
帳號權限：<?=$access_level?>(<?=$rs3[$c3][3]?>)<br>
帳號狀態：<?=$banned?>(<?=$rs3[$c3][6]?>)<br>
註冊時間：<?=$rs2[$c2][5]?> (<?=$rs2[$c2][6]?>)<br>
<?
		$acbasetime=mktime(date("H"),date("i"),date("s"),date("m")-1,date("d"),date("Y"));
		$actime=date("Y-m-d H:i:s",$acbasetime);
		if($rs2[$c2][10]<="$actime"){
			$logintime="<font color=red>".$rs2[$c2][10]."</font>";
		}else{
			$logintime=$rs2[$c2][10];
		}
?>
登入時間：<?=$logintime?> (<?=$rs2[$c2][11]?>)<br>
註冊信箱：<?=$rs2[$c2][4]?><br>
帳號點數：<?=$rs2[$c2][7]?><br>
回饋點數：<?=$rs2[$c2][8]?><br>
推薦人：<?=$rs2[$c2][9]?><br>
</div></td></tr>
</table>
<?
		$c=0;
		if($rsc1>0){
?>
<hr width="80%">
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="5%" bgcolor=#303030><div align="center">ObjID</div></td>
		<td width="20%" bgcolor=#303030><div align="center">角色名稱</div></td>
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
		<td width="5%" bgcolor=#303030><div align="center">卡點</td>
		<td width="5%" bgcolor=#303030><div align="center">刪除</td>
	</tr>
<?
			While($c<$rsc1){
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][1]?></td>
		<td bgcolor=#202020><div align="center"><a href='edit.php?objid=<?=$rs1[$c][1]?>'><?=$rs1[$c][2]?></a></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][3]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][6]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][8]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][10]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][11]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][13]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][12]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][16]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][14]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][15]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs1[$c][37]?></td>
		<td bgcolor=#202020><div align="center"><input type='button' value='解除' onclick=javascript:if(confirm('是否解除此角色卡點?')){window.location.href('move.php?objid=<?=$rs1[$c][1]?>')} style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></td>
		<td bgcolor=#202020><div align="center"><?if($rs1[$c][20]!=0 || $rs1[$c][28]==0){?><input type='button' value='刪除' onclick=javascript:if(confirm('是否刪除此角色資料?')){window.location.href('delchar.php?objid=<?=$rs1[$c][1]?>&acc_name=<?=$acc_name?>')} style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?}?></td>
	</tr>
<?
				$c++;
			}
?>
</table>
<hr width="80%">
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td bgcolor=#303030 width="50%"><div align="center">最後登入IP</td>
		<td bgcolor=#303030 width="50%"><div align="center">最後登入時間</td>
	</td>
	<tr>
<?
		if($rs3[$c3][2]<="$actime"){$gamelogintime="<font color=red>".$rs3[$c3][2]."</font>";}
		else{$gamelogintime=$rs3[$c3][2];}
?>
		<td bgcolor=#202020><div align="center"><?=$rs3[$c3][4]?></td>
		<td bgcolor=#202020><div align="center"><?=$gamelogintime?></td>
	</td>
</table>
<?
		}else{
?>
<BR>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td bgcolor=#202020><div align="center">此帳號無角色。</td>
	</tr>
</table>
<BR>
<?
		}
		// 開始新增讀取角色倉庫資料	
		$rs2=CT("SELECT * FROM character_warehouse WHERE `account_name` ='".$acc_name."' ORDER BY item_id ASC");
		$rsc2=count($rs2);
		$c2=0;
		if($rsc2>0){
?>
<hr width="80%">
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="20%" bgcolor=#303030><div align="center">流水編號</div></td>
		<td width="20%" bgcolor=#303030><div align="center">道具編號</div></td>
		<td width="40%" bgcolor=#303030><div align="center">道具名稱</div></td>
		<td width="10%" bgcolor=#303030><div align="center">數量</div></td>
		<td width="10%" bgcolor=#303030><div align="center">精鍊程度</div></td>
	</tr>
<?
			while($c2<$rsc2){
?>
	<tr>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][0]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][2]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][3]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][4]?></td>
		<td bgcolor=#202020><div align="center"><?=$rs2[$c2][6]?></td>
	</tr>
<?
				$c2++;
			}
		}
	// 結束新增讀取角色倉庫

	}else{
?>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="7"><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="7"><div align="left">無此帳號</div></td>
	</tr>
<?
	}

	echo "</table>";
	echo "<input type='button' value='返回上頁' onclick=window.location='index.php' style=\"width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff\">";
	echo "</body>";
	echo "</html>";
}else{
	header("refresh: 0; url=index.php");
}
?>