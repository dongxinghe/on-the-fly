输入文件
src/output/program.txt    系统输入【可替换】

注意: 原子命题均为小写字母

程序生成
src/output/acc.txt            ACC集合	
src/output/result.txt       叉乘结果【v,A,w】
src/output/transition.txt   迁移关系
src/output/transitions.png   迁移关系图
src/output/program.png   系统图

Python 2.7  需要安装pip install graphviz
JDK 1.8
src：源码
startup.bat：双击执行 
执行步骤：
1、输入LTL不能有空格    注意: 原子命题均为小写字母
2、回车执行【将LTL转化为Transitions】
3、回车执行【将Transition绘制图】
4、回车执行【将Program绘制图】
5、回车执行【做叉乘】
6、回车执行【进行check】
7、输出PASS表示通过