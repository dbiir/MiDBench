#coding=utf-8
import sys, time
from neo4j.v1 import GraphDatabase
#reload(sys)
#sys.setdefaultencoding('utf-8')

class GraphExporter:
    def __init__(self):
        pass

    def __get_clean_string(self, s):
        return str(s).replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "").replace(u'\u3000', u'')

    def __get_item_names(self, tx, class_node):
        temp = []
        for record in tx.run(
            "match (m:"+str(class_node)+") "
            "return m.plm_m_oid, m.plm_i_name "
            ):
            item = self.__get_clean_string(record["m.plm_i_name"])
            if not item in temp:
                temp.append(item)
        return temp

    def __get_distribution_stat(self, tx, inames, class_node, class_edge):
        #type_number: have the same name and in the same graph
        iname_dict = {}
        type_number = {}
        count = 0
        max_level_g1 = 0
        max_level_g2 = 0
        for i in inames:
            iname_dict[i] = {}
        for record in tx.run(
            "match (m:"+str(class_node)+")-[:"+str(class_edge)+"]->(n:"+str(class_node)+") "
            "return m.plm_m_oid, m.plm_i_name, n.plm_m_oid, n.plm_i_name "
            ):
            plm_m_oid_m = self.__get_clean_string(record["m.plm_m_oid"])
            plm_i_name_m = self.__get_clean_string(record["m.plm_i_name"])
            plm_m_oid_n = self.__get_clean_string(record["n.plm_m_oid"])
            plm_i_name_n = self.__get_clean_string(record["n.plm_i_name"])


            if not plm_i_name_n in type_number:
                count += 1
                type_number[plm_i_name_n] = count
            if not plm_i_name_m in type_number:
                count += 1
                type_number[plm_i_name_m] = count

            if not plm_m_oid_m in iname_dict[plm_i_name_m]:
                iname_dict[plm_i_name_m][plm_m_oid_m] = {'name': plm_i_name_m, 'in': [], 'out': [], 'level_g1': -1, 'level_g2': -1, 'type_number': type_number[plm_i_name_m]}
            if not plm_m_oid_n in iname_dict[plm_i_name_n]:
                iname_dict[plm_i_name_n][plm_m_oid_n] = {'name': plm_i_name_n, 'in': [], 'out': [], 'level_g1': -1, 'level_g2': -1, 'type_number': type_number[plm_i_name_n]}
            iname_dict[plm_i_name_m][plm_m_oid_m]['out'].append({"type_number": type_number[plm_i_name_n], "oid": plm_m_oid_n})
            iname_dict[plm_i_name_n][plm_m_oid_n]['in'].append({"type_number": type_number[plm_i_name_m], "oid": plm_m_oid_m})
        
        for i in iname_dict:
            for j in iname_dict[i]:
                # print j
                if not (j == '1A5BB4CCAF85494EA998E8E810974E2B'):
                    for record in tx.run(
                        "match (m:"+str(class_node)+" {plm_m_oid: $oid}), (n1:"+str(class_node)+" {plm_m_oid: '1A5BB4CCAF85494EA998E8E810974E2B'}), "
                        "sp1 = shortestPath((n1)-[*]->(m)) "
                        "return length(sp1)"
                        , oid = j):
                        level_temp = int(record["length(sp1)"])
                        if level_temp >= 0:
                            iname_dict[i][j]['level_g1'] = level_temp
                            if level_temp > max_level_g1:
                                max_level_g1 = level_temp
                else:
                    iname_dict[i][j]['level_g1'] = 0
        for i in iname_dict:
            for j in iname_dict[i]:
                # print j
                if not (j == '0B5DCE9C6B72AB4EB3BBF37B34AB421F'):
                    for record in tx.run(
                        "match (m:"+str(class_node)+" {plm_m_oid: $oid}), (n2:"+str(class_node)+" {plm_m_oid: '0B5DCE9C6B72AB4EB3BBF37B34AB421F'}), "
                        "sp2 = shortestPath((n2)-[*]->(m)) "
                        "return length(sp2)"
                        , oid = j):
                        level_temp = int(record["length(sp2)"])
                        if level_temp >= 0:
                            iname_dict[i][j]['level_g2'] = level_temp
                            if level_temp > max_level_g2:
                                max_level_g2 = level_temp
                else:
                    iname_dict[i][j]['level_g2'] = 0
        return iname_dict, max_level_g1, max_level_g2

    def export(self, uri, usename, password, class_node, class_edge):
        driver = GraphDatabase.driver(uri, auth=(usename, password))
        with driver.session() as session:
            with session.begin_transaction() as tx:
                raw_graph, g1_level, g2_level = self.__get_distribution_stat(tx, self.__get_item_names(tx, class_node), class_node, class_edge)
        print("[INFO] Finish getting raw graph from database. ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")")
        driver.close()

        return raw_graph, g1_level, g2_level
