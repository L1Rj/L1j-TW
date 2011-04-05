/* 20110406 l1jtw 頑皮龍 淘氣龍資料修正 */

Update pettypes Set NpcIdForEvolving = '91154' Where BaseNpcId = '90500';
Update pettypes Set NpcIdForEvolving = '91152' Where BaseNpcId = '91150';
Update pettypes Set NpcIdForEvolving = '91153' Where BaseNpcId = '91151';
Update pettypes Set NpcIdForEvolving = '0' Where BaseNpcId = '91152';

Update pettypes Set EvolvItemId = '40070' Where BaseNpcId = '90500';
Update pettypes Set EvolvItemId = '40070' Where BaseNpcId = '91150';
Update pettypes Set EvolvItemId = '40070' Where BaseNpcId = '91151';
Update pettypes Set EvolvItemId = '0' Where BaseNpcId = '46046';

Update pettypes Set ItemIdForTaming = '0' Where BaseNpcId = '46046';
Update pettypes Set ItemIdForTaming = '40062' Where BaseNpcId = '90500';
Update pettypes Set ItemIdForTaming = '0' Where BaseNpcId = '91150';
Update pettypes Set ItemIdForTaming = '0' Where BaseNpcId = '91151';
Update pettypes Set ItemIdForTaming = '0' Where BaseNpcId = '91152';
Update pettypes Set ItemIdForTaming = '0' Where BaseNpcId = '91153';
Update pettypes Set ItemIdForTaming = '0' Where BaseNpcId = '91154';
