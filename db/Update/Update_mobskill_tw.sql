/*
 * 20090517 怪物技能中文說明
 */
Update mobskill Set mobname = '暴走兔(冰錐)' Where mobid = '45049';
Update mobskill Set mobname = '歐吉(範圍)' Where mobid = '45278';
Update mobskill Set mobname = '黑暗棲林者(遠距離攻擊)' Where mobid = '45347';
Update mobskill Set mobname = '恐怖的地獄犬(火燄)' Where mobid = '45471';
Update mobskill Set mobname = '大王烏賊(迴轉漩渦)' Where mobid = '45734';
Update mobskill Set mobname = '阿魯巴(加速術)' Where mobid = '45373';
Update mobskill Set mobname = '阿魯巴(加速術)' Where mobid = '45505';
Update mobskill Set mobname = '小安加斯(火焰噴吐)' Where mobid = '45746';
Update mobskill Set mobname = '食腐獸(遠距離特殊攻擊)' Where mobid = '46065' AND actNo = '0';
Update mobskill Set mobname = '地獄束縛犬(火焰噴吐)' Where mobid = '45512';
Update mobskill Set mobname = '血騎士(迴旋斬)' Where mobid = '45527';
Update mobskill Set mobname = '混沌(呼喚傳送術)' Where mobid = '45625' AND actNo = '0';
Update mobskill Set mobname = '混沌(物理攻擊)' Where mobid = '45625'AND Type = '1';
Update mobskill Set mobname = '混沌(衝擊之暈)' Where mobid = '45625' AND actNo = '2';
Update mobskill Set mobname = '混沌(地裂術)' Where mobid = '45625' AND actNo = '3';
Update mobskill Set mobname = '混沌(召喚混沌的司祭A)' Where mobid = '45625' AND actNo = '4';
Update mobskill Set mobname = '混沌(召喚混沌的司祭b)' Where mobid = '45625' AND actNo = '5';
Update mobskill Set mobname = '混沌(召喚混沌的司祭c)' Where mobid = '45625' AND actNo = '6';

/* 20090527 修正移除技能後的順序*/
Update mobskill Set actNo = '0' Where mobid = '45614' AND actNo = '3';
Update mobskill Set actNo = '1' Where mobid = '45614' AND actNo = '5';

/*20090528 修正混沌技能順序*/
Update mobskill Set actNo = '1' Where mobid = '45625' AND actNo = '2';
Update mobskill Set actNo = '2' Where mobid = '45625' AND actNo = '3';
Update mobskill Set actNo = '3' Where mobid = '45625' AND actNo = '4';
Update mobskill Set actNo = '4' Where mobid = '45625' AND actNo = '5';
Update mobskill Set actNo = '5' Where mobid = '45625' AND actNo = '6';
Update mobskill Set actNo = '6' Where mobid = '45625' AND actNo = '7';