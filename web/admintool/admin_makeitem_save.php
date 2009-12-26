<?
require("../setup.php");
$id=$_POST[id];
$id2=$_POST[id2];
$password=$_COOKIE["linsfuserpass"];
$err_count=0;
$msg="";
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
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
<?
if($id2=="手動輸入"){$id=$id;}
else{$id=$id2;}

$str="select count(*) from user_register where name='$id'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號不存在<br>";
}

if($password!=$adminpass){
	$err_count++;
	$msg=$msg."管理密碼錯誤<br>";
}

if($err_count==0){
	$itemid=$_POST[itemid];
	$itemlv=$_POST[itemlv];
	$itemname=$_POST[itemname];
	$itemcount=$_POST[itemcount];
	$cardc=$_POST[cardc];
	$c=0;

	While($c<$cardc){
		$ran_string="";
		$ran_chars = '1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
		for($i = 0; $i < 14; $i++){
			$ran_string .= $ran_chars[rand(0, 61)];
		}
		if($c<10){$c2="0".$c;}
		else{$c2=$c;}

		$cardpassword=$zsit.$c2.$ran_string;
		$str="select count(*) from zwls_item_card where password='$cardpassword'";
		$chk_card=$db->get_var($str);
		While($chk_card!=0){
			$ran_string="";
			$ran_chars = '1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
			for($i = 0; $i < 14; $i++){
				$ran_string .= $ran_chars[rand(0, 61)];
			}
			$cardpassword=$zsit.$c2.$ran_string;
			$str="select count(*) from zwls_item_card where password='$cardpassword'";
			$chk_card=$db->get_var($str);
		}

		$str="insert into `zwls_item_card` (`password` , `item` , `itemlv` , `itemname` , `count` , `used` , `name` , `whouse` , `time` , `time2`) values ('$cardpassword','$itemid','$itemlv','$itemname','$itemcount','0','$id',NULL,'$time',NULL)";
		$db->query($str);
		$c++;
	}
	$msg=$msg."物品建立完成<br>";
}
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</table>
<input type="submit" OnClick="history.back();" value="回上一頁" style="width: 80px; color: #ffffff; background-color: #191919; border:1 solid #ffffff">
</center>
</body>
</html>