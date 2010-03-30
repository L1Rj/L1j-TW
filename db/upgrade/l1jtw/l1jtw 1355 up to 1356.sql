/* 20100331 l1jtw 高等寵物進化黃金龍 */

/* 新增黃金龍至 pettypes */
INSERT INTO `pettypes` VALUES (46046, '黃金龍', 40057, 8, 11, 3, 5, 41310, 0, 1088, 1089, 1090, 1091, 1092, 1074);

/* 修正進化後對應黃金龍  */
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45686';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45687';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45688';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45689';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45690';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45691';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45692';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45693';
Update pettypes Set NpcIdForEvolving = '46046' Where BaseNpcId = '45694';