<?
require("../../setup.php");
require("itemlist.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$code="";
	$codestr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i = 0; $i < 10; $i++){
		$code .= $codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	$a=0;
	$b=1;
	While($a<$b){
		$c=${"randitem".$b};
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
include("../../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<?if($err_count==0){?>
<form method="post" action="randitem_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>轉蛋購買</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">購買轉蛋1顆需要點數<?=$randitempoint?>點。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">轉蛋數量：<select size="1" name="randitemc" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<option value="1">1顆</option>
<option value="2">2顆</option>
<option value="3">3顆</option>
<option value="4">4顆</option>
<option value="5">5顆</option>
<option value="6">6顆</option>
<option value="7">7顆</option>
<option value="8">8顆</option>
<option value="9">9顆</option>
<option value="10">10顆</option>
</select>
</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="left">轉蛋道具清單：<br>
<?
$randitemnum=1;
While($randitemnum<=$b){
	$randitemid=${"randitem".$randitemnum}[1];
	$randitemlv=${"randitem".$randitemnum}[2];
	$randitemname=${"randitem".$randitemnum}[3];
	$randitemcount=${"randitem".$randitemnum}[4];
	$usetime=${"randitem".$randitemnum}[5];
	if($randitemid>=100000 && $randitemid<200000){
		$color="受祝福的 ";
	}elseif($randitemid>=200000 && $randitemid<300000){
		$color="受詛咒的 ";
	}else{
		$color="";
	}
	$usetimet="";

	$randitemforwardrandnum=${"randitem".($randitemnum-1)}[0];
	$randitemrandnum=${"randitem".$randitemnum}[0];
	$randitemp=(($randitemrandnum-$randitemforwardrandnum)/$maxrandnum)*100000;
	$randitemp=((int)(($randitemp)+5)-((($randitemp)+5)%10))/1000;

	if($usetime!=0){$usetimet="(".$usetime."秒)";}
	if($randitemlv==0){$itemname=$color.$randitemname.$usetimet." (".$randitemcount.") ".$randitemp."%<br>";}
	else{$itemname="+".$randitemlv." ".$color.$randitemname.$usetimet." (".$randitemcount.") ".$randitemp."%<br>";}
?>
<?=$itemname?>
<?
	$randitemnum++;
}
?>
銘謝惠顧
</div></td></tr>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</form>
</table>
</center>
</body>

</html>
