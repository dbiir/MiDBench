#coding=utf-8
import sys, os, json, random, math, time, copy
from Node import Node
from DataSimulator import DataSimulator
from DistributionAnalyzer import DistributionAnalyzer
# reload(sys)
# sys.setdefaultencoding('utf-8')

class GraphGenerator:
    # def __init__(self, raw_graph, g1_level, g2_level, size_target, level_target, number):
    #     self.__raw_graph = raw_graph
    #     self.__g1_level = g1_level
    #     self.__g2_level = g2_level
    #     self.__size_target = size_target
    #     self.__level_target = level_target
    #     self.__number = number
    
    def __init__(self, raw_graph, g1_level, g2_level, graph_conf,base_path,base_path_data,base_path_result):
        self.__raw_graph = raw_graph
        self.__g1_level = g1_level
        self.__g2_level = g2_level
        self.__graph_conf = graph_conf
        self.__base_path = base_path
        self.__base_path_data = base_path_data
        self.__base_path_result = base_path_result


    
    def create(self):
        nodeid = 0
        edge_id = 0
        file_header = True
        file_footer = False
        out_in = "in"

        # raw_graph
        self.__dump_raw_graph(self.__raw_graph)

        # raw_nodes
        # g_raw_nodes_by_name = {name}[level]
        # g_nodes_by_oid = {oid}[]
        g1_raw_size, g2_raw_size, g1_raw_nodes_by_name, g2_raw_nodes_by_name, g_nodes_by_oid = self.__get_raw_nodes(self.__raw_graph)
        print("[INFO] Finish extraction the node of the raw graph (size-1: "+str(g1_raw_size)+", size-2: "+str(g2_raw_size)+"). ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")")

        # dump
        self.__dump_raw_nodes(g1_raw_nodes_by_name, self.__base_path_data+"/g1_raw_nodes.txt")
        # self.__dump_raw_nodes(g2_raw_nodes_by_name, "data/g2_raw_nodes.txt")

        # raw_edge
        # raw_edge = {name}{level}[outs]
        g1_raw_edge, g2_raw_edge, g1_edge_num, g2_edge_num = self.__get_raw_edge(self.__raw_graph, g_nodes_by_oid, out_in)
        print("[INFO] Finish extraction the edge of the raw graph (size-1: "+str(g1_edge_num)+", size-2: "+str(g2_edge_num)+"). ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")")

        # dump
        self.__dump_raw_edge(g1_raw_edge, self.__base_path_data+"/g1_raw_edge.txt", out_in)
        # self.__dump_raw_edge(g2_raw_edge, "data/g2_raw_edge.txt", out_in)

        new_graphs = []
        for i in range(0, len((self.__graph_conf))):

            # new_nodes
            # new_nodes = [Node]
            # new_nodes_by_name = {name}{level}[Node]
            g1_new_nodes, g1_new_nodes_by_name, nodeid = self.__create_new_nodes(g1_raw_nodes_by_name, g1_raw_edge, g1_raw_size, (self.__graph_conf[i][0]), self.__g1_level, (self.__graph_conf[i][1]), nodeid)
            print("[INFO] Finish creating "+str(len(g1_new_nodes))+" node of the graph "+str(i)+"-1. ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")")
            # g2_new_nodes, g2_new_nodes_by_name, nodeid = self.__create_new_nodes(g2_raw_nodes_by_name, g2_raw_edge, g2_raw_size, (self.__graph_conf[i][0]), self.__g2_level, (self.__graph_conf[i][1]), nodeid)
            # print "[INFO] Finish creating "+str(len(g2_new_nodes))+" node of the graph "+str(i)+"-2. ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")"
            
            g1_graph_by_level = self.__get_graph_by_level(g1_new_nodes)
            
            # create_new_outs
            g1_num_edge = self.__create_new_edge(g1_raw_nodes_by_name, g1_raw_edge, g1_new_nodes_by_name, g1_graph_by_level, g1_raw_size, (self.__graph_conf[i][0]), self.__g1_level, (self.__graph_conf[i][1]), out_in)
            print("[INFO] Finish creating "+str(g1_num_edge)+" edge of the graph "+str(i)+"-1. ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")")
            # g2_num_edge = self.__create_new_edge(g2_raw_nodes_by_name, g2_raw_edge, g2_new_nodes_by_name, g2_raw_size, (self.__graph_conf[i][0]), self.__g2_level, (self.__graph_conf[i][1]), out_in)
            # print "[INFO] Finish creating "+str(g2_num_edge)+" edge of the graph "+str(i)+"-2. ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")"

            # dump
            self.__dump_graph_by_name(g1_new_nodes_by_name, self.__base_path_data+"/g1_new_nodes_by_name_"+str(i)+".txt", out_in)
            # self.__dump_graph_by_name(g2_new_nodes_by_name, "data/g2_new_nodes_by_name_"+str(i)+".txt", out_in)
            self.__dump_graph_by_level(g1_graph_by_level, self.__base_path_data+"/g1_new_nodes_by_level_"+str(i)+".txt", out_in)
            # self.__dump_graph_by_level(g2_new_nodes, "data/g2_new_nodes_by_level_"+str(i)+".txt", out_in)
            if i == len((self.__graph_conf)) - 1:
                file_footer = True
            file_header, file_footer, node_id, edge_id = self.__dump_graph_as_files(g1_new_nodes, self.__base_path_result+"/g1_new_nodes_as_files", out_in, edge_id, file_header, file_footer)
            # self.__dump_graph_as_files(g2_new_nodes, "data/g2_new_nodes_as_files"+str(i)+"", out_in)

            new_graphs.append(g1_graph_by_level)
            # new_graphs.append(g2_graph_by_level)
        return new_graphs, node_id, edge_id

    def __get_graph_by_level(self, graph):
        graph_by_level = {}
        for i in graph:
            if not i.level in graph_by_level:
                graph_by_level[i.level] = []
            graph_by_level[i.level].append(i)
        return graph_by_level
    
    def __dump_graph_as_files(self, graph, filename, out_in, edge_id, file_header, file_footer):
        wllx = ["专用零件","企标件","机加件","国标件","非标外购件","专用部件","产品配套"]
        wlly = ["自制","外协","外购"]
        temp_id = 0


        #生成oracle导入语句
        fp_sql_item = open(filename + "_item.sql", "a")
        fp_sql_relation = open(filename + "_relation.sql", "a")

        fp_csv_item = open(filename+"_item.csv", "a")
        fp_csv_relation = open(filename+"_relation.csv", "a")

        if file_header == True:

            #oracle
            fp_sql_item.write("prompt PL/SQL Developer import file\n")
            fp_sql_item.write("set feedback off\n")
            fp_sql_item.write("set define off\n")
            fp_sql_item.write("alter table T_ITEM_TEST disable all triggers;\n")
            # fp_sql_item.write("delete from T_ITEM_TEST;\n")
            fp_sql_item.write("commit;\n")

            fp_sql_relation.write("prompt PL/SQL Developer import file\n")
            fp_sql_relation.write("set feedback off\n")
            fp_sql_relation.write("set define off\n")
            fp_sql_relation.write("alter table T_RELATION_TEST disable all triggers;\n")
            # fp_sql_relation.write("delete from T_RELATION_TEST;\n")
            fp_sql_relation.write("commit;\n")


            #neo4j
            fp_csv_item.write("plm_m_oid,plm_oid,plm_m_id,plm_i_name,plm_i_createtime,plm_i_checkintime,plm_cailiao,plm_checkintime,plm_weight,plm_wllx,plm_wlly,plm_guige,plm_gylx\n")

            fp_csv_relation.write("plm_oid,plm_leftobj,plm_rightobj,plm_createtime,plm_order,plm_jianhao,plm_number\n")

            file_header = False

        for i in graph:
            temp_id = i.id
            time_now = time.localtime(time.time())
            mytime = str(time.strftime('%Y-%m-%d %H:%M:%S', time_now))
            mytime_sql = str(time.strftime('%d-%m-%Y %H:%M:%S', time_now))
            plm_weight = str(100 * random.random())
            plm_wllx = wllx[random.randint(0, 1000000) % 7]
            plm_wlly = wlly[random.randint(0, 1000000) % 3]
            fp_sql_item.write(
                "insert into T_ITEM_TEST (plm_m_oid, plm_oid, plm_m_id, plm_i_name, plm_i_createtime, plm_i_checkintime, plm_cailiao, plm_checkintime, plm_weight, plm_wllx, plm_wlly, plm_guige, plm_gylx)\n")
            fp_sql_item.write("values ('" + str(i.id) + "', '" + str(i.id) + "', '" + str(
                i.id) + "', '" + i.name + "', to_date('" + mytime_sql + "', 'dd-mm-yyyy hh24:mi:ss'), to_date('" + mytime_sql + "', 'dd-mm-yyyy hh24:mi:ss'), '" + i.name + "', to_date('" + mytime_sql + "', 'dd-mm-yyyy hh24:mi:ss'), " + plm_weight + ", '" + plm_wllx + "', '" + plm_wlly + "', null, null);\n")
            fp_csv_item.write(str(i.id) + "," + str(i.id) + "," + str(
                i.id) + "," + i.name + ",\"" + mytime + "\",\"" + mytime + "\"," + i.name + ",\"" + mytime + "\"," + plm_weight + "," + plm_wllx + "," + plm_wlly + ",NULL,NULL\n")
            for j in i.ins:
                plm_order = str(random.randint(0, 50))
                plm_jianhao = str(random.randint(0, 50))
                plm_number = str(random.randint(0, 50))
                if out_in == "in":
                    edge_id += 1
                    fp_sql_relation.write(
                        "insert into T_RELATION_TEST (plm_oid, plm_leftobj, plm_rightobj, plm_createtime, plm_order, plm_jianhao, plm_number)\n")
                    fp_sql_relation.write("values ('" + str(edge_id) + "', '" + str(j) + "', '" + str(
                        i.id) + "', to_date('" + mytime_sql + "', 'dd-mm-yyyy hh24:mi:ss'), " + plm_order + ", " + plm_jianhao + ", " + plm_number + ");\n")
                    fp_csv_relation.write(str(edge_id) + "," + str(j) + "," + str(
                        i.id) + ",\"" + mytime + "\"," + plm_order + "," + plm_jianhao + "," + plm_number + "\n")
                elif out_in == "out":
                    edge_id += 1
                    fp_sql_relation.write(
                        "insert into T_RELATION_TEST (plm_oid, plm_leftobj, plm_rightobj, plm_createtime, plm_order, plm_jianhao, plm_number)\n")
                    fp_sql_relation.write("values ('" + str(edge_id) + "', '" + str(i.id) + "', '" + str(
                        j) + "', to_date('" + mytime_sql + "', 'dd-mm-yyyy hh24:mi:ss'), " + plm_order + ", " + plm_jianhao + ", " + plm_number + ");\n")
                    fp_csv_relation.write(str(edge_id) + "," + str(i.id) + "," + str(
                        j) + ",\"" + mytime + "\"," + plm_order + "," + plm_jianhao + "," + plm_number + "\n")
                else:
                    print("[ERROR] out_in uncertain.")
                # if edge_id%1000 == 0:
                #     fp_sql_relation.write("commit;\n")
                #     fp_sql_relation.write("prompt "+str(edge_id)+" records committed...\n")

        if file_footer == True:
            fp_sql_item.write("commit;\n")
            fp_sql_item.write("prompt " + str(temp_id) + " records committed...\n")
            fp_sql_item.write("alter table T_ITEM_TEST enable all triggers;\n")
            fp_sql_item.write("set feedback on\n")
            fp_sql_item.write("set define on\n")
            fp_sql_item.write("prompt Done.\n\n")
            fp_sql_item.close()

            fp_sql_relation.write("commit;\n")
            fp_sql_relation.write("prompt " + str(edge_id) + " records committed...\n")
            fp_sql_relation.write("alter table T_RELATION_TEST enable all triggers;\n")
            fp_sql_relation.write("set feedback on\n")
            fp_sql_relation.write("set define on\n")
            fp_sql_relation.write("prompt Done.\n\n")
            fp_sql_relation.close()

        return file_header, file_footer, temp_id, edge_id

    def __get_raw_nodes(self, dict):
        g_nodes = {}
        g1_nodes_by_name = {}
        count_g1 = 0
        g2_nodes_by_name = {}
        count_g2 = 0
        for i in dict:
            distribution_g1 = []
            distribution_g2 = []
            for j in dict[i]:
                g_nodes[j] = dict[i][j]
                if not dict[i][j]['level_g1'] == -1:
                    count_g1 += 1
                    distribution_g1.append(dict[i][j]['level_g1'])
                if not dict[i][j]['level_g2'] == -1:
                    count_g2 += 1
                    distribution_g2.append(dict[i][j]['level_g2'])
            if not len(distribution_g1) == 0:
                g1_nodes_by_name[i] = distribution_g1
            if not len(distribution_g2) == 0:
                g2_nodes_by_name[i] = distribution_g2
        return count_g1, count_g2, g1_nodes_by_name, g2_nodes_by_name, g_nodes

    def __get_raw_edge(self, raw_graph, g_nodes_by_oid, out_in):
        num1 = 0
        num2 = 0
        nodes1 = {}
        nodes2 = {}
        for i in raw_graph:
            for j in raw_graph[i]:

                if (not raw_graph[i][j]['level_g1']==-1) and (not raw_graph[i][j][out_in]==[]):
                    if not i in nodes1:
                        nodes1[i] = {}
                    if not raw_graph[i][j]['level_g1'] in nodes1[i]:
                        nodes1[i][raw_graph[i][j]['level_g1']] = {"level": [], "number": [], "name": []}
                    count = 0
                    for k in raw_graph[i][j][out_in]:
                        node_by_oid = g_nodes_by_oid[k['oid']]
                        if not node_by_oid['level_g1'] == -1:
                            nodes1[i][raw_graph[i][j]['level_g1']]['level'].append(node_by_oid['level_g1'])
                            nodes1[i][raw_graph[i][j]['level_g1']]['name'].append(node_by_oid['name'])
                            count += 1
                    nodes1[i][raw_graph[i][j]['level_g1']]['number'].append(count)
                    num1 += count
                
                if (not raw_graph[i][j]['level_g2']==-1) and (not raw_graph[i][j][out_in]==[]):
                    if not i in nodes2:
                        nodes2[i] = {}
                    if not raw_graph[i][j]['level_g2'] in nodes2[i]:
                        nodes2[i][raw_graph[i][j]['level_g2']] = {"level": [], "number": [], "name": []}
                    count = 0
                    for k in raw_graph[i][j][out_in]:
                        node_by_oid = g_nodes_by_oid[k['oid']]
                        if not node_by_oid['level_g2'] == -1:
                            nodes2[i][raw_graph[i][j]['level_g2']]['level'].append(node_by_oid['level_g2'])
                            nodes2[i][raw_graph[i][j]['level_g2']]["name"].append(node_by_oid["name"])
                            count += 1
                    nodes2[i][raw_graph[i][j]['level_g2']]['number'].append(count)
                    num2 += count

        return nodes1, nodes2, num1, num2

    def __create_new_nodes(self, raw_nodes, raw_outs, size_raw, size_target, level_raw, level_target, nodeid):
        new_nodes = []
        new_nodes_by_name = {}
        for i in raw_nodes:
            if not i in new_nodes_by_name:
                new_nodes_by_name[i] = {}
            raw_data = raw_nodes[i]
            ds = DataSimulator("", raw_data)
            size = len(raw_data)
            scale = 1.0
            if not raw_nodes[i] == [0]:
                size = int( round( 1.0 * size_target / size_raw * len(raw_data) ) )
                scale = ( 1.0 * level_target / level_raw )
            item = ds.simulate( size, scale )
            new_distr = item.getData()
            for j in new_distr:
                if not j in new_nodes_by_name[i]:
                    new_nodes_by_name[i][j] = []
                nodeid += 1

                onenode = Node(nodeid, i, j, [], [], ds.getConf())
                new_nodes_by_name[i][j].append(onenode)
                new_nodes.append(onenode)
        
        return new_nodes, new_nodes_by_name, nodeid

    def __create_new_edge(self, raw_nodes, raw_outs, new_nodes_by_name, graph_by_level, size_raw, size_target, level_raw, level_target, out_in):
        num_edge = 0
        inturn_count1 = 0
        inturn_count2 = 0
        inturn_count3 = 0
        # mycount = 0
        # fp = file("data/test_new_edge.txt", "a")
        for i in new_nodes_by_name:
            for j in new_nodes_by_name[i]:
                for k in new_nodes_by_name[i][j]:
                    
                    if i in raw_outs:
                        outs_level = int( round( 1.0 * j / ( 1.0 * level_target / level_raw ) ) )
                        deviation = 1
                        lastone = 0
                        flag = True
                        while not outs_level in raw_outs[i]:
                            outs_level += math.pow((-1), deviation) * deviation
                            # nextone = outs_level + math.pow((-1), (deviation+1)) * (deviation+1)
                            if (lastone < 0 or lastone > level_raw) and (outs_level < 0 or outs_level > level_raw):
                                flag = False
                                print("[INFO] skip: "+str(k.name))
                                break
                            else:
                                lastone = outs_level
                                deviation += 1
                        # whether created out by some probability
                        if flag == True and random.random() < ( 1.0 * len(raw_outs[i][outs_level]['number']) / raw_nodes[i].count(outs_level) ):
                            # create certain length of outs by some probability
                            position = random.randint(0, len(raw_outs[i][outs_level]['number'])-1)
                            item_out = DataSimulator("", raw_outs[i][outs_level]['level']).simulate( int( round( 1.0 * size_target / size_raw * raw_outs[i][outs_level]['number'][position] ) ), (1.0 * level_target / level_raw), True)
                            item_out_data = item_out.getData()
                            item_out_data_temp = []
                            last_level_flag = False
                            for l in item_out_data:
                                if l >= j - 1:
                                    # item_out_data_temp.append(l)
                                    item_out_data_temp.append(j-1)
                                if l == j - 1:
                                    last_level_flag = True
                            item_out_data = item_out_data_temp
                            if last_level_flag == False:
                                item_out_data.append(j-1)
                            
                            outs = []
                            if len(item_out_data) > 0:
                                names = raw_outs[i][outs_level]['name']
                                # levels = raw_outs[i][outs_level]['level']
                                length = len(names)
                                count = 0
                                count2 = 0
                                deviation = 0
                                last_notfound = 0
                                while count2 < len(item_out_data):
                                    flag = False
                                    for nouse in range(0, length):
                                        m = count % length
                                        count += 1
                                        if (names[m] in new_nodes_by_name) and (item_out_data[count2] in new_nodes_by_name[names[m]]):
                                            target_nodes = new_nodes_by_name[names[m]][item_out_data[count2]]
                                            temp_out = target_nodes[( inturn_count1 % len(target_nodes) )].id
                                            if not temp_out in outs:
                                                outs.append(temp_out)
                                                inturn_count1 += 1
                                            num_edge += 1
                                            flag = True
                                            count2 += 1
                                            break
                                    if flag == False:
                                        candidate_name = []
                                        for l in raw_outs[i]:
                                            for ll in raw_outs[i][l]['name']:
                                                if not ll in candidate_name:
                                                    candidate_name.append(ll)
                                        
                                        node_can_choose = []
                                        # bugs to be fixed: sometimes out of index
                                        level_temp = item_out_data[count2]
                                        for l in graph_by_level[level_temp]:
                                            if l.name in candidate_name:
                                                node_can_choose.append(l)

                                        if not len(node_can_choose) == 0:
                                            temp_out = node_can_choose[( inturn_count2 % len(node_can_choose) )].id
                                            if not temp_out in outs:
                                                outs.append(temp_out)
                                                inturn_count2 += 1
                                        else:
                                            if item_out_data[count2] == j-1:
                                                for nouse2 in range(0, len(graph_by_level[item_out_data[count2]])):
                                                    now_item = graph_by_level[ item_out_data[count2] ][ inturn_count3 % len(graph_by_level[item_out_data[count2]]) ]
                                                    if (now_item.name in raw_outs) and (not now_item.id in outs):
                                                        outs.append(now_item.id)
                                                        inturn_count3 += 1
                                                        break
                                        count2 += 1
                            outs = list(set(outs))
                            if out_in == "out":
                                k.outs = outs
                            elif out_in == "in":
                                k.ins = outs
                            else:
                                print("[Error] out_in uncertain.")

        return num_edge

    def __dump_raw_edge(self, raw_edge, filename, out_in):
        fp = open(filename, "a")
        for i in raw_edge:
            for j in raw_edge[i]:
                fp.write("Name: "+str(i)+" | Level: "+str(j)+" | "+str(out_in)+":"+str(json.dumps(raw_edge[i][j]))+"\n")
        fp.close()

    def __dump_raw_nodes(self, graph, filename):
        fp = open(filename, "a")
        for i in graph:
            fp.write(i+": "+json.dumps(graph[i])+"\n")
        fp.close()

    def __dump_graph_by_name(self, graph, filename, out_in):
        fp = open(filename, "a")
        for i in graph:
            fp.write(i+": \n")
            for j in graph[i]:
                for k in graph[i][j]:
                    # fp.write("\tID: "+str(k.id)+" | Name: "+str(k.name)+" | Level: "+str(k.level))
                    fp.write("\tID: "+str(k.id)+" | Name: "+str(k.name)+" | Level: "+str(k.level)+" | Distribution: "+str(k.distribution))
                    if out_in == "out":
                        fp.write(" | Out: "+str(json.dumps(k.outs))+"\n")
                    elif out_in == "in":
                        fp.write(" | In: "+str(json.dumps(k.ins))+"\n")
                    else:
                        print("[Error] out_in uncertain in dump.")
        fp.close()

    def __dump_graph_by_level(self, statistic, filename, out_in):
        fp = open(filename, "a")

        for i in statistic:
            fp.write("level "+str(i)+" :"+str(len(statistic[i]))+" items\n")
        fp.write("\n--------------------\n\n")
        
        for i in statistic:
            count = 0
            for j in statistic[i]:
                count += len(j.outs)
            fp.write(str(i)+" (number: "+str(len(statistic[i]))+", out: "+str(count)+")\n")
            for j in statistic[i]:
                # fp.write("\tID: "+str(j.id)+" | Name: "+str(j.name)+" | Level: "+str(j.level))
                fp.write("\tID: "+str(j.id)+" | Name: "+str(j.name)+" | Level: "+str(j.level)+" | Distribution: "+str(j.distribution))
                if out_in == "out":
                    fp.write(" | Out: "+str(json.dumps(j.outs))+"\n")
                elif out_in == "in":
                    fp.write(" | In: "+str(json.dumps(j.ins))+"\n")
                else:
                    print("[Error] out_in uncertain in dump.")
        fp.close()

    def __dump_raw_graph(self, raw_graph):

        fppp = open(self.__base_path_data+"/raw_data.txt","a")
        for i in raw_graph:
            for j in raw_graph[i]:
                fppp.write(i+": "+json.dumps(raw_graph[i][j])+"\n")
        fppp.close()
