<?php
$passwordSalt = test; // 自訂加密

$userAccount = admin; // 玩家帳號
$userPassword = 12345; // 玩家密碼

$md5 = hash('md5', $userAccount);

$encodePassword = hash('sha256', "$passwordSalt$userPassword$md5"); 

echo $encodePassword; // 顯示已加密密碼
?>