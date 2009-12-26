<?
require("../setup.php");
$no=$_POST[no];
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
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
$str="select count(*) from accounts where login='$id' and password='$e_pass' and access_level='200'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$announcename=$_POST[announcename];
	$announce=$_POST[announce];

	if($announcename==NULL){
		$err_count++;
		$msg=$msg."公告名稱未輸入<br>";
	}

	if($announce==NULL){
		$err_count++;
		$msg=$msg."公告內容未輸入<br>";
	}
}

if($err_count==0){
	$str="update zwls_announce set announcename='$announcename' , announce='$announce' where no=$no";
	$db->query($str);
	$msg=$msg."修改完成<br>";
}
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</table>
</center>
</body>
</html>