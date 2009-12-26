<?
require("../setup.php");
$code=$_POST[code];
$id=$_COOKIE["linsfuserid"];
$d_pass=$_COOKIE["linsfuserpass"];
$e_pass=base64_encode(mhash(MHASH_SHA1,$d_pass));
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
if(empty($id)||empty($d_pass)){
$err_count++;
$msg=$msg."帳號密碼不得為空<br>";
}

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
$err_count++;
$msg=$msg."帳號密碼錯誤<br>";
}

$str="select count(*) from accounts where login='$id' and banned='1'"; 
$chk_lock=$db->get_var($str);
if($chk_lock==1){
$err_count++;
$msg=$msg."帳號鎖定中<br>";
}

$str="select count(*) from zwls_freechar where account='$id'"; 
$chk_fcc=$db->get_var($str);
if($chk_fcc!=0){
$err_count++;
$msg=$msg."您不具有申請資格<br>";
}

if($ckmail==1){
$str="select count(*) from user_register where name='$id' and locked='1'"; 
$chk_lock=$db->get_var($str);
if($chk_lock==1){
$err_count++;
$msg=$msg."信箱尚未通過驗證<br>";
$msg=$msg."如未收到驗證信請<a href=\"../mail/resend.php\">點選我</a><br>";
}
}

$str="select count(*) from zwls_code where code='$code' and account='$id'";
$chk_code=$db->get_var($str);
if($chk_code==0){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($err_count==0){
$charname = $_POST[charname];
$lv = $_POST[lv];
$type = $_POST[type];
$sex = $_POST[sex];
$sta = $_POST[sta];
$dex = $_POST[dex];
$con = $_POST[con];
$wis = $_POST[wis];
$cha = $_POST[cha];
$int = $_POST[int];
$tatol=$sta+$dex+$con+$wis+$cha+$int;
$b=1;
$c=1;

$charpoint=0;

if($open_chcr!="1"){
$err_count++;
$msg=$msg."本系統尚未開放<br>";
}

if($lv!=48){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($type<0||$type>5){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($sex<0||$sex>1){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($sta<7||$sta>25){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($dex<7||$dex>25){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($con<7||$con>25){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($wis<7||$wis>25){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($cha<7||$cha>25){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($int<7||$int>25){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if($tatol!=80){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

if(strlen($charname)>12){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

$charname=ucfirst($charname);

if(!preg_match ("/^[a-z0-9]+$/i", $charname)){
$err_count++;
$msg=$msg."異常錯誤<br>";
}

$str="select count(*) from characters where char_name='$charname'";
$chk_count=$db->get_var($str);
if($chk_count!=0){
$err_count++;
$msg=$msg."暱稱重複<br>";
}

$str="select count(*) from characters where account_name='$id'";
$chk_count=$db->get_var($str);
if($chk_count>=4){
$err_count++;
$msg=$msg."最多只能擁有4隻人物<br>";
}
}

if($err_count==0){
if($con<=15){$cona=15;}
else{$cona=$con;}
if($wis<=15){$wisa=15;}
else{$wisa=$wis;}
$bonushp=$setbonushp+$cona;
$bonusmp=$setbonusmp;

if($type==0&&$type==2&&$type==4){if($wis<=11){$wisa=11;}else{$wisa=$wis;}}
else if($type==1){if($wis<=9){$wisa=9;}else{$wisa=$wis;}}
else if($type==3){if($wis<=8){$wisa=8;}else{$wisa=$wis;}}

if($type==0 && $sex==0){$class=0;$hp=14;$mp=2;}
else if($type==1 && $sex==0){$class=61;$hp=16;$mp=1;}
else if($type==2 && $sex==0){$class=138;$hp=15;$mp=4;}
else if($type==3 && $sex==0){$class=734;$hp=12;$mp=8;}
else if($type==4 && $sex==0){$class=2786;$hp=12;$mp=3;}
else if($type==0 && $sex==1){$class=1;$hp=14;$mp=2;}
else if($type==1 && $sex==1){$class=48;$hp=16;$mp=1;}
else if($type==2 && $sex==1){$class=37;$hp=15;$mp=4;}
else if($type==3 && $sex==1){$class=1186;$hp=12;$mp=8;}
else if($type==4 && $sex==1){$class=2796;$hp=12;$mp=3;}

While($b<$lv){
if($type==0){
$randhp=rand($cona-10,$bonushp-5);
if($wis<=11){$randmp=rand(2,$bonusmp+3);}
else if($wis>=12&&$wis<=14){$randmp=rand(2,$bonusmp+4);}
else if($wis>=15&&$wis<=17){$randmp=rand(3,$bonusmp+5);}
else if($wis>=18&&$wis<=20){$randmp=rand(3,$bonusmp+6);}
else if($wis>=21&&$wis<=23){$randmp=rand(4,$bonusmp+7);}
else if($wis>=24){$randmp=rand($wis-20,$bonusmp+8);}
$hp=$hp+$randhp;
$mp=$mp+$randmp;
}
else if($type==1){
$randhp=rand($cona-9,$bonushp-3);
if($wis<=9){$randmp=rand(0,$bonusmp+2);}
else if($wis>=10&&$wis<=14){$randmp=rand(1,$bonusmp+2);}
else if($wis>=15&&$wis<=17){$randmp=rand(2,$bonusmp+3);}
else if($wis>=18&&$wis<=20){$randmp=rand(2,$bonusmp+4);}
else if($wis>=21&&$wis<=23){$randmp=rand(2,$bonusmp+4);}
else if($wis>=24){$randmp=rand($wis-22,$bonusmp+5);}
$hp=$hp+$randhp;
$mp=$mp+$randmp;
}
else if($type==2||$type==4){
$randhp=rand($cona-10,$bonushp-5);
if($wis<=11){$randmp=rand(3,$bonusmp+4);}
else if($wis>=12&&$wis<=14){$randmp=rand(3,$bonusmp+6);}
else if($wis>=15&&$wis<=17){$randmp=rand(4,$bonusmp+7);}
else if($wis>=18&&$wis<=20){$randmp=rand(4,$bonusmp+9);}
else if($wis>=21&&$wis<=23){$randmp=rand(6,$bonusmp+10);}
else if($wis>=24){$randmp=rand($wis-18,$bonusmp+12);}
$hp=$hp+$randhp;
$mp=$mp+$randmp;
}
else if($type==3){
$randhp=rand($cona-12,$bonushp-9);
if($wis<=8){$ranmp=rand(2,4);}
else if($wis==9){$ranmp=rand(2,$bonusmp+6);}
else if($wis>=10&&$wis<=11){$randmp=rand(4,$bonusmp+6);}
else if($wis>=12&&$wis<=14){$randmp=rand(4,$bonusmp+8);}
else if($wis>=15&&$wis<=17){$randmp=rand(6,$bonusmp+10);}
else if($wis>=18&&$wis<=20){$randmp=rand(6,$bonusmp+12);}
else if($wis>=21&&$wis<=23){$randmp=rand(8,$bonusmp+14);}
else if($wis==24){$randmp=rand(8,$bonusmp+16);}
else if($wis==25){$randmp=rand(10,$bonusmp+16);}
$hp=$hp+$randhp;
$mp=$mp+$randmp;
}
$b++;
}

if($type==0){
$lvb=$lv/5;
$hitup=(int)$lvb;
$dmgup=0;
}
else if($type==1){
$lvb=$lv/3;
$lvc=$lv/10;
$hitup=(int)$lvb;
$dmgup=(int)$lvc;
}
else if($type==2){
$lvb=$lv/5;
$lvc=$lv/10;
$hitup=(int)$lvb;
$dmgup=(int)$lvc;
}
else if($type==3){
$hitup=0;
$dmgup=0;
}
else if($type==4){
$lvb=$lv/3;
$hitup=(int)$lvb;
$dmgup=0;
}

if($lv==48){$exp=9834000;}
else if($lv==52){$exp=127942000;}
else if($lv==60){$exp=416462000;}
else if($lv==65){$exp=596788000;}
else if($lv==70){$exp=777113000;}

if($dex>=7&&$dex<=9){$acc=$lv/8;}
else if($dex>=10&&$dex<=12){$acc=$lv/7;}
else if($dex>=13&&$dex<=15){$acc=$lv/6;}
else if($dex==16&&$dex==17){$acc=$lv/5;}
else if($dex>=18){$acc=$lv/4;}
$acc=(int)$acc;
$ac=10-$acc;

$str="select count(*) from characters where objid='$objid'";
$chk_ob=$db->get_var($str);
$str2="select count(*) from pets where objid='$objid'";
$chk_ob2=$db->get_var($str2);
While($chk_ob!=0 || $chk_ob2!=0){
$objid=$objid+1;
$str="select count(*) from characters where objid='$objid'";
$chk_ob=$db->get_var($str);
$str2="select count(*) from pets where objid='$objid'";
$chk_ob2=$db->get_var($str2);
}

$str="select count(*) from character_warehouse where id='$randid'";
$chk_it=$db->get_var($str);
$str2="select count(*) from character_items where id='$randid'";
$chk_it2=$db->get_var($str2);
$str3="select count(*) from clan_warehouse where id='$randid'";
$chk_it3=$db->get_var($str3);
While($chk_it!=0 || $chk_it2!=0 || $chk_it3!=0){
$randid=$randid+1;
$str="select count(*) from character_warehouse where id='$randid'";
$chk_it=$db->get_var($str);
$str2="select count(*) from character_items where id='$randid'";
$chk_it2=$db->get_var($str2);
$str3="select count(*) from clan_warehouse where id='$randid'";
$chk_it3=$db->get_var($str3);
}

$str="insert into `characters` (`account_name` , `objid` , `char_name` , `level` , `Exp` , `MaxHp` , `CurHp` , `MaxMp` , `CurMp` , `Ac` , `Str` , `Con` , `Dex` , `Cha` , `Intel` , `Wis` , `Class` , `Sex` , `Type` , `Heading` , `LocX` , `LocY` , `MapID` , `Food` , `Lawful` , `ElixirStatus`) values ('$id','$objid','$charname','$lv','$exp','$hp','$hp','$mp','$mp','$ac','$sta','$con','$dex','$cha','$int','$wis','$class','$sex','$type','5','33079','33402','4','29','32767','5')";
$db->query($str);
$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','1000','9999','0')";
$db->query($str);
$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','1001','9999','0')";
$db->query($str);
$str="insert into `character_buff` (`char_obj_id` , `skill_id` , `remaining_time` , `poly_id`) values ('$objid','67','9999','363')";
$db->query($str);
$str="insert into `character_items` (`id` , `item_id` , `char_id` , `item_name` , `count` , `is_equipped` , `enchantlvl` , `is_id` , `durability`) values ('$randid','40308','$objid','金幣','1000000','0','0','0','0')";
$db->query($str);
$str="insert into `zwls_freechar` (`account` , `objid`) values ('$id','$objid')";
$db->query($str);
$str="update user_register set event_point=event_point-$charpoint , bonus_point=bonus_point+$charpoint where name='$id'";
$db->query($str);
$str="update zwls_setup set setup=$randid where type='itemno'";
$db->query($str);
$str="update zwls_setup set setup=$objid where type='charno'";
$db->query($str);
$msg=$msg."免費人物創造完成<br>本次花費點數 $charpoint 點<br>獲得回饋點數 $charpoint 點<br>";
}
$str="Delete from zwls_code where account='$id'";
$db->query($str);
?>
<tr><td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td></tr>
</table>
</center>
</body>
</html>