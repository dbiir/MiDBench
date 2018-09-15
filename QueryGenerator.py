#coding=utf-8
import sys, os, random
from Item import Item
from DataSimulator import DataSimulator
#reload(sys)
#sys.setdefaultencoding('utf-8')

class QueryGenerator:
    def __init__(self, graph, graph_conf, node_id, edge_id,base_path,graph_path,base_path_data,base_path_result):
        self.graph = graph
        self.graph_conf = graph_conf
        self.node_id = node_id
        self.edge_id = edge_id
        self.base_path = base_path
        self.graph_path = graph_path
        self.base_path_data = base_path_data
        self.base_path_result = base_path_result

        self.filename_neo4j_query = base_path_result+"/Query_Neo4j.cypher"
        self.filename_neo4j_deploy = base_path_result+"/Deploy_Neo4j.cypher" #导入
        self.filename_neo4j_result_import = base_path_result+"/Result_Import_Neo4j.cypher"
        self.filename_neo4j_result_query = base_path_result+"/Result_Query_Neo4j.cypher"
        self.filename_neo4j_result_insert = base_path_result+"/Result_Insert_Neo4j.cypher"

        self.filename_oracle_query = base_path_result+"/Query_Oracle.sql"
        self.filename_oracle_deploy = base_path_result+"/Deploy_Oracle.sql"  # 导入


    def generate(self):
        fp_oracle = open(self.filename_oracle_query, "a")
        fp_oracle.write("alter system flush buffer_cache;\n")
        fp_oracle.write("alter system flush shared_pool;\n")
        fp_oracle.write("select 'Start time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;\n")
        fp_oracle.close()
        fp_neo4j = open(self.filename_neo4j_query, "a")
        fp_neo4j.write("return 'Start time: ' + timestamp();\n")
        fp_neo4j.close()

        for i in range(0, len(self.graph)):
            flag_first_time = 0
            onegraph = (self.graph)[i]
            for j in range(0, int((self.graph_conf)[i][2])):
                root = self.__get_node_1_to_3(onegraph, flag_first_time)
                level = len(onegraph)
                leaf = self.__get_node_last_1(onegraph)
                compare1 = self.__get_node_1_to_3(onegraph, flag_first_time)
                compare2 = self.__get_node_1_to_3(onegraph, flag_first_time)
                self.__write_file(str(root.id), str(level), str(leaf.id), str(compare1.id), str(compare2.id), str(i),
                                  str(j))
                flag_first_time += 1
            fp_oracle = open(self.filename_oracle_query, "a")
            fp_oracle.write("select 'All execution time on Graph " + str(
                i) + ": ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;\n")
            fp_oracle.close()
            fp_neo4j = open(self.filename_neo4j_query, "a")
            fp_neo4j.write("return 'All execution time on Graph " + str(i) + ": ' + timestamp();\n")
            fp_neo4j.close()

        fp_oracle = open(self.filename_oracle_query, "a")
        fp_oracle.write("\n")
        fp_oracle.write("select 'End time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;\n")
        fp_oracle.write("quit;\n")
        fp_oracle.close()
        fp_neo4j = open(self.filename_neo4j_query, "a")
        fp_neo4j.write("\n")
        fp_neo4j.write("return 'End time: ' + timestamp();\n")
        fp_neo4j.close()
        self.__generate_execute_command()
        self.__generate_deploy_file()


    def __get_node_1_to_3(self, onegraph, flag_first_time):
        nodes = []
        if flag_first_time == 0:
            for i in range(0, 1):
                nodes += onegraph[i]
        elif flag_first_time == 1:
            for i in range(1, 2):
                nodes += onegraph[i]
        else:
            for i in range(0, 3):
                nodes += onegraph[i]
        return nodes[random.randint(0, len(nodes) - 1)]
    
    def __get_node_last_1(self, onegraph):
        nodes = onegraph[ len(onegraph) - 1 ]
        onenode = nodes[ random.randint(0, len(nodes)-1) ]
        return onenode

    def __write_file(self, root, level, leaf, compare1, compare2, gnum, qnum):
        
        fp_neo4j = open(self.filename_neo4j_query, "a")
        fp_neo4j.write("""
            CALL example.where_used2('%s', %s, 'item_test', 'PointTo_test') yield pid, cid, poid, coid, rel_oid, cnt, lev, path return 'where_used2: '+count(*);
            call example.generate_structure2('%s', %s, 'item_test', 'PointTo_test') yield ROOTID, PITEM_OID, CITEM_OID, PITEM_ID, CITEM_ID, ITEM_NAME, ITEM_OID, ITEM_NUMBER, ITEM_LEVEL, IS_LEAF, ITEM_PATH return 'generate_structure2: '+count(*);
            call example.structure_diff2('%s', '%s', %s, 'item_test', 'PointTo_test') yield diff_type, src_rel_oid, src_pid, src_cid, src_number, dst_rel_oid, dst_pid, dst_cid, dst_number return 'structure_diff2: '+count(*);
            CALL example.struct_aggr2('%s', %s, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum return 'struct_aggr2: '+count(*);
            call example.struct_aggr2('%s', %s, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly with root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly where plm_wlly="外购" return 'plan: '+count(*);
            call example.struct_aggr2('%s', %s, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly with root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly where usage_count > 1 return 'usage: '+count(*);
            call example.struct_aggr2('%s', %s, 'item_test', 'PointTo_test') yield root_id, root_name, cid, cname, usage_count, min_lev, max_lev, qty_sum, weight_sum, plm_wlly with root_id, root_name, count(cid) as count_cid, max(max_lev) as max_max_lev, sum(qty_sum) as sum_qty_sum, sum(weight_sum) as sum_weight_sum return 'statistic: '+count(*);
           
        """ % (
            leaf, level,
            root, level,
            compare1, compare2, level,
            root, level,
            root, level,
            root, level,
            root, level
        ))
        fp_neo4j.close()

        # oracle
        fp_oracle = open(self.filename_oracle_query, "a")
        fp_oracle.write("""
           -- **********
           select 'Graph: %s. Query: %s.' from dual;
           -- ##########
           select 'where_used2: ', count(*) from table(query_bom_test.where_userd('%s'));
           select 'generate_structure2: ', count(*) from table(query_bom_test.generate_structure('%s', %s));
           select 'structure_diff2: ', count(*) from table(query_bom_test.structure_diff('%s', '%s'));
           select 'struct_aggr2: ', count(*) from table(query_bom_test.struct_aggr('%s'));
           select 'plan: ', count(*) from table(query_bom_test.struct_aggr('%s')) t join t_item_test i on t.cid = i.plm_m_id where i.plm_wlly = '外购';
           select 'usage: ', count(*) from table(query_bom_test.struct_aggr('%s')) t where t.usage_count > 1;
           select 'statistic: ', count(*) from ( select root_id, root_name, count(distinct cid), sum(qty_sum), sum(weight_sum), max(max_lev) from table(query_bom_test.struct_aggr('%s')) group by root_id, root_name );
           -- ##########
           select 'Time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;
           -- **********
                   """ % (
            gnum, qnum,
            leaf,
            root, level,
            compare1, compare2,
            root,
            root,
            root,
            root
        ))
        fp_oracle.close()


    def __generate_deploy_file(self):
        fp_neo4j = open(self.filename_neo4j_deploy, "a")
        fp_neo4j.write('''
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
        ''')
        fp_neo4j.close()

        #oracle
        fp_oracle = open(self.filename_oracle_deploy, "a")
        fp_oracle.write('''
        delete from T_ITEM_TEST;
        delete from T_RELATION_TEST;

        select 'Start time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;

        @%s\\result\\g1_new_nodes_as_files_item.sql
        @%s\\result\\g1_new_nodes_as_files_relation.sql

        select 'End time: ', to_char(current_timestamp,'yyyy-mm-dd hh24:mi:ss.ff6') from dual;

        quit;
                ''' % (
            os.getcwd(),
            os.getcwd()
        ))
        fp_oracle.close()



    def __generate_execute_command(self):
        fp = open(self.base_path_result+"/Execute_Command.sh", "a")
        fp.write('''
            #Neo4j:
            #copy csv to neo4j import dir
            cp %s/*.csv %s/import
            #execute Deploy.cypher to load csv to database instance
            cd %s/bin
            ./neo4j-shell -file %s
            #performance test
            ./neo4j-shell -file %s
        ''' % (self.base_path_result,self.graph_path,self.graph_path,self.filename_neo4j_deploy,self.filename_neo4j_query))
        fp.close()

#