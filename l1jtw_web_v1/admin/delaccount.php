<?php
include_once("setup.php");

if($_COOKIE['linlogin']=="ok" && $_COOKIE["linsfuserpass"]==$adminpass){
	$acc_name = $_GET['account'];
	$rs1=CT("SELECT * FROM characters WHERE account_name ='".$acc_name."' ORDER BY objid ASC");
	$rsc1=count($rs1);
	$c=0;
	if($rsc1>0){
		while($c<$rsc1){
			$char_objid=$rs1[$c][1];
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
			$upsql = "Delete from characters where objid ='".$char_objid."' and Type != 0 or ClanID = 0";
			FT($upsql);
			$upsql = "update characters set account_name = '".$_COOKIE["linsfuserid"]."' where objid ='".$char_objid."' and Type == 0 and ClanID != 0";
			FT($upsql);
			$c++;
		}
	}
	$upsql = "Delete from character_elf_warehouse where account_name ='".$acc_name."'";
	FT($upsql);
	$upsql = "Delete from character_warehouse where account_name ='".$acc_name."'";
	FT($upsql);
	$upsql = "Delete from accounts where login ='".$acc_name."'";
	FT($upsql);
	$upsql = "Delete from user_register where name ='".$acc_name."'";
	FT($upsql);

	$upsql = "Delete zwls_invite_code where name ='".$acc_name."' and used = 0";
	FT($upsql);
	$upsql = "Delete zwls_item_card where name ='".$acc_name."' and used = 0";
	FT($upsql);
	$upsql = "Delete zwls_point_card where name ='".$acc_name."' and used = 0";
	FT($upsql);

	$upsql = "update zwls_accounts_trade set whosell = '".$_COOKIE["linsfuserid"]."' where whosell = '".$acc_name."' and tradestatus = 0";
	FT($upsql);
	$upsql = "update zwls_item_trade set whosell = '".$_COOKIE["linsfuserid"]."' where whosell = '".$acc_name."' and tradestatus = 0";
	FT($upsql);
	$upsql = "update zwls_accounts_trade set whosell = '*".$acc_name."*' where whosell = '".$acc_name."' and tradestatus = 1";
	FT($upsql);
	$upsql = "update zwls_item_trade set whosell = '*".$acc_name."*' where whosell = '".$acc_name."' and tradestatus = 1";
	FT($upsql);
	$upsql = "update zwls_changename set account = '*".$acc_name."*' where account ='".$acc_name."'";
	FT($upsql);
	$upsql = "update zwls_item_get_log set account = '*".$acc_name."*' where account ='".$acc_name."'";
	FT($upsql);
	$upsql = "update zwls_notebook set account = '*".$acc_name."*' where account ='".$acc_name."'";
	FT($upsql);
	$upsql = "update zwls_onlinerepays set account = '*".$acc_name."*' where account ='".$acc_name."'";
	FT($upsql);
	$upsql = "update zwls_user_get_point set account = '*".$acc_name."*' where account ='".$acc_name."'";
	FT($upsql);

	header("refresh: 0; url=index.php");
}else{
	header("refresh: 0; url=index.php");
}
?>