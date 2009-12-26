<?php
require_once('../setup.php');
//測試用函數
function FT($t){
	$host='localhost';//修改此處
	$db_name='l1jdb';//修改此處
	$db_user='root';//修改此處
	$db_passwd='';//修改此處
	if(!$link_db=mysql_connect($host,$db_user,$db_passwd)){return "連線失敗";}
	if(!mysql_select_db($db_name,$link_db)){return "選擇失敗";}
	mysql_query("SET NAMES utf8");
	return mysql_query($t);
}
//測試用函數
function CT($t){
	$host='localhost';//修改此處
	$db_name='l1jdb';//修改此處
	$db_user='root';//修改此處
	$db_passwd='';//修改此處
	if(!$link_db=mysql_connect($host,$db_user,$db_passwd)){return "連線失敗";}
	if(!mysql_select_db($db_name,$link_db)){return "選擇失敗";}
	mysql_query("SET NAMES utf8");
	if($t!=""){if(!$db_result=mysql_query($t)){return "查詢失敗";}}
	$z1=mysql_num_rows($db_result);
	for($z=0;$z<$z1;++$z){$row[$z]=mysql_fetch_array($db_result);}
	return $row; 
}
$logname = $adminid;
$logpass = $adminpass;
?>