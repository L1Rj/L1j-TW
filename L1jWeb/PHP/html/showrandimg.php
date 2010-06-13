<?php
   /*   網站驗証碼程式
    *   營運環境︰ PHP5.0.18 下測試通過
    *   需要 gd2 圖形庫支援（PHP.INI中 php_gd2.dll開啟）
    *   檔案名: showimg.php
    *   作者︰  17php.com
    *   Date:   2007.03
    *   技術支援︰ www.17php.com
    */

   //隨機生成一個 $num_max 位數的數字驗証碼
    $num="";
	$num_max = 6;
	$img_height = 15;
	$img_width = 100;
	$mass = 50;
    for($i=0;$i<$num_max;$i++){
    $num .= rand(0,9);
    }
   //將生成的驗証碼寫入session，備驗証頁面使用
    Session_start();
    $_SESSION["Checknum"] = $num;
   //創建圖片，定義顏色值
    Header("Content-type: image/PNG");
    srand((double)microtime()*1000000);
    $im = imagecreate($img_width,$img_height);
    $black = ImageColorAllocate($im, 255,255,255);
    $massblack = ImageColorAllocate($im, 255,0,0);
    $massblack2 = ImageColorAllocate($im, 0,255,0);
    $massblack3 = ImageColorAllocate($im, 0,0,255);
    $gray = ImageColorAllocate($im, 0,0,0);
    imagefill($im,0,0,$gray);

    //隨機繪製兩條虛線，起干擾作用
    $style = array($black, $black, $black, $black, $black, $gray, $gray, $gray, $gray, $gray);
    imagesetstyle($im, $style);
    $y1=rand(0,$img_height);
    $y2=rand(0,$img_height);
    $y3=rand(0,$img_height);
    $y4=rand(0,$img_height);
    imageline($im, 0, $y1, $img_width, $y3, IMG_COLOR_STYLED);
    imageline($im, 0, $y2, $img_width, $y4, IMG_COLOR_STYLED);

    //在畫布上隨機生成大量黑點，起干擾作用;
    for($i=0;$i<$mass;$i++)
    {
   imagesetpixel($im, rand(0,$img_width), rand(0,$img_height), $massblack);
    }
    for($i=0;$i<$mass;$i++)
    {
   imagesetpixel($im, rand(0,$img_width), rand(0,$img_height), $massblack2);
    }
    for($i=0;$i<$mass;$i++)
    {
   imagesetpixel($im, rand(0,$img_width), rand(0,$img_height), $massblack3);
    }

    //將四個數字隨機顯示在畫布上,字符的水準間距和位置都按一定波動範圍隨機生成
    $strx=10;
    for($i=0;$i<$num_max;$i++){
    $strpos=rand(-2,2);
    imagestring($im,5,$strx,$strpos, substr($num,$i,1), $black);
    $strx+=15;
    }

    ImagePNG($im);
    ImageDestroy($im);
   ?>