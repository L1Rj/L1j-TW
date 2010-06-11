/* 20100329 l1jtw 修正 究極xxT恤 特製究極xxT恤 究極xxT恤箱子 7色染料箱 多魯嘉之袋 龍之鑰匙 道具圖示 grdgfx 仍不正確 
修正 龍之鑰匙 use_type 
道具新增 七色染料 頑皮幼龍蛋 淘氣幼龍蛋
*/

/* 究極xxT恤 */
Update armor Set invgfx = '2663' Where item_id = '21301';
Update armor Set invgfx = '2663' Where item_id = '21302';
Update armor Set invgfx = '2663' Where item_id = '21303';
Update armor Set invgfx = '2663' Where item_id = '21304';
Update armor Set invgfx = '2663' Where item_id = '21305';
Update armor Set invgfx = '2663' Where item_id = '21306';
Update armor Set invgfx = '2663' Where item_id = '21307';
Update armor Set invgfx = '2663' Where item_id = '21308';
Update armor Set invgfx = '2663' Where item_id = '21309';
/* 特製究極xxT恤 */
Update armor Set invgfx = '2663' Where item_id = '21310';
Update armor Set invgfx = '2663' Where item_id = '21311';
Update armor Set invgfx = '2663' Where item_id = '21312';
Update armor Set invgfx = '2663' Where item_id = '21313';
Update armor Set invgfx = '2663' Where item_id = '21314';
Update armor Set invgfx = '2663' Where item_id = '21315';
Update armor Set invgfx = '2663' Where item_id = '21316';
Update armor Set invgfx = '2663' Where item_id = '21317';
Update armor Set invgfx = '2663' Where item_id = '21318';
/* 7色染料箱 */
Update etcitem Set invgfx = '3286' Where item_id = '30211';
/* 多魯嘉之袋 3638? , 3639? */
Update etcitem Set invgfx = '3638' Where item_id = '50500';
/* 龍之鑰匙 */
Update etcitem Set invgfx = '3635' Where item_id = '50501';
Update etcitem Set use_type = 'normal' Where item_id = '50501';

/* 道具新增 七色染料  grdgfx 未知 */
INSERT INTO `etcitem` VALUES ('30212', '紅色T恤染料', '$5398', '$5398', 'other', 'normal', 'glass', '10', '2681', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('30213', '橙色T恤染料', '$5399', '$5399', 'other', 'normal', 'glass', '10', '2682', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('30214', '黃色T恤染料', '$5400', '$5400', 'other', 'normal', 'glass', '10', '2679', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('30215', '綠色T恤染料', '$5401', '$5401', 'other', 'normal', 'glass', '10', '2683', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('30216', '藍色T恤染料', '$5402', '$5402', 'other', 'normal', 'glass', '10', '2684', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('30217', '深藍色T恤染料', '$5403', '$5403', 'other', 'normal', 'glass', '10', '2678', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('30218', '紫色T恤染料', '$5404', '$5404', 'other', 'normal', 'glass', '10', '2680', '3041', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');

/* 道具新增 淘氣/頑皮幼龍蛋 grdgfx 未知 Item Desc 未確定 */
INSERT INTO `etcitem` VALUES ('50502', '淘氣幼龍蛋', '$7779', '$7779', 'other', 'none', 'animalmatter', '1000', '3787', '69', '4047', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
INSERT INTO `etcitem` VALUES ('50503', '頑皮幼龍蛋', '$7780', '$7780', 'other', 'none', 'animalmatter', '1000', '3787', '69', '4048', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '1');
