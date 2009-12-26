<?php

	// ==================================================================
	//  Author:    Justin Vincent (justin@visunet.ie)
	//	Web:       http://php.justinvincent.com
	//	Name:      EZ Results
	// 	Desc:      Class to make it fast and easy to display results on your website.
	//  Licence:   LGPL (No Restrictions)
	//	Version:   2.00

	class ez_results
	{
		
		/********************************************************
		*	BASIC SETTINGS
		*/

		var $num_results_per_page = 10;
		var $num_browse_links     = 5;
		var $hide_results         = false;
		var $set_num_results      = 0;
		var $cur_num_results      = 0;

		/********************************************************
		*	RESULT FORMATTING 
		*/

		// Alternating row color (you can have up to three alternating colors)
		var $alt_color1a				= "ffffff"; // ALTCOLOR1 in results row
		var $alt_color1b				= "dddddd";
		
		var $alt_color2a				= "ffffff"; // ALTCOLOR2 in results row
		var $alt_color2b				= "dddddd";

		var $alt_color3a				= "ffffff"; // ALTCOLOR3 in results row
		var $alt_color3b				= "dddddd";

		// Print something before main results (but after browse links)
		var $results_prepend		= "";

		// Start results (in this case as a table)
		var $results_open			= "<table style='font-family: verdana; font-size: 8pt; color: 000066;' cellpadding=5 cellspacing=1 bgcolor=CACAD2>";

		// Heading row (eg <tr><td>Heading 1</td><td>Heading 2</td></tr>
		var $results_heading		= "";

		// Single result row
		var $results_row 			= "<tr bgcolor=ALTCOLOR1><td bgcolor=ALTCOLOR2>COL1</td><td>COL2</td><td>COL3</td></tr>";

		// Close the results (in this case table)
		var $results_close			= "</table>";

		// Print something after main results (but before lower browse links)
		var $results_postpend		= "";

		// Message to display if there are no results
		var $results_empty			= "No Results";

		/********************************************************
		*	NAVIGATION FORMATTING
		*/

		// navigation at top of results?
		var $nav_top			= true;		

		// navigation at bottom of results?
		var $nav_bottom			= true;		

		// Space between navigation links and results
		var $height_below_top_nav		= 7;
		var $height_above_bottom_nav	= 2;

		// Special variables that offer access to all dynamic numbers (before << 1 2 3 4 5 >> )
		var $show_mixed_nav_left = false;
		var $mixed_nav_left = "Browsing CUR_START-CUR_END of TOTAL_RESULTS results (page CUR_PAGE of NUM_PAGES pages)";
		
		// Special variable that offer access to all dynamic numbers (<< 1 2 3 4 5 >> after)
		var $show_mixed_nav_right = false;
		var $mixed_nav_right = "Browsing CUR_START-CUR_END of TOTAL_RESULTS results (page CUR_PAGE of NUM_PAGES pages)";

		// Special variable that offer access to all dynamic numbers (<< 1 2 3 4 5 >> after)
		var $show_mixed_nav_middle = false;
		var $mixed_nav_middle = "Browsing CUR_START-CUR_END of TOTAL_RESULTS results (page CUR_PAGE of NUM_PAGES pages)";

		// Total Records: 20 ..
		var $show_count			= true;
		var $text_count			= "NUMBER Results";
		var $style_count		= 'font-family: verdana; color: 000066; font-size: 8pt; font-weight: bold;';
		var $class_count		= '';

		// .. Next 20 >> (NOTE: _na_ stands for not active)
		var $text_next			= "Next NUMBER &gt;&gt;";
		var $text_na_next		= "Next NUMBER &gt;&gt;";
		var $style_next			= 'font-family: verdana; color: 990000; font-size: 8pt; text-decoration: none;';
		var $class_next			= '';
		var $style_na_next		= 'font-family: verdana; color: bbbbbb; font-size: 8pt; text-decoration: none;';
		var $class_na_next		= '';
		
		// << 20 Back .. (NOTE: _na_ stands for not active)
		var $text_back			= "&lt;&lt; NUMBER Back";
		var $text_na_back		= "&lt;&lt; NUMBER Back";
		var $style_back			= 'font-family: verdana; color: 990000; font-size: 8pt; text-decoration: none;';
		var $class_back			= '';
		var $style_na_back		= 'font-family: verdana; color: bbbbbb; font-size: 8pt; text-decoration: none;';
		var $class_na_back		= '';

		// Turn on/off main nav links
		var $show_links         = true;

		// .. 1 ..
		var $style_nolink		= 'font-family: verdana; color: 990000; font-size: 8pt; font-weight: bold;';
		var $class_nolink		= '';
		
		// .. 2 3 4 5 ..
		var $style_link			= 'font-family: verdana; color: 000066; font-size: 8pt;';
		var $class_link			= '';
	
		//  .. (6 Pages) ..
		var $show_num_pages 	= true;
		var $text_num_pages 	= "| NUMBER PAGES |";
		var $style_num_pages	= 'font-family: verdana; color: 555555; font-size: 7pt;';
		var $class_num_pages	= '';
		
		//  ..Pages) - ..
		var $show_sep1			= true;
		var $text_sep1			= "-";
		var $style_sep1			= '';
		var $class_sep1			= '';

		// .. - Next 20 >>
		var $show_sep2 			= true;
		var $text_sep2 			= "-";
		var $style_sep2			= '';
		var $class_sep2			= '';
		
		// .. Records: 20 - [start] .. (NOTE: _na_ stands for not active)
		var $show_start_page 	= true;	
		var $text_start_page 	= "[<u>start</u>]";
		var $text_na_start_page = "[<u>start</u>]";
		var $style_start_page	= 'font-family: verdana; color: 555555; font-size: 7pt; text-decoration: none;';
		var $class_start_page	= '';
		var $style_na_start_page = 'font-family: verdana; color: bbbbbb; font-size: 7pt; text-decoration: none;';
		var $class_na_start_page = '';

		// .. [last of 6 pages] .. (NOTE: _na_ stands for not active)
		var $show_last_page 	= true;
		var $text_last_page 	= "[<u>last of NUMBER pages</u>]";
		var $text_na_last_page 	= "[<u>last of NUMBER pages</u>]";
		var $style_last_page	= 'font-family: verdana; color: 555555; font-size: 7pt; text-decoration: none;';
		var $class_last_page	= '';
		var $style_na_last_page = 'font-family: verdana; color: bbbbbb; font-size: 7pt; text-decoration: none;';
		var $class_na_last_page = '';

		// Message(s) that are displayed when user hovers mouse over nav links
		var $text_hover_msg_link     = 'Goto page NUMBER of results..';
		var $text_hover_msg_next     = 'Goto next NUMBER results..';
		var $text_hover_msg_back     = 'Goto previous NUMBER results..';
		var $text_hover_msg_start    = 'Goto start of results..';
		var $text_hover_msg_end      = 'Goto end of results..';
		
		/********************************************************
		*	$ez_results->ez_results
		*
		*	Constructor. Allows users to use ez_sql object other than the std $db->
		*/
		
		function ez_results( $ez_sql_object = 'db')
		{
			$this->ez_sql_object = $ez_sql_object ;
			
			// Stop annoying warnign message that comes up in new versions of PHP
			ini_set('allow_call_time_pass_reference', true);
		}
		
		/********************************************************
		*	$ez_results->query_mysql
		*
		*	Perform results query (mysql & ezSQL) can use normal queries
		*
		*	$query = 'SELECT user, name, password FROM users'
		*/

		function query_mysql($query)
		{
			global ${$this->ez_sql_object};
			
			// Make sure query is not on multiple lines
			$query = str_replace("\n", '', $query);
			
			// make sure that start row is set to zero if first call
			$this->init_start_row();
			
			// get total number of results
			$this->get_num_results($query);

			// Do query
			$this->results = $db->get_results($query . " LIMIT {$_REQUEST['BRSR']},$this->num_results_per_page",ARRAY_N);

			$this->cur_num_results = count($this->results);

		}

		/********************************************************
		*	$ez_results->query_oracle
		*
		*	Perform an an oracle query.
		*	Needs to be split up due to nature of oracle.
		*
		*	This only works for single table queries
		*
		*	$table       = 'TABLE_NAME'
		*	$field_list  = 'ID, USER, PASSWORD' (must not be *)
		*	$where       = 'ID > 50 AND ID < 60'
		*	$order_by    = 'USER DESC'
		*/

		function query_oracle($table,$field_list,$where=false,$order_by=false)
		{
			global ${$this->ez_sql_object};

			// Make sure that start row is set to zero if first call
			$this->init_start_row();

			// Count total number of results
			$this->get_num_results("SELECT count(*) FROM $table ".($where?"WHERE $where ":null));

			// Do query
			$this->results = $db->get_results("SELECT A.* 
				FROM (SELECT B.*,ROWNUM ROW_B
					  FROM (SELECT $field_list
						  	FROM $table ".($where?"WHERE $where ":null)." ".($order_by?"ORDER BY $order_by":null).") B
					  ) A
			    WHERE A.ROW_B >= ".$_REQUEST['BRSR']." AND A.ROW_B <= ".($_REQUEST['BRSR']+$this->num_results_per_page)
				,ARRAY_N);

			// Keep track of current number of results
			$this->cur_num_results = count($this->results);
		}

		/********************************************************
		*	$ez_results->get
		*
		*	Main function to get and format the results.
		*	This function returns results rather than prints them to screen.
		*/

		function get()
		{
			$out = '';
			
			// If we are hiding results then we only want to build navigation
			// so do it and then exit the function
			if ( $this->hide_results )
			{
				return $this->build_navigation();
			}
			
			// If not hiding results and there are some results..
			if ( isset($this->results) && is_array($this->results) )
			{

				// Initialise this every time
				$results_row = $this->results_row;

				// Replace COL1, COL2 etc with \$C1, \$C2 etc (now counting backwards)
				for ( $i = count($this->results[0]) ; $i > 0 ; --$i )
				{ 
					$results_row = str_replace("COL$i","{\$C$i}",$results_row);
				}


				// Replace ALTCOLOR1 with $ALTCOLOR1
				for ( $i=1; $i <= 3; $i++ )
				{ 
					$results_row = str_replace("ALTCOLOR$i","{\$ALTCOLOR$i}",$results_row);
				}

				// Create the top navigation for this results set
				if ( $this->nav_top )
				{
					// See build navigation for detail
					$out .= $this->build_navigation();
					
					// Spacer between nav and results
					if ( $this->height_below_top_nav )
					{
						$out .= "<table cellspacing=0 cellpadding=0 height=$this->height_below_top_nav><tr><td></td></tr></table>";	
					}
				}
	
				// Results Pre Pend
				$out .= $this->results_prepend;
				
				// Results open
				$out .= $this->results_open;

				// Results heading
				$out .= $this->results_heading;
	
				// Geat each result row and merge with $row
				$i=0;
				$altswitch1 = 0; 
				$altswitch2 = 0;
				$altswitch3 = 0;
				foreach($this->results as $row)
				{
					if ( $i < $this->num_results_per_page )
					{
						$func_args = '';
						
						// Make $row[0], $row[1] accessible by using $C1, $C2 etc.
						foreach ($row as $k => $v)
						{
							${'C'.($k+1)} = $v;
							
							$func_args .= "&\$C".($k+1).",";
						}

						// Give the user a chance to tweak the results with any function of their choice
						// tweak functions are registered with $ez_results->register_function('func_name');
						if ( is_array($this->tweak_functions) )
						{
							// Tweak results with each registered function
							foreach ( $this->tweak_functions as $tweak_function )
							{
								// If function C1, C2, etc exists then run it
								if ( function_exists($tweak_function) )
								{
									eval("$tweak_function(".substr($func_args,0,-1).");");
								}
							}
						}

						// Insert the alternating color ALTCOLOR1 - 3 into the results row
						for ( $c=1; $c <= 3; $c++ )
						{ 
							${"ALTCOLOR$c"} = (++${"altswitch$c"}%2==0?$this->{'alt_color'.$c.'b'}:$this->{'alt_color'.$c.'a'});
						}
						
						// Merge values
						eval("\$out .= \"" . str_replace("\"","\\\"",$results_row) . "\";");
					
					}
					
					$i++;
				}

				// Results close
				$out .= $this->results_close;
	
				// Results Post Pend
				$out .= $this->results_postpend;

				// Create the bottom navigation for this results set
				if ( $this->nav_bottom )
				{
					// Spacer between nav and results
					if ( $this->height_above_bottom_nav )
					{
						$out .= "<table cellspacing=0 cellpadding=0 height=$this->height_above_bottom_nav><tr><td></td></tr></table>";	
					}
	
					// Create the navigation for this results set
					$out .= $this->build_navigation();
				}

				return $out;
			}
			else
			{
				// Just display the empty results message
				return $this->results_empty;
			}
			
		}

		/********************************************************
		*	$ez_results->get
		*
		*	Print results to screen (rather than to variable)
		*/

		function display()
		{
			echo $this->get();
		}

		/********************************************************
		*	$ez_results->set_qs_val
		*
		*	Appends values to the GET query string to be carried over
		*	during browsing - useful to change order by etc
		*/

		var $qs;
		
		function set_qs_val($name,$val)
		{
			$this->qs .= "&$name=".urlencode($val);
		}

		/********************************************************
		*	$ez_results->debug
		*
		*	Maps out this object and all values it contains
		*/

		function debug()
		{
			print "<pre>";
			print_r($this);
			print "</pre>";
		}

		/********************************************************
		*	$ez_results->register_function
		*
		*	Used to register any function that can be used to tweak
		*	results. For example:
		*
		*	$ez_results->register_function('tweak_results');
		*
		*   function tweak_results($C1,$C2,$C3)
		*   {	
		*       if ( $C1 == 'hello' )
		*       {
		*           $C2 = 'New Value';	
		*       }
		*   }
		*
		*/

		var $tweak_functions = array('tweak_results');

		function register_function($function_name)
		{
			$this->tweak_functions[] = $function_name;
		}

		/********************************************************
		*	$ez_results->merge_mixed_nav (internal)
		*
		*	Merge mixed variable nav with numbers
		*/

		function merge_mixed_nav($str,$cur_start,$cur_end,$cur_page,$total_results,$num_pages)
		{
			$str = str_replace('CUR_START',$cur_start,$str);
			$str = str_replace('CUR_END',$cur_end,$str);
			$str = str_replace('CUR_PAGE',$cur_page,$str);
			$str = str_replace('TOTAL_RESULTS',number_format($total_results),$str);
			$str = str_replace('NUM_PAGES',$num_pages,$str);
			return $str;
		}

		/********************************************************
		*	$ez_results->build_navigation (internal)
		*
		*	Main function that builds the result output.
		*	(Note: print out is returned not printed)
		*/
		
		function build_navigation()
		{

			$out = '';
			
			// This is for if we are just using the navigation part
			if ( ! isset($this->num_results) )
			{
				$this->num_results = $this->set_num_results;
			}
			
			// Calculate number of pages (of results)
			$this->num_pages = ($this->num_results - ($this->num_results % $this->num_results_per_page)) / $this->num_results_per_page;
			if ( $this->num_results % $this->num_results_per_page ) 
			{
				$this->num_pages++;
			}
			
			// Calculate which page we are browsing
			$this->cur_page = ($_REQUEST['BRSR'] - ( $_REQUEST['BRSR'] % $this->num_results_per_page )) / $this->num_results_per_page;

			// Calculate which set of $this->num_browse_links we are browsing
			$this->cur_page_set = ($this->cur_page - ( $this->cur_page % $this->num_browse_links )) / $this->num_browse_links;

			// Output mixed navigation (left)
			if ( $this->show_mixed_nav_left )
			{
				$out .= $this->merge_mixed_nav($this->mixed_nav_left,($_REQUEST['BRSR']+1),(($_REQUEST['BRSR']+$this->num_results_per_page)-($this->num_results_per_page-$this->cur_num_results)),$this->cur_page+1,$this->num_results,$this->num_pages);
			}

			// Output total num records if required
			$out .= $this->show_count ? '<span '.$this->get_style('count').'>'.$this->merge_num('count',number_format($this->num_results)).'</span> ' : '' ;

			// Output num pages if required
			$out .= $this->show_num_pages && ($this->num_pages >= $this->num_browse_links) ? '<span '.$this->get_style('num_pages').'>'.$this->merge_num('num_pages',$this->num_pages).'</span> ' : '' ;

			// Output back to start page
			if ( $this->show_start_page && $this->cur_page && ($this->num_pages >= $this->num_browse_links))
			{				
				$out .= $this->create_link(preg_replace("/\?.*/",'',$_SERVER['PHP_SELF']) .  '?BRSR=0'.$this->qs,$this->text_start_page,$this->text_hover_msg_start,$this->get_style('start_page')).' ';			
			}
			else
			{
				if ( $this->show_start_page && ($this->num_pages >= $this->num_browse_links))
				{
					$out .= '<span '.$this->get_style_na('start_page').'>'.$this->text_na_start_page.'</span> ';
				}
			}

			// Output back if not on first page
			if ( $this->cur_page && ( $this->num_pages >= $this->num_browse_links ) )
			{
				$out .= $this->create_link(preg_replace("/\?.*/",'',$_SERVER['PHP_SELF']) .  '?BRSR='. ($_REQUEST['BRSR']-$this->num_results_per_page) .$this->qs,$this->merge_num('back',$this->num_results_per_page),$this->merge_num('hover_msg_back',$this->num_results_per_page),$this->get_style('back')).' ';
			}
			else
			{
				if ( $this->num_pages >= $this->num_browse_links )
				{
					$out .= '<span '.$this->get_style_na('back').'>'.$this->merge_num('na_back',$this->num_results_per_page).'</span> ';
				}
			}

			// Output sep1 if required
			$out .= $this->show_sep1 && ($this->num_pages >= $this->num_browse_links) ? '<span '.$this->get_style('sep1').'>'.$this->text_sep1.'</span> ' : '' ;


			// Output mixed navigation (middle)
			if ( $this->show_mixed_nav_middle )
			{
				$out .= $this->merge_mixed_nav($this->mixed_nav_middle,($_REQUEST['BRSR']+1),(($_REQUEST['BRSR']+$this->num_results_per_page)-($this->num_results_per_page-$this->cur_num_results)),$this->cur_page+1,$this->num_results,$this->num_pages);
			}


			// Output nav links
			if ( $this->num_results > $this->num_results_per_page && $this->show_links )
			{
				
				for ( $i=($this->cur_page_set*$this->num_browse_links); $i < ($this->cur_page_set*$this->num_browse_links)+$this->num_browse_links; $i++ )
				{
					if ( ($i*$this->num_results_per_page) < $this->num_results )
					{
						// if current page
						if ($i==$this->cur_page)
						{
							$out .= '<span '.$this->get_style('nolink').'>'.($i+1).'</span> ';
						}
						// if a nav link
						else
						{
							$out .= $this->create_link(preg_replace("/\?.*/",'',$_SERVER['PHP_SELF']) .  '?BRSR='. ($i*$this->num_results_per_page) .$this->qs,($i+1),$this->merge_num('hover_msg_link',($i+1)),$this->get_style('link')).' ';
						}
					}		
				}

			}

			// Output sep2 if required
			if ( $this->num_pages >= $this->num_browse_links )
			{
				$out .= $this->show_sep2 ? '<span '.$this->get_style('sep2').'>'.$this->text_sep2.'</span> ' : '' ;
			}

			// Output Next (if not on last page and ther eare more pages than cur page
			if ( ($this->num_pages >= $this->num_browse_links) && (($_REQUEST['BRSR'] + $this->num_results_per_page) < $this->num_results))
			{
				$out .= $this->create_link(preg_replace("/\?.*/",'',$_SERVER['PHP_SELF']) .  '?BRSR='. ($_REQUEST['BRSR']+$this->num_results_per_page) .$this->qs,$this->merge_num('next',$this->num_results_per_page),$this->merge_num('hover_msg_next',$this->num_results_per_page),$this->get_style('next')).' ';
			}
			else
			{
				if ( $this->num_pages >= $this->num_browse_links )
				{
					$out .= '<span '.$this->get_style_na('next').'>'.$this->merge_num('na_next',$this->num_results_per_page).'</span> ';
				}
			}
			
			// Output last page
			if ( $this->show_last_page && ($this->num_pages >= $this->num_browse_links) && (($_REQUEST['BRSR'] + $this->num_results_per_page) < $this->num_results))
			{
				$out .= $this->create_link(preg_replace("/\?.*/",'',$_SERVER['PHP_SELF']) .  '?BRSR='. (($this->num_pages*$this->num_results_per_page)-$this->num_results_per_page) .$this->qs,$this->merge_num('last_page',$this->num_pages),$this->merge_num('hover_msg_end',$this->num_pages),$this->get_style('last_page')).' ';
			}
			else
			{
				if ( $this->show_last_page && ($this->num_pages >= $this->num_browse_links))
				{
					$out .= '<span '.$this->get_style_na('last_page').'>'.$this->merge_num('na_last_page',($this->num_pages)).'</span> ';
				}
			}

			// Output mixed navigation (right)
			if ( $this->show_mixed_nav_right )
			{
				$out .= $this->merge_mixed_nav($this->mixed_nav_right,($_REQUEST['BRSR']+1),(($_REQUEST['BRSR']+$this->num_results_per_page)-($this->num_results_per_page-$this->cur_num_results)),$this->cur_page+1,$this->num_results,$this->num_pages);
			}

			// Return nav as built
			return $out;

		}

		/********************************************************
		*	$ez_results->get_num_results (internal)
		*
		*	Count total results for this query
		*/

		function get_num_results($query)
		{
			global ${$this->ez_sql_object};

			if ( $this->set_num_results )
			{
				// Allow the user to set number of results using thier own query - if for 
				// some reason there is a problem with the basic reg expression 
				// which has happened with 'distinct' etc.
				$this->num_results = $this->set_num_results;
			}
			else
			{
				// Count total number of results for this query
				$this->num_results =  $db->get_var(preg_replace("/SELECT.*FROM/Ui","SELECT count(*) FROM",$query));
			}
		}

		/********************************************************
		*	$ez_results->init_start_row (internal)
		*
		*	Internal function to make sure that start row is set to zero
		*/
		
		function init_start_row()
		{

             if (isset($_POST['BRSR'])) 
                  $_REQUEST['BRSR'] = $_POST['BRSR']; 
                  
            if (isset($_GET['BRSR'])) 
                  $_REQUEST['BRSR'] = $_GET['BRSR']; 
                  
            if (isset($_COOKIE['BRSR'])) 
                  $_REQUEST['BRSR'] = $_COOKIE['BRSR'];

			// browse results start row from GET, POST, COOKIE, etc
			if ( ! isset($_REQUEST['BRSR']) || !is_numeric($_REQUEST['BRSR']) )
			{
				$_REQUEST['BRSR'] = 0;
			}
		
		}

		/********************************************************
		*	$ez_results->get_style (internal)
		*
		*	Internal function returns style='etc' or class='etc' depending
		*	on which variables are set
		*/

		function get_style($var_name)
		{
			// targets the relevent variable by using $var_name see top of 
			// this class for a list of variables
			return $this->{'class_'.$var_name} ? "class='".$this->{'class_'.$var_name}."'" : "style='".$this->{'style_'.$var_name}."'";	
		}

		/********************************************************
		*	$ez_results->get_style_na (internal)
		*
		*	Same as get style but this is used for non active text
		*/

		function get_style_na($var_name)
		{
			// targets the relevent variable by using $var_name see top of 
			// this class for a list of variables
			return $this->{'class_na_'.$var_name} ? "class='".$this->{'class_na_'.$var_name}."'" : "style='".$this->{'style_na_'.$var_name}."'";	
		}

		/********************************************************
		*	$ez_results->merge_num (internal)
		*
		*	Same as get style but used to merge the word NUMBER into a numeric value
		*/
		
		function merge_num($var_name,$number=0)
		{
			// targets the relevent variable by using $var_name see top of 
			// this class for a list of variables
			return str_replace("NUMBER",$number,$this->{'text_'.$var_name});
		}

		function create_link($url,$text,$hover,$style)
		{
			return "<a href=\"$url\" $style onMouseOut=\"window.status=''; return true;\" onMouseOver=\"window.status='".str_replace("'","\\'",$hover)."'; return true;\" title=\"".str_replace('"','\"',$hover)."\">$text</a>";
		}

	}
	
	$ezr = new ez_results();
?>