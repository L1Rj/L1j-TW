/* 20100206 l1jtw 修改寵物格式欄位資料 */

/* pettypes 欄位名稱修正 */
Update pettypes Set Name = '虎男' Where BaseNpcId = '45313';

/* pettypes 欄位新增 evolvItemId 欄位 (控制進化道具) */
alter table pettypes add evolvItemId int(10) default '0' after MpUpMax ;

Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45034'; /* 牧羊犬 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45039'; /* 貓 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45040'; /* 熊 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45042'; /* 杜賓狗 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45043'; /* 狼 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45044'; /* 浣熊 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45046'; /* 小獵犬 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45047'; /* 聖伯納犬 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45048'; /* 狐狸 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45049'; /* 暴走兔 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45053'; /* 哈士奇 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45054'; /* 柯利 */
Update pettypes Set evolvItemId = '40070' Where BaseNpcId = '45313'; /* 虎男 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45686'; /* 高等狼 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45687'; /* 高等牧羊犬 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45688'; /* 高等杜賓狗 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45689'; /* 高等哈士奇 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45690'; /* 高等熊 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45691'; /* 高等柯利 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45692'; /* 高等小獵犬 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45693'; /* 高等聖伯納犬 */
Update pettypes Set evolvItemId = '41310' Where BaseNpcId = '45694'; /* 高等狐狸 */