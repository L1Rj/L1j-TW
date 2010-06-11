/* 20100518 l1jtw 新增 NPC 修正NPC名稱 修正武器防具道具 gfxid 修正 不死鳥技能 */

/* rev 1465 */
Update mobskill Set SkillId = '10018' Where mobid = '45617' And actNo = '0';
/* rev 1466 */
Update etcitem Set grdgfx = '7206' Where item_id = '49300';
Update etcitem Set grdgfx = '7200' Where item_id = '49301';
Update etcitem Set grdgfx = '7202' Where item_id = '49302';
Update etcitem Set grdgfx = '7204' Where item_id = '49303';
Update etcitem Set grdgfx = '7205' Where item_id = '49304';
Update etcitem Set grdgfx = '7199' Where item_id = '49305';
Update etcitem Set grdgfx = '7201' Where item_id = '49306';
Update etcitem Set grdgfx = '7203' Where item_id = '49307';

/* rev 1467 */
Update armor Set grdgfx = '7208' Where item_id = '21540';
Update armor Set grdgfx = '7209' Where item_id = '21541';
Update armor Set grdgfx = '7210' Where item_id = '21542';
Update armor Set grdgfx = '7212' Where item_id = '21543';
Update weapon Set grdgfx = '7207' Where item_id = '510';
Update weapon Set grdgfx = '7210' Where item_id = '510';

/* rev 1468 */
Update armor Set invgfx = '3555' Where item_id = '21535';
Update armor Set invgfx = '35554' Where item_id = '21536';
Update armor Set invgfx = '3553' Where item_id = '21537';
Update armor Set invgfx = '3556' Where item_id = '21538';
Update armor Set invgfx = '3552' Where item_id = '21539';

/* rev 1472 */
/* 侏儒部落 */
INSERT INTO `npc` VALUES ('91208', '侏儒族工人', '$7768', '侏儒部落', 'L1Monster', '54', '38', '650', '15', '-35', '10', '12', '10', '7', '10', '60', '1445', '-15', 'small', '2', '1', '1', '600', '1480', '0', '1480', '1480', '0', '0', '0', '1', '0', '0', 'dwarf', '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '6', '0', '0', '0');
INSERT INTO `npc` VALUES ('91209', '侏儒族哨兵', '$7769', '侏儒部落', 'L1Monster', '1184', '38', '650', '15', '-40', '15', '13', '15', '9', '9', '70', '1445', '-20', 'small', '2', '1', '1', '640', '1160', '0', '1160', '1160', '0', '0', '0', '1', '0', '0', 'dwarf', '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0', '0', '0');
INSERT INTO `npc` VALUES ('91210', '侏儒族將軍', '$7770', '侏儒部落', 'L1Monster', '7335', '45', '700', '15', '-50', '15', '13', '15', '9', '9', '70', '2026', '-30', 'small', '2', '1', '1', '640', '1160', '0', '1160', '1160', '0', '0', '0', '1', '0', '0', 'dwarf', '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0', '0', '0');
INSERT INTO `npc` VALUES ('91211', '侏儒族魔法師', '$7771', '侏儒部落', 'L1Monster', '7362', '45', '700', '600', '-35', '15', '13', '15', '9', '9', '90', '2026', '-30', 'small', '2', '1', '1', '640', '1160', '0', '1160', '1160', '0', '0', '0', '1', '0', '0', 'dwarf', '1', '-1', '-1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '-1', '0', '0', '0', '0', '0');
/* 地底湖 */
INSERT INTO `npc` VALUES ('91212', '須曼', '$4735', '地底湖', 'L1Monster', '5767', '80', '12000', '5000', '-78', '125', '50', '24', '35', '40', '80', '6401', '-1000', 'large', '1', '2', '0', '0', '1960', '0', '2000', '2400', '0', '0', '0', '1', '1', '1', '', '0', '-1', '-1', '0', '0', '0', '5000', '5000', '5000', '50', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '-1', '0', '0', '0', '0', '0');
INSERT INTO `npc` VALUES ('91213', '騎士哈達', '$4736', '地底湖', 'L1Monster', '5802', '50', '800', '1000', '-78', '125', '50', '24', '35', '40', '40', '2501', '-200', 'large', '1', '2', '0', '0', '1960', '0', '2000', '2400', '0', '0', '0', '1', '1', '1', '', '0', '-1', '-1', '0', '0', '0', '5000', '5000', '5000', '50', '0', '0', '0', '0', '0', '0', '0', '0', '1', '0', '0', '0', '0', '0', '-1', '0', '0', '0', '0', '0');

/* rev 1473 */
Update npc Set name = '受詛咒的 蟑螂人' Where npcid = '45920';
Update npc Set name = '受詛咒的 穴居人' Where npcid = '45921';
Update npc Set name = '受詛咒的 巨鼠' Where npcid = '45922';
Update npc Set name = '受詛咒的 鼠人' Where npcid = '45924';
Update npc Set name = '受詛咒的 多眼怪' Where npcid = '45926';
Update npc Set name = '受詛咒的 蛇女' Where npcid = '45928';
Update npc Set name = '受詛咒的 蛇女' Where npcid = '45929';
Update npc Set name = '受詛咒的 梅杜莎' Where npcid = '45935';
Update npc Set name = '受詛咒的 人魚' Where npcid = '45940';
Update npc Set name = '受詛咒的 水精靈王' Where npcid = '45942';