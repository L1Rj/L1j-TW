<?
include_once("setup.php");
if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
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
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<form method="get" action="search.php" name="form1" onsubmit="B1.disabled=1">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>查詢帳號所有角色資料</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">帳號名稱：<input type="text" name="account" style="width: 150px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"> <input type="button" value="登出" onclick=window.location='logout.php' style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
</table>
<hr width="80%">
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="25%" bgcolor=#101010><div align="center"><a href="../admintool/admin_makeitem.php">建立物品序號</a></div></td>
		<td width="25%" bgcolor=#101010><div align="center"><a href="../admintool/admin_makeinvite.php">建立註冊碼序號</a></div></td>
		<td width="25%" bgcolor=#101010><div align="center"><a href="../admintool/admin_makepoint.php">建立點數序號</a></div></td>
		<td width="25%" bgcolor=#101010><div align="center"></div></td>
	</tr>
	<tr>
		<td width="25%" bgcolor=#202020><div align="center"><a href="../admintool/admin_delcard.php">清除序號</a></div></td>
		<td width="25%" bgcolor=#202020><div align="center"><a href="../admintool/admin_cardpassword.php">查詢序號</a></div></td>
		<td width="25%" bgcolor=#202020><div align="center"></div></td>
		<td width="25%" bgcolor=#202020><div align="center"></div></td>
	</tr>
</table>
<hr width="80%">
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
	<tr>
		<td width="100%" bgcolor=#101010><div align="center">
<?
	$rs=CT("SELECT `name`,`logintime` FROM `user_register` ORDER BY `serial` ASC");
	$rsc=count($rs);
	$c=0;
	if($rsc>0){
		echo "<table>";
		$count=0;
		while($c<$rsc){

			$account=$rs[$c][0];
			$lastlogintime=$rs[$c][1];
			$str="select count(*) from characters where account_name='$account'";
			$chk_char=$db->get_var($str);
			if($chk_char==0){$account2="<font color=yellow>$account</font>";}
			else{$account2="$account";}

			$str="select count(*) from accounts where login='$account' and access_level=200";
			$chk_gm=$db->get_var($str);
			if($chk_gm!=0){$account2="<font color=orange>$account</font>";}

			$str="select count(*) from accounts where login='$account' and banned=1";
			$chk_lock=$db->get_var($str);
			if($chk_lock!=0){$account2="<font color=red>$account</font>";}

			$acbasetime=mktime(date("H"),date("i"),date("s"),date("m")-1,date("d"),date("Y"));
			$actime=date("Y-m-d H:i:s",$acbasetime);
			if($lastlogintime<="$actime"){$account2="<b><font title=\"".$lastlogintime."\">".$account2."</font></b>";}

			if($count==0){echo "<tr>";}
			echo "<td width='150'>";
			echo "<a href='search.php?account=";
			echo $rs[$c][0];
			echo "'>";
			echo $account2."(".$chk_char.")";
			echo "</a>";
			echo "</td>";
			$count++;
			if($count==5){echo "</tr>";$count=0;echo "\n";}
			$c++;
		}
		echo "
</div></td></tr>
</table>
</center>
</body>
</html>";
	}else{
		echo "沒有此帳號!";
		echo "<BR>";
	}

}else{
	include_once("login.php");
}
?>