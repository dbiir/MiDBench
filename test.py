graph_size = "2000,5,1"
_graph_size = graph_size.split("&")
graph_conf2 = []
for i in _graph_size:
    _i = i.split(",")
    for j in range(0, int(_i[2])):
        graph_conf2.append([int(_i[0]), int(_i[1]),3])
print(graph_conf2)

graph_size = [ [2000,5,1] ]
graph_conf = []
for i in graph_size:
    for j in range(0, i[2]):
        graph_conf.append([i[0], i[1], 5])
print(graph_conf)