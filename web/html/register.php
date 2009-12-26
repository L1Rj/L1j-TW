<?
require("../setup.php");
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
    <tr><td bgcolor=#303030 width="100%"><div align="center"><font size=2 color=red>帳號申請說明</font></div></td></tr>
    <tr><td bgcolor=#202020 width="100%"><font>1.本站帳號採取推薦制，若無註冊碼將無法申請帳號。</font></td></tr>
    <tr><td bgcolor=#101010 width="100%"><font>2.申請帳號時需要註冊碼，所有註冊碼僅能使用一次。</font></td></tr>
    <tr><td bgcolor=#202020 width="100%"><font>3.所有遊戲帳號為即時申請即時開通。</font></td></tr>
    <tr><td bgcolor=#101010 width="100%"><font>4.本站不定期提供免費註冊碼，數量有限用完為止。</font></td></tr>
    <tr><td bgcolor=#202020 width="100%"><font>5.註冊碼與推薦帳號為相對應的，其中一個錯誤即無法申請。</font></td></tr>
    <tr><td bgcolor=#101010 width="100%"><font>6.介紹他人申請帳號時，僅需給予他人註冊碼即可。</font></td></tr>
    <tr><td bgcolor=#202020 width="100%"><font>7.嚴禁將本站遊戲帳號、遊戲道具、活動點數進行有價證券交換。</font></td></tr>
    <tr><td bgcolor=#101010 width="100%"><font>8.一人可以擁有多個帳號，但是不得使用免費註冊碼擁有多個帳號。</font></td></tr>
    <tr><td bgcolor=#202020 width="100%"><font>9.若使用免費註冊碼的方式擁有多個帳號者，將會鎖定所有帳號。</font></td></tr>
    <tr><td bgcolor=#101010 width="100%"><font>10.當您同意以上規定時，即可利用我同意以上規定來申請遊戲帳號。</font></td></tr>
</table>
<hr width="80%">
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
    <tr>
     <td bgcolor=#303030 width="50%"><div align="center"><a href="../modules/register.php?itc=<?=$_GET[itc]?>&e_mail=<?=$_GET[e_mail]?>"><font color=red>我同意以上規定</font></a></div></td>
     <td bgcolor=#202020 width="50%"><div align="center"><a href="announce.php"><font color=red>我不同意以上規定</font></a><br></div></td>
    </tr>
</table>
</body></html>
