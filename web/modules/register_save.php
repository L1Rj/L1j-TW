<?
require("../setup.php");
$id=$_POST[id];
$d_pass=$_POST[pass];
$e_id=hash('md5', $id);
$e_pass=hash('sha256', "$d_PasswordSalt$d_pass$e_id");
//$e_pass=base64_encode(mhash(MHASH_SHA1,$d_pass));
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
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
<?
if(empty($id)||empty($d_pass)||empty($e_mail)||empty($username)){
	$err_count++;
	$msg=$msg."帳號密碼、信箱、暱稱不得為空<br>";
}

if(!ereg("^.+@.+\..+$", $e_mail)){
	$err_count++;
	$msg=$msg."信箱格式錯誤<br>";
}

if($open_register!=1){
	$err_count++;
	$msg=$msg."目前不開放註冊<br>";
}

if($err_count==0){
	$chk_pass=$_POST[pass_chk];
	$reg_code=$_POST[reg_code];

	$str="select count(*) from accounts where login='$id'";
	$chk_id=$db->get_var($str);
	if($chk_id!=0){
		$err_count++;
		$msg=$msg."帳號重複<br>";
	}
	
	if(substr_count($id, "1234")>=1 || substr_count($id, "4321")>=1){
		$err_count++;
		$msg=$msg."帳號強度過於簡單<br>";
	}

	if(substr_count($d_pass, "1234")>=1 || substr_count($d_pass, "4321")>=1){
		$err_count++;
		$msg=$msg."密碼強度過於簡單<br>";
	}

	$ckidv=0;
	$a=0;
	while($a<=10 && $ckidv==0){
		$ck_id=substr_count($id, $a);
		if($ck_id>=strlen($id)){
			$ckidv++;
			$msg=$msg."帳號強度過於簡單<br>";
		}
		$a++;
	}

	$ckpsv=0;
	$b=0;
	while($b<=10 && $ckpsv==0){
		$ck_pass=substr_count($d_pass, $b);
		if($ck_pass>=strlen($d_pass)){
			$ckpsv++;
			$msg=$msg."密碼強度過於簡單<br>";
		}
		$b++;
	}

	if($id=="$d_pass"){
		$err_count++;
		$msg=$msg."密碼請勿與帳號相同<br>";
	}

	$str="select count(*) from user_register where e_mail='$e_mail'";
	$chk_e_mail=$db->get_var($str);
	if($chk_e_mail!=0){
		$err_count++;
		$msg=$msg."信箱重複<br>";
	}

	if(strlen($id)<4){
		$err_count++;
		$msg=$msg."帳號少於4位半形英數<br>";
	}

	if(strlen($id)>12){
		$err_count++;
		$msg=$msg."帳號超過12位半形英數<br>";
	}

	if(strlen($d_pass)<4){
		$err_count++;
		$msg=$msg."密碼少於4位半形英數<br>";
	}

	if(strlen($d_pass)>13){
		$err_count++;
		$msg=$msg."密碼超過13位半形英數<br>";
	}

	if(!preg_match ("/^[a-z0-9]+$/i", $id)){
		$err_count++;
		$msg=$msg."帳號非半形英數<br>";
	}

	if(!preg_match ("/^[a-z0-9]+$/i", $d_pass)){
		$err_count++;
		$msg=$msg."密碼非半形英數<br>";
	}

	if($d_pass!=$chk_pass){
		$err_count++;
		$msg=$msg."遊戲密碼與確認密碼不符<br>";
	}

	$str="select count(*) from zwls_invite_code where invitecode='$reg_code' and used=0";
	$chk_rc=$db->get_var($str);
	if($chk_rc==0){
		$err_count++;
		$msg=$msg."註冊碼錯誤<br>";
	}
}

if($err_count==0){
	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM zwls_invite_code WHERE `invitecode` LIKE '$reg_code' ORDER BY `invitecode` DESC", $str);
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);
	$id2=$strac['name'];

	$str="insert into accounts (`login` , `password` , `lastactive` ,  `access_level` , `ip` , `host` , `banned`) values ('$id','$e_pass','$time','0','$ip','$ip','0')";
	$db->query($str);
	if($id2==free){
		$newaccpoint=$newaccpointa;
	}else{
		$newaccpoint=$newaccpointb;
	}
	$str="insert into user_register (`name` , `d_pass` , `e_pass` , `register_time` , `regip`  , `e_mail` , `event_point` , `invited` , `username`) values ('$id','$d_pass','$e_pass','$time','$ip','$e_mail','$newaccpoint','$id2','$username')";
	$db->query($str);
	$str="update zwls_invite_code set used=1 , whouse='$id' , time2='$time' where invitecode='$reg_code'";
	$db->query($str);
	$msg=$msg."帳號申請完成<br>";
}
?>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
</table>
</center>
</body>
</html>