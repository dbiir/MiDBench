#coding=utf-8
import sys
from Item import Item
from DataSimulator import DataSimulator
#reload(sys)
#sys.setdefaultencoding('utf-8')

class Node:
    def __init__(self, id, name, level, outs = [], ins = [], distribution = []):
        self.id = id
        self.name = name
        self.level = level
        self.outs = outs
        self.ins = ins
        self.distribution = distribution
    
    # def addOut(self, to_id):
    #     (self.pointOut).append(to_id)
    
    # def getID(self):
    #     return self.id
    
    # def getName(self):
    #     return self.name
    
    # def getLevel(self):
    #     return self.level
    
    # def getOuts(self):
    #     return self.pointOut

# if __name__ == "__main__":
#     raw_data = [5,6,5,3,4,4,6,2,4,4,5]# 2:1 3:1 4:4 5:3 6:2
#     # raw_data = [3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,4]# 2:13 3:2 4:2
#     # raw_data = [2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2]# 2:2 3:13 4:2
#     # raw_data = [2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4]# 2:2 3:13 4:2

#     item = DataSimulator("helloworld", raw_data).simulate(100)
#     print item.getDistribution()
#     print item.getData()
#     print item.getStatistic()
