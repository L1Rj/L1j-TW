<?
require("../setup.php");
$code=$_POST[code];
$id=$_COOKIE["linsfuserid"];
$d_pass=$_POST[pass];
$e_id=hash('md5', $id);
$e_pass=hash('sha256', "$d_PasswordSalt$d_pass$e_id");
// $e_pass=base64_encode(mhash(MHASH_SHA1,$d_pass));
$e_mail=$_POST[e_mail];
$username=$_POST[username];
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
<tr><td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td></tr>
<?
if(empty($id)||empty($d_pass)||empty($e_mail)||empty($username)){
	$err_count++;
	$msg=$msg."帳號密碼、信箱、暱稱不得為空<br>";
}

if(!ereg("^.+@.+\..+$", $e_mail)){
	$err_count++;
	$msg=$msg."信箱格式錯誤<br>";
}

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from user_register where name!='$id' and e_mail='$e_mail'";
$chk_id=$db->get_var($str);
if($chk_id!=0){
	$err_count++;
	$msg=$msg."電子信箱重複<br>";
}

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
	$err_count++;
	$msg=$msg."異常錯誤(非法執行)<br>";
}

if($err_count==0){
	$new_pass=$_POST[new_pass];
	$chk_new_pass=$_POST[new_pass_chk];
	if($new_pass==NULL){
		$new_pass=$d_pass;
		$chk_new_pass=$d_pass;
	}
	$enew_pass=hash('sha256', "$d_PasswordSalt$new_pass$e_id");
//	$enew_pass=base64_encode(mhash(MHASH_SHA1,$new_pass));

	if(strlen($new_pass)<4){
		$err_count++;
		$msg=$msg."密碼少於4位半形英數<br>";
	}

	if(strlen($new_pass)>13){
		$err_count++;
		$msg=$msg."密碼超過13位半形英數<br>";
	}

	if(!preg_match ("/^[a-z0-9]+$/i", $new_pass)){
		$err_count++;
		$msg=$msg."密碼非半形英數<br>";
	}

	if($new_pass!=$chk_new_pass){
		$err_count++;
		$msg=$msg."更改密碼與確認密碼不符<br>";
	}
}

if($err_count==0){
	$str="update accounts set password='$enew_pass' where login='$id'";
	$db->query($str);
	$str="update user_register set d_pass='$new_pass' , e_pass='$enew_pass' , e_mail='$e_mail' , username='$username' where name='$id'";
	$db->query($str);
	$msg=$msg."資料修改完成<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
<tr><td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td></tr>
</table>
</center>
</body>
</html>
