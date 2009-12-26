<?
require("../setup.php");
require_once('Connections/login_on.php');

mysql_select_db($database_login_on, $login_on);
mysql_query("SET NAMES utf8");
$query_Recordset1 = "SELECT * FROM castle ORDER BY `castle_id` ASC";
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
<center>
  <table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff>
    <tr>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">城堡</div></td>
      <td bgcolor=#303030 width="10%"><div align="center" class="style7">下次攻城時間</div></td>
      <td bgcolor=#303030 width="3%"><div align="center" class="style7">稅率</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">稅收</div></td>
      <td bgcolor=#303030 width="7%"><div align="center" class="style7">城主</div></td>
      <td bgcolor=#303030 width="5%"><div align="center" class="style7">血盟名稱</div></td>
    </tr>
    <?php do { 
$query_Recordset2 = "SELECT * FROM clan_data WHERE `hascastle` LIKE '".$row_Recordset1['castle_id']."' ORDER BY `hascastle` ASC";
$Recordset2 = mysql_query($query_Recordset2, $login_on) or die(mysql_error());
$row_Recordset2 = mysql_fetch_assoc($Recordset2);
$totalRows_Recordset2 = mysql_num_rows($Recordset2);
?>
      <tr>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['name']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php echo $row_Recordset1['war_time']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php if($row_Recordset1['tax_rate'] == "50")
echo "<font color=red>".$row_Recordset1['tax_rate']."</font>";
else if($row_Recordset1['tax_rate'] >= "40" && $row_Recordset1['tax_rate'] < "50")
echo "<font color=orange>".$row_Recordset1['tax_rate']."</font>";
else if($row_Recordset1['tax_rate'] >= "30" && $row_Recordset1['tax_rate'] < "40")
echo "<font color=yellow>".$row_Recordset1['tax_rate']."</font>";
else if($row_Recordset1['tax_rate'] >= "20" && $row_Recordset1['tax_rate'] < "30")
echo "<font color=blue>".$row_Recordset1['tax_rate']."</font>";
else
echo $row_Recordset1['tax_rate']; ?>%</span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?
echo $row_Recordset1['public_money']
?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php if($row_Recordset2['leader_name'] == NULL)
echo "<s>尚無城主</s>";
else
echo $row_Recordset2['leader_name']; ?></span></div></td>
        <td bgcolor=#202020><div align="center" class="style11"><span class="style10"><?php if($row_Recordset2['leader_name'] == NULL)
echo "<s>尚無血盟</s>";
else
echo $row_Recordset2['clan_name']; ?></span></div></td>
      </tr>
      <?php } while ($row_Recordset1 = mysql_fetch_assoc($Recordset1)); ?>
  </table>
</center>
</body>
</html>
<?php
mysql_free_result($Recordset1);
?>
