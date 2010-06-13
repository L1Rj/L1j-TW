<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$page=$_GET[page];
$err_count=0;
$msg="";

if($page==NULL){
	$page=1;
}

if($page<1){
	$page=1;
}

if($page>100){
	$page=100;
}

$page=(int)$page;

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

$leftmsg=($page*2)-2;
$rightmsg=($page*2)-1;
$leftpage=($page*2)-1;
$rightpage=($page*2);

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

	$str="select count(*) from characters where account_name='$id'";
	$chk_char=$db->get_var($str);
	if($chk_char==0){
		$err_count++;
		$msg=$msg."尚未建立人物<br>";
	}

	$str="select count(*) from zwls_notebook";
	$chk_msgs=$db->get_var($str);
	if($chk_msgs==0){
		$msg=$msg."尚無記事紀錄<br>";
	}

	$backpage=$page-1;
	$nextpage=$page+1;
	if($backpage<1){
		$backpage=1;
	}

	if($nextpage>100){
		$nextpage=100;
	}

	$maxpage=$chk_msgs/2;
	$maxpage=$maxpage+0.5;
	if($nextpage>=$maxpage){
		$nextpage=$maxpage;
	}

	$nextpage=(int)$nextpage;

	mysql_select_db($sql_dbname, $login_on);
	$straa=sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' ORDER BY `objid` ASC");
	$strab=mysql_query($straa, $login_on) or die(mysql_error());
	$strac=mysql_fetch_assoc($strab);

	$strba=sprintf("SELECT * FROM zwls_notebook ORDER BY `no` DESC LIMIT $leftmsg,2");
	$strbb=mysql_query($strba, $login_on) or die(mysql_error());
	$strbc=mysql_fetch_assoc($strbb);
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
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="center"><a href="notebook.php?id=<?=$id?>&pass=<?=$pass?>"><font size=2 color=red>共同筆記本</font></a></div></td>
	</tr>
<?
	if($chk_msgs!=0){
?>
	<tr>
<?
	$a=0;
	do{
		if($a==0){
			$anotemsg=$strbc['msg'];
			$anotemsg=str_replace(chr(13).chr(10),"<br>",$anotemsg); 
			$anotename=$strbc['name'];
			$atitle=$strbc['title'];
			$adatetime=$strbc['datetime'];

			$strca=sprintf("SELECT * FROM characters WHERE `char_name` LIKE '$anotename' ORDER BY `objid` ASC");
			$strcb=mysql_query($strca, $login_on) or die(mysql_error());
			$strcc=mysql_fetch_assoc($strcb);

			if($strcc['AccessLevel']=="200"){
				$anotename="<font color=orange>".$anotename."</font>";
			}

			if($strcc['Sex']=="0"){
				$anotename="<font color=blue>♂".$anotename;
			}else{
				$anotename="<font color=red>♀".$anotename;
			}

			$a++;
		}else{
			$bnotemsg=$strbc['msg'];
			$bnotemsg=str_replace(chr(13).chr(10),"<br>",$bnotemsg); 
			$bnotename=$strbc['name'];
			$btitle=$strbc['title'];
			$bdatetime=$strbc['datetime'];

			$strca=sprintf("SELECT * FROM characters WHERE `char_name` LIKE '$bnotename' ORDER BY `objid` ASC");
			$strcb=mysql_query($strca, $login_on) or die(mysql_error());
			$strcc=mysql_fetch_assoc($strcb);

			if($strcc['AccessLevel']=="200"){
				$bnotename="<font color=orange>".$bnotename."</font>";
			}

			if($strcc['Sex']=="0"){
			$bnotename="<font color=blue>♂".$bnotename;
			}else{
				$bnotename="<font color=red>♀".$bnotename;
			}
		}
	}while($strbc = mysql_fetch_assoc($strbb));
		if($atitle!=NULL){
?>
		<td width="50%" bgcolor=#202020><div align="center"><font color=red>§ <?=$atitle?></font></div></td>
<?
		}else{
?>
		<td width="50%" bgcolor=#202020><div align="center"><font color=red>§ 尚無記事紀錄</font></div></td>
<?
		}
		if($btitle!=NULL){
?>
		<td width="50%" bgcolor=#202020><div align="center"><font color=red>§ <?=$btitle?></font></div></td>
<?
		}else{
?>
		<td width="50%" bgcolor=#202020><div align="center"><font color=red>§ 尚無記事紀錄</font></div></td>
<?
		}
?>
	</tr>
	<tr>
<?
		if($atitle!=NULL){
?>
		<td bgcolor=#101010 height="300"><div align="left"><?=$anotemsg?></div><br>
<div align="right"><font size=2>ψ 紀錄者<?=$anotename?></font> 紀錄於<font color=green><?=$adatetime?></font> ψ</font></div></td>
<?
		}else{
?>
		<td width="50%" bgcolor=#202020 height="300"><div align="left">尚無記事紀錄</div></td>
<?
		}
		if($btitle!=NULL){
?>
		<td bgcolor=#101010><div align="left"><?=$bnotemsg?></div><br>
<div align="right"><font size=2>ψ 紀錄者<?=$bnotename?></font> 紀錄於<font color=green><?=$bdatetime?></font> ψ</font></div></td>
<?
		}else{
?>
		<td width="50%" bgcolor=#202020><div align="left">尚無記事紀錄</div></td>
<?
		}
?>
	</tr>
	<tr>
		<td bgcolor=#303030><div align="left"><a href="index.php?page=<?=$backpage?>">P.<?=$leftpage?></a></div></td>
		<td bgcolor=#303030><div align="right"><a href="index.php?page=<?=$nextpage?>">P.<?=$rightpage?></a></div></td>
	</tr>
<?
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
