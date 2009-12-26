<?php

   /**********************************************************************
   *  Author: Justin Vincent (justin@visunet.ie)
   *  Web...: http://php.justinvincent.com
   *  Name..: ezSQL_mysql
   *  Desc..: mySQL component (part of ezSQL databse abstraction library)
   *
   */

   /**********************************************************************
   *  ezSQL error strings - mySQL
   */

   $ezsql_mysql_str = array
   (
      1 => 'Require $dbuser and $dbpassword to connect to a database server',
      2 => 'Error establishing mySQL database connection. Correct user/password? Correct hostname? Database server running?',
      3 => 'Require $dbname to select a database',
      4 => 'mySQL database connection is not active',
      5 => 'Unexpected error while trying to select database'
   );

   /**********************************************************************
   *  ezSQL Database specific class - mySQL
   */

   if ( ! function_exists ('mysql_connect') ) die('<b>Fatal Error:</b> ezSQL_mysql requires mySQL Lib to be compiled and or linked in to the PHP engine');
   if ( ! class_exists ('ezSQLcore') ) die('<b>Fatal Error:</b> ezSQL_mysql requires ezSQLcore (ez_sql_core.php) to be included/loaded before it can be used');

   class ezSQL_mysql extends ezSQLcore
   {

      /**********************************************************************
      *  Constructor - allow the user to perform a qucik connect at the
      *  same time as initialising the ezSQL_mysql class
      */

      function ezSQL_mysql($dbuser='', $dbpassword='', $dbname='', $dbhost='localhost') {
	    $this->quick_connect($dbuser, $dbpassword, $dbname, $dbhost);
      }

      /**********************************************************************
      *  Short hand way to connect to mySQL database server
      *  and select a mySQL database at the same time
      */

      function quick_connect($dbuser='', $dbpassword='', $dbname='', $dbhost='localhost') {
    	 $return_val = false;
    	 if ( ! $this->connect($dbuser, $dbpassword, $dbhost,true) ) ;
    	 else if ( ! $this->select($dbname) ) ;
    	 else $return_val = true;
    	 return $return_val;
      }

      /**********************************************************************
      *  Try to connect to mySQL database server
      */

      function connect($dbuser='', $dbpassword='', $dbhost='localhost')
      {
	 global $ezsql_mysql_str; $return_val = false;

	 // Must have a user and a password
	 if ( ! $dbuser )
	 {
	    $this->register_error($ezsql_mysql_str[1].' in '.__FILE__.' on line '.__LINE__);
	    $this->show_errors ? trigger_error($ezsql_mysql_str[1],E_USER_WARNING) : null;
	 }
	 // Try to establish the server database handle
	 else if ( ! $this->dbh = @mysql_connect($dbhost,$dbuser,$dbpassword) )
	 {
	    $this->register_error($ezsql_mysql_str[2].' in '.__FILE__.' on line '.__LINE__);
	    $this->show_errors ? trigger_error($ezsql_mysql_str[2],E_USER_WARNING) : null;
	    header("Content-type: text/html; charset=utf-8"); 
	    echo "<html>";
	    echo "<head><title>資料庫連接錯誤！</title></head>";
	    echo "<h1>資料庫連接錯誤</h1>";
	    echo "<p>對不起，我們的資料庫目前出了一些問題，請稍後再試！</p>";
	    echo "<p>如果您是本站系統管理員，請檢查您是否有正確啟動 MySQL 資料庫，或是您的資料庫設定是否正確。</p>";	  
	    echo "</html>";
	    die();
	 }
	 else
	 $return_val = true;

	 return $return_val;
      }

      /**********************************************************************
      *  Try to select a mySQL database
      */

      function select($dbname='')
      {
	 global $ezsql_mysql_str; $return_val = false;

	 // Must have a database name
	 if ( ! $dbname )
	 {
	    $this->register_error($ezsql_mysql_str[3].' in '.__FILE__.' on line '.__LINE__);
	    $this->show_errors ? trigger_error($ezsql_mysql_str[3],E_USER_WARNING) : null;
	 }

	 // Must have an active database connection
	 else if ( ! $this->dbh )
	 {
	    $this->register_error($ezsql_mysql_str[4].' in '.__FILE__.' on line '.__LINE__);
	    $this->show_errors ? trigger_error($ezsql_mysql_str[4],E_USER_WARNING) : null;
	 }

	 // Try to connect to the database
	 else if ( !@mysql_select_db($dbname,$this->dbh) )
	 {
	    // Try to get error supplied by mysql if not use our own
	    if ( !$str = @mysql_error($this->dbh))
	    $str = $ezsql_mysql_str[5];

	    $this->register_error($str.' in '.__FILE__.' on line '.__LINE__);
	    $this->show_errors ? trigger_error($str,E_USER_WARNING) : null;
	 }
	 else
	 $return_val = true;

	 return $return_val;
      }

      /**********************************************************************
      *  Format a mySQL string correctly for safe mySQL insert
      *  (no mater if magic quotes are on or not)
      */

      function escape($str)
      {
	 return mysql_escape_string(stripslashes($str));
      }

      /**********************************************************************
      *  Return mySQL specific system date syntax
      *  i.e. Oracle: SYSDATE Mysql: NOW()
      */

      function sysdate()
      {
	 return 'NOW()';
      }

      /**********************************************************************
      *  Perform mySQL query and try to detirmin result value
      */

      function query($query)
      {

	 // For reg expressions
	 $query = trim($query);

	 // Initialise return
	 $return_val = 0;

	 // Flush cached values..
	 $this->flush();

	 // Log how the function was called
	 $this->func_call = "\$db->query(\"$query\")";

	 // Keep track of the last query for debug..
	 $this->last_query = $query;

	 // Perform the query via std mysql_query function..
	 $this->result = @mysql_query($query,$this->dbh);
	 $this->num_queries++;

	 // If there is an error then take note of it..
	 if ( $str = @mysql_error($this->dbh) )
	 {
	    $this->register_error($str);
	    $this->show_errors ? trigger_error($str,E_USER_WARNING) : null;
	    return false;
	 }

	 // Query was an insert, delete, update, replace
	 if ( preg_match("/^(insert|delete|update|replace)\s+/i",$query) )
	 {
	    $this->rows_affected = @mysql_affected_rows();

	    // Take note of the insert_id
	    if ( preg_match("/^(insert|replace)\s+/i",$query) )
	    {
	       $this->insert_id = @mysql_insert_id($this->dbh);
	    }

	    // Return number fo rows affected
	    $return_val = $this->rows_affected;
	 }
	 // Query was a select
	 else
	 {

	    // Take note of column info
	    $i=0;
	    while ($i < @mysql_num_fields($this->result))
	    {
	       $this->col_info[$i] = @mysql_fetch_field($this->result);
	       $i++;
	    }

	    // Store Query Results
	    $num_rows=0;
	    while ( $row = @mysql_fetch_object($this->result) )
	    {
	       // Store relults as an objects within main array
	       $this->last_result[$num_rows] = $row;
	       $num_rows++;
	    }

	    @mysql_free_result($this->result);

	    // Log number of rows the query returned
	    $this->num_rows = $num_rows;

	    // Return number of rows selected
	    $return_val = $this->num_rows;
	 }

	 // If debug ALL queries
	 $this->trace || $this->debug_all ? $this->debug() : null ;

	 return $return_val;

      
      }

      var $infos ="";
      var $volumes ="";
      var $cat ="";
      var $post ="";
      var $images ="";
      var $subscribers ="";
      var $attaches ="";

   }

?>
