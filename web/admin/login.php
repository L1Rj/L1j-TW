<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$d_pass=$_COOKIE["linsfuserpass"];
$e_id=hash('md5', $id);
$e_pass=hash('sha256', "$d_PasswordSalt$d_pass$e_id");
//$e_pass=base64_encode(mhash(MHASH_SHA1,$d_pass)); // 舊的密碼寫法
$err_count=0;
$msg="";

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
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
<form method="post" action="check.php" name="form1" onsubmit="B1.disabled=1">
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>站長登入</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left">非站務人員請離開。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
</table>
</center>
</body>
</html>