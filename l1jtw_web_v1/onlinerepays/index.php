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
	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	$str="select count(*) from zwls_onlinerepays where account='$id'";
	$chk_rpc=$db->get_var($str);
	if($chk_rpc<=10){
		$chcmin=0;
		$chcmax=$chk_rpc;
	}else{
		$chcmin=$chk_rpc-10;
		$chcmax=$chk_rpc;
	}

	$str="select count(*) from characters where account_name='$id'";
	$chk_char=$db->get_var($str);
	if($chk_char==0){
		$err_count++;
		$msg=$msg."尚未建立人物<br>";
	}

	$str="select count(*) from zwls_onlinerepays where account='$id'";
	$chk_rps=$db->get_var($str);
	if($chk_rps==0){
		$msg=$msg."尚無回報紀錄<br>";
	}

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' ORDER BY `objid` ASC");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);

	$strba = sprintf("SELECT * FROM zwls_onlinerepays where account='$id' ORDER BY `no` ASC LIMIT $chcmin,$chcmax");
	$strbb = mysql_query($strba, $login_on) or die(mysql_error());
	$strbc = mysql_fetch_assoc($strbb);
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
<?
if($err_count==0){
?>
<form method="post" action="repays_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center"><font size=2 color=red>線上回報</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">本系統僅顯示前10次回報訊息。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">請詳細填寫問題內容、發生時間點。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">回報暱稱：<select size="1" name="name" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?
	do{
?>
<option value="<?=$strac['char_name']?>"><?=$strac['char_name']?></option>
<?
	} while ($strac = mysql_fetch_assoc($strab));
?>
</select></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="left">回報類型：<select size="1" name="type" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">網站問題</option>
<option value="2">遊戲問題</option>
<option value="3">角色問題</option>
<option value="4">道具問題</option>
<option value="5">倉庫問題</option>
<option value="6">技能問題</option>
<option value="7">血盟問題</option>
<option value="8">帳號問題</option>
<option value="9">檢舉違規</option>
<option value="10">其他</option>
</select>
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="5"><div align="left">回報內容：<textarea name="text" rows="5" style="width: 300px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></textarea></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="5"><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
	<tr>
		<td width="100%" bgcolor=#303030 colspan="5"><div align="center">個人回報紀錄</div></td>
	</tr>
<?
	if($chk_rps!=0){
?>
	<tr>
		<td width="20%" bgcolor=#303030><div align="center">NO.</div></td>
		<td width="20%" bgcolor=#303030><div align="center">回報類型</div></td>
		<td width="20%" bgcolor=#303030><div align="center">回報暱稱</div></td>
		<td width="20%" bgcolor=#303030><div align="center">回報時間</div></td>
		<td width="20%" bgcolor=#303030><div align="center">處理進度</div></td>
	</tr>
<?
		do{
		$type=$strbc['type'];
		$new="";

		if($type==1){$type="網站問題";}
		elseif($type==2){$type="遊戲問題";}
		elseif($type==3){$type="角色問題";}
		elseif($type==4){$type="道具問題";}
		elseif($type==5){$type="倉庫問題";}
		elseif($type==6){$type="技能問題";}
		elseif($type==7){$type="血盟問題";}
		elseif($type==8){$type="帳號問題";}
		elseif($type==9){$type="檢舉違規";}
		elseif($type==11){$type="系統訊息";}
		else{$type="其他";}

		if($strbc['userread']==0){
			$type="<b>".$type."</b>";
		}

		if($strbc['userread']==0){
			$new="New! ";
		}
?>
	<tr>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strbc['no']?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$new?><a href="javascript://" onClick="window.open('repays.php?no=<?=$strbc['no']?>','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=400,height=400')"><?=$type?></a></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strbc['name']?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?=$strbc['time']?></div></td>
		<td bgcolor=#202020><div align="center" class="style7"><?
		if($strbc['mastertime']==NULL){
			$repaysmsg="<font color=red>尚未處理</font>";
		}else{
			$repaysmsg="<font color=blue>處理完畢</font>";
		}
		echo $repaysmsg;
?></div></td>
	</tr>
<?
		}while($strbc = mysql_fetch_assoc($strbb));
	}else{
?>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left"><?=$msg?></div></td>
	</tr>
<?
	}
}else{
?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</center>
</body></html>
