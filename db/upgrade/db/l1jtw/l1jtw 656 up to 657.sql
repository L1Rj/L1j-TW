/* 20090629 l1jtw 刪除原本的妖精PK紀錄 l1j-tw用 與l1j更新無關 */
alter table characters drop column PKEcount;
alter table characters drop column CWstatus;
alter table characters drop column LastPke;
alter table characters drop column LastCw;