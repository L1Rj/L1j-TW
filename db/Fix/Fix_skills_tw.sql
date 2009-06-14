/*
 * 20090521 技能修正
 * 未來的新技能，客戶端已經出現資料，但是沒有中文名稱
 * 日方已採用 - 坊日版資料與台版不一又被改變 備份
Update skills Set buffDuration = '20' Where skill_id = '33'; // 木乃伊的詛咒
Update skills Set buffDuration = '3600' Where skill_id = '36'; // 迷魅術
Update skills Set castgfx = '6342' Where skill_id = '42'; // 體魄強健術 日版用751??
Update skills Set castgfx = '230' Where skill_id = '67'; // 變形術
Update skills Set buffDuration = '32' Where skill_id = '80'; // 冰雪颶風
Update skills Set buffDuration = '6' Where skill_id = '87'; // 衝擊之暈
Update skills Set buffDuration = '60' Where skill_id = '182'; // 燃燒擊砍
Update skills Set buffDuration = '60' Where skill_id = '188'; // 恐懼無助
Update skills Set buffDuration = '60' Where skill_id = '193'; // 驚悚死神
Update skills Set buffDuration = '8' Where skill_id = '202'; // 混亂
Update skills Set buffDuration = '2', castgfx = '7020' Where skill_id = '208'; // 骷髏毀壞
Update skills Set buffDuration = '5' Where skill_id = '212'; // 幻想
*/

/* 20090614 修正衝擊之暈 延遲秒數為八秒 */
Update skills Set buffDuration = '8000' Where skill_id = '87';