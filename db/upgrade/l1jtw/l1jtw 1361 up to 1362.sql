/* 20100402 l1jtw 多魯嘉之袋延遲時間與次數修正 不可堆疊 */

Update etcitem Set max_charge_count = '30' Where item_id = '50500';
Update etcitem Set delay_time = '86400' Where item_id = '50500';
Update etcitem Set stackable = '' Where item_id = '50500';