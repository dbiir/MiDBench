alter system flush buffer_cache;
alter system flush shared_pool;
select 'Start time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;

           -- **********
           select 'Graph: 0. Query: 0.' from dual;
           -- ##########
           select 'where_used2: ', count(*) from table(query_bom_test.where_userd('2080'));
           select 'generate_structure2: ', count(*) from table(query_bom_test.generate_structure('1777', 6));
           select 'structure_diff2: ', count(*) from table(query_bom_test.structure_diff('1777', '1777'));
           select 'struct_aggr2: ', count(*) from table(query_bom_test.struct_aggr('1777'));
           select 'plan: ', count(*) from table(query_bom_test.struct_aggr('1777')) t join t_item_test i on t.cid = i.plm_m_id where i.plm_wlly = '外购';
           select 'usage: ', count(*) from table(query_bom_test.struct_aggr('1777')) t where t.usage_count > 1;
           select 'statistic: ', count(*) from ( select root_id, root_name, count(distinct cid), sum(qty_sum), sum(weight_sum), max(max_lev) from table(query_bom_test.struct_aggr('1777')) group by root_id, root_name );
           -- ##########
           select 'Time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;
           -- **********
                   select 'All execution time on Graph 0: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;

select 'End time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;
quit;
