<?
setcookie("linsfuserid", "", time()-3600);
setcookie("linsfuserpass", "", time()-3600);
header("Location:html/menu.php");
?>