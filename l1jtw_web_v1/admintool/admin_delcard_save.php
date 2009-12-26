<?
require("../setup.php");
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
if($password!=$adminpass){
	$err_count++;
	$msg=$msg."管理密碼錯誤<br>";
}

if($err_count==0){
	$delmode = $_POST[delmode];

	if($delmode==1){
		$str="Delete from zwls_invitecode where used='1'";
		$db->query($str);
	}elseif($delmode==2){
		$str="Delete from zwls_itemcard where used='1'";
		$db->query($str);
	}elseif($delmode==3){
		$str="Delete from zwls_pointcard where used='1'";
		$db->query($str);
	}
	$msg=$msg."序號清除完成<br>";
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