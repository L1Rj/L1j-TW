/* 20100803 Account 防雙登 DB控制欄位 */
alter table accounts add online_status int(2) default '0' after character_slot;