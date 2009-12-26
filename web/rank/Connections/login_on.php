<?php
require_once('../setup.php');
# FileName="Connection_php_mysql.htm"
# Type="MYSQL"
# HTTP="true"
$hostname_login_on = $sql_server;
$database_login_on = $sql_dbname;
$username_login_on = $sql_id;
$password_login_on = $sql_passwd;
$login_on = mysql_pconnect($hostname_login_on, $username_login_on, $password_login_on) or trigger_error(mysql_error(),E_USER_ERROR); 
?>