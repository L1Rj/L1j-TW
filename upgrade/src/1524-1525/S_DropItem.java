diff U3b C:/Users/waja/AppData/Local/Temp/S_DropItem.java-rev1524.svn000.tmp.java C:/Users/waja/AppData/Local/Temp/S_DropItem.java-rev1525.svn000.tmp.java
--- C:/Users/waja/AppData/Local/Temp/S_DropItem.java-rev1524.svn000.tmp.java	Fri Jun 11 23:47:33 2010
+++ C:/Users/waja/AppData/Local/Temp/S_DropItem.java-rev1525.svn000.tmp.java	Fri Jun 11 23:47:34 2010
@@ -58,7 +58,7 @@
                     } else {
                         writeS(item.getItem().getUnidentifiedNameId());
                     }
-                } else if (item.isIdentified() && item.getCount() > 1) {
+		} else if (item.isIdentified()) {
                     if (item.getCount() > 1) {
                         writeS(item.getItem().getIdentifiedNameId() + "(" + item.getCount() + ")");
                     } else {
