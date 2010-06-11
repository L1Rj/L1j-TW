diff U3b C:/Users/waja/AppData/Local/Temp/L1MerchantInstance.java-rev1524.svn000.tmp.java C:/Users/waja/AppData/Local/Temp/L1MerchantInstance.java-rev1525.svn000.tmp.java
--- C:/Users/waja/AppData/Local/Temp/L1MerchantInstance.java-rev1524.svn000.tmp.java	Fri Jun 11 23:37:05 2010
+++ C:/Users/waja/AppData/Local/Temp/L1MerchantInstance.java-rev1525.svn000.tmp.java	Fri Jun 11 23:37:17 2010
@@ -1610,7 +1610,15 @@
 						&& player.getInventory().checkItem(40631)) {
 					htmlid = "lukein10";
 				} else if (player.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == L1Quest.QUEST_END) {
+						/* hms 海賊島任務 - 爺爺的寶物 修正 */
+						if (player.getInventory().checkItem(20269)) {
 					htmlid = "lukein0";
+						} else if(!player.getInventory().checkItem(20269)) {
+							player.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 0);
+							player.getQuest().set_step(L1Quest.QUEST_TBOX1, 0);
+							player.getQuest().set_step(L1Quest.QUEST_TBOX2, 0);
+							player.getQuest().set_step(L1Quest.QUEST_TBOX3, 0);
+						}
 				} else if (player.getQuest().get_step(L1Quest.QUEST_LUKEIN1) == 11) {
 					if (player.getInventory().checkItem(40716)) {
 						htmlid = "lukein9";
