#coding=utf-8
import sys, numpy, math
from numpy.random import normal, zipf, uniform
#reload(sys)
#sys.setdefaultencoding('utf-8')

# !!!!!!!!!! whether need to keep the decimal part
class Item:
    def __init__(self, name):
        self.name = name

    def __checkInt(self, para):
        if not int(para)==para:
            print("Error: The bounds are not integers.")
            return False
        else:
            return True

    def setZipfian(self, para_a, sequence, size, scale, a_change):
        for i in range(0, len(sequence)):
            sequence[i] = int( round( 1.0 * sequence[i] * scale ) )
            # sequence[i] = 1.0 * sequence[i] * scale
        result = []
        count = 0
        while count < size:
            one = zipf(para_a)
            if one >= 1 and one <= len(sequence):
                result.append(sequence[one-1])
                count += 1
        self.data = result
        self.distribution = 'zipfian'

    def setNormal(self, mu, sigma, a, b, size, scale, a_change):
        if (not self.__checkInt(a)) or (not self.__checkInt(b)):
            exit()
        b = int( round( 1.0 * b * scale ) )
        # b = 1.0 * b * scale
        if a_change == True:
            a = int( math.floor( 1.0 * a * scale ) )
        result = []
        count = 0
        while count < size:
            one = int( round( ( 1.0 * normal(mu, sigma) * scale ) ) )
            # one = 1.0 * normal(mu, sigma) * scale
            if one >= a and one <= b:
                result.append(one)
                count += 1
        self.data = result
        self.distribution = 'normal'

    def setUniform(self, a, b, size, scale, a_change):
        if (not self.__checkInt(a)) or (not self.__checkInt(b)):
            exit()
        b = int( round( 1.0 * b * scale) )
        if a_change == True:
            a = int( math.floor( 1.0 * a * scale ) )
        # b = 1.0 * b * scale
        unif = uniform(a, (b+1), size)
        result = []
        for i in unif:
            result.append(int(i))
        self.data = result
        self.distribution = 'uniform'

    def getName(self):
        return self.name
    
    def getData(self):
        return self.data
    
    def getDistribution(self):
        return self.distribution

    def getStatistic(self):
        result = {}
        for i in self.data:
            if not i in result:
                result[i] = 0
            result[i] += 1
        return result
    
# if __name__ == "__main__":
#     item = Item("Zipfian")
#     item.setZipfian(2, [3,5,1,4], 15)
#     print item.getName()
#     print item.getData()

#     item2 = Item("Normal")
#     item2.setNormal(4.36, 1.2175, 2, 6, 11)
#     print item2.getName()
#     print item2.getData()

#     item3 = Item("Uniform")
#     item3.setUniform(3, 3, 13)
#     print item3.getName()
#     print item3.getData()
