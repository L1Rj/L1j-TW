/* 20100402 l1jtw 多魯嘉之袋延遲時間與次數修正 */

Update etcitem Set max_charge_count = '30' Where item_id = '49300';
Update etcitem Set delay_time = '86400' Where item_id = '49300';