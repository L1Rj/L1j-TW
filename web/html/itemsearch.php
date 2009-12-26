<?
require("../setup.php");
//===============設定===============
$host="$sql_server";      
$host_user="$sql_id";                 
$host_pw="$sql_passwd"; 
$host_db="$sql_dbname";

$settings['chance'] = 1;	//顯示掉寶率	0=關,1=開
$settings['replace'] = 1;	//搜尋文字變紅	0=關,1=開
//===============設定===============

mysql_connect($host, $host_user, $host_pw);
mysql_select_db($host_db);
mysql_query("SET NAMES 'big5'");

$str = $_POST['item_name'];
$action = $_POST['action'];
$item_id = intval($_GET['item_id']);
$npc_id = intval($_GET['npc_id']);
$item_name = stripslashes($_GET['item_name']);
$npc_name = stripslashes($_GET['npc_name']);
$submit = $_POST['B1'];
if(!$item_name&&!$npc_name&&$str&&$action)$search_str = "<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width=\"80%\">
    <tr><td bgcolor=#303030 width=\"100%\"><div align=\"center\"><font size=2 color=red>搜尋<b>$str</b>的結果如下：</font></div></td></tr>";
$action=="npc" ? $checked[1] = ' checked="checked"' : $checked[0] = ' checked="checked"';

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title></head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<form id="form1" name="form1" method="post" action="<?=$HTTP_SERVER_VARS[PHP_SELF]?>" onsubmit="B1.disabled=1">
    <tr><td bgcolor=#303030 width="100%"><div align="center"><font size=2 color=red>怪物資訊</font><br></div></td></tr>
    <tr><td width="100%" bgcolor=#202020><div align="left">關鍵字：<input type="text" name="item_name" value="<?=$str?>" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td></tr>
    <tr><td width="100%" bgcolor=#101010><div align="left"><input name="action" type="radio" value="item"<?=$checked[0]?>>搜尋物品 <input name="action" type="radio" value="npc"<?=$checked[1]?>>搜尋怪物</div></td></tr>
    <tr><td width="100%" bgcolor=#101010><div align="center"><input type="submit" name="B1" value="送出" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td></tr>
</form>
</table>
<br>
<?if($item_name!=NULL){?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
    <tr><td bgcolor=#303030 width="100%"><div align="center"><font size=2 color=red><?=$item_name?></font></div></td></tr>
<?
}
if($npc_name!=NULL){?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
    <tr><td bgcolor=#303030 width="100%"><div align="center"><font size=2 color=red><?=$npc_name?></font></div></td></tr>
<?
}
if($search_str!=NULL){?>
<?=$search_str?>
<?
}

if(!$action&&$submit)echo '<h3 class="style3 b">搜尋選項不能為空白。</h3>';
if(!$str&&!($item_id||$npc_id)&&$submit) echo "<h3 class=\"style3 b\">搜尋內容不能為空白。</h3>";

if($action=="item"&&!$item_id&&!$npc_id&&$str){
	$query = f_item_id($str);
	while ($result = mysql_fetch_assoc($query)){
		$replace_name = $result[name];
		if($settings['replace']&&$str)$replace_name = preg_replace("/$str/","<font class=\"style3 b\">$str</font>",$result[name]);
		echo <<<END
<tr><td width="100%" bgcolor=#202020><div align="left"><a href="?item_id=$result[item_id]&item_name=$result[name]">$replace_name</a></div></td></tr>
END;
	}
	echo "</table>";
}

if($action=="npc"&&!$item_id&&!$npc_id&&$str){
	$query = f_npc_id($str);
	while ($result = mysql_fetch_assoc($query)){
		$replace_name = $result[name];
		if($settings['replace']&&$str)$replace_name = preg_replace("/$str/","<font class=\"style3 b\">$str</font>",$result[name]);
		echo <<<END
<tr><td width="100%" bgcolor=#202020><div align="left"><a href="?npc_id=$result[npcid]&npc_name=$result[name]">$replace_name</a></div></td></tr>
END;
	}
	echo "</table>";
}

if($npc_id&&!$item_id){
	
	$query = f_item_name($npc_id);
	while ($result = mysql_fetch_assoc($query)){
		$chance = chance($result[chance]);
		echo <<<END
<tr><td width="100%" bgcolor=#202020><div align="left"><a href="?item_id=$result[item_id]&item_name=$result[name]">$result[name]</a> $chance</div></td></tr>
END;
	}
	echo "</table>";
}

if($item_id&&!$npc_id){
	
	$query = f_npc_name($item_id);
	while ($result = mysql_fetch_assoc($query)){
		$chance = chance($result[chance]);
		echo <<<END
<tr><td width="100%" bgcolor=#202020><div align="left"><a href="?npc_id=$result[npcid]&npc_name=$result[name]">$result[name]</a> $chance</div></td></tr>
END;
	}
	echo "</table>";
}

function f_npc_name($item_id){		//GET搜索什麼怪物會掉
	$query = 'SELECT n.npcid , n.name , d.chance
	FROM npc n
	INNER JOIN droplist d ON n.npcid = d.mobId
	WHERE d.itemId = '.$item_id;
	$result = my_query($query);
	return $result;
}

function f_item_name($mob_id){		//GET搜索怪物會掉什麼
	$query = '
	SELECT w.item_id , w.name , d.chance
	FROM droplist d
	INNER JOIN weapon w ON w.item_id = d.itemId
	WHERE d.mobId = '.$mob_id.'
	UNION ALL
	SELECT a.item_id , a.name , d.chance
	FROM droplist d
	INNER JOIN armor a ON a.item_id = d.itemId
	WHERE d.mobId = '.$mob_id.'
	UNION ALL
	SELECT e.item_id , e.name , d.chance
	FROM droplist d
	INNER JOIN etcitem e ON e.item_id = d.itemId
	WHERE d.mobId = '.$mob_id;
	$result = my_query($query);
	return $result;
}

function f_item_id($str){		//直接搜索相似名字的物品
	$query = '
	SELECT item_id , name from weapon where name LIKE \'%'.$str.'%\'
	UNION ALL
	SELECT item_id , name from armor where name LIKE \'%'.$str.'%\'
	UNION ALL
	SELECT item_id , name from etcitem where name LIKE \'%'.$str.'%\'';
	$result = my_query($query);
	return $result;
}

function f_npc_id($str){		//直接搜索相似名字的怪物
	$query = 'SELECT n.npcid , n.name 
	FROM npc n 
	WHERE n.name LIKE \'%'.$str.'%\'';
	$result = my_query($query);
	return $result;
}

function my_query($query){
	$result = mysql_query($query);
	return $result;
}

function chance($chance){
	global $settings;
	if($settings['chance']){
		$chance = ($chance/10000);
		return "<b>$chance%</b>";
	}
}

?>
<br>
<hr width="80%">
<font size="2">COPYRIGHT (C) 2008, oreing . ALL RIGHT RESERVED</font>
</center>
</body>
</html>