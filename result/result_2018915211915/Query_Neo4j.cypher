return 'Start time: ' + timestamp();

            CALL example.where_used2('1476', 6, 'item_test', 'PointTo_test') yield pid, cid, poid, coid, rel_oid, cnt, lev, path return 'where_used2: '+count(*);
            call example.generate_structure2('1777', 6, 'item_test', 'PointTo_test') yield ROOTID, PITEM_OID, CITEM_OID, PITEM_ID, CITEM_ID, ITEM_NAME, ITEM_OID, ITEM_NUMBER, ITEM_LEVEL, IS_LEAF, ITEM_PATH return 'generate_structure2: '+count(*);
            call example.structure_diff2('1777', '1777', 6, 'item_test', 'PointTo_test') yield diff_type, src_rel_oid, src_pid, src_cid, src_number, dst_rel_oid, dst_pid, dst_cid, dst_number return 'structure_diff2: '+count(*);
            CALL example.struct_aggr2('1777', 6, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum return 'struct_aggr2: '+count(*);
            call example.struct_aggr2('1777', 6, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly with root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly where plm_wlly="外购" return 'plan: '+count(*);
            call example.struct_aggr2('1777', 6, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly with root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly where usage_count > 1 return 'usage: '+count(*);
            call example.struct_aggr2('1777', 6, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly with root_id, root_name, count(cid) as count_cid, max(max_lev) as max_max_lev, sum(qty_sum) as sum_qty_sum, sum(weight_sum) as sum_weight_sum return 'statistic: '+count(*);
           
        return 'All execution time on Graph 0: ' + timestamp();

return 'End time: ' + timestamp();
