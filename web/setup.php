<?
$version="v1.0 beta";	// 程式版本

require_once("class/class.ezsql.core.php");   						// ezsql檔案，勿動
require_once("class/class.ezsql.mysql.php");						// ezsql檔案，勿動
require_once("class/class.ezsql.results.php");						// ezsql檔案，勿動

//------資料庫設定區------
$sql_server="localhost";	// 資料庫ip
$sql_id="資料庫帳號";		// 資料庫帳號
$sql_passwd="資料庫密碼";	// 資料庫密碼
$sql_dbname="l1jdb";		// 資料庫名稱
$d_PasswordSalt="test";		// L1J-TW 伺服器自訂密碼編碼

//------無須更動------
$db = new ezSQL_mysql($sql_id,$sql_passwd,$sql_dbname,$sql_server); // ezsql檔案，勿動
mysql_query("SET NAMES utf8");
$login_on = mysql_pconnect($sql_server, $sql_id, $sql_passwd) or trigger_error(mysql_error(),E_USER_ERROR);
mysql_query("SET NAMES utf8");

mysql_select_db($sql_dbname, $login_on);
$strsetup = sprintf("SELECT * FROM zwls_setup WHERE `type` LIKE 'itemno' ORDER BY `no` ASC");
$strsetup = mysql_query($strsetup, $login_on) or die(mysql_error());
$strsetup = mysql_fetch_assoc($strsetup);
$randid=$strsetup['setup'];

if (empty($_SERVER['HTTP_X_FORWARDED_FOR'])) {$ip = $_SERVER['REMOTE_ADDR'];}
else {$ip = explode(',', $_SERVER['HTTP_X_FORWARDED_FOR']);$ip = $ip[0];}

$basetime=mktime(date("H"),date("i"),date("s"),date("m"),date("d"),date("Y"));
$time=date("Y-m-d H:i:s",$basetime);

//------伺服器設定(僅供顯示用)------
$lineage_server_ip="";	// IP位址
$lineage_server_name="";	// 名稱
$lineage_server_port="2000";	// port
$lineage_server_ver="Rev2012";	// 版本
$lineage_server_exp="40.0";	// 經驗倍率
$lineage_server_money="3.0";	// 金幣倍率
$lineage_server_item="1.0";	// 掉寶倍率
$lineage_server_law="1.0";	// 善惡倍率
$lineage_server_kar="1.0";	// 友好倍率
$lineage_server_w="0";	// 武器強化成功率
$lineage_server_a="0";	// 防具強化成功率
$lineage_server_wei="2.0";	// 負重倍率
$lineage_server_petexp="0";	// 寵物經驗倍率
$lineage_server_petwei="1.0";	// 寵物負重倍率
$lineage_server_shopsell="1.0";	// 商店販賣倍率
$lineage_server_shopbuy="1.0";	// 商店收購倍率
$lineage_server_restart="無";	// 重新啟動時間
$lineage_server_closetime="無";	// 固定關機時間

//------管理員設定------
$adminid="admin";		// 管理者帳號
$adminpass="1234";		// 管理者密碼
$e_adminid=hash('md5', $adminid);
$adminpass=hash('sha256', "$d_PasswordSalt$adminpass$e_adminid");
//$adminpass=base64_encode(mhash(MHASH_SHA1,$adminpass));	// 舊的密碼編碼方式

$open_gm="1";	// 是否開放管理員管理專區(1為開放)

$zsiv="SLIV";	// 註冊序號開頭4碼
$zsit="SLIT";	// 物品序號開頭4碼
$zsep="SLEP";	// 點數序號開頭4碼

//------網頁設定------
$master_mail="123@com.tw";	// 管理員e-mail
$web_name="$lineage_server_name";	// 網頁名稱
$web_url="http://123.com.tw";	// 網站網址(後面不要/)

//------註冊設定------
$open_register=1;	// 是否開放玩家註冊系統(1為開放)
$newaccpointa="0";	// 註冊帳號贈送多少點數(免費註冊碼)
$newaccpointb="0";	// 註冊帳號贈送多少點數
$codepoint="100";	// 註冊碼所需點數

//------其他服務設定------
$warehouse_moneyid="40308";	// 購買點數1點所需物品代號
$warehouse_moneyname="金幣";	// 購買點數1點所需物品名稱
$warehouse_money="100000";	// 購買點數1點所需倉庫物品數量

$open_buy="0";	// 是否開放玩家購買點數(1為開放)
$open_event="0";	// 是否開放玩家物品兌換(1為開放)
$open_rand="0";	// 是否開放玩家購買轉蛋(1為開放)
$open_bonusrand="0";	// 是否開放玩家購買回饋轉蛋(1為開放)
$open_buycode="0";	// 是否開放玩家購買註冊碼(1為開放)
$open_notebook="0";	// 是否開放玩家使用共同筆記本(1為開放)
$open_invitezone="0";	// 是否開放玩家使用分享專區(1為開放)

$open_item="1";	// 是否開放玩家二手市場(1為開放)
$itemtrademaxsell=10;	// 二手市場單人販售數量限制
$itemtrademaxallsell=300;	// 二手市場全體販售數量限制
$itemtrademinsellprice=10;	// 二手市場最低交易點數
$itemtrademaxsellprice=200000;	// 二手市場最高交易點數
$itemtradetax=10;	// 二手市場點數處理費(%)(建議10 範圍0~100請勿超過)

$open_chtr="0";	// 是否開放玩家角色交易(1為開放)
$chartrademaxsell=1;	// 角色交易單人販售數量限制
$chartrademaxallsell=10;	// 角色交易全體販售數量限制
$chartrademinsellprice=10;	// 角色交易最低交易點數
$chartrademaxsellprice=10000;	// 角色交易最高交易點數
$chartradetax=10;	// 角色交易點數處理費(%)(建議10 範圍0~100請勿超過)
$charmax=6;	// 角色最大擁有數量

$open_save="1";	// 是否開放玩家點數儲值(1為開放)
$open_saveitem="1";	// 是否開放玩家虛寶儲值(1為開放)

$open_changename="0";	// 是否開放玩家更名系統(1為開放)
$changenamepoint="10000";	// 角色更名所需點數

$open_reset="0";	// 是否開放角色重置系統(1為開放)
$charresetpoint="25000";	// 角色重置所需點數

$open_poly="0";	// 是否開放變身專區系統(1為開放)
$polypoint="10";	// 變身所需點數(1為開放)

$open_usermoney="0";	// 是否開放全體玩家領取獎勵(1為開放)
$usermoneymin="0";	// 全體玩家登入獎勵最低值
$usermoneymax="0";	// 全體玩家登入獎勵最高值

//------網站連結設置------
//$menuurl第X行第Y個=Array('連結名稱','連結位置','目標框架位置');

$menuurla1=Array('遊戲公告','../announce/','main');
$menuurla2=Array('活動公告','../announce/event.php','main');
$menuurla3=Array('檔案下載','download.php','main');
$menuurla4=Array('玩家排行','../rank/','main');
$menuurla5=Array('線上名單','../rank/online.php','main');
$menuurla6=Array('服務狀況','../modules/server_state.php','main');
$menuurla7=Array('回報專區','../onlinerepays/','main');

$menuurlb1=Array('城堡資訊','../rank/castle.php','main');
$menuurlb2=Array('怪物資訊','itemsearch.php','main');
$menuurlb3=Array('補充能量','../point/point.php?mode=0','main');
$menuurlb4=Array('無限再生','../point/point.php?mode=2','main');
$menuurlb5=Array('百寶倉庫','../point/seach.php','main');
$menuurlb6=Array('儲值專區','../point/point.php?mode=1','main');
$menuurlb7=Array('心情筆記','../notebook/','main');

$menuurlc1=Array('變身功能','../poly/','main');
$menuurlc2=Array('地圖資訊','../html/map.php','main');
$menuurlc3=Array('','','main');
$menuurlc4=Array('改名換性','../char/char.php?mode=1','main');
$menuurlc5=Array('點數重置','../char/char.php?mode=2','main');
$menuurlc6=Array('角色交易','../trade/char.php','main');
$menuurlc7=Array('二手市場','../trade/item.php','main');

$menuurld1=Array('裝備販售','../item/buyitem.php','main');
$menuurld2=Array('夢想之屋','../item/a/randitem.php','main');
$menuurld3=Array('回饋夢想','../bonus/a/randitem.php','main');
$menuurld4=Array('','','main');
$menuurld5=Array('卡點自救','../modules/resetloc.php','main');
$menuurld6=Array('修改資料','../modules/changepass.php','main');
$menuurld7=Array('推薦朋友','../invite/apply.php','main');

$menuurle1=Array('網站紀錄','../point/point_log.php','main');
$menuurle2=Array('','','main');
$menuurle3=Array('','','main');
$menuurle4=Array('','','main');
$menuurle5=Array('','','main');
$menuurle6=Array('','','main');
$menuurle7=Array('測試','(變更無效)','(變更無效)');
?>