<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$err_count=0;

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$a=1;
	$b=1;
	$err_count=0;

	$pagecount=50;
	$page=$_GET[page];
	if($page==NULL){$page=1;}
	$page2=$page-1;
	$page2=$page2*$pagecount;
	$page3=$page2+1;
	$page4=$page2+$pagecount;

	$str="select count(*) from zwls_item_list";
	$chk_count=$db->get_var($str);

	$pagetotal=$chk_count/$pagecount;
	$pagetotal2=(int)$pagetotal;
	$pagetotal3=$pagetotal-$pagetotal2;
	$pagetotal=(int)$pagetotal;
	$pagetotal=$pagetotal+1;
	if($pagetotal3!=0){$pagetotal=$pagetotal+1;}

	$c=$page-1;
	$b=$b+($c*$pagecount);

	mysql_select_db($sql_dbname, $login_on);
	$straa = sprintf("SELECT * FROM zwls_item_list ORDER BY `id` ASC LIMIT ".$page2.",".$pagecount."");
	$strab = mysql_query($straa, $login_on) or die(mysql_error());
	$strac = mysql_fetch_assoc($strab);

	$str="select count(*) from zwls_item_list";
	$chk_count=$db->get_var($str);
	if($chk_count==0){
		$err_count++;
		$msg=$msg."無道具資料<br>";
	}
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
		<td width="100%" bgcolor=#303030 colspan="8"><div align="center"><font size=2 color=red>物品兌換</font></div></td>
	</tr>
	<tr>
		<td width="5%" bgcolor=#202020><div align="center" class="style7">No.</div></td>
		<td width="35%" bgcolor=#202020><div align="center" class="style7">名稱</div></td>
		<td width="10%" bgcolor=#202020><div align="center" class="style7">數量</div></td>
		<td width="10%" bgcolor=#202020><div align="center" class="style7">剩餘</div></td>
		<td width="10%" bgcolor=#202020><div align="center" class="style7">點數</div></td>
		<td width="20%" bgcolor=#202020><div align="center" class="style7">兌換時間</div></td>
		<td width="10%" bgcolor=#202020><div align="center" class="style7">購買</div></td>
	</tr>
<?
	do{
		$charge_count=$strac['charge_count'];
		$itemname=$strac['itemname'];
		if($charge_count!=0){$itemname=$itemname." (".$charge_count.")";}
		$itemstr="";
		$itemlv=$strac['itemlv'];
		if($strac['itemid']>=100000 && $strac['itemid']<200000){
			if($itemlv!=0){
				$color="<font color=yellow>+".$itemlv." ".$itemname."</font>";
			}else{
				$color="<font color=yellow>".$itemname."</font>";
			}
		}elseif($strac['itemid']>=200000 && $strac['itemid']<300000){
			if($itemlv!=0){
				$color="<font color=red>+".$itemlv." ".$itemname."</font>";
			}else{
				$color="<font color=red>".$itemname."</font>";
			}
		}else{
			if($itemlv!=0){
				$color="+".$itemlv." ".$itemname;
			}else{
				$color=$itemname;
			}
		}

		if($strac['starttime']==NULL){$starttime="即日起開始兌換";}
		else{$starttime=$strac['starttime'];}
		if($strac['stoptime']==NULL){$strac['stoptime']="無限制";}
		$s1="";
		$s2="";
		if($strac['count']==0){
			$s1="<s>";
			$s2="</s>";
		}

		if($strac['stoptime']<=$time){
			$s1="<s>";
			$s2="</s>";
		}
?>
	<tr>
		<td bgcolor=#101010><div align="center"><?=$b?></div></td>
		<td bgcolor=#101010><div align="left"><?if($strac['itemid']==0){?><?=$strac['itemname']?><?}else{?><?=$s1?><?=$color?><?=$s2?><?}?> <?if($strac['usetime']!=0){?>(<?=$strac['usetime']?>秒)<?}?></div></td>
		<td bgcolor=#101010><div align="right"><?if($strac['itemid']==0){?>-<?}else{?><?=$s1?><?=$strac['itemcount']?>個<?=$s2?><?}?></div></td>
		<td bgcolor=#101010><div align="right"><?if($strac['itemid']==0){?>-<?}else{?><?=$s1?><?=$strac['count']?>組<?=$s2?><?}?></div></td>
		<td bgcolor=#101010><div align="right"><?if($strac['itemid']==0){?>-<?}else{?><?=$s1?><?=$strac['point']?>點<?=$s2?><?}?></div></td>
		<td bgcolor=#101010><div align="center"><?if($strac['itemid']==0){?>-<?}else{?><?=$s1?><?=$starttime?><?=$s2?><?}?></div></td>
		<td bgcolor=#101010><div align="center"><?if($strac['itemid']==0){?>-<?}else{?><?=$s1?><a href="javascript://" onClick="window.open('item.php?eid=<?=$strac['id']?>','','menubar=no,status=no,scrollbars=yes,top=20,left=50,toolbar=no,width=600,height=400')">兌換</a><?=$s2?><?}?></div></td>
	</tr>
<?
		$b++;
	} while ($strac = mysql_fetch_assoc($strab));
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
<?
$aa=1;
While($aa<$pagetotal){
	$ab=$aa/10;
	$ac=(int)$ab;
	$ad=$ab-$ac;
	$ae=$aa;
	if($aa<10){$ae="0".$ae;}
	if($aa<100){$ae="0".$ae;}
	if($ad==0){
		echo "<a href=\"buyitem.php?id=".$_GET[id]."&pass=".$_GET[pass]."&page=".$aa."\">第".$ae."頁</a><br>";
	}else{
		echo "<a href=\"buyitem.php?id=".$_GET[id]."&pass=".$_GET[pass]."&page=".$aa."\">第".$ae."頁</a> ";
	}
	$aa++;
}
?>
</body>

</html>