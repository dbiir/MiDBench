#coding=utf-8
import sys, numpy
from scipy.stats import norm, zipf
#reload(sys)
#sys.setdefaultencoding('utf-8')

class DistributionAnalyzer:
    def __init__(self, name=""):
        self.name = name

    def __getStatistic(self, data):
        result = {}
        for i in data:
            if not i in result:
                result[i] = 0
            result[i] += 1
        return result

    def __getNormalDifference(self, mu, sigma, sample, sample_s, keys_s):
        standard_dist_sum = 0.0
        standard_dist = {}
        for i in keys_s:
            standard_dist[i] = norm.pdf(i, mu, sigma)
            standard_dist_sum += standard_dist[i]
        difference = 0.0
        for i in keys_s:
            if not standard_dist_sum == 0:
                difference += pow(( 1.0 * standard_dist[i] / standard_dist_sum * len(sample) - sample_s[i]), 2)
            else:
                difference += pow((sample_s[i]), 2)
        return difference
    
    def __recursiveNormalSearch(self, bottom, top, mu, sample, sample_s, keys_s):
        threshold = 4
        if round(bottom, threshold) == round(top, threshold):
            return round(bottom, threshold), self.__getNormalDifference(mu, bottom, sample, sample_s, keys_s)
        p_now = bottom
        p_last = p_now
        v_now = self.__getNormalDifference(mu, bottom, sample, sample_s, keys_s)
        v_last = v_now
        for i in range(0, 11):
            sigma = ( 1.0 * (top-bottom) / 10 * i + bottom )
            temp = self.__getNormalDifference(mu, sigma, sample, sample_s, keys_s)
            if temp > v_now:
                return self.__recursiveNormalSearch(p_last, sigma, mu, sample, sample_s, keys_s)
            elif i == 10:
                return self.__recursiveNormalSearch(p_now, sigma, mu, sample, sample_s, keys_s)
            else:
                p_last = p_now
                v_last = v_now
                p_now = sigma
                v_now = temp

    def __getZipfianDifference(self, a, sample_ss):
        sum_sample = 0
        for i in sample_ss:
            sum_sample += i[1]
        difference = 0
        count = 0
        for i in sample_ss:
            count += 1
            difference += pow( (1.0*sum_sample*zipf.pmf(count, a)-i[1]), 2)
        return difference
    
    def __recursiveZipfianSearch(self, bottom, top, sample_ss):
        threshold = 4
        if round(bottom, threshold) == round(top, threshold):
            return round(bottom, threshold), self.__getZipfianDifference(bottom, sample_ss)
        p_now = bottom
        p_last = p_now
        v_now = self.__getZipfianDifference(bottom, sample_ss)
        v_last = v_now
        for i in range(0, 11):
            a = ( 1.0 * (top-bottom) / 10 * i + bottom )
            temp = self.__getZipfianDifference(a, sample_ss)
            if temp > v_now:
                return self.__recursiveZipfianSearch(p_last, a, sample_ss)
            elif i == 10:
                return self.__recursiveZipfianSearch(p_now, a, sample_ss)
            else:
                p_last = p_now
                v_last = v_now
                p_now = a
                v_now = temp

    def __checkUniform(self, sample, minvalue, maxvalue):
        sample_s = self.__getStatistic(sample)
        keys_s = sorted(sample_s)
        # temp_sum = 0
        # for i in sample:
        #     temp_sum += i
        # avg = 1.0 * temp_sum / len(sample)
        standard_dist_sum = 0.0
        standard_dist = {}
        for i in keys_s:
            standard_dist[i] = 1.0 / ( (maxvalue + 1) - minvalue)
            standard_dist_sum += standard_dist[i]
        difference = 0.0
        for i in keys_s:
            difference += pow(( standard_dist[i] / standard_dist_sum * len(sample) - sample_s[i]), 2)
        return minvalue, maxvalue, difference

    def __checkNormal(self, sample):
        sample_s = self.__getStatistic(sample)
        keys_s = sorted(sample_s)
        temp_sum = 0
        for i in sample:
            temp_sum += i
        mu = 1.0 * temp_sum / len(sample)
        sigma, difference = self.__recursiveNormalSearch(0.001, 1000.0, mu, sample, sample_s, keys_s)
        return mu, sigma, difference

    def __checkZipfian(self, sample):
        sample_s = self.__getStatistic(sample)
        sample_ss = sorted(sample_s.items(), key = lambda sample_s:sample_s[1], reverse = True)
        aa, difference = self.__recursiveZipfianSearch(1.001, 100.0, sample_ss)
        sequence = []
        for i in sample_ss:
            sequence.append(i[0])
        return aa, sequence, difference

    def analyze(self, sample):
        if not type(sample) == list:
            "Error: sample is not a list."
            exit()
        
        datasize = len(sample)
        minvalue = sys.maxsize
        maxvalue = -(sys.maxsize-1)
        for i in sample:
            if i > maxvalue:
                maxvalue = i
            if i < minvalue:
                minvalue = i

        # print "----------\ndata: "+str(sample)
        # print "Data distribution: "+str(self.__getStatistic(sample))
        minvalue, maxvalue, difference_uniform = self.__checkUniform(sample, minvalue, maxvalue)
        # print "UNIFORM: a: "+str(minvalue)+" b: "+str(maxvalue)+" | difference: "+str(difference_uniform)
        mu, sigma, difference_normal = self.__checkNormal(sample)
        # print "NORMAL: mu: "+str(mu)+" | sigma: "+str(sigma)+" | difference: "+str(difference_normal)
        aa, sequence, difference_zipfian = self.__checkZipfian(sample)
        # print "ZIPFIAN: a: "+str(aa)+" difference: "+str(difference_zipfian)

        if difference_uniform<=difference_normal and difference_uniform<=difference_zipfian:
            # print "It is likely a UNIFORM distribution!\n----------"
            return {
                'distribution': 'uniform',
                'difference': difference_uniform,
                'max': maxvalue,
                'min': minvalue,
                'size': datasize
            }
        elif difference_normal<=difference_uniform and difference_normal<=difference_zipfian:
            # print "It is likely a NORMAL distribution!\n----------"
            return {
                'distribution': 'normal',
                'mu': mu,
                'sigma': sigma,
                'difference': difference_normal,
                'max': maxvalue,
                'min': minvalue,
                'size': datasize
            }
        elif difference_zipfian<=difference_uniform and difference_zipfian<=difference_normal:
            # print "It is likely a ZIPFIAN distribution!\n----------"
            return {
                'distribution': 'zipfian',
                'a': aa,
                'sequence': sequence,
                'difference': difference_zipfian,
                'max': maxvalue,
                'min': minvalue,
                'size': datasize
            }
        else:
            print("ERROR: can't judge the type of distribution.")

    def getName(self):
        return self.name

# if __name__ == "__main__":
#     da = DistributionAnalyzer("helloworld")
#     print da.analyze([5,6,5,3,4,4,6,2,4,4,5])# 2:1 3:1 4:4 5:3 6:2
#     print da.analyze([3,3,2,2,2,2,2,2,2,2,2,2,2,2,2,2,4,4])# 2:2 3:13 4:2