@echo off 
cd %cd%/src
set /p a=��������Ҫ����LTL��ʽ: 
echo %a%
java -jar LtlToBuchi.jar %a%
echo "transition���ɳɹ����س�����"
pause
start python draw.py
echo "transitionͼ�λ��Ƴɹ����س�����"
pause
start python draw_pro.py
echo "programͼ�λ��Ƴɹ����س�����"
pause
start python corss_product.py
echo "program��transition���в�ˣ��س�����"
pause
java -jar checker.jar
echo "������"
pause

