@echo off 
cd %cd%/src
set /p a=请输入需要检测的LTL公式: 
echo %a%
java -jar LtlToBuchi.jar %a%
echo "transition生成成功！回车继续"
pause
start python draw.py
echo "transition图形绘制成功！回车继续"
pause
start python draw_pro.py
echo "program图形绘制成功！回车继续"
pause
start python corss_product.py
echo "program和transition进行叉乘！回车继续"
pause
java -jar checker.jar
echo "检测结束"
pause

