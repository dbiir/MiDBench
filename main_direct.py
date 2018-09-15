#coding=utf-8
import sys, time, os
from GraphExporter import GraphExporter
from GraphGenerator import GraphGenerator
from QueryGenerator import QueryGenerator
import numpy as np

# def main(usename, password, raw_graph_class_node, raw_graph_class_edge, query_size, graph_size,socket,uri,graph_path):
if __name__ == "__main__":

    print ("[INFO] Start. ("+str(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))+")")

    # Neo4j database configuration
    uri = "bolt://10.77.50.193:5002"
    usename = "neo4j"
    password = "xiaojian"
    raw_graph_class_node = "item_1"
    raw_graph_class_edge = "rel_1"

# 需要根据项目所在目录进行改变的
    #获取项目的路径
    base_path = os.getcwd()
    #获取服务器上neo4j（图数据库）的路径
    graph_path = "/software/mark/neo4j-community-3.4.6"

    # New graph configuration: graph_conf[size, level, query_num]
    query_size = 1
    # graph_size = [ [2000, 5, 1] ]
    # query_size = 1
    # graph_size = [ [3000, 7, 1], [6000, 10, 1], [12000, 14, 1], [24000, 20, 1] ]

    # 2000，5，1&2000, 6, 1
    graph_size = "400000,20,1"
    _graph_size = graph_size.split("&")
    graph_conf = []
    for i in _graph_size:
        _i = i.split(",")
        for j in range(0, int(_i[2])):
            graph_conf.append([int(_i[0]), int(_i[1]), query_size])
    # print(graph_conf)

    # graph_conf = []
    # for i in graph_size:
    #     for j in range(0, i[2]):
    #         graph_conf.append([i[0], i[1], query_size])

    # Export raw graph from Neo4j DB
    # socket.wfile.write(("Start to Analyze ！<br>").encode())
    ge = GraphExporter()
    raw_graph, g1_level, g2_level = ge.export(uri, usename, password, raw_graph_class_node, raw_graph_class_edge)
    #
    # # Create new graph (including dumping to file)
    # # gg = GraphGenerator(raw_graph, g1_level, g2_level, graph_size, graph_level, graph_number)
    print("Analyze End!")
    # socket.wfile.write(("Analyze End!！<br>").encode())
    gg = GraphGenerator(raw_graph, (g1_level + 1), (g2_level + 1), graph_conf,base_path)
    new_graphs, node_id, edge_id = gg.create()
    print("BOM Generate End!")
    # socket.wfile.write(("BOM Generate End!<br>").encode())

    qg = QueryGenerator(new_graphs, graph_conf, node_id, edge_id,base_path,graph_path)
    qg.generate()
    print("Query Generate End!")
    # socket.wfile.write(("Query Generate End!<br>").encode())
    print("[INFO] End. (" + str(time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time()))) + ")")
    # return