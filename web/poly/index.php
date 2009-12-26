<?
require("../setup.php");
require("poly_list.php");
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
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);

	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	$str="select count(*) from characters where account_name='$id'";
	$chk_char=$db->get_var($str);
	if($chk_char==0){
		$err_count++;
		$msg=$msg."尚未建立人物<br>";
	}

	$a=0;
	$b=1;
	While($a<$b){
		$c=${"poly".$b};
		if($c==''){$b=$b-1;}
		else{$b++;}
		$a++;
	}
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
<?if($err_count==0){?>
<form method="post" action="poly_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>變身專區</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">本系統每次使用將依照選擇變身支付變身處理費。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">變身<font color=red>不限制等級</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">變身附送與選擇秒數相同時效的通暢氣脈術、體魄強健術、加速術、鋼鐵防護、鋼鐵士氣、衝擊士氣、名譽貨幣效果。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">使用本系統時，請將角色<font color=red>登出</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">變身角色：<select size="1" name="name" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?do{?>
<option value="<?=$strac['char_name']?>"><?=$strac['char_name']?></option>
<?}while($strac=mysql_fetch_assoc($strab));?>
</select></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">變身內容：<select size="1" name="polyid" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?
$polynum=0;
While($polynum<=$b){
	$polyname=${"poly".$polynum}[1];
	$polypoint=${"poly".$polynum}[2];
	$polysec=${"poly".$polynum}[3];

	if($polysec==0){
?>
<option value="<?=$polynum?>"><?=$polyname?></option>
<?
	}else{
?>
<option value="<?=$polynum?>"><?=$polypoint?>點-<?=$polyname?>[<?=$polysec?>秒]</option>
<?
	}
	$polynum++;
}
?>
</select></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
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
</center>
</body>
</html>