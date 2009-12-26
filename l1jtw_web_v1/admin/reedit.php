<?php
include_once("setup.php");
if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$char_acc = $_POST['char_acc'];
	$objid = $_POST['objid'];
	$char_name = $_POST['char_name'];
	$char_title = $_POST['char_title'];
	$char_level = $_POST['char_level'];
	$char_maxhp = $_POST['char_maxhp'];
	$char_maxmp = $_POST['char_maxmp'];
	$char_ac    = $_POST['char_ac'];
	$char_str   = $_POST['char_str'];
	$char_dex   = $_POST['char_dex'];
	$char_con   = $_POST['char_con'];
	$char_wis   = $_POST['char_wis'];
	$char_cha   = $_POST['char_cha'];
	$char_intel = $_POST['char_intel'];
	$char_isgm  = $_POST['char_isgm'];
	$upsql = "Update characters set char_name='".$char_name."', title='".$char_title."', level ='".$char_level."', maxhp ='".$char_maxhp."', maxmp ='".$char_maxmp."', ac   = '".$char_ac."', str  = '".$char_str."', dex  = '".$char_dex."', con  = '".$char_con."', wis  = '".$char_wis."', cha  = '".$char_cha."', intel= '".$char_intel."', AccessLevel= '".$char_isgm."' where objid='".$objid."'";
	$ch=FT($upsql);
	echo "<body bgcolor=\"#000000\" text=\"#FFFFFF\" link=\"#FFFFFF\" vlink=\"#FFFFFF\" alink=\"#FFFFFF\">";
	$rs1=CT("SELECT * FROM characters WHERE objid = '".$objid."' ORDER BY objid ASC");
	$rsc1=count($rs1);
	$c=0;
	if($rsc1>0){
		echo "<form method='POST' action='reedit.php'>";
		echo "<table>";
		echo "<tr><td width='100'>ObjID</td><td width='100'>角色名稱</td><td width='100'>封號名稱</td><td width='40'>等級</td><td width='50'>血量</td><td width='50'>魔力</td>";
		echo "<td width='50'>防禦</td><td width='50'>力量</td><td width='50'>敏捷</td><td width='50'>體質</td>";
		echo "<td width='50'>精神</td><td width='50'>魅力</td><td width='50'>智力</TD><td width='50'>GM</TD></tr>";
		while($c<$rsc1){
			echo "<tr>";
			echo "<td>";
			echo $rs1[$c][1];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][2];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][27];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][3];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][6];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][8];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][10];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][11];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][13];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][12];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][16];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][14];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][15];
			echo "</td>";
			echo "<td>";
			echo $rs1[$c][36];
			echo "</td>";
			echo "</tr>";
			$c++;
		}
		echo "</table>";
		echo "<hr noshade size='1'>";
		echo "<p>角色資料修改完成！<p>";
		echo "<input type='button' value='返回上頁' onclick=window.location='search.php?account=".$char_acc."'>";
	}
}else{
	header("refresh: 0; url=index.php");
}
?>