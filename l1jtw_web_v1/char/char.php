<?
require("../setup.php");
$id=$_COOKIE["linsfuserid"];
$e_pass=$_COOKIE["linsfuserpass"];
$mode=$_GET[mode];

if($mode==NULL || $mode<1 || $mode>2){
	$mode=1;
}

$err_count=0;
$msg="";

$str="select count(*) from user_register where name='$id' and e_pass='$e_pass'";
$chk_id=$db->get_var($str);
if($chk_id==0){
	$err_count++;
	$msg=$msg."帳號密碼錯誤<br>";
}

if($err_count==0){
	$code="";
	$codestr='ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for($i=0;$i<10;$i++){
		$code.=$codestr[rand(0, 25)];
	}

	$str="Delete from zwls_code where account='$id'";
	$db->query($str);
	$str="insert into `zwls_code` (`account` , `code` , `time` , `ip`) values ('$id','$code','$time','$ip')";
	$db->query($str);

	if($mode==1 || $mode==2){
		mysql_select_db($sql_dbname, $login_on);
		$straa=sprintf("SELECT * FROM characters WHERE `account_name` LIKE '$id' ORDER BY `objid` ASC");
		$strab=mysql_query($straa, $login_on) or die(mysql_error());
		$strac=mysql_fetch_assoc($strab);

		$str="select count(*) from characters where account_name='$id'";
		$chk_char=$db->get_var($str);
		if($chk_char==0){
			$err_count++;
			$msg=$msg."尚未建立人物<br>";
		}
	}
}
?>
<html>

<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><?=$web_name?></title>

<?if($mode==2){?>
<script language="javascript">
function val(num){
 var num=parseFloat(num);
 if(isNaN(num)) return 0;
 return num;
}

function abs(num){
 if(num<0) return -num;
 return num;
}

function DecPlaces(num){
 var str=""+num;
 str=str.substr(str.indexOf(".")+1);
 return str.length;
}

function Fix(num,numOfDec){
 if(!numOfDec) numOfDec=0;
 
 num=num*Math.pow(10,numOfDec);
 num=Math.round(num);
 num=num/Math.pow(10,numOfDec);
 return num;
}

String.prototype.chkIsContain=function(){
 var arg=arguments;
 for(var i=0;i<arg.length;i++){
  if(this.indexOf(arg[i])!=-1) return true;
 }
 return false;
}

String.prototype.chkIsMatch=function(){
 var arg=arguments;
 for(var i=0;i<arg.length;i++){
  if(this==arg[i]) return true;
 }
 return false;
}

function chkIsExist(name){
 for(var i=0;i<document.forms.length;i++){
  for(var j=0;j<document.forms[i].elements.length;j++){
   if(document.forms[i].elements[j].name==name) return true;
   if(document.forms[i].elements[j].id==name) return true;
  }
 }
 return false;
}

var UpDownObj=new Object();
UpDownObj.length=0;

UpDownObj.checkUpDown=function(obj){
 obj.value=Fix(val(obj.value),DecPlaces(val(obj.inc) ) );
 if(val(obj.value)>val(obj.max)) obj.value=obj.max;
 if(val(obj.value)<val(obj.min)) obj.value=obj.min;
}

UpDownObj.plusUpDown=function(obj,chkpoint){
 if(!obj.disabled && !chkpoint.disabled){
  obj.value1=obj.value;
  obj.value-=-val(obj.inc);
  UpDownObj.checkUpDown(obj);
  obj.value2=obj.value;
  if(chkpoint.value==0) obj.value=obj.value1;
  if(obj.value1!=obj.value2){
    chkpoint.value-=val(chkpoint.inc);
    UpDownObj.checkUpDown(chkpoint);
  }
 }
}

UpDownObj.substUpDown=function(obj,chkpoint){
 if(!obj.disabled && !chkpoint.disabled){
  obj.value1=obj.value;
  obj.value-=val(obj.inc);
  UpDownObj.checkUpDown(obj);
  obj.value2=obj.value;
  if(obj.value1!=obj.value2){
    chkpoint.value-=-val(chkpoint.inc);
    UpDownObj.checkUpDown(chkpoint);
  }
 }
}

UpDownObj.checkKeyIsNum=function(obj,e){
 if(document.all){
  var e=window.event;
  var k=e.keyCode;
 }else{
  var k=e.which;
 }
 
 function isthiskey(keyCode){
  var arg=arguments;
  for(var i=1;i<arg.length;i++){
   if(keyCode==arg[i]) return true;
  }
 }

 if(k==13){
  UpDownObj.checkUpDown(obj);
  return true;
 }
 if(k==38) UpDownObj.plusUpDown(obj);
 if(k==40) UpDownObj.substUpDown(obj);
 
 if(k>="0".charCodeAt(0) && k<="9".charCodeAt(0)){
  return true;
 }
 
 if(k>=96 && k<=105){
  return true;
 }
 
 if(k==110 || k==190){
  if(val(obj.inc)!=val(Math.floor(obj.inc)) && !obj.value.chkIsContain(".") ) return true;
 }
 
 if(k==109 || k==189 || k==45){
  if(val(obj.min)<0 && !obj.value.chkIsContain("-") ) return true;
 }
 
 if( isthiskey(k,35,36,37,39,46,8,9,20,144,145) ) return true;

 return false;
}

UpDownObj.mkUpDown=function mkUpDown(name,size,value,min,max,inc,mustNum,maxlength,propertiesForText,propertiesForBtn){
 UpDownObj.length+=1;
 
 if(!name || name==""){
  alert("建立第 "+ UpDownObj.length +" 個 UpDown 欄位失敗。\n\n請為欄位取名 !");
  return false;
 }else if( !isNaN(parseFloat(name))
  || name.chkIsMatch("null","undefined","if","else","for","while","do","break","continue","return","true","false","top","parent","window","self")
  || name.chkIsContain(" ",",",".","<",">","/","\\",'"',"'","[","]","=","+","-","*",":",";","?","!","@","#","$","%","^","&","(",")","{","}","`","~","|") ){
  alert("建立第 "+ UpDownObj.length +" 個 UpDown 欄位失敗。\n\n名稱「"+name+"」並不符合 JavaScript 規格，請另外取名 !");
  return false;
 }else if(chkIsExist(name)){
  alert("建立第 "+ UpDownObj.length +" 個 UpDown 欄位失敗。\n\n名稱「"+name+"」已經使用，請另外取名 !");
  return false;
 }
 
 if(!!size) size=abs(val(size));
 if(!size) size=5;
 
 if(!value || isNaN(value)) value=0;
 
 if(!min || isNaN(min)) min=0;
 
 if((!max && max!=0) || isNaN(max)) max=100;
 
 if(!!maxlength || maxlength==0){
  if(isNaN(maxlength) || val(maxlength)<0){
   maxlength='""';
  }else{
   maxlength=val(maxlength);
  }
 }else if(!maxlength && maxlength!=0){
  maxlength='""';
 }
 
 if(!inc || isNaN(inc)) inc=1;
 if(!propertiesForText) propertiesForText='';
 if(!propertiesForBtn) propertiesForBtn='';
 
 if((typeof mustNum).toLowerCase=="boolean") mustNum=true;
 mustNum=!!mustNum;
 if(mustNum) propertiesForText=' onkeydown="return UpDownObj.checkKeyIsNum(this)" onkeypress="return UpDownObj.checkKeyIsNum(this)" '+propertiesForText;
 
 document.write('<input type=text onpaste="return false" maxlength="'+maxlength+'" onblur="UpDownObj.checkUpDown(this)" name='+name+' value='+value+' max='+max+' min='+min+' size='+size+' inc='+inc+' '+propertiesForText+' style="color: #ffffff; background-color: #191919; border:1 solid #ffffff" readonly=readonly>');
if(name!="chkpoint"){
 document.write('<input name=btnUp'+name+' type=button value="▲" onclick="UpDownObj.plusUpDown(this.form.'+name+',chkpoint)" ondblclick=this.onclick() '+propertiesForBtn+' style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">');
 document.write('<input name=btnDown'+name+' type=button value="▼" onclick="UpDownObj.substUpDown(this.form.'+name+',chkpoint)" ondblclick=this.onclick() '+propertiesForBtn+' style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">');
}
}
</script>
<?
}
?>
</head>

<body bgcolor="#000000" text="#FFFFFF" link="#FFFFFF" vlink="#FFFFFF" alink="#FFFFFF">
<?
include("../html/head.php");
?>
<table border=0 cellpadding=1 cellspacing=1 bgcolor=ffffff width="80%">
<?
if($err_count==0){
if($mode==1){?>
<form method="post" action="changename_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="center"><font size=2 color=red>更名系統</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">更名1個人物需<?=$changenamepoint?>點。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left"><font color=red>請勿輸入繁體中文、半形英數或超過12字元以外的文字</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">若使用<font color=red>英文開頭</font>時，<font color=red>開頭</font>請務必使用<font color=red>大寫</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">所有<font color=red>曾經</font>違反以上規定者，將<font color=red>鎖定帳號永不解除</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">使用本系統時，請將帳號<font color=red>完整離線</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">原始暱稱：<select size="1" name="oldname" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?do{?>
<option value="<?=$strac['char_name']?>"><?=$strac['char_name']?></option>
<?}while($strac=mysql_fetch_assoc($strab));?>
</select></div></td></tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">更名暱稱：<input name="newname" style="width: 100px; color: #ffffff; background-color: #191919; border:1 solid #ffffff" maxlength="12"></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">變更性別：<input name="changesex" type="radio" value="0" checked>不變 <input name="changesex" type="radio" value="1">男 <input name="changesex" type="radio" value="2">女</div></td>
	</tr>
<?}elseif($mode==2){?>
<form method="post" action="reset_save.php" name="form1" onsubmit="B1.disabled=1">
<input type="hidden" name="code" value="<?=$code?>">
	<tr>
		<td width="100%" bgcolor=#303030 colspan="2"><div align="center"><font size=2 color=red>重置系統</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">重置1個人物需<?=$charresetpoint?>點。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">使用此系統將會<font color=red>重置原始點數</font>、<font color=red>萬能藥點數</font>、<font color=red>升級點數</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">原始點數將於<font color=red>本頁面設定</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">萬能藥點數以及升級點數需由<font color=red>遊戲中增加</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">能力綜合值需為<font color=red>75</font>。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">參考配點1：20、20、14、7、7、7，</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">參考配點2：18、18、18、7、7、7。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">重置系統可選擇在重置角色的同時重置所有的任務。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020 colspan="2"><div align="left">重置所有的任務的同時，也將<font color=red>刪除所有任務中的半成品</font>，成品將保留。</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="left">角色暱稱：<select size="1" name="charname" style="color: #ffffff; background-color: #191919; border:1 solid #ffffff">
<?do{?>
<option value="<?=$strac['char_name']?>"><?=$strac['char_name']?></option>
<?}while($strac=mysql_fetch_assoc($strab));?>
</div></td></tr>
	<tr>
		<td width="50%" bgcolor=#202020><div align="left">力量：<script language="javascript">UpDownObj.mkUpDown("sta",5,7,7,20,1,true,2);</script>(7~20)</div></td>
		<td width="50%" bgcolor=#202020><div align="left">精神：<script language="javascript">UpDownObj.mkUpDown("wis",5,7,7,20,1,true,2);</script>(7~20)</div></td>
	</tr>
	<tr>
		<td width="50%" bgcolor=#101010><div align="left">敏捷：<script language="javascript">UpDownObj.mkUpDown("dex",5,7,7,20,1,true,2);</script>(7~20)</div></td>
		<td width="50%" bgcolor=#101010><div align="left">魅力：<script language="javascript">UpDownObj.mkUpDown("cha",5,7,7,20,1,true,2);</script>(7~20)</div></td>
	</tr>
	<tr>
		<td width="50%" bgcolor=#202020><div align="left">體質：<script language="javascript">UpDownObj.mkUpDown("con",5,7,7,20,1,true,2);</script>(7~20)</div></td>
		<td width="50%" bgcolor=#202020><div align="left">智力：<script language="javascript">UpDownObj.mkUpDown("int",5,7,7,20,1,true,2);</script>(7~20)</div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="center">剩餘點數<script language="javascript">UpDownObj.mkUpDown("chkpoint",5,33,0,33,0,true,2);</script></div></td>
	</tr>
<?}?>
	<tr>
		<td width="100%" bgcolor=#101010 colspan="2"><div align="center"><input type="submit" value="送出" name="B1" style="width: 50px; color: #ffffff; background-color: #191919; border:1 solid #ffffff"></div></td>
	</tr>
</form>
<?}else{?>
	<tr>
		<td width="100%" bgcolor=#303030><div align="center"><font size=2 color=red>系統訊息</font></div></td>
	</tr>
	<tr>
		<td width="100%" bgcolor=#202020><div align="left"><?=$msg?></div></td>
	</tr>
<?}?>
</table>
</center>
</body>
</html>