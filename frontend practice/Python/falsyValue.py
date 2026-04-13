'''
 c/c++ :---
  Truthy value :- non-zero
  Falsy value  :- zero
 java :-
   True :--- true
   False :-- false
 Javascript :-
   Falsy  :- 0, "", false, null, NaN, undefined
   True :- non-zero, "hello", true, [],{}
 
   c/c++/java/js
   a = 10
   b = 20
   c = 30
   d = 40

   x = (a > b) && (++c < d)
 
 Python :-
   Falsy :- 0, "", False, None, [],{}  

 Logical Operator :-
   and , or
   not
'''
a,b,c,d = 10,20,30,40

result = a > b and c < d
print(f"Result : {result}")

print(0 and 1) # 0
print(100 and 200) # 200
print(True and None)
print("Hello" and "Hii")
print(-100 and {})