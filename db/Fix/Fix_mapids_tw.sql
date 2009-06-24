/* 20090607 修正海賊島後半部可瞬移 */
Update mapids Set teleportable = '1' Where mapid = '480';

/* 20090622 修正 貝希摩斯 可標記&瞬移*/
Update mapids Set teleportable = '1' Where mapid = '1001';
Update mapids Set markable = '1' Where mapid = '1001';