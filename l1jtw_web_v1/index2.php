<?
require("setup.php");
?>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title><?=$web_name?></title>
</head>
<frameset rows="200,*" framespacing="0" frameborder="0">
	<frame name="logo" scrolling="NO" noresize src="html/menu.php?itc=<?=$_GET[itc]?>&e_mail=<?=$_GET[e_mail]?>">
	<frameset cols="*" frameborder="no" border="0" framespacing="0">
		<frame name="main" noresize src="html/announce.php">
	</frameset>
	<noframes>
		<body></body>
	</noframes>
</frameset>
</html>
