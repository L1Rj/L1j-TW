/* 20090625 l1jtw 修改角色欄位資料 */

/* characters 欄位新增 */
alter table characters add PKEcount int(10) default '0' after PKcount ;
alter table characters add CWstatus int(10) default '0' after PKEcount ;
alter table characters add LastPke int(10) default '0' after LastPk ;
alter table characters add LastCw int(10) default '0' after LastPke ;

/* 精靈公主 類型改變 */
Update npc Set impl  = 'L1Merchant' Where npcid  = '70853';