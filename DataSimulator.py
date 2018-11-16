#coding=utf-8
import sys
from Item import Item
from DistributionAnalyzer import DistributionAnalyzer
#reload(sys)
#sys.setdefaultencoding('utf-8')

class DataSimulator:
    def __init__(self, name="", raw_data=[]):
        self.name = name
        self.raw_data = raw_data
        self.conf = ""
        for i in raw_data:
            if not int(i) == i:
                print("Error: Data must be integers.")
                exit()

    def simulate(self, size, scale = 1.0, min_change = False):
        da = DistributionAnalyzer()
        result = da.analyze(self.raw_data)
        item = Item(self.name)
        self.conf = result
        if result['distribution'] == 'zipfian':
            item.setZipfian(result['a'], result['sequence'], size, scale, min_change)
        elif result['distribution'] == 'normal':
            item.setNormal(result['mu'], result['sigma'], result['min'], result['max'], size, scale, min_change)
        elif result['distribution'] == 'uniform':
            item.setUniform(result['min'], result['max'], size, scale, min_change)
        else:
            print('Error: There is no this distribution.')
        return item
    
    def getConf(self):
        return self.conf

# if __name__ == "__main__":
#     # raw_data = [5,6,5,3,4,4,6,2,4,4,5]# 2:1 3:1 4:4 5:3 6:2
#     raw_data = [3,3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,4,4,4,4,2]# 2:13 3:2 4:2
#     # raw_data = [2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2]# 2:2 3:13 4:2
#     # raw_data = [2,2,2,2,2,2,2,3,3,3,3,3,3,4,4,4,4,4,4]# 2:2 3:13 4:2
#     # raw_data = [2,2,2,2,2,2,2,3,3,3,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4]# 2:2 3:13 4:2

#     result = {}
#     for i in raw_data:
#         if not i in result:
#             result[i] = 0
#         result[i] += 1
#     print result

#     item = DataSimulator("helloworld", raw_data).simulate(100, 2.0)
#     print item.getDistribution()
#     # print item.getData()
#     print item.getStatistic()
