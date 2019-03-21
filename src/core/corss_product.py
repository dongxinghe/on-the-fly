# coding:utf-8
import os
from graphviz import Digraph
# os.environ["PATH"] += os.pathsep + 'C:\\Users\\wangh\\Anaconda3\\envs\\tensorflow\Library\bin'

file1 = "output/transition.txt"
file2 = "output/program.txt"

dot = Digraph(comment='cross_product', format='png')

visit = []
pro_list = []
stack = []
combine = []

result = []

with open(file2) as f2:
    for line in f2.readlines():
        line = line[1: -2]
        line = line.split(';')
        pro_list.append(line)

        if line[0] == "{s0}":
            stack.append(line[0])
            visit.append(line[0])

tran_list = []            

with open(file1) as f1:
    for line in f1.readlines():
        line = line[1: -2]
        line = line.split(';')
        tran_list.append(line)
            
            
tran = []

f = open('output/result.txt', 'w')

if(len(stack)):
    elem = stack[-1]
    for item in pro_list:
        if item[0] == elem:
            tran.append(item)

    size1 = len(tran)
    for t2 in tran:
        if size1 >= 1:
            size1 -= 1
            size2 = len(tran_list)
            for t1 in tran_list:
                if len(t1) >= 4:
                    if (t1[1].find(t2[1]) != -1) or t1[1] == 'true':
                        n1 = t1[0] + ',' + t2[0]
                        n2 = t1[-1] + ',' + t2[-1]
                        dot.node(n1, n1)
                        dot.node(n2, n2)
                        dot.edge(n1, n2, t1[2])
                        result.append([n1, t1[2], [t1], n2])
                        f.write(str([[n1], [t1[2]],[n2]]).replace('\'',' ') + '\n')

                        if t2[-1] not in visit:
                            stack.append(t2[-1])
                            visit.append(t2[-1])
dot.render("output/result")

print(result)