# coding:utf-8

import os
from graphviz import Digraph

filename = "output/transition.txt"

final = []
combine = []

with open(filename) as f:
    for line in f.readlines():
        line = line[1: -2]         #remove brackets
        line = line.split(';')     #split with ','
        combine.append(line)

print(combine)

dot = Digraph(comment='Puchi Automataton', format='png')

for item in combine:
    dot.node(item[0], item[-1])
    dot.edge(item[0], item[-1], item[1])

dot.render(output/transitions) 