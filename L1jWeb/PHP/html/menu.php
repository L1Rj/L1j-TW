<?
require("../setup.php");
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title></head>
<?
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE[linsfuserpass];
$err_count=0;
$error_msg=$_GET[error_msg];

if($error_msg==1){
	$msg="<font color=red>帳號密碼錯誤</font>";
}elseif($error_msg==2){
	$msg="<font color=red>驗證密碼錯誤</font>";
}

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
?>
<script language="JavaScript">
function OpenWindow()
{
	window.status = "";
	strFeatures = "";
	objNewWindow = window.open("announce.php" , "main", strFeatures);
}
</script>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF" onload="OpenWindow()">

<center>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
<form name="postForm" method="post" action="../login.php">
	<tr>
		<td bgcolor=#101010 width="50%">遊戲帳號：<input type="text" size="10" name="id" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><?=$msg?></td>
		<td bgcolor=#101010 width="50%"></td></tr>
	<tr>
		<td bgcolor=#202020 width="50%">遊戲密碼：<input type="password" size="10" name="pass" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></td>
		<td bgcolor=#202020 width="50%"></td>
	</tr>
	<tr>
		<td bgcolor=#101010 width="50%">驗證密碼：<input type="text" name="checknum" id="checknum" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"><a href="#" id="reload-img"><img src="showrandimg.php" id="rand-img" border="0" title="更換驗證密碼"></a></td>
		<td bgcolor=#101010 width="50%">登入紀錄：<input name="cookietime" type="radio" checked value="1">1日 <input name="cookietime" type="radio" value="2">7日 <input name="cookietime" type="radio" value="3">30日 <input name="cookietime" type="radio" value="4">365日</td>
	</tr>
	<tr>
		<td bgcolor=#202020 width="100%" colspan="2"><input type="submit" value="登入" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"> <a href="register.php?itc=<?=$_GET[itc]?>&e_mail=<?=$_GET[e_mail]?>" target="main">帳號申請</a></td>
	</tr>
</form>
<script language="javascript">

(function(){
	var reloadImg = function(dImg) {
		var sOldUrl = dImg.src;
		var sNewUrl = sOldUrl + "?rnd=" + Math.random();
		dImg.src = sNewUrl;
	};

	var dReloadLink = document.getElementById("reload-img");
	var dImg = document.getElementById("rand-img");
  
	dReloadLink.onclick = function(e) {
		reloadImg(dImg);
		if(e) e.preventDefault();
			return false;
	};
})();

</script>
</table>
</center>
<?
}

if($err_count==0){
	$str="select count(*) from zwls_onlinerepays where account='$id' and userread=0";
	$chk_msgc=$db->get_var($str);

	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM accounts WHERE `login` LIKE '$id' ORDER BY `login` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$id=$strac['login'];

	$str="select count(*) from accounts where login='$id' and access_level='200'"; 
	$chk_gm=$db->get_var($str);
	if($chk_gm==1){
		$status="管理員";
	}else{
		$status="一般";
	}

	$str="select count(*) from accounts where login='$id' and banned='1'"; 
	$chk_lock=$db->get_var($str);
	if($chk_lock==1){
		$status="鎖定";
	}

	$strba =sprintf("SELECT * FROM user_register WHERE `name` LIKE '$id' ORDER BY `name` DESC");
	$strbb=mysql_query($strba, $login_on) or die(mysql_error());
	$strbc=mysql_fetch_assoc($strbb);
	$username=$strbc['username'];
	$email=$strbc['e_mail'];

	$strca=sprintf("SELECT * FROM character_warehouse WHERE `account_name` LIKE '$id' AND `item_id` LIKE '$warehouse_moneyid' ORDER BY `account_name` DESC");
	$strcb=mysql_query($strca, $login_on) or die(mysql_error());
	$strcc=mysql_fetch_assoc($strcb);
	$money_count="$warehouse_moneyname (".$strcc['count'].")";

	$str="select count(*) from character_warehouse where account_name='$id' and item_id='$warehouse_moneyid'"; // 檢查倉庫金幣
	$chk_id=$db->get_var($str);
	if($chk_id==0){
		$money_count = "沒有具有 $warehouse_moneyname";
	}

	$lasttime=mktime(date("H"),date("i")-30,date("s"),date("m"),date("d"),date("Y"));
	$lasttime=date("Y-m-d H:i:s",$lasttime);

	$str="Delete from zwls_code where time<='$lasttime'";
	$db->query($str);

	if($open_usermoney==1){
		$userdatetime=mktime(date("H"),date("i"),date("s"),date("m"),date("d"),date("Y"));
		$userdate=date("Y-m-d",$userdatetime);
		$str="select count(*) from zwls_user_get_point where account='$id' and date='$userdate'";
		$chk_userm=$db->get_var($str);
		$str="select count(*) from zwls_user_get_point where ip='$ip' and date='$userdate'";
		$chk_userip=$db->get_var($str);
	}
?>
<script language="JavaScript">
function OpenWindow()
{
	window.status = "";
	strFeatures = "";
<?
	if($open_usermoney==1 && $chk_userm==0 && $chk_userip==0){
?>
	objNewWindow = window.open("../point/getpoint.php" , "main", strFeatures);
<?
	}else{
?>
	objNewWindow = window.open("announce.php" , "main", strFeatures);
<?
	}
?>
}
</script>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF" onload="OpenWindow();">

<center>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="100%">
	<tr>
		<td width="16%" bgcolor=101010><div align="left"><font color=ffffff size=3>帳號：<font color=ffffff size=2 title=<?=$status?>><?=$id?></font></font></font></div></td>
		<td width="12%" bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurla1[1]?>" target="<?=$menuurla1[2]?>"><?=$menuurla1[0]?></a></font></div></td>
		<td width="12%" bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurla2[1]?>" target="<?=$menuurla2[2]?>"><?=$menuurla2[0]?></a></font></div></td>
		<td width="12%" bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurla3[1]?>" target="<?=$menuurla3[2]?>"><?=$menuurla3[0]?></a></font></div></td>
		<td width="12%" bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurla4[1]?>" target="<?=$menuurla4[2]?>"><?=$menuurla4[0]?></a></font></div></td>
		<td width="12%" bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurla5[1]?>" target="<?=$menuurla5[2]?>"><?=$menuurla5[0]?></a></font></div></td>
		<td width="12%" bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurla6[1]?>" target="<?=$menuurla6[2]?>"><?=$menuurla6[0]?></a></font></div></td>
 		<td width="12%" bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurla7[1]?>" target="<?=$menuurla7[2]?>"><?=$menuurla7[0]?></a></font></div></td>
	</tr>
	<tr>
		<td bgcolor=202020><div align="left"><font color=ffffff size=3>點數：<font color=ff0000 size=2><?=$strbc['event_point']?></font>點</font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb1[1]?>" target="<?=$menuurlb1[2]?>"><?=$menuurlb1[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb2[1]?>" target="<?=$menuurlb2[2]?>"><?=$menuurlb2[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb3[1]?>" target="<?=$menuurlb3[2]?>"><?=$menuurlb3[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb4[1]?>" target="<?=$menuurlb4[2]?>"><?=$menuurlb4[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb5[1]?>" target="<?=$menuurlb5[2]?>"><?=$menuurlb5[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb6[1]?>" target="<?=$menuurlb6[2]?>"><?=$menuurlb6[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlb7[1]?>" target="<?=$menuurlb7[2]?>"><?=$menuurlb7[0]?></a></font></div></td>
	</tr>
	<tr>
		<td bgcolor=101010><div align="left"><font color=ffffff size=3>回饋點數：<font color=0000ff size=2><?=$strbc['bonus_point']?></font>點</font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc1[1]?>" target="<?=$menuurlc1[2]?>"><?=$menuurlc1[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc2[1]?>" target="<?=$menuurlc2[2]?>"><?=$menuurlc2[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc3[1]?>" target="<?=$menuurlc3[2]?>"><?=$menuurlc3[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc4[1]?>" target="<?=$menuurlc4[2]?>"><?=$menuurlc4[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc5[1]?>" target="<?=$menuurlc5[2]?>"><?=$menuurlc5[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc6[1]?>" target="<?=$menuurlc6[2]?>"><?=$menuurlc6[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurlc7[1]?>" target="<?=$menuurlc7[2]?>"><?=$menuurlc7[0]?></a></font></div></td>
    	</tr>
	<tr>
		<td bgcolor=202020><div align="left"><font color=ffffff size=3>倉庫<font color=00ff00 size=2><?=$money_count?></font></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurld1[1]?>" target="<?=$menuurld1[2]?>"><?=$menuurld1[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurld2[1]?>" target="<?=$menuurld2[2]?>"><?=$menuurld2[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurld3[1]?>" target="<?=$menuurld3[2]?>"><?=$menuurld3[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurld4[1]?>" target="<?=$menuurld4[2]?>"><?=$menuurld4[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurld5[1]?>" target="<?=$menuurld5[2]?>"><?=$menuurld5[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurld6[1]?>" target="<?=$menuurld6[2]?>"><?=$menuurld6[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurld7[1]?>" target="<?=$menuurld7[2]?>"><?=$menuurld7[0]?></a></font></div></td>
	</tr>
	<tr>
		<td bgcolor=101010><div align="left"><font color=ffffff size=3><a href="javascript:location.reload()">重整</a> <a href="../logout.php">登出</a><?if($id==$adminid && $e_pass==$adminpass){?> <a href="../admin/index.php" target="main">站長</a><?}?></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurle1[1]?>" target="<?=$menuurle1[2]?>"><?=$menuurle1[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurle2[1]?>" target="<?=$menuurle2[2]?>"><?=$menuurle2[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurle3[1]?>" target="<?=$menuurle3[2]?>"><?=$menuurle3[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurle4[1]?>" target="<?=$menuurle4[2]?>"><?=$menuurle4[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><a href="<?=$menuurle5[1]?>" target="<?=$menuurle5[2]?>"><?=$menuurle5[0]?></a></font></div></td>
		<td bgcolor=101010><div align="center"><font color=ffffff size=3><a href="<?=$menuurle6[1]?>" target="<?=$menuurle6[2]?>"><?=$menuurle6[0]?></a></font></div></td>
		<td bgcolor=202020><div align="center"><font color=ffffff size=3><?if($chk_gm==1){?><a href="../gm/" target="main"><?=$menuurle7[0]?></a><?}?></font></div></td>
	</tr>
</table>
</center>
<?
}
?>
</body>
</html>