use fim_system;
insert into Mod_Data(mod_name,ip_address) values('印刷機','192.168.50.102'); 

insert into Mod_Sen(sen_name,sen_code) values('溫濕度dht11感應資料','dht11'); 
insert into Mod_Sen(sen_name,sen_code) values('重量感應','hx711'); 
insert into Mod_Sen(sen_name,sen_code) values('電源控制','switch'); 

insert into Mod_Parm(mod_sen_id,parm_name,parm_code) 
select id,'濕度','humidity' from Mod_Sen where sen_code='dht11';
insert into Mod_Parm(mod_sen_id,parm_name,parm_code) 
select id,'溫度(攝氏H)','temp_cal' from Mod_Sen where sen_code='dht11';
insert into Mod_Parm(mod_sen_id,parm_name,parm_code) 
select id,'溫度(華氏C)','temp_fah' from Mod_Sen where sen_code='dht11';

insert into Mod_Parm(mod_sen_id,parm_name,parm_code) 
select id,'重量(g)','weight' from Mod_Sen where sen_code='hx711';

insert into Mod_Parm(mod_sen_id,parm_name,parm_code) 
select id,'電池電力(v)','battery_volt' from Mod_Sen where sen_code='switch';
insert into Mod_Parm(mod_sen_id,parm_name,parm_code) 
select id,'電源開關狀態','pow_status' from Mod_Sen where sen_code='switch';

insert into Mod_Data_Sen_R(mod_data_id,mod_sen_id) values(1,1); 
