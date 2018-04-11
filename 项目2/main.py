#!/usr/bin/python
# -*- coding: utf-8 -*-

import random;

class Bit_War:

    n = 0 # 回合次数

    def __init__(self, number_war):
        Bit_War.n = number_war 
        self.choose_list = []

    def clean_list(self):
        self.choose_list = []

    def stategy_t1(self):
        # 策略1 永远选择1
        self.clean_list()
        self.choose_list = [1] * Bit_War.n

    def stategy_t2(self, prob_to_1):
        # 策略2 每次以某个概率随机选择1 否则选择0
        # 0 <= prob_to_1 <= 100
        self.clean_list()
        i = Bit_War.n
        while i > 0:
            rand_num = random.randint(0,99)
            if rand_num < prob_to_1:
                self.choose_list.append(1)
            else:
                self.choose_list.append(0)
            i -= 1

    def stategy_t3(self, other_choose_list):
        # 策略3 第一次选择1 以后每次选择对方上一次的选择
        self.clean_list()
        self.choose_list.append(1)
        for item in other_choose_list:
            self.choose_list.append(item)
        self.choose_list.pop(Bit_War.n); #删除最后一个多余元素

    def stategy_t4(self, other_choose_list):
        self.clean_list()
        # 策略4 和策略3差不多，只是会随机选择一次0
        self.stategy_t3(other_choose_list)
        rand_num = random.randint(0, Bit_War.n - 1)
        self.choose_list[rand_num] = 0

    def stategy_t5(self, other_choose_list):
        # 策略5 一直选择1 一旦对方选择0，则一直选择0
        self.clean_list()
        try:
            the_firet_zore_index = other_choose_list.index(0)
        except:
            self.choose_list = other_choose_list
        else:
            self.choose_list[:the_firet_zore_index] = [1] * the_firet_zore_index
            self.choose_list[the_firet_zore_index + 1:] = [0] * (Bit_War.n - the_firet_zore_index)

    def return_list(self):
        return self.choose_list

# @param num 回合次数
# @param a_list A的列表
# @param a_list B的列表
def count_score(num, a_list, b_list):
    sum_A = 0
    sum_B = 0
    i = 0
    while i < num:
        if a_list[i] == 0:  # An = 0
            if b_list[i] == 0:  # Bn = 0
                sum_A += 1
                sum_B += 1
            else:  # Bn = 1
                sum_A += 5
                sum_B += 0
        else:  # An = 1
            if b_list[i] == 0:  # Bn = 0
                sum_A += 0
                sum_B += 5
            else:
                sum_A += 1
                sum_B += 1
        i += 1
    return [sum_A, sum_B]


# 对战 A用策略12 B用策略12345
# @param num 回合次数
def A12(num):
    result = []
    A = Bit_War(num)

    A.stategy_t1()
    a_list = A.return_list()
    print "A1-B1      A1-B2     A1-B3     A1-B4     A1-B5"
    B12345(num, a_list) # A1 B12345
    print ""

    A.stategy_t2(20)
    a_list = A.return_list()
    print "A2-B1      A2-B2     A2-B3     A2-B4     A2-B5"
    B12345(num, a_list) # A2 B12345
    print ""

# 对战 A用策略3 B用12345
# @param num 回合次数
def A3(num):
    result = []
    A = Bit_War(num)

    B = Bit_War(num)
    B.stategy_t1()
    b_list = B.return_list()

    A.stategy_t3(b_list)
    a_list = A.return_list()

    result.append(count_score(num, a_list, b_list)) # A3 B1

    B.stategy_t2(30)
    b_list = B.return_list()
    A.stategy_t3(b_list)
    result.append(count_score(num, a_list, b_list)) # A3 B2

    a_list = [1] * num
    b_list = [1] * num
    result.append(count_score(num, a_list, b_list)) # A3 B3

    rand_num = random.randint(1, num - 1)
    a_list = [1] * num
    b_list = [1] * num
    b_list[rand_num] = 0
    if rand_num < num - 1:
        a_list[rand_num + 1] = 0
    result.append(count_score(num, a_list, b_list)) # A3 B4

    a_list = [1] * num
    b_list = [1] * num
    result.append(count_score(num, a_list, b_list)) # A3 B5

    print "A3-B1      A3-B2     A3-B3     A3-B4     A3-B5"
    print result
    print ""



# 对战 A用策略4 B用12345
# @param num 回合次数
def A4(num):
    result = []
    A = Bit_War(num)

    B = Bit_War(num)
    B.stategy_t1()
    b_list = B.return_list()

    A.stategy_t4(b_list)
    a_list = A.return_list()

    result.append(count_score(num, a_list, b_list)) # A4 B1

    B.stategy_t2(30)
    b_list = B.return_list()
    A.stategy_t4(b_list)
    result.append(count_score(num, a_list, b_list)) # A4 B2

    a_list = [1] * num
    b_list = [1] * num
    rand_num = random.randint(1, num - 1)
    a_list[rand_num] = 0
    result.append(count_score(num, a_list, b_list)) # A4 B3

    rand_num = random.randint(1, num - 1)
    a_list = [1] * num
    b_list = [1] * num
    b_list[rand_num] = 0
    if rand_num < num - 1:
        a_list[rand_num + 1] = 0
    result.append(count_score(num, a_list, b_list)) # A4 B4

    a_list = [1] * num
    rand_num = random.randint(1, num - 1)
    a_list[rand_num] = 0
    B.stategy_t5(a_list)
    result.append(count_score(num, a_list, b_list)) # A4 B5

    print "A4-B1      A4-B2     A4-B3     A4-B4     A4-B5"
    print result
    print ""

# 对战 A用策略5 B用12345
# @param num 回合次数
def A5(num):
    result = []
    A = Bit_War(num)

    B = Bit_War(num)
    B.stategy_t1()
    b_list = B.return_list()

    A.stategy_t5(b_list)
    a_list = A.return_list()

    result.append(count_score(num, a_list, b_list)) # A5 B1

    B.stategy_t2(30)
    b_list = B.return_list()
    A.stategy_t4(b_list)
    result.append(count_score(num, a_list, b_list)) # A5 B2

    a_list = [1] * num
    b_list = [1] * num
    result.append(count_score(num, a_list, b_list)) # A5 B3

    rand_num = random.randint(1, num - 1)
    b_list = [1] * num
    b_list[rand_num] = 0
    A.stategy_t5(b_list)
    a_list = A.return_list()
    result.append(count_score(num, a_list, b_list)) # A5 B4

    a_list = [1] * num
    b_list = [1] * num
    result.append(count_score(num, a_list, b_list)) # A5 B5

    print "A5-B1      A5-B2     A5-B3     A5-B4     A5-B5"
    print result
    print ""

# B策略12345
# @param num 回合次数
# @param a_list a的列表
def B12345(num, a_list):
    result = []
    B = Bit_War(num)

    B.stategy_t1()
    b_list = B.return_list()
    result.append(count_score(num, a_list, b_list)) # A1 B1

    B.stategy_t2(50) #  50% 几率选1
    b_list = B.return_list()
    result.append(count_score(num, a_list, b_list)) # A1 B2

    B.stategy_t3(a_list) 
    b_list = B.return_list()
    result.append(count_score(num, a_list, b_list)) # A1 B2

    B.stategy_t4(a_list) 
    b_list = B.return_list()
    result.append(count_score(num, a_list, b_list)) # A1 B2

    B.stategy_t5(a_list) 
    b_list = B.return_list()
    result.append(count_score(num, a_list, b_list)) # A1 B2

    print result

def main():
    num_of_war = 200
    A12(num_of_war)
    A3(num_of_war)
    A4(num_of_war)
    A5(num_of_war)


if __name__ == "__main__":
    main()