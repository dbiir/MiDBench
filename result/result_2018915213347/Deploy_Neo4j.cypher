
            MATCH ()-[r:PointTo_test]->()
            DELETE r;
            
            MATCH (n:item_test)
            DELETE n;
            
            return 'Start time: ' + timestamp();
            
            LOAD CSV WITH HEADERS FROM "file:///g1_new_nodes_as_files_item.csv" AS csvLine
            CREATE (p:item_test { plm_m_oid: csvLine.plm_m_oid, plm_oid: csvLine.plm_oid, plm_m_id: csvLine.plm_m_id, plm_i_name: csvLine.plm_i_name, plm_i_createtime: csvLine.plm_i_createtime, plm_i_checkintime: csvLine.plm_i_checkintime, plm_cailiao: csvLine.plm_cailiao, plm_checkintime: csvLine.plm_checkintime, plm_weight: csvLine.plm_weight, plm_wlly: csvLine.plm_wlly, plm_guige: csvLine.plm_guige, plm_gylx: csvLine.v });
            
            CREATE CONSTRAINT ON (p:item_test) ASSERT p.plm_m_oid IS UNIQUE;
            CREATE INDEX ON :item_test(plm_oid);
            CREATE INDEX ON :item_test(plm_m_id);
            
            LOAD CSV WITH HEADERS FROM "file:///g1_new_nodes_as_files_relation.csv" AS csvLine
            MATCH (p1:item_test { plm_m_oid: csvLine.plm_leftobj}), (p2:item_test { plm_m_oid: csvLine.plm_rightobj})
            CREATE (p1)-[:PointTo_test { plm_rightobj:csvLine.plm_rightobj, plm_leftobj:csvLine.plm_leftobj, plm_oid: csvLine.plm_oid, plm_createtime: csvLine.plm_createtime, plm_order: csvLine.plm_order, plm_jianhao: csvLine.plm_jianhao, plm_number: csvLine.plm_number }]->(p2);
            
            return 'End time: ' + timestamp();
            
            CREATE INDEX ON :PointTo_test(plm_rightobj);
            CREATE INDEX ON :PointTo_test(plm_leftobj);
            CREATE INDEX ON :PointTo_test(plm_oid);
        