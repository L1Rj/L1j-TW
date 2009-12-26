<?php
include_once("setup.php");
if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$acc_name = $_GET['acc_name'];
	$char_objid = $_GET['objid'];
	$upsql = "Delete from character_buddys where char_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from character_buff where char_obj_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from character_config where object_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from character_items where char_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from character_quests where char_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from character_skills where char_obj_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from character_teleport where char_id ='".$char_objid."'";
	FT($upsql);
	$upsql = "Delete from characters where objid ='".$char_objid."'";
	FT($upsql);
	echo "<body bgcolor=\"#000000\" text=\"#FFFFFF\" link=\"#FFFFFF\" vlink=\"#FFFFFF\" alink=\"#FFFFFF\">";
	echo "角色刪除完成!!<p>";
	echo "<input type='button' value='返回上頁' onclick=window.location='search.php?account=".$acc_name."'>";
}else{
	header("refresh: 0; url=index.php");
}
?>