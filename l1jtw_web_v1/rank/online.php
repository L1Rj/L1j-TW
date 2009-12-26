<?
require("../setup.php");
require_once('Connections/login_on.php');

mysql_select_db($database_login_on, $login_on);
mysql_query("SET NAMES utf8");
$query_Recordset1 = "SELECT * FROM characters WHERE `OnlineStatus` LIKE '1' ORDER BY `Exp` DESC";
$Recordset1 = mysql_query($query_Recordset1, $login_on) or die(mysql_error());
$row_Recordset1 = mysql_fetch_assoc($Recordset1);
$totalRows_Recordset1 = mysql_num_rows($Recordset1);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
  <table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff>
    <tr>
      <td bgcolor=#303030 width="18%"><div align="center" class="style7">名稱</div></td>
      <td bgcolor=#303030 width="13%"><div align="center" class="style7">職業</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">LV</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">HP</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">MP</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">STR</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">DEX</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">CON</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">WIS</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">CHA</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">INT</div></td>
      <td bgcolor=#303030 width="15%"><div align="center" class="style7">血盟</div></td>
      <td bgcolor=#303030 width="15%"><div align="center" class="style7">正義值</div></td>
    </tr>
    <?php do { ?>
      <tr>
	<td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php if($row_Recordset1['OnlineStatus'] == 1)
echo "<b>";?>
<?php if($row_Recordset1['AccessLevel'] == 200)
//echo "<font color=orange>".$row_Recordset1['char_name']."</font>";
echo $row_Recordset1['char_name'];
else
echo $row_Recordset1['char_name']; ?>
<?php if($row_Recordset1['OnlineStatus'] == 1)
echo "</b>";?>
</span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php if($row_Recordset1['Type'] == "0") echo "王族";
if($row_Recordset1['Type'] == "1") echo "騎士";
elseif($row_Recordset1['Type'] == "2") echo "妖精";
elseif($row_Recordset1['Type'] == "3") echo "法師";
elseif($row_Recordset1['Type'] == "4") echo "黑暗妖精";
elseif($row_Recordset1['Type'] == "5") echo "龍騎士";
elseif($row_Recordset1['Type'] == "6") echo "幻術師";
?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['level']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['MaxHp']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['MaxMp']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Str']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Dex']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Con']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Wis']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Cha']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Intel']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Clanname']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['Lawful']; ?></span></div></td>
      <?php } while ($row_Recordset1 = mysql_fetch_assoc($Recordset1)); ?>
  </table>
<hr width="80%">
<font size="2">Copyleft (C) 2009.</font>
</center>
</body>
</html>
<?php
mysql_free_result($Recordset1);
?>
